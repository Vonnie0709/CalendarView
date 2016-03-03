package com.longpingzou.calendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {

    private CanlenderView mCanlender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCanlender = (CanlenderView) findViewById(R.id.canlender);
        mCanlender.setOnCellClickListner(new CanlenderView.OnCellClickListener() {
            @Override
            public void onCellClick(String clickDate) {
                Log.i("ABC", "ClickDate" + clickDate);
            }
        });
    }
}
