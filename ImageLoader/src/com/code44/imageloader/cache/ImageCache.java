package com.code44.imageloader.cache;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.code44.imageloader.BuildConfig;
import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.getter.data.FileBitmapData;
import com.code44.imageloader.getter.parser.FileBitmapParser;

public class ImageCache
{
	protected static final String				TAG							= ImageLoader.TAG + " - Cache";

	protected static final CompressFormat		DEFAULT_COMPRESS_FORMAT		= CompressFormat.PNG;
	protected static final int					DEFAULT_COMPRESS_QUALITY	= 90;
	protected static final String				NOMEDIA_FILE_NAME			= ".nomedia";
	protected static final String				ORIGINAL_CACHE_DIR			= "original";
	protected static final String				PROCESSED_CACHE_DIR			= "processed";

	protected final Context						context;
	protected final CacheSettings				cacheSettings;
	protected final LruCache<String, Bitmap>	memoryCache;
	protected File								rootCacheDir;
	protected File								originalCacheDir;
	protected File								processedCacheDir;

	// Singleton
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static ImageCache					instance;

	public static void initImageCache(Context context, CacheSettings settings)
	{
		instance = new ImageCache(context, settings);
	}

	public static ImageCache getDefault(Context context)
	{
		if (instance == null)
		{
			instance = new ImageCache(context, null);
			if (BuildConfig.DEBUG)
				Log.w(TAG,
						"Using default ImageCache. If you want to customize it, call initImageCache(Context, CacheSettings settings) in Application.onCreate().");
		}
		return instance;
	}

	private ImageCache(Context context, CacheSettings settings)
	{
		this.context = context.getApplicationContext();

		if (settings == null)
			cacheSettings = new CacheSettings(context);
		else
			cacheSettings = settings;

		memoryCache = new LruCache<String, Bitmap>(cacheSettings.getMemoryCacheSize())
		{
			@TargetApi(12)
			@Override
			protected int sizeOf(String key, Bitmap bitmap)
			{
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1)
				{
					return bitmap.getByteCount();
				}
				// Pre HC-MR1
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};

		rootCacheDir = null;
		originalCacheDir = null;
		processedCacheDir = null;
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public File getRootCacheDir()
	{
		if (rootCacheDir == null)
		{
			final String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state) && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) && checkWriteExternalPermission())
				rootCacheDir = context.getExternalCacheDir();
			else
				rootCacheDir = context.getCacheDir();

			if (!rootCacheDir.isDirectory())
				rootCacheDir.mkdirs();
		}

		return rootCacheDir;
	}

	public File getOriginalCacheDir()
	{
		if (originalCacheDir == null)
		{
			originalCacheDir = new File(getRootCacheDir(), ORIGINAL_CACHE_DIR);
			if (!originalCacheDir.isDirectory())
				originalCacheDir.mkdirs();
		}

		return originalCacheDir;
	}

	public File getProcessedCacheDir()
	{
		if (processedCacheDir == null)
		{
			processedCacheDir = new File(getRootCacheDir(), PROCESSED_CACHE_DIR);
			if (!processedCacheDir.isDirectory())
				processedCacheDir.mkdirs();
		}

		return processedCacheDir;
	}

	// Public bitmap methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Tries to retrieve image from memory cache.
	 * 
	 * @param imageInfo
	 *            Info for image to retrieve from memory cache.
	 * @return {@link Bitmap} or {@code null} if bitmap was not found in memory.
	 */
	public Bitmap getFromMemory(ImageInfo imageInfo)
	{
		final Bitmap bitmap = memoryCache.get(imageInfo.getCacheName());
		if (bitmap != null)
		{
			if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
				Log.i(TAG, "Memory cache hit [" + imageInfo.toString() + "]");
			return bitmap;
		}

		if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
			Log.i(TAG, "Not found in memory cache [" + imageInfo.toString() + "]");

		return null;
	}

	/**
	 * Tries to retrieve image from processed images file cache.
	 * 
	 * @param imageInfo
	 *            Info for image to retrieve from processed images file cache.
	 * @return {@link Bitmap} or {@code null} if bitmap was not found.
	 */
	public Bitmap getFromFile(ImageInfo imageInfo)
	{
		final File bitmapFile = new File(getProcessedCacheDir(), imageInfo.getCacheName());
		final Bitmap bitmap = FileBitmapParser.getDefault().parseBitmap(imageInfo, new FileBitmapData(bitmapFile, false));
		if (bitmap != null)
		{
			if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
				Log.i(TAG, "Processed file cache hit [" + imageInfo.toString() + "]");
			return bitmap;
		}

		if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
			Log.i(TAG, "Not found in processed file cache [" + imageInfo.toString() + "]");

		return null;
	}

	/**
	 * Tries to retrieve original image file.
	 * 
	 * @param imageInfo
	 *            Info for image to retrieve from original images file cache.
	 * @return {@link File} or {@code null} if file was not found.
	 */
	public File getOriginalFile(ImageInfo imageInfo)
	{
		final File bitmapFile = new File(getOriginalCacheDir(), imageInfo.getBitmapName());
		if (bitmapFile.exists())
		{
			if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
				Log.i(TAG, "Original file cache hit [" + imageInfo.toString() + "]");
			return bitmapFile;
		}

		if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
			Log.i(TAG, "Not found in original file cache [" + imageInfo.toString() + "]");

		return null;
	}

	/**
	 * Puts bitmap to memory cache if it's not already there.
	 * 
	 * @param imageInfo
	 *            Info about image.
	 * @param bitmap
	 *            Bitmap to put to memory cache.
	 * @return {@code true} if bitmap was added to memory cache; {@code false} if bitmap is {@code null} or it is already in memory cache.
	 */
	public boolean putToMemory(ImageInfo imageInfo, Bitmap bitmap)
	{
		final String cacheName = imageInfo.getCacheName();
		if (bitmap != null && memoryCache.get(cacheName) == null)
		{
			if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
				Log.i(TAG, "Added to memory cache [" + imageInfo.toString() + "]");
			memoryCache.put(cacheName, bitmap);
			return true;
		}

		if (BuildConfig.DEBUG && cacheSettings.isLoggingOn() && bitmap == null)
		{
			Log.w(TAG, "Cannot put null to memory cache [" + imageInfo.toString() + "]");
			return false;
		}

		if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
			Log.i(TAG, "Already in memory cache [" + imageInfo.toString() + "]");

		return false;
	}

	/**
	 * Puts bitmap to processed images file cache.
	 * 
	 * @param imageInfo
	 *            Info about image.
	 * @param bitmap
	 *            Bitmap to put to processed images file cache.
	 * @return {@code true} if bitmap was added to processed images file cache; {@code false} if bitmap is {@code null} or it is already in cache.
	 */
	public boolean putToFile(ImageInfo imageInfo, Bitmap bitmap)
	{
		// Check if bitmap is not null
		if (bitmap == null)
		{
			if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
			{
				Log.w(TAG, "Cannot put null to processed file cache [" + imageInfo.toString() + "]");
				return false;
			}
		}

		// Try save bitmap to file
		try
		{
			if (saveBitmapToFile(bitmap, new File(getProcessedCacheDir(), imageInfo.getCacheName())))
			{
				if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
					Log.i(TAG, "Added to file processed cache. [" + imageInfo.toString() + "]");
				return true;
			}
			else
			{
				if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
					Log.i(TAG, "Already in processed file cache. [" + imageInfo.toString() + "]");
			}
		}
		catch (FileNotFoundException e)
		{
			if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
				Log.w(TAG, "Failed saving to file cache. [" + imageInfo.toString() + "]");
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Puts bitmap to original images file cache.
	 * 
	 * @param imageInfo
	 *            Info about image.
	 * @param bitmap
	 *            Bitmap to put to original images file cache.
	 * @return {@code true} if bitmap was added to original images file cache; {@code false} if bitmap is {@code null} or it is already in cache.
	 */
	public boolean putToFileOriginal(ImageInfo imageInfo, File file)
	{
		// Check if bitmap is not null
		if (file == null)
		{
			if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
			{
				Log.w(TAG, "Cannot put null to original file cache [" + imageInfo.toString() + "]");
				return false;
			}
		}

		// Copy file
		try
		{
			final File bitmapFile = new File(getOriginalCacheDir(), imageInfo.getBitmapName());
			if (!bitmapFile.exists())
			{
				copy(file, bitmapFile);
				if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
					Log.i(TAG, "Added to original file cache. [" + imageInfo.toString() + "]");
				return true;
			}
			else
			{
				if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
					Log.i(TAG, "Already in original file cache. [" + imageInfo.toString() + "]");
			}
		}
		catch (IOException e)
		{
			if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
				Log.w(TAG, "Failed saving to original file cache. [" + imageInfo.toString() + "]");
			e.printStackTrace();
		}

		return false;
	}

	// Protected methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	protected boolean saveBitmapToFile(final Bitmap bitmap, final File bitmapFile) throws FileNotFoundException
	{
		if (!bitmapFile.exists())
		{
			FileOutputStream fos = new FileOutputStream(bitmapFile, false);
			bitmap.compress(DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY, fos);
			return true;
		}
		return false;
	}

	protected void addNomediaFile(File dir)
	{
		try
		{
			new File(dir, NOMEDIA_FILE_NAME).createNewFile();
		}
		catch (Exception e)
		{
			if (cacheSettings.isLoggingOn())
				Log.w(TAG, "Problem creating nomedia file.", e);
		}
	}

	private boolean checkWriteExternalPermission()
	{
		String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
		int res = context.checkCallingOrSelfPermission(permission);
		return res == PackageManager.PERMISSION_GRANTED;
	}

	public void copy(File src, File dst) throws IOException
	{
		InputStream in = null;
		OutputStream out = null;
		try
		{
			in = new FileInputStream(src);
			out = new FileOutputStream(dst);
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
		}
		finally
		{
			closeSilently(out);
			closeSilently(in);
		}
	}

	public void closeSilently(Closeable c)
	{
		try
		{
			if (c != null)
			{
				c.close();
			}
		}
		catch (Exception e)
		{
			Log.w(TAG, "Problem closing stream ", e);
		}
	}
}