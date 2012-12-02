package com.code44.imageloader.fetcher;

import java.io.File;

/**
 * Result that comes from {@link FileFetcher}. If {@link #isDeleteFile()} is {@code true} then file will be deleted after bitmap is retrieved from it.
 * 
 * @author Mantas Varnagiris
 */
public class FileResult
{
	private final File		file;
	private final boolean	deleteFile;

	public FileResult(File file, boolean deleteFile)
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