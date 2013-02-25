package com.code44.imageloader.getter.data;

import java.io.File;

import com.code44.imageloader.getter.parser.BitmapParser;

/**
 * Contains information about bitmap in resource. {@link BitmapParser} should take care of resource decoding.
 * 
 * @author Mantas Varnagiris
 */
public class ResourceBitmapData extends BitmapData
{
	private final int	resId;

	public ResourceBitmapData(int resId)
	{
		super(false);
		this.resId = resId;
	}

	// BitmapData
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public File getFileForOriginalCache()
	{
		return null;
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public int getResId()
	{
		return resId;
	}
}