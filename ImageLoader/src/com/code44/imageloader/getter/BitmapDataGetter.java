package com.code44.imageloader.getter;

import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.getter.parser.BitmapParser;

/**
 * Interface that defines how {@link BitmapData} is retrieved.
 * 
 * @author Mantas Varnagiris
 */
public interface BitmapDataGetter
{
	public static final String	TAG	= ImageLoader.TAG + " - ImageGetter";

	/**
	 * Create any instance of {@link BitmapData} here. This object will be handled by an instance of {@link BitmapParser}.
	 * 
	 * @param imageInfo
	 *            Information about image that needs to be loaded.
	 * @return Instance of {@link BitmapData}.
	 */
	public BitmapData getBitmapData(ImageInfo imageInfo);
}