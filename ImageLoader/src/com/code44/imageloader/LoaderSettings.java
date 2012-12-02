package com.code44.imageloader;

public class LoaderSettings
{
	private boolean	isLoggingOn	= false;

	// Constructors
	// ------------------------------------------------------------------------------------------------------------------------------------

	public LoaderSettings()
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
		private LoaderSettings	loaderSettings	= new LoaderSettings();

		// Public methods
		// ------------------------------------------------------------------------------------------------------------------------------------

		public LoaderSettings build()
		{
			return loaderSettings;
		}

		public Builder withLogginOn(boolean isLoggingOn)
		{
			loaderSettings.isLoggingOn = isLoggingOn;
			return this;
		}
	}
}