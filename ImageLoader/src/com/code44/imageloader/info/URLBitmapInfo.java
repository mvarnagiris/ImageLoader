package com.code44.imageloader.info;

import com.code44.imageloader.getter.FileBitmapDataGetter;
import com.code44.imageloader.getter.parser.ScaledBitmapParser.SizeType;

import android.content.Context;
import android.webkit.URLUtil;

public class URLBitmapInfo extends FileBitmapInfo
{
	public URLBitmapInfo(String url, SizeType sizeType)
	{
		super(url, sizeType);
	}

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
	public FileBitmapDataGetter getBitmapDataGetter(Context context)
	{
		return super.getBitmapDataGetter(context);
	}
}