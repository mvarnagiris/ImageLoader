package com.code44.imageloader.getter.parser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.code44.imageloader.ImageInfo;
import com.code44.imageloader.ImageSettings;
import com.code44.imageloader.getter.data.BitmapData;

/**
 * Parser that can decode scaled bitmaps and resize them as necessary.
 * 
 * @author Mantas Varnagiris
 */
public abstract class ScaledBitmapParser implements BitmapParser
{
	public enum SizeType
	{
		NONE, // Image will not be scaled.
		MAX, // If ImageInfo has at least one dimension set, image will be scaled down if necessary to fit within those dimensions.
		FIT, // If ImageInfo has at least one dimension set, image will be scaled down or up to fit within those dimensions.
		FILL // If ImageInfo has at least one dimension set, image will centered, scaled until most image fills all area and then cropped.
	}

	protected final SizeType	sizeType;

	public ScaledBitmapParser(SizeType sizeType)
	{
		this.sizeType = sizeType;
	}

	// Protected methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	protected int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight, SizeType sizeType, int downSampleBy)
	{
		int inSampleSize = 1;

		// If size type is NONE then no need to calculate sample size
		if (sizeType == SizeType.NONE)
			return inSampleSize + downSampleBy;

		// Calculate sample size based on SizeType
		switch (sizeType)
		{
			case FILL:
				if (height > reqHeight && width > reqWidth)
				{
					if (height - reqHeight < width - reqWidth)
						inSampleSize = Math.round((float) height / (float) reqHeight);
					else
						inSampleSize = Math.round((float) width / (float) reqWidth);
				}
				break;

			case FIT:
			case MAX:
				// TODO If both dimensions are smaller, calculation for FIT should be different?
				if (height > reqHeight || width > reqWidth)
				{
					if (height - reqHeight > width - reqWidth)
						inSampleSize = Math.round((float) height / (float) reqHeight);
					else
						inSampleSize = Math.round((float) width / (float) reqWidth);
				}
				break;

			default:
				break;
		}

		return inSampleSize + downSampleBy;
	}

	// Abstract methods
	// ------------------------------------------------------------------------------------------------------------------------------------

	protected abstract Bitmap decodeBitmap(ImageInfo imageInfo, BitmapData bitmapData, BitmapFactory.Options options);

	// BitmapParser
	// ------------------------------------------------------------------------------------------------------------------------------------

	@Override
	public Bitmap parseBitmap(ImageInfo imageInfo, BitmapData bitmapData)
	{
		Bitmap bitmap = null;

		// Read settings
		final ImageSettings settings = imageInfo.getImageSettings();
		final int reqWidth = settings.getWidth();
		final int reqHeight = settings.getHeight();

		// Check bitmap dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		options.inInputShareable = true;
		decodeBitmap(imageInfo, bitmapData, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, reqWidth, reqHeight, sizeType, settings.getDownSampleBy());

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap tempBitmap = decodeBitmap(imageInfo, bitmapData, options);

		// Resize and crop bitmap if necessary
		switch (sizeType)
		{
			case FILL:
				// Create scaled bitmap
				final float scale;
				if (options.outWidth - reqWidth < options.outHeight - reqHeight)
					scale = (float) reqWidth / (float) options.outWidth;
				else
					scale = (float) reqHeight / (float) options.outHeight;

				final int scaledWidth = (int) (options.outWidth * scale);
				final int scaledHeight = (int) (options.outHeight * scale);
				bitmap = Bitmap.createScaledBitmap(tempBitmap, scaledWidth, scaledHeight, true);
				tempBitmap.recycle();
				tempBitmap = bitmap;

				// Create cropped bitmap
				bitmap = Bitmap.createBitmap(tempBitmap, Math.max((scaledWidth - reqWidth) / 2, 0), Math.max((scaledHeight - reqHeight) / 2, 0), reqWidth,
						reqHeight);
				tempBitmap.recycle();
				tempBitmap = null;
				break;

			case FIT:
				// TODO Implement
				break;

			case MAX:
				// TODO Implement
				break;

			default:
				break;
		}

		if (imageInfo.isLoggingOn() && bitmap != null)
		{
			String sizeTypeText;
			switch (sizeType)
			{
				case MAX:
					sizeTypeText = "MAX";
					break;

				case FIT:
					sizeTypeText = "FIT";
					break;

				case FILL:
					sizeTypeText = "FILL";
					break;

				default:
					sizeTypeText = "NONE";
					break;
			}
			Log.i(TAG, "Bitmap decoded with inSampleSize=" + options.inSampleSize + " and scaled to " + sizeTypeText + ". [" + imageInfo.toString() + "]");
		}

		return bitmap;
	}
}