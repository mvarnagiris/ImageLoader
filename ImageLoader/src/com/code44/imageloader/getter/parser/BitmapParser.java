package com.code44.imageloader.getter.parser;

import java.io.File;

import android.graphics.Bitmap;

import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.getter.data.BitmapData;

/**
 * Interface that defines how {@link BitmapData} is transformed into {@link Bitmap}.
 * 
 * @author Mantas Varnagiris
 */
public abstract class BitmapParser
{
	public static final String	TAG	= ImageLoader.TAG + " - BitmapParser";

	// Abstract methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Parse {@link BitmapData} into {@link Bitmap} here.
	 * 
	 * @param imageInfo
	 *            Image info for this bitmap.
	 * @param bitmapData
	 *            This should contain all data you need to get {@link Bitmap}.
	 * @return {@link Bitmap}.
	 */
	public abstract Bitmap parseBitmap(ImageInfo imageInfo, BitmapData bitmapData);

	/**
	 * Parse {@link File} into {@link Bitmap} here. If you don't want to user original file cache, just return {@code null}.
	 * 
	 * @param imageInfo
	 * @param file
	 *            {@link File} from original file cache.
	 * @return {@link Bitmap}.
	 */
	public abstract Bitmap parseFromFile(ImageInfo imageInfo, File file);
}