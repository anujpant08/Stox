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
    String changeValue = "0.00";
    DayData dayData1;
    DayData dayData2;
    DayData dayData3;
    DayData dayData4;
    DayData dayData5;
    boolean isFav = false;

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
                '}';
    }
}
