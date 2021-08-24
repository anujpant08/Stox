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

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Stock(){

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
                ", changeValue=" + changeValue +
                '}';
    }
}
