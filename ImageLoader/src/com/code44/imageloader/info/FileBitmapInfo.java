package com.code44.imageloader.info;

import android.content.Context;
import android.text.TextUtils;

import com.code44.imageloader.getter.BitmapDataGetter;
import com.code44.imageloader.getter.FileBitmapDataGetter;
import com.code44.imageloader.getter.parser.BitmapParser;
import com.code44.imageloader.getter.parser.FileBitmapParser;
import com.code44.imageloader.getter.parser.ScaledBitmapParser.SizeType;
import com.code44.imageloader.utils.StringUtils;

/**
 * Use this when you want to load bitmap from file system.
 * 
 * @author Mantas Varnagiris
 */
public class FileBitmapInfo extends BitmapInfo
{
	protected final String		filePath;
	protected final SizeType	sizeType;

	public FileBitmapInfo(String filePath)
	{
		this.filePath = filePath;
		this.sizeType = SizeType.FILL_CROP;
	}

	public FileBitmapInfo(String filePath, SizeType sizeType)
	{
		this.filePath = filePath;
		this.sizeType = sizeType;
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

		return filePath != null && filePath.equals(fbi.filePath) && sizeType.equals(fbi.sizeType);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;

		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + sizeType.ordinal();

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
	public BitmapDataGetter getBitmapDataGetter(Context context)
	{
		return FileBitmapDataGetter.getDefault();
	}

	@Override
	public BitmapParser getBitmapParser(Context context)
	{
		return FileBitmapParser.getDefault(sizeType);
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public String getFilePath()
	{
		return filePath;
	}
}