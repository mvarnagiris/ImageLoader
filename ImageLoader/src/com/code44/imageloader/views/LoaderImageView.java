package com.code44.imageloader.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.ImageSettings;
import com.code44.imageloader.info.BitmapInfo;

public class LoaderImageView extends ImageView
{
	protected final ImageLoader	imageLoader;
	protected ImageSettings		imageSettings	= null;
	protected BitmapInfo		bitmapInfo		= null;

	public LoaderImageView(Context context)
	{
		this(context, null);
	}

	public LoaderImageView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public LoaderImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		if (!isInEditMode())
			imageLoader = new ImageLoader(context);
		else
			imageLoader = null;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		if (w > 0 && h > 0)
		{
			if (imageSettings == null)
				imageSettings = new ImageSettings();

			boolean needLoadImages = imageSettings.getWidth() == 0 && bitmapInfo != null;
			imageSettings.setSize(w, h);
			if (needLoadImages)
			{
				onBeforeLoad();
				imageLoader.loadImage(this, bitmapInfo, imageSettings);
			}
		}
		else
		{
			if (imageSettings == null)
				imageSettings = new ImageSettings();
			imageSettings.setSize(0, 0);
		}
	}

	// Public methods
	// --------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Loads image using view size.
	 * 
	 * @param bitmapInfo
	 *            Image to load.
	 */
	public void loadImage(BitmapInfo bitmapInfo)
	{
		this.bitmapInfo = bitmapInfo;
		if (imageSettings != null && imageSettings.getWidth() > 0 && bitmapInfo != null)
		{
			onBeforeLoad();
			imageLoader.loadImage(this, bitmapInfo, imageSettings);
		}
	}

	/**
	 * Set default image settings. Width and height values will be ignored and image view size values will be used instead.
	 * 
	 * @param imageSettings
	 */
	public void setImageSettings(ImageSettings imageSettings)
	{
		int width = 0;
		int height = 0;
		if (this.imageSettings != null)
		{
			width = this.imageSettings.getWidth();
			height = this.imageSettings.getHeight();
		}
		this.imageSettings = imageSettings;
		if (this.imageSettings != null)
		{
			this.imageSettings.setSize(width, height);
		}
	}

	// Protected methods
	// --------------------------------------------------------------------------------------------------------------------------------

	protected void onBeforeLoad()
	{
	}
}