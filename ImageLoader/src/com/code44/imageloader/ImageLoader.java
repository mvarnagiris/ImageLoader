package com.code44.imageloader;

import java.io.File;
import java.util.concurrent.RejectedExecutionException;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import com.code44.imageloader.cache.ImageCache;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.getter.data.FileBitmapData;
import com.code44.imageloader.info.BitmapInfo;
import com.code44.imageloader.processor.ImageProcessor;

/**
 * Loads images in background and handles caching.
 * 
 * @author Mantas Varnagiris
 */
public class ImageLoader
{
	public static final String		TAG	= "ImageLoader";

	protected final Context			context;
	protected final ImageCache		imageCache;
	protected final LoaderSettings	loaderSettings;
	protected final ImageSettings	defaultImageSettings;
	protected ImageLoaderListener	listener;

	// Constructors
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Uses default values for {@link LoaderSettings} and {@link ImageSettings}.
	 * 
	 * @param context
	 *            Context.
	 */
	public ImageLoader(Context context)
	{
		this(context, null, null);
	}

	/**
	 * Uses default values for {@link ImageSettings}.
	 * 
	 * @param context
	 * @param loaderSettings
	 */
	public ImageLoader(Context context, LoaderSettings loaderSettings)
	{
		this(context, loaderSettings, null);
	}

	/**
	 * Uses default values for {@link LoaderSettings}.
	 * 
	 * @param context
	 * @param defaultImageSettings
	 */
	public ImageLoader(Context context, ImageSettings defaultImageSettings)
	{
		this(context, null, defaultImageSettings);
	}

	public ImageLoader(Context context, LoaderSettings loaderSettings, ImageSettings defaultImageSettings)
	{
		this.context = context.getApplicationContext();
		this.imageCache = ImageCache.getDefault(context);
		this.loaderSettings = loaderSettings != null ? loaderSettings : new LoaderSettings();
		this.defaultImageSettings = defaultImageSettings != null ? defaultImageSettings : new ImageSettings();
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Use this to load image. Default or the ones that you've passed in a constructor {@link ImageSettings} will be used. If you want to use different
	 * {@link ImageSettings}, use {@link #loadImage(ImageView, BitmapInfo, ImageSettings)}.
	 * 
	 * @param imageView
	 * @param bitmapInfo
	 */
	public void loadImage(final ImageView imageView, final BitmapInfo bitmapInfo)
	{
		loadImage(imageView, bitmapInfo, null);
	}

	/**
	 * Use this to load image.
	 * 
	 * @param imageView
	 * @param bitmapInfo
	 * @param imageSettings
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void loadImage(final ImageView imageView, final BitmapInfo bitmapInfo, ImageSettings imageSettings)
	{
		final boolean isLoggingOn = BuildConfig.DEBUG && loaderSettings.isLoggingOn();

		// Use default settings if they are not provided
		if (imageSettings == null)
			imageSettings = defaultImageSettings;

		// Check if we can start image loading
		if (imageView == null || bitmapInfo == null)
		{
			if (isLoggingOn)
				Log.w(TAG, "ImageView or BitmapInfo is null. Loading will not start.");
			if (imageView != null)
				imageView.setImageDrawable(imageSettings.errorDrawable);
			return;
		}

		// Check if bitmap info has correct data
		if (!bitmapInfo.checkInfo())
		{
			if (isLoggingOn)
				Log.w(TAG, "BitmapInfo check failed. Setting error drawable. [" + bitmapInfo.toString() + "]");
			imageView.setImageDrawable(imageSettings.getErrorDrawable());
			return;
		}

		// Create ImageInfo
		final ImageInfo imageInfo = new ImageInfo(imageView, bitmapInfo, imageSettings, loaderSettings.isLoggingOn());

		// Try to get bitmap from memory cache
		Bitmap bitmap = null;
		if (imageInfo.getImageSettings().isUseMemoryCache())
		{
			bitmap = imageCache.getFromMemory(imageInfo);
			if (bitmap != null)
			{
				if (isLoggingOn)
					Log.i(TAG, "Bitmap found in memory cache. [" + imageInfo.toString() + "]");
				setImage(imageView, imageInfo, bitmap);
				return;
			}
		}

		// If bitmap was not found in cache and same work is not already running - load it
		if (imageInfo.cancelPotentialWork())
		{
			// Set loading drawable if necessary
			if (bitmap == null)
				imageView.setImageDrawable(imageSettings.getLoadingDrawable());

			try
			{
				final GetBitmapTask task = new GetBitmapTask(imageInfo);
				imageView.setTag(task);
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
				else
					task.execute();
			}
			catch (RejectedExecutionException e)
			{
				if (isLoggingOn)
					Log.e(TAG, "Failed to start loading. [" + imageInfo.toString() + "]", e);
			}
		}
	}

	public void setImageLoaderListener(ImageLoaderListener listener)
	{
		this.listener = listener;
	}

	// Protected methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	protected void setImage(ImageView imageView, ImageInfo imageInfo, Bitmap bitmap)
	{
		if (bitmap == null)
			Log.i("ASDASD", "NULL");
		imageView.setImageBitmap(bitmap);
		if (listener != null)
			listener.onBitmapLoaded(imageView, imageInfo, bitmap);
	}

	// GetBitmapTask
	// ------------------------------------------------------------------------------------------------------------------------------------

	public class GetBitmapTask extends AsyncTask<Void, Bitmap, Bitmap>
	{
		protected final ImageInfo	imageInfo;

		public GetBitmapTask(ImageInfo imageInfo)
		{
			this.imageInfo = imageInfo;
		}

		@Override
		protected Bitmap doInBackground(Void... params)
		{
			final boolean isLoggingOn = imageInfo.isLoggingOn() && BuildConfig.DEBUG;
			boolean needProcessing = false;
			Bitmap bitmap = null;

			// Try to get bitmap from memory
			if (imageInfo.getImageSettings().isUseMemoryCache())
			{
				bitmap = imageCache.getFromMemory(imageInfo);
				if (isLoggingOn && bitmap != null)
					Log.i(TAG, "Bitmap found in memory cache. [" + imageInfo.toString() + "]");
			}

			// Try to get bitmap from file
			if (bitmap == null && !isCancelled() && imageInfo.getImageSettings().isUseFileCache())
			{
				bitmap = imageCache.getFromFile(imageInfo);
				if (isLoggingOn && bitmap != null)
					Log.i(TAG, "Bitmap found in file cache. [" + imageInfo.toString() + "]");
			}

			// If bitmap was not found in file cache, try to get bitmap from original bitmap file
			if (bitmap == null && !isCancelled() && imageInfo.getImageSettings().isUseFileCache())
			{
				final File bitmapFile = imageCache.getOriginalFile(imageInfo);
				if (bitmapFile != null)
				{
					needProcessing = true;
					if (isLoggingOn)
						Log.i(TAG, "Bitmap original found in file cache. [" + imageInfo.toString() + "]");
					bitmap = imageInfo.parseFileBitmapData(context, new FileBitmapData(bitmapFile, false));
					if (isLoggingOn && bitmap != null)
						Log.i(TAG, "Bitmap parsed. [" + imageInfo.toString() + "]");
				}
			}

			// If bitmap original was not found in original file, try to fetch it
			if (bitmap == null && !isCancelled())
			{
				final BitmapData bitmapData = imageInfo.loadBitmapData(context);
				if (bitmapData != null)
				{
					if (isLoggingOn)
						Log.i(TAG, "Bitmap original fetched. [" + imageInfo.toString() + "]");

					bitmap = imageInfo.parseBitmapData(context, bitmapData);
					if (isLoggingOn && bitmap != null)
						Log.i(TAG, "Bitmap parsed. [" + imageInfo.toString() + "]");
					if (imageCache.putToFileOriginal(imageInfo, bitmapData.getFile()) && isLoggingOn)
						Log.i(TAG, "Bitmap original stored in file. [" + imageInfo.toString() + "]");
					needProcessing = true;

					// Delete file if necessary
					final File file = bitmapData.getFile();
					if (bitmapData.isDeleteFile() && file != null)
					{
						if (file.delete() && imageInfo.isLoggingOn())
							Log.i(TAG, "Temporary file deleted. [" + imageInfo.toString() + "]");
					}
				}
			}

			// Process bitmap
			final ImageProcessor processor = imageInfo.getImageSettings().getImageProcessor();
			if (needProcessing && bitmap != null && processor != null)
			{
				bitmap = processor.processImage(bitmap);
				if (isLoggingOn)
					Log.i(TAG, "Bitmap processed. [" + imageInfo.toString() + "], Processor: " + processor.toString());
			}

			// Put to cache
			if (bitmap != null)
			{
				if (imageInfo.getImageSettings().isUseFileCache() && imageCache.putToFile(imageInfo, bitmap) && isLoggingOn)
					Log.i(TAG, "Bitmap added to file cache. [" + imageInfo.toString() + "]");
				if (imageInfo.getImageSettings().isUseMemoryCache() && imageCache.putToMemory(imageInfo, bitmap) && isLoggingOn)
					Log.i(TAG, "Bitmap added to memory cache. [" + imageInfo.toString() + "]");
			}

			return bitmap;
		}

		@Override
		protected void onProgressUpdate(Bitmap... values)
		{
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Bitmap bitmap)
		{
			final ImageView imageView = imageInfo.getImageView();

			if (isCancelled())
			{
				imageView.setTag(null);
				bitmap = null;
				if (BuildConfig.DEBUG && imageInfo.isLoggingOn())
					Log.i(TAG, "Bitmap load canceled. [" + imageInfo.toString() + "]");
				return;
			}

			if (imageView != null && bitmap != null && imageInfo.getGetBitmapTask() == this)
			{
				imageView.setTag(null);
				setImage(imageView, imageInfo, bitmap);
			}
		}

		// Public methods
		// ------------------------------------------------------------------------------------------------------------------------------------

		public ImageInfo getImageInfo()
		{
			return imageInfo;
		}
	}

	// Interface
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static interface ImageLoaderListener
	{
		public void onBitmapLoaded(ImageView imageView, ImageInfo imageInfo, Bitmap bitmap);
	}
}