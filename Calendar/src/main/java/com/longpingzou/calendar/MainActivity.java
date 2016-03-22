package com.longpingzou.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends Activity {

    private CalendarViewPager mViewPager;
    private List<View> items = new ArrayList<>();
    private CalendarView mCalendarCurrent;
    private CalendarView mCalnedarNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (CalendarViewPager) findViewById(R.id.viewPager);

        View v1 = getLayoutInflater().inflate(R.layout.layout_calendar_item, null);
        View v2 = getLayoutInflater().inflate(R.layout.layout_calendar_item1, null);
        mCalendarCurrent = (CalendarView) v1.findViewById(R.id.calendar);
        mCalnedarNext = (CalendarView) v2.findViewById(R.id.calendar);
        mCalendarCurrent.setOnCellClickListner(new CalendarView.OnCellClickListener() {
            @Override
            public void onCellClick(CustomDate clickDate) {
                mCalnedarNext.setSelectDay(-1);
            }
        });

        mCalnedarNext.setOnCellClickListner(new CalendarView.OnCellClickListener() {
            @Override
            public void onCellClick(CustomDate clickDate) {
                mCalendarCurrent.setSelectDay(-1);
            }
        });
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DATE);
        CustomDate customDate = new CustomDate(year, month + 1, day);
        mCalendarCurrent.setShowDate(customDate);
        mCalendarCurrent.setSelectDay(day + 1);

        CustomDate nextDate = new CustomDate(year, month + 2, day);
        mCalnedarNext.setShowDate(nextDate);
        mCalnedarNext.setSelectDay(-1);
        items.add(v1);
        items.add(v2);
        Adapter mAdapter = new Adapter(items);
        mViewPager.setAdapter(mAdapter);
    }


    private class Adapter extends PagerAdapter {

        private List<View> items;

        public Adapter(List<View> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(items.get(position));
            return items.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(items.get(position));
        }

    }
}
