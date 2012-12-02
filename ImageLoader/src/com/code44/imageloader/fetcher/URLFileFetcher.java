package com.code44.imageloader.fetcher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.util.Log;

import com.code44.imageloader.BuildConfig;
import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.info.URLBitmapInfo;

public class URLFileFetcher extends FileFetcher
{
	private final Context			context;

	// Singleton
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static URLFileFetcher	instance	= null;

	public static URLFileFetcher getDefault(Context context)
	{
		if (instance == null)
			instance = new URLFileFetcher(context);
		return instance;
	}

	public URLFileFetcher(Context context)
	{
		this.context = context.getApplicationContext();
	}

	// FileFetcher
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public FileResult getFile(ImageInfo imageInfo)
	{
		final boolean isLoggingOn = BuildConfig.DEBUG && imageInfo.isLoggingOn();

		// Create file to store bitmap
		final File tempFile = new File(context.getCacheDir(), imageInfo.getImageName());

		// Setup connection
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		URL url = null;

		try
		{
			// Download bitmap to temporary file
			url = new URL(((URLBitmapInfo) imageInfo.getBitmapInfo()).getFilePath());

			if (isLoggingOn)
				Log.d(TAG, "Downloading bitmap to temporary file [" + imageInfo.toString() + "]");

			urlConnection = (HttpURLConnection) url.openConnection();
			final InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			out = new BufferedOutputStream(new FileOutputStream(tempFile));

			int b;
			while ((b = in.read()) != -1)
			{
				out.write(b);
			}
			in.close();

			return new FileResult(tempFile, true);
		}
		catch (final MalformedURLException e)
		{
			if (isLoggingOn)
				Log.w(ImageLoader.TAG, "Bad url [" + imageInfo.toString() + "]", e);
		}
		catch (final IOException e)
		{
			if (isLoggingOn)
				Log.w(ImageLoader.TAG, "Failed downloading bitmap to file [" + imageInfo.toString() + "]");
		}
		finally
		{
			// Close connection
			if (urlConnection != null)
				urlConnection.disconnect();

			// Close stream
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}