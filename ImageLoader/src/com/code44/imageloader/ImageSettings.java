package com.code44.imageloader;

import android.graphics.drawable.Drawable;

import com.code44.imageloader.processor.ImageProcessor;

public class ImageSettings
{
	public enum SizeType
	{
		MAX, FIT, FILL
	}

	protected int				width					= 0;
	protected int				height					= 0;
	protected int				downSampleBy			= 0;
	protected Drawable			loadingDrawable			= null;
	protected Drawable			errorDrawable			= null;
	protected ImageProcessor	imageProcessor			= null;
	protected String			fileFolder				= null;
	protected boolean			showSmallerIfAvailable	= true;
	protected boolean			useMemoryCache			= true;
	protected boolean			useFileCache			= true;
	protected SizeType			sizeType				= SizeType.FILL;

	// Getters/Setters
	// ------------------------------------------------------------------------------------------------------------------------------------

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getDownSampleBy()
	{
		return downSampleBy;
	}

	public Drawable getLoadingDrawable()
	{
		return loadingDrawable;
	}

	public Drawable getErrorDrawable()
	{
		return errorDrawable;
	}

	public ImageProcessor getImageProcessor()
	{
		return imageProcessor;
	}

	public String getFileFolder()
	{
		return fileFolder;
	}

	public boolean isShowSmallerIfAvailable()
	{
		return showSmallerIfAvailable;
	}

	public boolean isUseMemoryCache()
	{
		return useMemoryCache;
	}

	public boolean isUseFileCache()
	{
		return useFileCache;
	}

	public SizeType getSizeType()
	{
		return sizeType;
	}

	// Builder
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static class Builder
	{
		private ImageSettings	imageSettings	= new ImageSettings();

		// Public methods
		// ------------------------------------------------------------------------------------------------------------------------------------

		public ImageSettings build()
		{
			if (imageSettings.errorDrawable == null)
				imageSettings.errorDrawable = imageSettings.loadingDrawable;
			return imageSettings;
		}

		public Builder withWidth(int width)
		{
			imageSettings.width = width;
			return this;
		}

		public Builder withHeight(int height)
		{
			imageSettings.height = height;
			return this;
		}

		public Builder withDownSampleBy(int downSampleBy)
		{
			imageSettings.downSampleBy = downSampleBy;
			return this;
		}

		public Builder withLoadingDrawable(Drawable loadingDrawable)
		{
			imageSettings.loadingDrawable = loadingDrawable;
			return this;
		}

		public Builder withErrorDrawable(Drawable errorDrawable)
		{
			imageSettings.errorDrawable = errorDrawable;
			return this;
		}

		public Builder withImageProcessor(ImageProcessor imageProcessor)
		{
			imageSettings.imageProcessor = imageProcessor;
			return this;
		}

		public Builder withFileFolder(String fileFolder)
		{
			imageSettings.fileFolder = fileFolder;
			return this;
		}

		public Builder withShowSmallerIfAvailable(boolean showSmallerIfAvailable)
		{
			imageSettings.showSmallerIfAvailable = showSmallerIfAvailable;
			return this;
		}

		public Builder withUseMemoryCache(boolean useMemoryCache)
		{
			imageSettings.useMemoryCache = useMemoryCache;
			return this;
		}

		public Builder withUseFileCache(boolean useFileCache)
		{
			imageSettings.useFileCache = useFileCache;
			return this;
		}

		public Builder withSizeType(SizeType sizeType)
		{
			imageSettings.sizeType = sizeType;
			return this;
		}
	}
}