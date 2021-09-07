package com.example.stox;

import androidx.annotation.NonNull;

import java.util.Date;

public class MonthData {
    String monthName = "";
    Double open = 0.0;
    Double close = 0.0;
    Double monthHigh = 0.0;
    Double monthLow = 0.0;
    Double volume = null;
    Date date = null;

    public MonthData(String weekName) {
        this.monthName = weekName;
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

    public Double getMonthHigh() {
        return monthHigh;
    }

    public void setMonthHigh(Double monthHigh) {
        this.monthHigh = monthHigh;
    }

    public Double getMonthLow() {
        return monthLow;
    }

    public void setMonthLow(Double monthLow) {
        this.monthLow = monthLow;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getMonthName() {
        return monthName;
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
        return "MonthData{" +
                "monthName='" + monthName + '\'' +
                ", open=" + open +
                ", close=" + close +
                ", monthHigh=" + monthHigh +
                ", monthLow=" + monthLow +
                ", volume=" + volume +
                ", date=" + date +
                '}';
    }
}
