package com.code44.imageloader.info;

import android.content.Context;
import android.text.TextUtils;

import com.code44.imageloader.getter.BitmapGetter;
import com.code44.imageloader.getter.FileBitmapDataGetter;
import com.code44.imageloader.getter.parser.BitmapParser;
import com.code44.imageloader.getter.parser.FileBitmapParser;
import com.code44.imageloader.utils.StringUtils;

/**
 * Use this when you want to load bitmap from file system.
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

	// Object
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;

		if (o == null || o.getClass() != this.getClass())
			return false;

		final FileBitmapInfo fbi = (FileBitmapInfo) o;

		return filePath != null && filePath.equals(fbi.filePath);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;

		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());

		return result;
	}

	@Override
	public String toString()
	{
		return "File: " + filePath;
	}

	// BitmapInfo
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public String getBitmapName()
	{
		return StringUtils.md5(filePath);
	}

	@Override
	public boolean checkInfo()
	{
		return !TextUtils.isEmpty(filePath);
	}

	@Override
	public BitmapGetter getBitmapDataGetter(Context context)
	{
		return FileBitmapDataGetter.getDefault();
	}

	@Override
	public BitmapParser getBitmapParser(Context context)
	{
		return FileBitmapParser.getDefault();
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public String getFilePath()
	{
		return filePath;
	}
}