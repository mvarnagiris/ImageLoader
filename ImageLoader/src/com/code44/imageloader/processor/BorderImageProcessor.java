package com.code44.imageloader.processor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

public class BorderImageProcessor implements ImageProcessor
{
	protected final Paint	borderPaint		= new Paint();
	protected final Paint	bitmapMaskPaint	= new Paint();
	protected final Paint	bitmapPaint		= new Paint();

	protected final int		boderColor;
	protected final float	borderWidth;
	protected final float	borderRadius;

	protected final float	shadowRadius;
	protected final float	shadowDX;
	protected final float	shadowDY;
	protected final int		shadowColor;

	private final String	uniqueId;

	public BorderImageProcessor(int borderColor, float boderWidth, float borderRadius, float shadowRadius, float shadowDX, float shadowDY, int shadowColor)
	{
		this.boderColor = borderColor;
		this.borderWidth = boderWidth;
		this.borderRadius = borderRadius;
		this.shadowRadius = shadowRadius;
		this.shadowDX = shadowDX;
		this.shadowDY = shadowDY;
		this.shadowColor = shadowColor;
		this.uniqueId = BorderImageProcessor.class.getSimpleName() + "_" + borderColor + "_" + boderWidth + "_" + borderRadius + "_" + shadowRadius + "_"
				+ shadowDX + "_" + shadowDY + "_" + shadowColor;

		this.borderPaint.setAntiAlias(true);
		this.borderPaint.setColor(boderWidth <= 0 ? shadowColor : borderColor);
		if (shadowRadius > 0)
			this.borderPaint.setShadowLayer(shadowRadius, shadowDX, shadowDY, shadowColor);

		this.bitmapMaskPaint.setAntiAlias(true);
		this.bitmapMaskPaint.setColor(borderColor);
		// this.bitmapMaskPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));

		this.bitmapPaint.setAntiAlias(true);
		this.bitmapPaint.setColor(borderColor);
		this.bitmapPaint.setFilterBitmap(true);
		this.bitmapPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	}

	@Override
	public Bitmap processImage(Bitmap bitmap)
	{
		final float bWidth = bitmap.getWidth();
		final float bHeight = bitmap.getHeight();

		if (bWidth <= 0 || bHeight <= 0)
			return bitmap;

		// Find new image constrain size
		final float constraintW = bWidth - (shadowRadius * 2) - Math.max(0, shadowDX - shadowRadius) - (borderWidth * 2);
		final float constraintH = bHeight - (shadowRadius * 2) - Math.max(0, shadowDY - shadowRadius) - (borderWidth * 2);

		// Find scale and new image size
		final float scale;
		if (bWidth - constraintW > bHeight - constraintH)
			scale = constraintW / bWidth;
		else
			scale = constraintH / bHeight;
		final float newWidth = bWidth * scale;
		final float newHeight = bHeight * scale;

		// Find bitmap rect
		final float bitmapL = Math.max(shadowRadius - shadowDX, 0) + borderWidth + ((constraintW - newWidth) / 2);
		final float bitmapT = Math.max(shadowRadius - shadowDY, 0) + borderWidth + ((constraintH - newHeight) / 2);
		final float bitmapR = bitmapL + newWidth;
		final float bitmapB = bitmapT + newHeight;
		final RectF bitmapRectDst = new RectF(bitmapL, bitmapT, bitmapR, bitmapB);

		// Find border rect
		final RectF borderRect = new RectF(bitmapL - borderWidth, bitmapT - borderWidth, bitmapR + borderWidth, bitmapB + borderWidth);

		// Create rounded scaled bitmap
		Canvas canvas;
		final Bitmap roundedScaledBitmap = Bitmap.createBitmap((int) bWidth, (int) bHeight, Config.ARGB_8888);
		canvas = new Canvas(roundedScaledBitmap);
		canvas.drawRoundRect(bitmapRectDst, borderRadius, borderRadius, bitmapMaskPaint);
		canvas.drawBitmap(bitmap, null, bitmapRectDst, bitmapPaint);
		bitmap.recycle();

		// Draw border and shadow, then draw rounded scaled bitmap on top
		bitmapRectDst.set(0, 0, bWidth, bHeight);
		Bitmap output = null;
		output = Bitmap.createBitmap((int) bWidth, (int) bHeight, Config.ARGB_8888);
		canvas = new Canvas(output);
		canvas.drawRoundRect(borderRect, borderRadius + borderWidth / 2, borderRadius + borderWidth / 2, borderPaint);
		canvas.drawBitmap(roundedScaledBitmap, null, bitmapRectDst, null);

		return output;
	}

	@Override
	public String getUniqueId()
	{
		return uniqueId;
	}
}