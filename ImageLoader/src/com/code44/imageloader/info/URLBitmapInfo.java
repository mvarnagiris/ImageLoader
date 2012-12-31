package com.code44.imageloader.info;

import android.content.Context;
import android.webkit.URLUtil;

import com.code44.imageloader.getter.BitmapDataGetter;
import com.code44.imageloader.getter.URLBitmapDataGetter;
import com.code44.imageloader.getter.parser.ScaledBitmapParser.SizeType;

/**
 * Use this when you want to load bitmap from URL.
 * 
 * @author Mantas Varnagiris
 */
public class URLBitmapInfo extends FileBitmapInfo
{
	public URLBitmapInfo(String url)
	{
		super(url);
	}

	public URLBitmapInfo(String url, SizeType sizeType)
	{
		super(url, sizeType);
	}

	// Object
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public String toString()
	{
		return "URL: " + filePath;
	}

	// FileBitmapInfo
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean checkInfo()
	{
		return super.checkInfo() && URLUtil.isValidUrl(filePath);
	}

	@Override
	public BitmapDataGetter getBitmapDataGetter(Context context)
	{
		return URLBitmapDataGetter.getDefault(context);
	}
}