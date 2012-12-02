package com.code44.imageloader;

import java.io.File;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.widget.ImageView;

import com.code44.imageloader.ImageLoader.GetBitmapTask;
import com.code44.imageloader.fetcher.FileFetcher;
import com.code44.imageloader.fetcher.FileResult;
import com.code44.imageloader.info.BitmapInfo;

public class ImageInfo
{
	protected final WeakReference<ImageView>	imageViewReference;
	protected final BitmapInfo					bitmapInfo;
	protected final ImageSettings				imageSettings;
	protected final boolean						isLoggingOn;

	public ImageInfo(ImageView imageView, BitmapInfo bitmapInfo, ImageSettings imageSettings, boolean isLoggingOn)
	{
		this.imageViewReference = new WeakReference<ImageView>(imageView);
		this.bitmapInfo = bitmapInfo;
		this.imageSettings = imageSettings;
		this.isLoggingOn = isLoggingOn;
	}

	@Override
	public boolean equals(Object o)
	{
		// TODO Implement
		return super.equals(o);
	}

	@Override
	public int hashCode()
	{
		// TODO Implement
		return super.hashCode();
	}

	@Override
	public String toString()
	{
		// TODO Implement
		return super.toString();
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * ImageView is stored using {@link WeakReference} so it might be already recycled.
	 * 
	 * @return {@link ImageView} or {@code null}.
	 */
	public ImageView getImageView()
	{
		if (imageViewReference != null)
			return imageViewReference.get();

		return null;
	}

	public BitmapInfo getBitmapInfo()
	{
		return bitmapInfo;
	}

	public ImageSettings getImageSettings()
	{
		return imageSettings;
	}

	public boolean isLoggingOn()
	{
		return isLoggingOn;
	}

	/**
	 * Wrapper method that just calls {@link FileFetcher#getFile(ImageInfo)}.
	 * 
	 * @return {@link File} that is returned from {@link FileFetcher#getFile(ImageInfo)}.
	 */
	public FileResult fetchBitmapFile(Context context)
	{
		return bitmapInfo.getFileFetcher(context).getFile(this);
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

	public GetBitmapTask getGetBitmapTask()
	{
		ImageView imageView = getImageView();
		if (imageView != null)
			return (GetBitmapTask) imageView.getTag();

		return null;
	}

	public String getImageName()
	{
		return null;
	}
}