package com.longpingzou.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {

    private CalendarView mCanlender;
    private CalendarView mCanlender1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCanlender = (CalendarView) findViewById(R.id.canlender);
        mCanlender1 = (CalendarView) findViewById(R.id.canlender1);
        mCanlender.setOnCellClickListner(new CalendarView.OnCellClickListener() {
            @Override
            public void onCellClick(CustomDate clickDate) {
                Log.i("ABC", "ClickDate" + clickDate.day);
                mCanlender1.setSelectDay(clickDate);
            }
        });
        mCanlender1.setOnCellClickListner(new CalendarView.OnCellClickListener() {
            @Override
            public void onCellClick(CustomDate clickDate) {
                mCanlender.setSelectDay(clickDate);
            }
        });
    }
}
