package com.code44.imageloader.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.code44.imageloader.BuildConfig;
import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.getter.data.BitmapData;

public class ImageCache
{
	protected static final String	TAG	= ImageLoader.TAG + " - Cache";

	protected final CacheSettings	cacheSettings;

	// Singleton
	// ------------------------------------------------------------------------------------------------------------------------------------

	private static ImageCache		instance;

	public static void initImageCache(Context context, CacheSettings settings)
	{
		instance = new ImageCache(context, settings);
	}

	public static ImageCache getDefault(Context context)
	{
		if (instance == null)
		{
			instance = new ImageCache(context, null);
			if (BuildConfig.DEBUG)
				Log.w(TAG,
						"Using default ImageCache. If you want to customize it, call initImageCache(Context, CacheSettings settings) in Application.onCreate().");
		}
		return instance;
	}

	private ImageCache(Context context, CacheSettings settings)
	{
		if (settings == null)
			cacheSettings = new CacheSettings();
		else
			cacheSettings = settings;
	}

	// Public methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	public Bitmap getFromMemory(ImageInfo imageInfo)
	{
		return null;
	}

	public Bitmap getFromMemorySmaller(ImageInfo imageInfo)
	{
		return null;
	}

	public Bitmap getFromFile(ImageInfo imageInfo)
	{
		return null;
	}

	public Bitmap getFromFileSmaller(ImageInfo imageInfo)
	{
		return null;
	}

	public Bitmap getFromFileOriginal(ImageInfo imageInfo)
	{
		return null;
	}

	public boolean putToMemory(ImageInfo imageInfo, Bitmap bitmap)
	{
		return false;
	}

	public boolean putToFile(ImageInfo imageInfo, Bitmap bitmap)
	{
		return false;
	}

	public boolean putToFileOriginal(ImageInfo imageInfo, BitmapData bitmapData)
	{
		return false;
	}
}