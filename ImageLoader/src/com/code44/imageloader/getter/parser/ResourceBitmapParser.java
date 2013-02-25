package com.code44.imageloader.getter.parser;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.view.View;

import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.info.ResourceBitmapInfo;

/**
 * Decodes scaled bitmap from resource.
 * 
 * @author Mantas Varnagiris
 */
public class ResourceBitmapParser extends ScaledBitmapParser
{
	// Singleton
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static ResourceBitmapParser	instance;

	public static ResourceBitmapParser getDefault()
	{
		if (instance == null)
			instance = new ResourceBitmapParser();
		return instance;
	}

	protected ResourceBitmapParser()
	{
	}

	// ScaledBitmapParser
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	protected Bitmap decodeBitmap(ImageInfo imageInfo, BitmapData bitmapData, Options options)
	{
		final int resId = ((ResourceBitmapInfo) imageInfo.getBitmapInfo()).getResId();
		final View view = imageInfo.getView();
		if (resId > 0 && view != null)
			return BitmapFactory.decodeResource(view.getContext().getResources(), resId, options);
		return null;
	}

	@Override
	public Bitmap parseFromFile(ImageInfo imageInfo, File file)
	{
		return null;
	}
}