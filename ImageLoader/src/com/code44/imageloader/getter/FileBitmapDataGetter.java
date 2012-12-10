package com.code44.imageloader.getter;

import java.io.File;

import android.util.Log;

import com.code44.imageloader.BuildConfig;
import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.getter.data.FileBitmapData;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.getter.parser.BitmapParser;
import com.code44.imageloader.info.FileBitmapInfo;

/**
 * Singleton. Creates an instance of {@link FileBitmapData}. Use this for any {@link BitmapParser} that can handle this type of instance.
 * 
 * @author Mantas Varnagiris
 */
public class FileBitmapDataGetter implements BitmapDataGetter
{
	// Singleton
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static FileBitmapDataGetter	instance	= null;

	public static FileBitmapDataGetter getDefault()
	{
		if (instance == null)
			instance = new FileBitmapDataGetter();
		return instance;
	}

	public FileBitmapDataGetter()
	{
	}

	// BitmapDataGetter
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public BitmapData getBitmapData(ImageInfo imageInfo)
	{
		final File file = new File(((FileBitmapInfo) imageInfo.getBitmapInfo()).getFilePath());
		if (!file.exists() || file.isDirectory())
		{
			if (BuildConfig.DEBUG && imageInfo.isLoggingOn())
				Log.w(TAG, "File not found [" + imageInfo.toString() + "]");
			return null;
		}

		return new FileBitmapData(file, false);
	}
}