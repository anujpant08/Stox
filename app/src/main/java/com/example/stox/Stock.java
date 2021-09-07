package com.example.stox;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Stock {
    String stockSymbol = "";
    String stockName = "";
    Double openPrice = 0.00;
    Double closePrice = 0.00;
    Double matchPercentage = 0.00;
    Double lastTradePrice = 0.00;
    String changeValue = "";
    DayData dayData1;
    DayData dayData2;
    DayData dayData3;
    DayData dayData4;
    DayData dayData5;
    boolean isFav = false;
    String requestType = "Day";
    WeekData weekData1;
    WeekData weekData2;
    WeekData weekData3;
    WeekData weekData4;
    WeekData weekData5;
    MonthData monthData1;
    MonthData monthData2;
    MonthData monthData3;
    MonthData monthData4;
    MonthData monthData5;
    boolean resultFetched = true;

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getMatchPercentage() {
        return matchPercentage;
    }

    public Double getLastTradePrice() {
        return lastTradePrice;
    }

    public void setLastTradePrice(Double lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public void setMatchPercentage(Double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    public DayData getDayData1() {
        return dayData1;
    }

    public void setDayData1(DayData dayData1) {
        this.dayData1 = dayData1;
    }

    public DayData getDayData2() {
        return dayData2;
    }

    public void setDayData2(DayData dayData2) {
        this.dayData2 = dayData2;
    }

    public DayData getDayData3() {
        return dayData3;
    }

    public void setDayData3(DayData dayData3) {
        this.dayData3 = dayData3;
    }

    public DayData getDayData4() {
        return dayData4;
    }

    public void setDayData4(DayData dayData4) {
        this.dayData4 = dayData4;
    }

    public DayData getDayData5() {
        return dayData5;
    }

    public void setDayData5(DayData dayData5) {
        this.dayData5 = dayData5;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public WeekData getWeekData1() {
        return weekData1;
    }

    public void setWeekData1(WeekData weekData1) {
        this.weekData1 = weekData1;
    }

    public WeekData getWeekData2() {
        return weekData2;
    }

    public void setWeekData2(WeekData weekData2) {
        this.weekData2 = weekData2;
    }

    public WeekData getWeekData3() {
        return weekData3;
    }

    public void setWeekData3(WeekData weekData3) {
        this.weekData3 = weekData3;
    }

    public WeekData getWeekData4() {
        return weekData4;
    }

    public void setWeekData4(WeekData weekData4) {
        this.weekData4 = weekData4;
    }

    public WeekData getWeekData5() {
        return weekData5;
    }

    public void setWeekData5(WeekData weekData5) {
        this.weekData5 = weekData5;
    }

    public MonthData getMonthData1() {
        return monthData1;
    }

    public void setMonthData1(MonthData monthData1) {
        this.monthData1 = monthData1;
    }

    public MonthData getMonthData2() {
        return monthData2;
    }

    public void setMonthData2(MonthData monthData2) {
        this.monthData2 = monthData2;
    }

    public MonthData getMonthData3() {
        return monthData3;
    }

    public void setMonthData3(MonthData monthData3) {
        this.monthData3 = monthData3;
    }

    public MonthData getMonthData4() {
        return monthData4;
    }

    public void setMonthData4(MonthData monthData4) {
        this.monthData4 = monthData4;
    }

    public MonthData getMonthData5() {
        return monthData5;
    }

    public void setMonthData5(MonthData monthData5) {
        this.monthData5 = monthData5;
    }

    public boolean isResultFetched() {
        return resultFetched;
    }

    public void setResultFetched(boolean resultFetched) {
        this.resultFetched = resultFetched;
    }

    @Override
    public boolean equals(Object stockObject) {
        if (this == stockObject) return true;
        if (stockObject == null || getClass() != stockObject.getClass()) return false;
        Stock stock = (Stock) stockObject;
        return stockSymbol.equals(stock.stockSymbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockSymbol);
    }

    @NonNull
    @Override
    public String toString() {
        return "Stock{" +
                "stockSymbol='" + stockSymbol + '\'' +
                ", stockName='" + stockName + '\'' +
                ", openPrice=" + openPrice +
                ", closePrice=" + closePrice +
                ", matchPercentage=" + matchPercentage +
                ", lastTradePrice=" + lastTradePrice +
                ", changeValue='" + changeValue + '\'' +
                ", dayData1=" + dayData1 +
                ", dayData2=" + dayData2 +
                ", dayData3=" + dayData3 +
                ", dayData4=" + dayData4 +
                ", dayData5=" + dayData5 +
                ", isFav=" + isFav +
                ", requestType='" + requestType + '\'' +
                ", weekData1=" + weekData1 +
                ", weekData2=" + weekData2 +
                ", weekData3=" + weekData3 +
                ", weekData4=" + weekData4 +
                ", weekData5=" + weekData5 +
                '}';
    }
}
