package com.code44.imageloader.getter;

import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.getter.data.ResourceBitmapData;
import com.code44.imageloader.getter.parser.BitmapParser;
import com.code44.imageloader.info.ResourceBitmapInfo;

/**
 * Singleton. Creates an instance of {@link ResourceBitmapData}. Use this for any {@link BitmapParser} that can handle this type of instance.
 * 
 * @author Mantas Varnagiris
 */
public class ResourceBitmapDataGetter implements BitmapGetter
{
	// Singleton
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static ResourceBitmapDataGetter	instance	= null;

	public static ResourceBitmapDataGetter getDefault()
	{
		if (instance == null)
			instance = new ResourceBitmapDataGetter();
		return instance;
	}

	public ResourceBitmapDataGetter()
	{
	}

	// BitmapDataGetter
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public BitmapData getBitmapData(ImageInfo imageInfo)
	{
		return new ResourceBitmapData(((ResourceBitmapInfo) imageInfo.getBitmapInfo()).getResId());
	}
}