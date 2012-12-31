package com.code44.imageloader.processor;

import android.graphics.Bitmap;

/**
 * An interface specifying a way to process an image. This is applied after bitmap is loaded but before it is put in file cache and memory cache.
 * 
 * @author Mantas Varnagiris
 */
public interface ImageProcessor
{
	/**
	 * Modifies bitmap. Should recycle all bitmaps that are not returned in this method here.
	 * 
	 * @param bitmap
	 *            Bitmap to modify.
	 * @return A modified bitmap.
	 */
	public Bitmap processImage(Bitmap bitmap);

	/**
	 * Generates ID that uniquely identifies ImageProcessor.
	 * 
	 * @return Unique class ID.
	 */
	public String getUniqueId();
}