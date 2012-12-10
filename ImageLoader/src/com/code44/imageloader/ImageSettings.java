package com.code44.imageloader;

import android.graphics.drawable.Drawable;

import com.code44.imageloader.processor.ImageProcessor;

public class ImageSettings
{
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

	// Getters/Setters
	// ------------------------------------------------------------------------------------------------------------------------------------

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getWidth()
	{
		return width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public int getHeight()
	{
		return height;
	}

	public void setDownSampleBy(int downSampleBy)
	{
		this.downSampleBy = downSampleBy;
	}

	public int getDownSampleBy()
	{
		return downSampleBy;
	}

	public void setLoadingDrawable(Drawable loadingDrawable)
	{
		this.loadingDrawable = loadingDrawable;
		// TODO If error drawable is null, set error drawable as well
	}

	public Drawable getLoadingDrawable()
	{
		return loadingDrawable;
	}

	public void setErrorDrawable(Drawable errorDrawable)
	{
		this.errorDrawable = errorDrawable;
	}

	public Drawable getErrorDrawable()
	{
		return errorDrawable;
	}

	public void setImageProcessor(ImageProcessor imageProcessor)
	{
		this.imageProcessor = imageProcessor;
	}

	public ImageProcessor getImageProcessor()
	{
		return imageProcessor;
	}

	public void setFileFolder(String fileFolder)
	{
		this.fileFolder = fileFolder;
	}

	public String getFileFolder()
	{
		return fileFolder;
	}

	public void setShowSmallerIfAvailable(boolean showSmallerIfAvailable)
	{
		this.showSmallerIfAvailable = showSmallerIfAvailable;
	}

	public boolean isShowSmallerIfAvailable()
	{
		return showSmallerIfAvailable;
	}

	public void setUseMemoryCache(boolean useMemoryCache)
	{
		this.useMemoryCache = useMemoryCache;
	}

	public boolean isUseMemoryCache()
	{
		return useMemoryCache;
	}

	public void setUseFileCache(boolean useFileCache)
	{
		this.useFileCache = useFileCache;
	}

	public boolean isUseFileCache()
	{
		return useFileCache;
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

	}
}