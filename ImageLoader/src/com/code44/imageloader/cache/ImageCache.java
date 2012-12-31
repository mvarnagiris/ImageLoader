package com.code44.imageloader.cache;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.code44.imageloader.BuildConfig;
import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.getter.data.BitmapData;

public class ImageCache
{
	protected static final String				TAG	= ImageLoader.TAG + " - Cache";

	protected final CacheSettings				cacheSettings;
	protected final LruCache<String, Bitmap>	memoryCache;

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
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

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

	public Bitmap getFromFile(ImageInfo imageInfo)
	{
		return null;
	}

	public Bitmap getFromFileOriginal(ImageInfo imageInfo)
	{
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
			Log.i(TAG, "Cannot put null to memory cache [" + imageInfo.toString() + "]");
			return false;
		}

		if (BuildConfig.DEBUG && cacheSettings.isLoggingOn())
			Log.i(TAG, "Already in memory cache [" + imageInfo.toString() + "]");

		return false;
	}

	public boolean putToFile(ImageInfo imageInfo, Bitmap bitmap)
	{
		return false;
	}

	public boolean putToFileOriginal(ImageInfo imageInfo, BitmapData bitmapData)
	{
		return false;
	}
}