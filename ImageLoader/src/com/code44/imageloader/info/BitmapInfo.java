package com.code44.imageloader.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.getter.BitmapGetter;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.getter.parser.BitmapParser;

/**
 * Holds information about how to load {@link BitmapData} and how to parse it to {@link Bitmap} using {@link BitmapParser}. Extend this class if you want custom
 * bitmap loading.
 * <p>
 * <b>Important: </b>Don't forget to override {@link #equals(Object)} and {@link #hashCode()} methods. This is needed, because {@link ImageLoader} needs to be
 * able to check if if the same {@link BitmapInfo} is being loaded for {@link ImageView}.
 * </p>
 * <p>
 * <b>Note: </b>Override {@link #toString()} method for better logging.
 * </p>
 * 
 * @author Mantas Varnagiris
 */
public abstract class BitmapInfo
{
	// Abstract methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * This should be unique name identifying image. This name will be used for file cache and memory cache, so the name should be formatted so that it would be
	 * a correct file name in file cache. Eg. URLs should be MD5-ed.
	 * 
	 * @return Unique name identifying image.
	 */
	public abstract String getBitmapName();

	/**
	 * Used by {@link ImageLoader} to check if data is correct before star loading.
	 * 
	 * @return {@code true} if data is correct; {@code false} otherwise.
	 */
	public abstract boolean checkInfo();

	/**
	 * @return Instance of {@link BitmapGetter} that can handle this {@link BitmapInfo} and fetch bitmap.
	 */
	public abstract BitmapGetter getBitmapDataGetter(Context context);

	/**
	 * @return Instance of {@link BitmapParser} that can handle {@link BitmapData} returned from {@link BitmapGetter}.
	 */
	public abstract BitmapParser getBitmapParser(Context context);
}