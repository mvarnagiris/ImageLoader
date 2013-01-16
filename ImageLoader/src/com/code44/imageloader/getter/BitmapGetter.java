package com.code44.imageloader.getter;

import android.graphics.Bitmap;

import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.getter.parser.BitmapParser;

/**
 * Interface that defines how {@link Bitmap} is retrieved.
 * 
 * @author Mantas Varnagiris
 */
public interface BitmapGetter
{
	public static final String	TAG	= ImageLoader.TAG + " - BitmapGetter";

	/**
	 * Create any instance of {@link BitmapData} here. This object will be handled by an instance of {@link BitmapParser}.
	 * 
	 * @param imageInfo
	 *            Information about image that needs to be loaded.
	 * @return Instance of {@link BitmapData}.
	 */
	public BitmapData getBitmapData(ImageInfo imageInfo);
}