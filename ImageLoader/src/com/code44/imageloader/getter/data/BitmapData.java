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
	protected final boolean	deleteFile;

	/**
	 * @param deleteFile
	 *            If {@code true} file will be deleted when it will be stored in original file cache
	 */
	public BitmapData(boolean deleteFile)
	{
		this.deleteFile = deleteFile;
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * @return {@code true} if file needs to be deleted after it is stored in original file cache.
	 */
	public boolean isDeleteFile()
	{
		return deleteFile;
	}

	// Abstract methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This is used whenever original file cache is used. Create a temporary file here if necessary.
	 * 
	 * @return {@link File} or {@code null} if you don't want to use original file cache.
	 */
	public abstract File getFileForOriginalCache();
}