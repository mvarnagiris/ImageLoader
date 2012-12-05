package com.code44.imageloader.info;

import android.content.Context;
import android.text.TextUtils;

import com.code44.imageloader.getter.FileBitmapDataGetter;
import com.code44.imageloader.getter.parser.BitmapParser;
import com.code44.imageloader.getter.parser.FileBitmapParser;
import com.code44.imageloader.getter.parser.ScaledBitmapParser.SizeType;

/**
 * Information about bitmap in file system.
 * 
 * @author Mantas Varnagiris
 */
public class FileBitmapInfo extends BitmapInfo
{
	protected final String		filePath;
	protected final SizeType	sizeType;

	public FileBitmapInfo(String filePath, SizeType sizeType)
	{
		this.filePath = filePath;
		this.sizeType = sizeType;
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
	public FileBitmapDataGetter getBitmapDataGetter(Context context)
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