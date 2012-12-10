package com.code44.imageloader.getter.data;

import java.io.File;

import android.graphics.Bitmap;

import com.code44.imageloader.getter.parser.BitmapParser;

/**
 * Derived classes should contain all necessary information for {@link BitmapParser} to create {@link Bitmap}.
 * 
 * @author Mantas Varnagiris
 */
public abstract class BitmapData
{
	// Abstract methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public abstract File getFile();
}