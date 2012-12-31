package com.code44.imageloader.cache;

import android.app.ActivityManager;
import android.content.Context;

public class CacheSettings
{
	private boolean	isLoggingOn;
	private int		memoryCacheSize;

	// Constructors
	// ------------------------------------------------------------------------------------------------------------------------------------

	public CacheSettings(Context context)
	{
		this.isLoggingOn = false;
		setMemoryCacheSizePercent(context, 0.25f);
	}

	// Getters/Setters
	// ------------------------------------------------------------------------------------------------------------------------------------

	public boolean isLoggingOn()
	{
		return isLoggingOn;
	}

	/**
	 * Sets the memory cache size based on a percentage of the device memory class. Eg. setting percent to 0.2 would set the memory cache to one fifth of the
	 * device memory class. Throws {@link IllegalArgumentException} if percent is < 0.05 or > .8. This value should be chosen carefully based on a number of
	 * factors Refer to the corresponding Android Training class for more discussion: http://developer.android.com/training/displaying-bitmaps/
	 * 
	 * @param context
	 *            Context to use to fetch memory class
	 * @param percent
	 *            Percent of memory class to use to size memory cache
	 */
	public void setMemoryCacheSizePercent(Context context, float percent)
	{
		if (percent < 0.05f || percent > 0.8f)
		{
			throw new IllegalArgumentException("setMemoryCacheSizePercent - percent must be " + "between 0.05 and 0.8 (inclusive)");
		}
		memoryCacheSize = Math.round(percent * ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() * 1024 * 1024);
	}

	public int getMemoryCacheSize()
	{
		return memoryCacheSize;
	}

	// Builder
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static class Builder
	{
		private CacheSettings	cacheSettings;

		public Builder(Context context)
		{
			cacheSettings = new CacheSettings(context);
		}

		// Public methods
		// ------------------------------------------------------------------------------------------------------------------------------------

		public CacheSettings build()
		{
			return cacheSettings;
		}

		public Builder withLogginOn(boolean isLoggingOn)
		{
			cacheSettings.isLoggingOn = isLoggingOn;
			return this;
		}

		public Builder withMemoryCacheSizePercent(Context context, float percent)
		{
			cacheSettings.setMemoryCacheSizePercent(context, percent);
			return this;
		}
	}
}