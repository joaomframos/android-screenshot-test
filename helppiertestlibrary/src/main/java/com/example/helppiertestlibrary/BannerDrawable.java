package com.example.helppiertestlibrary;

/*import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

public class BannerDrawable extends Drawable {

    private static final double SQRT_2 = Math.sqrt(2);
    private final Rect mTextBounds;
    private Paint mPaintFill;
    private Paint mPaintText;
    private String mMessage = "I'M A PIRATE BANNER";
    private int mBannerWidth = 50;
    private int mTextSize;

    public BannerDrawable() {
        initPaintFill();
        initPaintText();
        mTextBounds = new Rect();
    }

    private void initPaintFill() {
        mPaintFill = new Paint(ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(getResources().getColor(R.color.banner));
    }

    private void initPaintText() {
        mPaintText = new Paint(ANTI_ALIAS_FLAG);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setColor(Color.WHITE);
        mPaintText.setTextSize(20);
        mPaintText.setShadowLayer(4.0f, 2.0f, 2.0f, Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        if (bounds.isEmpty()) {
            bounds = canvas.getClipBounds();
        }
        float width = bounds.width();

        adaptTextSize((int) (width * 0.9), (int) (mBannerWidth * 0.9));

        float bannerHyp = (float) (mBannerWidth * SQRT_2);

        canvas.translate(0, bounds.centerY() - mBannerWidth);
        canvas.rotate(45, bounds.centerX(), bounds.centerY() - mBannerWidth);
        canvas.drawRect(bounds.left - bannerHyp, bounds.top, bounds.right + bannerHyp, bounds.top + mBannerWidth, mPaintFill);

        canvas.drawText(mMessage, bounds.centerX() - mTextBounds.centerX(), mBannerWidth / 2 + mTextBounds.height() / 2, mPaintText);
    }

    private void adaptTextSize(float width, int height) {
        if (mTextSize > 0) {
            mPaintText.setTextSize(mTextSize);
            return;
        }
        int textSize = 10;
        int textHeight;
        int textWidth;
        boolean stop = false;
        while (!stop) {
            mTextSize = textSize++;
            mPaintText.setTextSize(mTextSize);
            mPaintText.getTextBounds(mMessage, 0, mMessage.length(), mTextBounds);

            textHeight = mTextBounds.height();
            textWidth = mTextBounds.width();

            stop = textHeight >= height || textWidth >= width;
        }
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}*/