package com.code44.imageloader.getter.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.getter.data.FileBitmapData;

/**
 * Decodes scaled bitmap from file.
 * 
 * @author Mantas Varnagiris
 */
public class FileBitmapParser extends ScaledBitmapParser
{
	// Singleton
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static final Map<SizeType, FileBitmapParser>	instances	= new HashMap<SizeType, FileBitmapParser>();

	public static FileBitmapParser getDefault(SizeType sizeType)
	{
		FileBitmapParser instance = instances.get(sizeType);
		if (instance == null)
		{
			instance = new FileBitmapParser(sizeType);
			instances.put(sizeType, instance);
		}
		return instance;
	}

	public FileBitmapParser(SizeType sizeType)
	{
		super(sizeType);
	}

	// ScaledBitmapParser
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	protected Bitmap decodeBitmap(ImageInfo imageInfo, BitmapData bitmapData, Options options)
	{
		return BitmapFactory.decodeFile(((FileBitmapData) bitmapData).getFile().getAbsolutePath(), options);
	}

	@Override
	public Bitmap parseBitmap(ImageInfo imageInfo, BitmapData bitmapData)
	{
		final Bitmap bitmap = super.parseBitmap(imageInfo, bitmapData);

		// Delete file if necessary
		final FileBitmapData fileBitmapData = (FileBitmapData) bitmapData;
		final File file = fileBitmapData.getFile();
		if (fileBitmapData.isDeleteFile() && file != null)
		{
			if (file.delete() && imageInfo.isLoggingOn())
				Log.i(TAG, "Temporary file deleted. [" + imageInfo.toString() + "]");
		}

		return bitmap;
	}
}