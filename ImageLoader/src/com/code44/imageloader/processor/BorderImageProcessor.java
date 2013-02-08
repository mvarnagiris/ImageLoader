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
	protected final Paint	borderMaskPaint	= new Paint();
	protected final Paint	bitmapMaskPaint	= new Paint();

	protected final int		boderColor;
	protected final int		borderWidth;
	protected final float	borderRadius;

	protected final float	shadowRadius;
	protected final float	shadowDX;
	protected final float	shadowDY;
	protected final int		shadowColor;

	private final String	uniqueId;

	public BorderImageProcessor(int borderColor, int boderWidth, float borderRadius, float shadowRadius, float shadowDX, float shadowDY, int shadowColor)
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
		this.borderPaint.setColor(borderColor);
		if (shadowRadius > 0)
			this.borderPaint.setShadowLayer(shadowRadius, shadowDX, shadowDY, shadowColor);

		this.borderMaskPaint.setAntiAlias(true);
		this.borderMaskPaint.setColor(borderColor);
		this.borderMaskPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));

		this.bitmapMaskPaint.setAntiAlias(true);
		this.bitmapMaskPaint.setColor(borderColor);
		this.bitmapMaskPaint.setFilterBitmap(true);
		this.bitmapMaskPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	}

	@Override
	public Bitmap processImage(Bitmap bitmap)
	{
		final int bWidth = bitmap.getWidth();
		final int bHeight = bitmap.getHeight();

		if (bWidth <= 0 || bHeight <= 0)
			return bitmap;

		final int bitmapL;
		final int bitmapT;
		final int bitmapR;
		final int bitmapB;
		if (shadowRadius > 0)
		{
			bitmapL = (int) (borderWidth + shadowRadius - shadowDX);
			bitmapT = (int) (borderWidth + shadowRadius - shadowDY);
			bitmapR = (int) (bWidth - borderWidth - shadowRadius - shadowDX);
			bitmapB = (int) (bHeight - borderWidth - shadowRadius - shadowDY);
		}
		else
		{
			bitmapL = borderWidth;
			bitmapT = borderWidth;
			bitmapR = bWidth - borderWidth;
			bitmapB = bHeight - borderWidth;
		}

		final RectF bitmapRectDst = new RectF(bitmapL, bitmapT, bitmapR, bitmapB);
		final RectF borderRect = new RectF(bitmapL - borderWidth, bitmapT - borderWidth, bitmapR + borderWidth, bitmapB + borderWidth);

		Canvas canvas;
		Bitmap border = null;
		if (borderWidth > 0)
		{
			border = Bitmap.createBitmap(bWidth, bHeight, Config.ARGB_8888);
			canvas = new Canvas(border);

			canvas.drawRoundRect(borderRect, borderRadius + borderWidth / 2, borderRadius + borderWidth / 2, borderPaint);
			canvas.drawRoundRect(bitmapRectDst, borderRadius, borderRadius, borderMaskPaint);
		}

		final Bitmap output = Bitmap.createBitmap(bWidth, bHeight, Config.ARGB_8888);
		canvas = new Canvas(output);

		canvas.drawRoundRect(bitmapRectDst, borderRadius, borderRadius, borderPaint);
		canvas.drawBitmap(bitmap, null, bitmapRectDst, bitmapMaskPaint);
		if (border != null)
		{
			canvas.drawBitmap(border, null, borderRect, null);
			border.recycle();
		}
		bitmap.recycle();

		return output;
	}

	@Override
	public String getUniqueId()
	{
		return uniqueId;
	}
}