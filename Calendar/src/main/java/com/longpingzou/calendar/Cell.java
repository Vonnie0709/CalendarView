package com.longpingzou.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by longpingzou on 2/26/16.
 */
public class Cell {
    public int row;
    public int col;
    public String date;
    public CanlenderState mState;
    private Paint mTextPaint;
    private Paint mCurrentDayBgPaint;
    private Paint mSelectDayBgPaint;
    public float width;
    public float height;
    public float radius;
    private static int TEXT_COLOR_TODAY = Color.parseColor("#FFFFFE");
    private static int TEXT_COLOR_SELECT_DAY = Color.parseColor("#FFFFFE");
    private static int TEXT_COLOR_CURRENT_MONTH = Color.parseColor("#FFFFFE");
    private static int TEXT_COLOR_CURRENT_PASSDAY = Color.parseColor("#EFEFEF");
    private static int TEXT_COLOR_UNREACH_MONTH = Color.parseColor("#EFEFEF");

    public Cell(int row, int col, String date, CanlenderState mState) {
        this.row = row;
        this.col = col;
        this.date = date;
        this.mState = mState;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mCurrentDayBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCurrentDayBgPaint.setStyle(Paint.Style.FILL);
        mCurrentDayBgPaint.setColor(Color.GRAY);
        mSelectDayBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectDayBgPaint.setStyle(Paint.Style.FILL);
        mSelectDayBgPaint.setColor(Color.parseColor("#d78787"));
    }

    public void drawCell(Canvas canvas, float width, float height) {
        mTextPaint.setTextSize(width / 3);
        radius = Math.min(width, height) / 3;
        switch (mState) {
            case CANLENDER_STATE_CURRENT_MONTH:
                mTextPaint.setColor(TEXT_COLOR_CURRENT_MONTH);
                break;
            case CANLENDER_STATE_PASS_MONTH:
                mTextPaint.setColor(TEXT_COLOR_CURRENT_PASSDAY);
                break;
            case CANLENDER_STATE_SELECTDAY:
                mTextPaint.setColor(TEXT_COLOR_SELECT_DAY);
                canvas.drawCircle((float) (0.5 + col) * width, (float) (0.5 + row) * height, radius, mSelectDayBgPaint);
                break;
            case CANLENDER_STATE_TODAY:
                mTextPaint.setColor(TEXT_COLOR_TODAY);
                canvas.drawCircle((float) (0.5 + col) * width, (float) (0.5 + row) * height, radius, mCurrentDayBgPaint);
                break;
            case CANLENDER_STATE_UNREACH_MONTH:
                mTextPaint.setColor(TEXT_COLOR_UNREACH_MONTH);
                break;
        }
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        float baseline = (float) (height * 0.5 - fm.descent + (fm.bottom - fm.top) * 0.5);
        canvas.drawText(date, (float) (col + 0.5) * width - mTextPaint.measureText(date) / 2, row * height + baseline, mTextPaint);


    }


}
