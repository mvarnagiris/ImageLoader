package com.code44.imageloader.info;

import com.code44.imageloader.fetcher.FileFetcher;

import android.content.Context;
import android.text.TextUtils;

/**
 * Information about bitmap in file system.
 * 
 * @author Mantas Varnagiris
 */
public class FileBitmapInfo extends BitmapInfo
{
	protected final String	filePath;

	public FileBitmapInfo(String filePath)
	{
		this.filePath = filePath;
	}

	@Override
	public String toString()
	{
		return "File: " + filePath;
	}

	// BitmapInfo
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean checkInfo()
	{
		return !TextUtils.isEmpty(filePath);
	}

	@Override
	public FileFetcher getFileFetcher(Context context)
	{
		return FileFetcher.getDefault();
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public String getFilePath()
	{
		return filePath;
	}
}