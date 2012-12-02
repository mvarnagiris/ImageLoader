package com.code44.imageloader.info;

import com.code44.imageloader.fetcher.FileFetcher;

import android.content.Context;
import android.webkit.URLUtil;

public class URLBitmapInfo extends FileBitmapInfo
{
	public URLBitmapInfo(String url)
	{
		super(url);
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
	public FileFetcher getFileFetcher(Context context)
	{
		return super.getFileFetcher(context);
	}
}