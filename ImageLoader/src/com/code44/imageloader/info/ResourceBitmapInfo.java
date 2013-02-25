package com.code44.imageloader.info;

import android.content.Context;

import com.code44.imageloader.getter.BitmapGetter;
import com.code44.imageloader.getter.ResourceBitmapDataGetter;
import com.code44.imageloader.getter.parser.BitmapParser;
import com.code44.imageloader.getter.parser.ResourceBitmapParser;
import com.code44.imageloader.utils.StringUtils;

public class ResourceBitmapInfo extends BitmapInfo
{
	protected final int	resId;

	public ResourceBitmapInfo(int resId)
	{
		this.resId = resId;
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

		final ResourceBitmapInfo rbi = (ResourceBitmapInfo) o;

		return resId == rbi.resId;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;

		result = prime * result + resId;

		return result;
	}

	@Override
	public String toString()
	{
		return "Resource: " + resId;
	}

	// BitmapInfo
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public String getBitmapName()
	{
		return "Resource_ " + StringUtils.md5(String.valueOf(resId));
	}

	@Override
	public boolean checkInfo()
	{
		return resId > 0;
	}

	@Override
	public BitmapGetter getBitmapDataGetter(Context context)
	{
		return ResourceBitmapDataGetter.getDefault();
	}

	@Override
	public BitmapParser getBitmapParser(Context context)
	{
		return ResourceBitmapParser.getDefault();
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public int getResId()
	{
		return resId;
	}
}