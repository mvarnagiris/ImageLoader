package com.code44.imageloader.getter;

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
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.getter.data.FileBitmapData;
import com.code44.imageloader.getter.parser.BitmapParser;
import com.code44.imageloader.info.URLBitmapInfo;

/**
 * Singleton. Downloads image from URL and stores it to temporary file (this file should be deleted by {@link BitmapParser}). Creates an instance of
 * {@link FileBitmapData}. Use this for any {@link BitmapParser} that can handle this type of instance.
 * 
 * @author Mantas Varnagiris
 */
public class URLBitmapDataGetter extends FileBitmapDataGetter
{
	private final Context				context;

	// Singleton
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static URLBitmapDataGetter	instance	= null;

	public static URLBitmapDataGetter getDefault(Context context)
	{
		if (instance == null)
			instance = new URLBitmapDataGetter(context);
		return instance;
	}

	public URLBitmapDataGetter(Context context)
	{
		this.context = context.getApplicationContext();
	}

	// FileBitmapDataGetter
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public BitmapData getBitmapData(ImageInfo imageInfo)
	{
		final boolean isLoggingOn = BuildConfig.DEBUG && imageInfo.isLoggingOn();

		// Create file to store bitmap
		final File tempFile = new File(context.getCacheDir(), imageInfo.getCacheName());

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

			return new FileBitmapData(tempFile, true);
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