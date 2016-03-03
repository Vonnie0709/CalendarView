package com.longpingzou.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
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
public class CalendarView extends View {

    private int mCellWidth; //each cell's width
    private int mCellHeight; //each cell's height
    private float mNormalCircleRadius; //circle radius
    private float mSelectCirCleRadius;
    private float mTodayCirCleRadius;

    private int mNormalCircleColor;// normal circle color
    private int mSelectCircleColor;// select circle color
    private int mTodayCircleColor; //today circle color

    private int mNormalTextColor;
    private int mTodayTextColor;
    private int mSelectTextColor;

    private float mNormalTextSize;
    private float mSelectTextSize;
    private float mTodayTextSize;


    private int totalRow = 6;
    private int totalCol = 7;
    private float mDownX;
    private float mDownY;
    private int touchSlop;
    private CustomDate showDate;
    private List<Cell> cells;
    private OnCellClickListener mCellClickListener;
    private int defaultYear;
    private int defaultMonth;
    private int selectDay;
    private CustomDate selectDate;
    private CustomDate currentDate;

    private void initAttrs(Context context, AttributeSet attrs) {

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mCellWidth = (int) mTypedArray.getDimension(R.styleable.CalendarView_eachCellWidth, -1);
        mCellHeight = (int) mTypedArray.getDimension(R.styleable.CalendarView_eachCellHeight, -1);


        mNormalCircleRadius = mTypedArray.getFloat(R.styleable.CalendarView_normalCircleRadius, -1);
        mSelectCirCleRadius = mTypedArray.getFloat(R.styleable.CalendarView_selectCircleRadius, -1);
        mTodayCirCleRadius = mTypedArray.getFloat(R.styleable.CalendarView_todayCircleRadius, -1);
        mSelectTextColor = mTypedArray.getColor(R.styleable.CalendarView_selectTextColor, Color.parseColor("#c6c6c6"));
        mNormalTextColor = mTypedArray.getColor(R.styleable.CalendarView_normalTextColor, Color.WHITE);
        mTodayTextColor = mTypedArray.getColor(R.styleable.CalendarView_todayTextColor, Color.WHITE);

        mNormalCircleColor = mTypedArray.getColor(R.styleable.CalendarView_normalCircleColor, Color.TRANSPARENT);
        mSelectCircleColor = mTypedArray.getColor(R.styleable.CalendarView_selectCircleColor, Color.parseColor("#d78787"));
        mTodayCircleColor = mTypedArray.getColor(R.styleable.CalendarView_todayCircleColor, Color.GRAY);


        mNormalTextSize = mTypedArray.getDimension(R.styleable.CalendarView_normalTextSize, -1);
        mSelectTextSize = mTypedArray.getDimension(R.styleable.CalendarView_selectTextSize, -1);
        mTodayTextSize = mTypedArray.getDimension(R.styleable.CalendarView_todayTextSize, -1);
        defaultYear = mTypedArray.getInt(R.styleable.CalendarView_showYear, 1970);
        defaultMonth = mTypedArray.getInt(R.styleable.CalendarView_showMonth, 1);
        selectDay = mTypedArray.getInt(R.styleable.CalendarView_selectDay, -1);
        showDate = new CustomDate(defaultYear, defaultMonth, selectDay);
        selectDate = new CustomDate(defaultYear, defaultMonth, selectDay);
        mTypedArray.recycle();
    }

    public CalendarView(Context context) {
        super(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView();

    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView();
    }


    private void initView() {

        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DATE);
        currentDate = new CustomDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        if (showDate.month == 0) {
            try {
                throw new Exception("month cannot be zero!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cal.set(showDate.year, showDate.month - 1, 1);
        int maxDate = cal.getActualMaximum(Calendar.DATE);
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int lastRowCount = (maxDate - (7 - week)) % 7;
        int firstRowCount = 6 - week;
        int centerCount = (maxDate - lastRowCount - firstRowCount) / 7;
        int day = 1;
        cells = new ArrayList<>();
        for (int i = week; i < 7; i++) {
            Cell cell = new Cell(0, i, new CustomDate(defaultYear, defaultMonth, day), CalendarState.CALENDAR_STATE_NORMAL);
            day++;
            cells.add(cell);
        }
        for (int j = 1; j < centerCount + 1; j++) {
            for (int i = 0; i < 7; i++) {
                Cell cell = new Cell(j, i, new CustomDate(defaultYear, defaultMonth, day), CalendarState.CALENDAR_STATE_NORMAL);
                day++;
                cells.add(cell);
            }
        }
        for (int i = 0; i < lastRowCount; i++) {
            Cell cell = new Cell(centerCount + 1, i, new CustomDate(defaultYear, defaultMonth, day), CalendarState.CALENDAR_STATE_NORMAL);
            day++;
            cells.add(cell);
        }

        if (showDate.year == currentDate.year && showDate.month == currentDate.month + 1) {
            cells.get(today - 1).mState = CalendarState.CALENDAR_STATE_TODAY;
        }
        if (selectDate.year == showDate.year && selectDate.month == showDate.month && selectDay != -1) {
            cells.get(selectDay - 1).mState = CalendarState.CALENDAR_STATE_SELECTDAY;
        }
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        totalRow = centerCount + 2;
        if (mCellWidth == -1) {
            mCellWidth = dm.widthPixels / totalCol;
        }
        if (mCellHeight == -1) {
            mCellHeight = dm.heightPixels / totalRow * 4 / 9;
        }
    }


    public void setSelectDay(CustomDate date) {
        selectDate = date;
        invalidateView();
    }

    private void invalidateView() {
        initView();
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
                    selectDate = cell.date;
                    selectDay = selectDate.day;
                    break;
                }
            }
        }
        if (selectDay == 0) {
            return;
        }
        if (mCellClickListener != null) {
            mCellClickListener.onCellClick(selectDate);
        }
        invalidateView();
    }


    public interface OnCellClickListener {
        void onCellClick(CustomDate clickDate);
    }


    public void setOnCellClickListner(OnCellClickListener l) {
        this.mCellClickListener = l;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int textColor = Color.WHITE;
        int backgroundColor = Color.TRANSPARENT;
        float radius = -1;
        float textSize = -1;
        for (Cell cell : cells) {
            switch (cell.mState) {
                case CALENDAR_STATE_NORMAL:
                    textColor = mNormalTextColor;
                    backgroundColor = mNormalCircleColor;
                    textSize = mNormalTextSize;
                    radius = mNormalCircleRadius;
                    break;
                case CALENDAR_STATE_SELECTDAY:
                    textColor = mSelectTextColor;
                    backgroundColor = mSelectCircleColor;
                    textSize = mSelectTextSize;
                    radius = mSelectCirCleRadius;
                    break;
                case CALENDAR_STATE_TODAY:
                    textColor = mTodayTextColor;
                    backgroundColor = mTodayCircleColor;
                    textSize = mTodayTextSize;
                    radius = mTodayCirCleRadius;
                    break;

            }
            cell.drawCell(canvas, mCellWidth, mCellHeight, radius, textColor, textSize, backgroundColor);
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
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = getPaddingTop() + getPaddingBottom() + mCellHeight * totalRow;
        }
        setMeasuredDimension(width, height);
    }


}
