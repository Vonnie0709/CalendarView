package com.longpingzou.calendar;

import android.util.Log;

/**
 * Created by longpingzou on 3/2/16.
 */
public class CustomDate {
    public int year;
    public int month;
    public int day;


    public CustomDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String toString() {
        return "Year:" + year + ",Month:" + month + ",Day:" + day;
    }

}
