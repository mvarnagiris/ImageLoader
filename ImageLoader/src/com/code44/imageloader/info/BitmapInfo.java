package com.code44.imageloader.info;

import android.content.Context;

import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.getter.BitmapDataGetter;
import com.code44.imageloader.getter.data.BitmapData;
import com.code44.imageloader.getter.parser.BitmapParser;

/**
 * Holds information about bitmap that will be fetched.
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
	 * Used by {@link ImageLoader} to check if data is correct before star loading.
	 * 
	 * @return {@code true} if data is correct; {@code false} otherwise.
	 */
	public abstract boolean checkInfo();

	/**
	 * @return Instance of {@link BitmapDataGetter} that can handle this {@link BitmapInfo} and fetch bitmap.
	 */
	public abstract BitmapDataGetter getBitmapDataGetter(Context context);

	/**
	 * @return Instance of {@link BitmapParser} that can handle {@link BitmapData} returned from {@link BitmapDataGetter}.
	 */
	public abstract BitmapParser getBitmapParser(Context context);
}