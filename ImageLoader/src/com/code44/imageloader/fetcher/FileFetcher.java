package com.code44.imageloader.fetcher;

import java.io.File;

import android.util.Log;

import com.code44.imageloader.BuildConfig;
import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.info.FileBitmapInfo;

public class FileFetcher
{
	public static final String	TAG			= ImageLoader.TAG + " - FileFetcher";

	// Singleton
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static FileFetcher	instance	= null;

	public static FileFetcher getDefault()
	{
		if (instance == null)
			instance = new FileFetcher();
		return instance;
	}

	public FileFetcher()
	{
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public FileResult getFile(ImageInfo imageInfo)
	{
		final File file = new File(((FileBitmapInfo) imageInfo.getBitmapInfo()).getFilePath());
		if (!file.exists() || file.isDirectory())
		{
			if (BuildConfig.DEBUG && imageInfo.isLoggingOn())
				Log.w(TAG, "File not found [" + imageInfo.toString() + "]");
			return null;
		}

		return new FileResult(file, false);
	}
}