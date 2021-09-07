package com.example.stox;

import androidx.annotation.NonNull;

import java.util.Date;

public class WeekData {
    String weekName;
    Double open = 0.0;
    Double close = 0.0;
    Double weekHigh = 0.0;
    Double weekLow = 0.0;
    Double volume = null;
    Date date = null;

    public WeekData(String weekName) {
        this.weekName = weekName;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getWeekHigh() {
        return weekHigh;
    }

    public void setWeekHigh(Double weekHigh) {
        this.weekHigh = weekHigh;
    }

    public Double getWeekLow() {
        return weekLow;
    }

    public void setWeekLow(Double weekLow) {
        this.weekLow = weekLow;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getWeekName() {
        return weekName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return "WeekData{" +
                "weekName='" + weekName + '\'' +
                ", open=" + open +
                ", close=" + close +
                ", weekHigh=" + weekHigh +
                ", weekLow=" + weekLow +
                ", volume=" + volume +
                ", date=" + date +
                '}';
    }
}
