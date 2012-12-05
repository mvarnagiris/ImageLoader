package com.code44.imageloader.getter.data;

import java.io.File;

import com.code44.imageloader.getter.parser.BitmapParser;

/**
 * Contains information about bitmap in file. {@link BitmapParser} should take care of file decoding and file deleting if necessary.
 * 
 * @author Mantas Varnagiris
 */
public class FileBitmapData extends BitmapData
{
	private final File		file;
	private final boolean	deleteFile;

	public FileBitmapData(File file, boolean deleteFile)
	{
		this.file = file;
		this.deleteFile = deleteFile;
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public File getFile()
	{
		return file;
	}

	public boolean isDeleteFile()
	{
		return deleteFile;
	}
}