package com.code44.imageloader.info;

import android.content.Context;

import com.code44.imageloader.ImageLoader;
import com.code44.imageloader.fetcher.FileFetcher;

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
	 * @return Instance of {@link FileFetcher} that can handle this {@link BitmapInfo} and fetch bitmap.
	 */
	public abstract FileFetcher getFileFetcher(Context context);
}