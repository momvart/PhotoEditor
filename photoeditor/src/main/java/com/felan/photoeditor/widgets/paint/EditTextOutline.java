package com.felan.photoeditor.widgets.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

public class EditTextOutline extends AppCompatEditText {

    private final Canvas mCanvas = new Canvas();
    private final TextPaint mPaint = new TextPaint();
    private Bitmap mCache;
    private boolean mUpdateCachedBitmap;
    private int mStrokeColor;
    private float mStrokeWidth;

    public EditTextOutline(Context context) {
        super(context);

        mStrokeColor = Color.TRANSPARENT;

        mUpdateCachedBitmap = true;
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isFocusable() && !isFocusableInTouchMode() && !isClickable())
            return false;

        return super.onTouchEvent(event);
    }

    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        mUpdateCachedBitmap = true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 && h > 0) {
            mUpdateCachedBitmap = true;
            mCache = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        } else {
            mCache = null;
        }
    }

    public void setStrokeColor(int strokeColor) {
        this.mStrokeColor = strokeColor;
        mUpdateCachedBitmap = true;
        invalidate();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        mUpdateCachedBitmap = true;
        invalidate();
    }

    @Override
    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        mUpdateCachedBitmap = true;
    }

    @Override
    public void setScaleY(float scaleY) {
        super.setScaleY(scaleY);
        mUpdateCachedBitmap = true;
    }

    protected void onDraw(Canvas canvas) {
        if (mCache != null && mStrokeColor != Color.TRANSPARENT) {
            if (mUpdateCachedBitmap) {
                final int w = getWidth() - getPaddingLeft() - getPaddingRight();
                final int h = getHeight();
                final String text = getText().toString();

                Canvas mCanvas = canvas;
//                mCanvas.setBitmap(mCache);
//                mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

                float strokeWidth = mStrokeWidth > 0 ? mStrokeWidth : (float) Math.ceil(getTextSize() / 11.5f);
                mPaint.setStrokeWidth(strokeWidth);
                mPaint.setColor(mStrokeColor);
                mPaint.setTextSize(getTextSize());
                mPaint.setTypeface(getTypeface());
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);

                StaticLayout sl = new StaticLayout(text, mPaint, w, getGravity() == Gravity.CENTER ? Layout.Alignment.ALIGN_CENTER : Layout.Alignment.ALIGN_NORMAL, 1, 0, true);

                mCanvas.save();
                float a = (h - getPaddingTop() - getPaddingBottom() - sl.getHeight()) / 2.0f;
                mCanvas.translate(getPaddingLeft(), a + getPaddingTop());
                sl.draw(mCanvas);
                mCanvas.restore();

//                mUpdateCachedBitmap = false;
            }
            canvas.drawBitmap(mCache, 0, 0, mPaint);
        }
        super.onDraw(canvas);
    }
}