package com.longpingzou.calendar;

/**
 * Created by longpingzou on 3/2/16.
 */
public class CustomDate {
    public int year = 1970;
    public int month = 1;
    public int day = 1;
    public int selectDay = 1;

    public CustomDate() {

    }

    public CustomDate(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public CustomDate(int year, int month, int selectDay) {
        this.year = year;
        this.month = month;
        this.selectDay = selectDay;
    }

}
