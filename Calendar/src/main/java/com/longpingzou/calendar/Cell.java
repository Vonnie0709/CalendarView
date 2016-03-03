package com.longpingzou.calendar;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by longpingzou on 2/26/16.
 */
public class Cell {
    public int row;
    public int col;
    public CustomDate date;
    public CalendarState mState;
    private Paint mTextPaint;
    private Paint mBackgroundPaint;

    public Cell(int row, int col, CustomDate date, CalendarState mState) {
        this.row = row;
        this.col = col;
        this.date = date;
        this.mState = mState;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

    }

    public void drawCell(Canvas canvas, float width, float height, float radius, int textColor, float textSize, int circleColor) {
        if (textSize == -1) {
            mTextPaint.setTextSize(width / 3);
        }
        if (radius == -1) {
            radius = Math.min(width, height) / 3;
        }
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mBackgroundPaint.setColor(circleColor);
        canvas.drawCircle((float) (0.5 + col) * width, (float) (0.5 + row) * height, radius, mBackgroundPaint);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float baseline = (float) (height * 0.5 - fm.descent + (fm.bottom - fm.top) * 0.5);
        canvas.drawText(date.day + "", (float) (col + 0.5) * width - mTextPaint.measureText(date.day + "") / 2, row * height + baseline, mTextPaint);


    }


}
