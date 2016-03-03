package com.longpingzou.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by longpingzou on 2/26/16.
 */
public class CanlenderView extends View {

    private int mCellWidth; // 视图的宽度
    private int mCellHeight;
    private int totalRow = 6;
    private int totalCol = 7;
    private float mDownX;
    private float mDownY;
    private int touchSlop;
    private int selectDate = 0;
    private List<Cell> cells;
    private OnCellClickListener mCellClickListener;

    public CanlenderView(Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        try {
            initView(new CustomDate(2016, 9, 21));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CanlenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        try {
            initView(new CustomDate(2016, 10, 21));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CanlenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        try {
            initView(new CustomDate(2016, 9, 21));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CanlenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        try {
            initView(new CustomDate(2016, 9, 21));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initView(CustomDate date) throws Exception {
        this.selectDate = date.selectDay;

        Calendar cal = Calendar.getInstance();
        if (date.month == 0) {
            throw new Exception("month cannot be zero!");
        }
        cal.set(date.year, date.month - 1, 1);
        int maxDate = cal.getActualMaximum(Calendar.DATE);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int lastRowCount = (maxDate - (7 - week)) % 7;
        int firstRowCount = 6 - week;
        int centerCount = (maxDate - lastRowCount - firstRowCount) / 7;
        int count = 1;
        cells = new ArrayList<>();
        for (int i = week; i < 7; i++) {
            Cell cell = new Cell(0, i, count + "", CanlenderState.CANLENDER_STATE_CURRENT_MONTH);
            count++;
            cells.add(cell);
        }
        for (int j = 1; j < centerCount + 1; j++) {
            for (int i = 0; i < 7; i++) {
                Cell cell = new Cell(j, i, count + "", CanlenderState.CANLENDER_STATE_CURRENT_MONTH);
                count++;
                cells.add(cell);
            }
        }
        for (int i = 0; i < lastRowCount; i++) {
            Cell cell = new Cell(centerCount + 1, i, count + "", CanlenderState.CANLENDER_STATE_CURRENT_MONTH);
            count++;
            cells.add(cell);
        }
        cells.get(selectDate - 1).mState = CanlenderState.CANLENDER_STATE_SELECTDAY;

        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        totalRow = centerCount + 2;
        Log.i("ABC", "totalRow" + totalRow);
        mCellWidth = dm.widthPixels / totalCol;
        mCellHeight = dm.heightPixels / totalRow * 3 / 9;
    }


    private void calcCells() {

    }

    private void invalidateView() {
        try {
            initView(new CustomDate(2016, 9, selectDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    int col = (int) (mDownX / mCellWidth);
                    int row = (int) (mDownY / mCellHeight);
                    measureClickCell(col, row);
                }
                break;
            default:
                break;
        }

        return true;
    }


    private void measureClickCell(int col, int row) {
        for (Cell cell : cells) {
            if (cell.col == col) {
                if (cell.row == row) {
                    selectDate = Integer.parseInt(cell.date);
                    break;
                }
            }
        }
        if (selectDate == 0) {
            return;
        }
        if (mCellClickListener != null) {
            Log.i("ABC", selectDate + " -----!!!");
            mCellClickListener.onCellClick(selectDate + "");
        }
        invalidateView();
    }


    public interface OnCellClickListener {
        void onCellClick(String clickDate);
    }


    public void setOnCellClickListner(OnCellClickListener l) {
        this.mCellClickListener = l;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Cell cell : cells) {
            cell.drawCell(canvas, mCellWidth, mCellHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = getPaddingLeft() + getPaddingRight() + mCellWidth * totalCol;
        } else {
            //Be whatever you want
//            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = getPaddingTop() + getPaddingBottom() + mCellHeight * totalRow;
        } else {
        }
        setMeasuredDimension(width, height);
    }


}
