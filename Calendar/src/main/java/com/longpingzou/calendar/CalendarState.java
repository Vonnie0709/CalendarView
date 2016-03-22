package com.longpingzou.calendar;

/**
 * Created by longpingzou on 2/26/16.
 */
public enum CalendarState {
    CALENDAR_STATE_TODAY("today"),
    CALENDAR_STATE_SELECTDAY("selectDay"),
    CALENDAR_STATE_NORMAL("normalDay"),
    CALENDAR_STATE_PASSDAY("passDay");
    private String desc;

    CalendarState(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
