package com.code44.imageloader;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.code44.imageloader.ImageLoader.GetBitmapTask;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.info.BitmapInfo;

/**
 * This object is created when you call {@link ImageLoader#loadImage(ImageView, BitmapInfo)} or
 * {@link ImageLoader#loadImage(ImageView, BitmapInfo, ImageSettings)}. It holds all necessary information for background task to load an image from anywhere
 * you specify.
 * <p>
 * Usually you don't use this class directly.
 * </p>
 * 
 * @author Mantas Varnagiris
 */
public class ImageInfo
{
	protected final WeakReference<View>	viewReference;
	protected final BitmapInfo			bitmapInfo;
	protected final ImageSettings		imageSettings;
	protected final boolean				isImageView;
	protected final boolean				isLoggingOn;

	/**
	 * Constructor
	 * 
	 * @param imageView
	 *            {@link ImageView} in which to load bitmap.
	 * @param bitmapInfo
	 *            Information about how to load a bitmap.
	 * @param imageSettings
	 *            Various settings that change behavior how bitmap is loaded.
	 * @param isLoggingOn
	 *            Flag that tells how to set an image.
	 * @param isLoggingOn
	 *            Flag to turn on/off logging.
	 */
	public ImageInfo(View view, BitmapInfo bitmapInfo, ImageSettings imageSettings, boolean isImageView, boolean isLoggingOn)
	{
		this.viewReference = new WeakReference<View>(view);
		this.bitmapInfo = bitmapInfo;
		this.imageSettings = imageSettings;
		this.isImageView = isImageView;
		this.isLoggingOn = isLoggingOn;
	}

	// Object
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;

		if (o == null || o.getClass() != this.getClass())
			return false;

		final ImageInfo imageInfo = (ImageInfo) o;

		// ImageView is always not null and always the same when we reach this method from cancelPotentialWork(). No need to check here

		return imageInfo.getImageSettings().equals(this.getImageSettings()) && imageInfo.getBitmapInfo().equals(this.getBitmapInfo());
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;

		result = prime * result + imageSettings.hashCode();
		result = prime * result + bitmapInfo.hashCode();

		return result;
	}

	@Override
	public String toString()
	{
		return bitmapInfo.toString();
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * View is stored using {@link WeakReference} so it might be already recycled.
	 * 
	 * @return {@link View} or {@code null}.
	 */
	public View getView()
	{
		if (viewReference != null)
			return viewReference.get();

		return null;
	}

	/**
	 * @return {@link BitmapInfo}.
	 */
	public BitmapInfo getBitmapInfo()
	{
		return bitmapInfo;
	}

	/**
	 * @return {@link ImageSettings}.
	 */
	public ImageSettings getImageSettings()
	{
		return imageSettings;
	}

	/**
	 * @return {@code true} if view is an instance of {@link ImageView}; {@code false} otherwise.
	 */
	public boolean isImageView()
	{
		return isImageView;
	}

	/**
	 * @return {@code true} if logging is on; {@code false} otherwise.
	 */
	public boolean isLoggingOn()
	{
		return isLoggingOn;
	}

	/**
	 * Wrapper method. Loads {@link BitmapData} from {@link BitmapInfo}.
	 * 
	 * @param context
	 *            Context.
	 * @return {@link BitmapData} or {@code null} when loading failed.
	 */
	public BitmapData loadBitmapData(Context context)
	{
		return bitmapInfo.getBitmapDataGetter(context).getBitmapData(this);
	}

	/**
	 * Wrapper method. Parses {@link BitmapData} into actual {@link Bitmap} using parser from {@link BitmapInfo}.
	 * 
	 * @param context
	 *            Context.
	 * @param bitmapData
	 *            {@link BitmapData} to parse.
	 * @return {@link Bitmap} or {@code null} when parsing fails.
	 */
	public Bitmap parseBitmapData(Context context, BitmapData bitmapData)
	{
		return bitmapInfo.getBitmapParser(context).parseBitmap(this, bitmapData);
	}

	/**
	 * Cancels task for this {@link ImageView} if {@link ImageInfo} is different
	 * 
	 * @return {@code true} if {@link ImageView} doesn't have a task or task is working for different {@link ImageInfo}; {@code false} otherwise.
	 */
	public boolean cancelPotentialWork()
	{
		final GetBitmapTask task = getGetBitmapTask();

		if (task != null)
		{
			final ImageInfo imageInfo = task.getImageInfo();
			if (!imageInfo.equals(this))
				task.cancel(true);
			else
				return false;
		}

		return true;
	}

	/**
	 * @return {@link GetBitmapTask} or {@code null} if {@link View} has been recycled.
	 */
	public GetBitmapTask getGetBitmapTask()
	{
		View imageView = getView();
		if (imageView != null)
			return (GetBitmapTask) imageView.getTag();

		return null;
	}

	/**
	 * This is unique name that contains important info about bitmap. This name is used as a key to store/retrieve image in/from memory and file cache.
	 * 
	 * @return
	 */
	public String getCacheName()
	{
		return getCacheName(imageSettings.getSettingsName(), bitmapInfo.getBitmapName());
	}

	/**
	 * This name contains all information about how image is processed.
	 * 
	 * @return
	 */
	public String getSettingsName()
	{
		return imageSettings.getSettingsName();
	}

	/**
	 * This is just a simple bitmap name that comes from {@link BitmapInfo}. This is used for original file cache.
	 * 
	 * @return
	 */
	public String getBitmapName()
	{
		return bitmapInfo.getBitmapName();
	}

	// Static methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static String getCacheName(String settingsName, String bitmapName)
	{
		return settingsName + "__" + bitmapName;
	}
}