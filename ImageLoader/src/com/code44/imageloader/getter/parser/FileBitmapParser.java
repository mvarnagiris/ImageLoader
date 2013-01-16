package com.code44.imageloader.getter.parser;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.getter.data.FileBitmapData;

/**
 * Decodes scaled bitmap from file. Also deletes file if necessary.
 * 
 * @author Mantas Varnagiris
 */
public class FileBitmapParser extends ScaledBitmapParser
{
	// Singleton
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static FileBitmapParser	instance;

	public static FileBitmapParser getDefault()
	{
		if (instance == null)
			instance = new FileBitmapParser();
		return instance;
	}

	protected FileBitmapParser()
	{
	}

	// ScaledBitmapParser
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	protected Bitmap decodeBitmap(ImageInfo imageInfo, BitmapData bitmapData, Options options)
	{
		final File bitmapFile = ((FileBitmapData) bitmapData).getFile();
		if (bitmapFile.exists())
			return BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
		return null;
	}

	@Override
	public Bitmap parseBitmapFromFile(ImageInfo imageInfo, FileBitmapData bitmapData)
	{
		return parseBitmap(imageInfo, bitmapData);
	}
}