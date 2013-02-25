package com.code44.imageloader;

import java.io.File;
import java.util.concurrent.RejectedExecutionException;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
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
	private static final boolean	SUPPORTS_JELLYBEAN	= android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
	private static final boolean	SUPPORTS_HONEYCOMB	= android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;

	public static final String		TAG					= "ImageLoader";

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
	public void loadImage(final ImageView imageView, final BitmapInfo bitmapInfo, ImageSettings imageSettings)
	{
		loadImage(imageView, bitmapInfo, imageSettings, true);
	}

	/**
	 * Use this to load view's background.
	 * 
	 * @param view
	 * @param bitmapInfo
	 * @param imageSettings
	 */
	public void loadBackground(final View view, final BitmapInfo bitmapInfo, ImageSettings imageSettings)
	{
		loadImage(view, bitmapInfo, imageSettings, false);
	}

	/**
	 * Use this to load image or background.
	 * 
	 * @param imageView
	 * @param bitmapInfo
	 * @param imageSettings
	 * @param isImageView
	 *            If {@code true}, then image will be set as for image view, else - background value will be set.
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	public void loadImage(final View view, final BitmapInfo bitmapInfo, ImageSettings imageSettings, boolean isImageView)
	{
		final boolean isLoggingOn = BuildConfig.DEBUG && loaderSettings.isLoggingOn();

		// Use default settings if they are not provided
		if (imageSettings == null)
			imageSettings = defaultImageSettings;

		// Check if we can start image loading
		if (view == null || bitmapInfo == null)
		{
			if (isLoggingOn)
				Log.w(TAG, "ImageView or BitmapInfo is null. Loading will not start.");
			if (view != null)
			{
				if (isImageView)
				{
					((ImageView) view).setImageDrawable(imageSettings.getErrorDrawable());
				}
				else
				{
					if (SUPPORTS_JELLYBEAN)
						view.setBackground(imageSettings.getErrorDrawable());
					else
						view.setBackgroundDrawable(imageSettings.getErrorDrawable());
				}
			}
			return;
		}

		// Check if bitmap info has correct data
		if (!bitmapInfo.checkInfo())
		{
			if (isLoggingOn)
				Log.w(TAG, "BitmapInfo check failed. Setting error drawable. [" + bitmapInfo.toString() + "]");

			if (isImageView)
			{
				((ImageView) view).setImageDrawable(imageSettings.getErrorDrawable());
			}
			else
			{
				if (SUPPORTS_JELLYBEAN)
					view.setBackground(imageSettings.getErrorDrawable());
				else
					view.setBackgroundDrawable(imageSettings.getErrorDrawable());
			}
			return;
		}

		// Create ImageInfo
		final ImageInfo imageInfo = new ImageInfo(view, bitmapInfo, imageSettings, isImageView, loaderSettings.isLoggingOn());

		// Try to get bitmap from memory cache
		Bitmap bitmap = null;
		if (imageInfo.getImageSettings().isUseMemoryCache())
		{
			bitmap = imageCache.getFromMemory(imageInfo);
			if (bitmap != null)
			{
				if (isLoggingOn)
					Log.i(TAG, "Bitmap found in memory cache. [" + imageInfo.toString() + "]");
				setImage(view, imageInfo, bitmap);
				return;
			}
		}

		// If bitmap was not found in cache and same work is not already running - load it
		if (imageInfo.cancelPotentialWork())
		{
			// Set loading drawable if necessary
			if (bitmap == null)
			{
				if (isImageView)
				{
					((ImageView) view).setImageDrawable(imageSettings.getLoadingDrawable());
				}
				else
				{
					if (SUPPORTS_JELLYBEAN)
						view.setBackground(imageSettings.getLoadingDrawable());
					else
						view.setBackgroundDrawable(imageSettings.getLoadingDrawable());
				}
			}

			try
			{
				final GetBitmapTask task = new GetBitmapTask(imageInfo);
				view.setTag(task);
				if (SUPPORTS_HONEYCOMB)
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

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	protected void setImage(View view, ImageInfo imageInfo, Bitmap bitmap)
	{
		if (imageInfo.isImageView)
		{
			((ImageView) view).setImageBitmap(bitmap);
		}
		else
		{
			if (SUPPORTS_JELLYBEAN)
				view.setBackground(new BitmapDrawable(context.getResources(), bitmap));
			else
				view.setBackgroundDrawable(new BitmapDrawable(context.getResources(), bitmap));
		}
		if (listener != null)
			listener.onBitmapLoaded(view, imageInfo, bitmap, imageInfo.isImageView());
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
			final View view = imageInfo.getView();

			if (isCancelled())
			{
				view.setTag(null);
				bitmap = null;
				if (BuildConfig.DEBUG && imageInfo.isLoggingOn())
					Log.i(TAG, "Bitmap load canceled. [" + imageInfo.toString() + "]");
				return;
			}

			if (view != null && bitmap != null && imageInfo.getGetBitmapTask() == this)
			{
				view.setTag(null);
				setImage(view, imageInfo, bitmap);
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
		public void onBitmapLoaded(View imageView, ImageInfo imageInfo, Bitmap bitmap, boolean isImageView);
	}
}