package com.code44.imageloader.info;

import android.content.Context;
import android.webkit.URLUtil;

import com.code44.imageloader.getter.BitmapGetter;
import com.code44.imageloader.getter.URLBitmapDataGetter;

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
	public BitmapGetter getBitmapDataGetter(Context context)
	{
		return URLBitmapDataGetter.getDefault(context);
	}
}