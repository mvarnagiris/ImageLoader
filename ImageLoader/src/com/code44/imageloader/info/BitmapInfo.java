package com.code44.imageloader.info;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.getter.BitmapDataGetter;
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

	public abstract String getUniqueName();

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