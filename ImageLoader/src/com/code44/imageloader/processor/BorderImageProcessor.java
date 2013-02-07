package com.code44.imageloader.processor;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BorderImageProcessor implements ImageProcessor
{
	protected final int		color;
	protected final int		width;
	protected final float	radius;

	protected final Paint	borderPaint		= new Paint();
	protected final Paint	borderMaskPaint	= new Paint();
	protected final Paint	bitmapMaskPaint	= new Paint();

	private final String	uniqueId;

	public BorderImageProcessor(int color, int width, float radius)
	{
		this.color = color;
		this.width = width;
		this.radius = radius;
		this.uniqueId = BorderImageProcessor.class.getSimpleName() + "_" + color + "_" + width + "_" + radius;

		this.borderPaint.setAntiAlias(true);
		this.borderPaint.setColor(color);

		this.borderMaskPaint.setAntiAlias(true);
		this.borderMaskPaint.setColor(color);
		this.borderMaskPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));

		this.bitmapMaskPaint.setAntiAlias(true);
		this.bitmapMaskPaint.setColor(color);
		this.bitmapMaskPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	}

	@Override
	public Bitmap processImage(Bitmap bitmap)
	{
		final int bWidth = bitmap.getWidth();
		final int bHeight = bitmap.getHeight();

		if (bWidth <= 0 || bHeight <= 0)
			return bitmap;

		final Rect rect = new Rect(0, 0, bWidth, bHeight);
		final RectF rectF = new RectF(rect);

		Canvas canvas;
		Bitmap border = null;
		if (width > 0)
		{
			border = Bitmap.createBitmap(bWidth, bHeight, Config.ARGB_8888);
			canvas = new Canvas(border);

			canvas.drawRoundRect(rectF, radius, radius, borderPaint);
			rectF.set(width, width, bWidth - width, bHeight - width);
			canvas.drawRoundRect(rectF, radius, radius, borderMaskPaint);
			rectF.set(rect);
		}

		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		canvas = new Canvas(output);

		canvas.drawRoundRect(rectF, radius, radius, borderPaint);
		canvas.drawBitmap(bitmap, rect, rect, bitmapMaskPaint);
		if (border != null)
			canvas.drawBitmap(border, rect, rect, null);

		return output;
	}

	@Override
	public String getUniqueId()
	{
		return uniqueId;
	}
}