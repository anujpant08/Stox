package com.example.stox;

import androidx.annotation.NonNull;

public class DayData {
    String dayName = "";
    Double open = 0.0;
    Double close = 0.0;
    Double dayHigh = 0.0;
    Double dayLow = 0.0;
    Double volume = null;

    public DayData(String dayName) {
        this.dayName = dayName;
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

    public Double getDayHigh() {
        return dayHigh;
    }

    public void setDayHigh(Double dayHigh) {
        this.dayHigh = dayHigh;
    }

    public Double getDayLow() {
        return dayLow;
    }

    public void setDayLow(Double dayLow) {
        this.dayLow = dayLow;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public String getDayName() {
        return dayName;
    }

    @NonNull
    @Override
    public String toString() {
        return "DayData{" +
                "dayName='" + dayName + '\'' +
                ", open=" + open +
                ", close=" + close +
                ", dayHigh=" + dayHigh +
                ", dayLow=" + dayLow +
                ", volume=" + volume +
                '}';
    }
}
