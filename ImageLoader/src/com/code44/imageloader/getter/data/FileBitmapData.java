package com.code44.imageloader.getter.data;

import java.io.File;

import com.code44.imageloader.getter.parser.BitmapParser;

/**
 * Contains information about bitmap in file. {@link BitmapParser} should take care of file decoding.
 * 
 * @author Mantas Varnagiris
 */
public class FileBitmapData extends BitmapData
{
	private final File	file;

	public FileBitmapData(File file, boolean deleteFile)
	{
		super(deleteFile);
		this.file = file;
	}

	// BitmapData
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public File getFile()
	{
		return file;
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public boolean isDeleteFile()
	{
		return deleteFile;
	}
}