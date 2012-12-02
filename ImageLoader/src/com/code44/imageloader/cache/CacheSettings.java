package com.code44.imageloader.cache;

public class CacheSettings
{
	private boolean	isLoggingOn	= false;

	// Constructors
	// ------------------------------------------------------------------------------------------------------------------------------------

	public CacheSettings()
	{
		this.isLoggingOn = false;
	}

	// Getters/Setters
	// ------------------------------------------------------------------------------------------------------------------------------------

	public boolean isLoggingOn()
	{
		return isLoggingOn;
	}

	// Builder
	// ------------------------------------------------------------------------------------------------------------------------------------

	public static class Builder
	{
		private CacheSettings	cacheSettings	= new CacheSettings();

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
	}
}