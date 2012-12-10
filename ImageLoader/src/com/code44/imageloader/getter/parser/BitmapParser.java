package com.code44.imageloader.getter.parser;

import android.graphics.Bitmap;

import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.getter.data.BitmapData;

/**
 * Interface that defines how {@link BitmapData} is transformed into {@link Bitmap}.
 * 
 * @author Mantas Varnagiris
 */
public interface BitmapParser
{
	public static final String TAG = ImageLoader.TAG + " - BitmapParser";
	
	/**
	 * Parse {@link BitmapData} into {@link Bitmap} here.
	 * 
	 * @param imageInfo
	 *            Image info for this bitmap.
	 * @param bitmapData
	 *            This should contain all data you need to get {@link Bitmap}.
	 * @return {@link Bitmap}.
	 */
	public Bitmap parseBitmap(ImageInfo imageInfo, BitmapData bitmapData);
}