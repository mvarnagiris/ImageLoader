package com.code44.imageloader;

import android.graphics.drawable.Drawable;

import com.code44.imageloader.processor.ImageProcessor;

/**
 * Holds information (size, loading/error drawables, image processors, etc.) about bitmap.
 * 
 * @author Mantas Varnagiris
 */
public class ImageSettings
{
	protected int				width			= 0;
	protected int				height			= 0;
	protected int				downSampleBy	= 0;
	protected Drawable			loadingDrawable	= null;
	protected Drawable			errorDrawable	= null;
	protected ImageProcessor	imageProcessor	= null;
	protected String			fileFolder		= null;
	protected boolean			useMemoryCache	= true;
	protected boolean			useFileCache	= true;

	// Object
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;

		if (o == null || o.getClass() != this.getClass())
			return false;

		final ImageSettings imageSettings = (ImageSettings) o;

		return width == imageSettings.width
				&& height == imageSettings.height
				&& downSampleBy == imageSettings.downSampleBy
				&& (loadingDrawable != null && loadingDrawable.equals(imageSettings.loadingDrawable))
				&& (errorDrawable != null && errorDrawable.equals(imageSettings.errorDrawable))
				&& (imageProcessor != null && imageSettings.imageProcessor != null && imageProcessor.getUniqueId().equals(
						imageSettings.imageProcessor.getUniqueId())) && (fileFolder != null && fileFolder.equals(imageSettings.fileFolder))
				&& useMemoryCache == imageSettings.useMemoryCache && useFileCache == imageSettings.useFileCache;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;

		result = prime * result + width;
		result = prime * result + height;
		result = prime * result + downSampleBy;
		result = prime * result + ((loadingDrawable == null) ? 0 : loadingDrawable.hashCode());
		result = prime * result + ((errorDrawable == null) ? 0 : errorDrawable.hashCode());
		result = prime * result + ((imageProcessor == null) ? 0 : imageProcessor.hashCode());
		result = prime * result + ((fileFolder == null) ? 0 : fileFolder.hashCode());
		result = prime * result + (useMemoryCache ? 1 : 0);
		result = prime * result + (useFileCache ? 1 : 0);

		return result;
	}

	// Getters/Setters
	// ------------------------------------------------------------------------------------------------------------------------------------

	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public int getWidth()
	{
		return width;
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

	/**
	 * If {@link #getErrorDrawable()} is {@code null} then {@code loadingDrawable} will be set to {@link #errorDrawable} as well.
	 * 
	 * @param loadingDrawable
	 */
	public void setLoadingDrawable(Drawable loadingDrawable)
	{
		this.loadingDrawable = loadingDrawable;
		if (this.errorDrawable == null)
			this.errorDrawable = loadingDrawable;
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

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This will be a part of bitmap name in cache.
	 * 
	 * @return Settings string.
	 */
	public String getSettingsName()
	{
		return width + ";;" + height + ";;" + downSampleBy + ";;" + (imageProcessor == null ? "null" : imageProcessor.getUniqueId());
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

		public Builder withSize(int width, int height)
		{
			imageSettings.width = width;
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