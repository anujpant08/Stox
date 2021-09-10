package com.example.stox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class StockDetailedViewModel extends ViewModel {
    private static final String TAG = "StockDetailedViewModel";
    private static final String API_KEY = "5LDENZCGIC5UU3VX";
    private static final String BASE_URL = "https://www.alphavantage.co/query?";
    public static final String MONTH = "Month";
    public static final String WEEK = "Week";
    public static final String DAY = "Day";
    private static MutableLiveData<Stock> stocks;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private static Stock stock;
    public LiveData<Stock> getStocksList(Context context, Stock stock) {
        StockDetailedViewModel.stock = stock;
        Log.d(TAG, "Got stock in view model: " + stock);
        if(stocks == null){
            stocks = new MutableLiveData<>();
        }
        this.context = context;
        retrieveDataFromAPI();
        return stocks;
    }
    public void setStocks(Stock stock){
        stocks.setValue(stock);
    }

    private void retrieveDataFromAPI() {
        AlphaVantageAPICall alphaVantageAPICall = new AlphaVantageAPICall(BASE_URL, API_KEY);
        List<String> params = new ArrayList<>();
        params.add("function=" + stock.getRequestType());
        params.add("symbol=" + stock.getStockSymbol());
        alphaVantageAPICall.setParams(params);
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String finalURL = alphaVantageAPICall.getURL();
        Log.d(TAG, "URL: " + finalURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalURL, this::extractJSON, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "exception occurred: " + error.getMessage());
                stock.setResultFetched(false);
                stocks.setValue(stock);
            }
        });
        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }
    private void extractJSON(String response) {
        try {
            if(stock.getRequestType().equals("TIME_SERIES_WEEKLY")){
                extractWeekData(response);
                return;
            }else if(stock.getRequestType().equals("TIME_SERIES_MONTHLY")){
                extractMonthData(response);
                return;
            }
            JSONObject jsonObject = new JSONObject(response);
            JSONObject timeSeriesDaily = jsonObject.getJSONObject("Time Series (Daily)");
            Iterator<String> keysIterator = timeSeriesDaily.keys();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat decimalFormat = new DecimalFormat("##,###.##");
            Date date;
            String key;
            JSONObject eachDay;
            DayData dayData;
            int dayNo = 1;
            while(keysIterator.hasNext() && dayNo <= 5){
                key = keysIterator.next();
                eachDay = timeSeriesDaily.getJSONObject(key);
                dayData = new DayData(DAY + dayNo);
                date = simpleDateFormat.parse(key);
                dayData.setDate(date);
                dayData.setDayHigh(eachDay.getDouble("2. high"));
                dayData.setDayLow(eachDay.getDouble("3. low"));
                dayData.setOpen(eachDay.getDouble("1. open"));
                dayData.setClose(eachDay.getDouble("4. close"));
                dayData.setVolume(eachDay.getDouble("5. volume"));
                switch(DAY + dayNo){
                    case "Day1":
                        stock.setDayData1(dayData);
                        stock.setLastTradePrice(stock.getDayData1().getClose());
                        break;
                    case  "Day2":
                        stock.setDayData2(dayData);
                        stock.setChangeValue(stock.getDayData1().getClose() - stock.getDayData2().getClose());
                        break;
                    case "Day3":
                        stock.setDayData3(dayData);
                        break;
                    case "Day4":
                        stock.setDayData4(dayData);
                        break;
                    case "Day5":
                        stock.setDayData5(dayData);
                        break;
                }
                Log.d(TAG, "Stock data for day" + dayNo + " is: " + dayData);
                dayNo++;
            }
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred while JSON parsing: ", e);
            stock.setResultFetched(false);
        }finally{
            stocks.setValue(stock);
        }
    }

    private void extractWeekData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject timeSeriesDaily = jsonObject.getJSONObject("Weekly Time Series");
            Iterator<String> keysIterator = timeSeriesDaily.keys();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            String key;
            WeekData weekData;
            int weekNo = 1;
            JSONObject eachDay;
            while (keysIterator.hasNext() && weekNo <= 5) {
                key = keysIterator.next();
                eachDay = timeSeriesDaily.getJSONObject(key);
                weekData = new WeekData(WEEK + weekNo);
                date = simpleDateFormat.parse(key);
                weekData.setDate(date);
                weekData.setWeekHigh(eachDay.getDouble("2. high"));
                weekData.setWeekLow(eachDay.getDouble("3. low"));
                weekData.setOpen(eachDay.getDouble("1. open"));
                weekData.setClose(eachDay.getDouble("4. close"));
                weekData.setVolume(eachDay.getDouble("5. volume"));
                switch (WEEK + weekNo) {
                    case "Week1":
                        stock.setWeekData1(weekData);
                        break;
                    case "Week2":
                        stock.setWeekData2(weekData);
                        break;
                    case "Week3":
                        stock.setWeekData3(weekData);
                        break;
                    case "Week4":
                        stock.setWeekData4(weekData);
                        break;
                    case "Week5":
                        stock.setWeekData5(weekData);
                        break;
                }
                Log.d(TAG, "Stock data for week" + weekNo + " is: " + weekData);
                weekNo++;
            }
            stocks.setValue(stock);
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred while JSON parsing: ", e);
        }
    }
    private void extractMonthData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject timeSeriesDaily = jsonObject.getJSONObject("Monthly Time Series");
            Iterator<String> keysIterator = timeSeriesDaily.keys();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            String key;
            MonthData monthData;
            int monthNo = 1;
            JSONObject eachDay;
            while (keysIterator.hasNext() && monthNo <= 5) {
                key = keysIterator.next();
                eachDay = timeSeriesDaily.getJSONObject(key);
                monthData = new MonthData(MONTH + monthNo);
                date = simpleDateFormat.parse(key);
                monthData.setDate(date);
                monthData.setMonthHigh(eachDay.getDouble("2. high"));
                monthData.setMonthLow(eachDay.getDouble("3. low"));
                monthData.setOpen(eachDay.getDouble("1. open"));
                monthData.setClose(eachDay.getDouble("4. close"));
                monthData.setVolume(eachDay.getDouble("5. volume"));
                switch (MONTH + monthNo) {
                    case "Month1":
                        stock.setMonthData1(monthData);
                        break;
                    case "Month2":
                        stock.setMonthData2(monthData);
                        break;
                    case "Month3":
                        stock.setMonthData3(monthData);
                        break;
                    case "Month4":
                        stock.setMonthData4(monthData);
                        break;
                    case "Month5":
                        stock.setMonthData5(monthData);
                        break;
                }
                Log.d(TAG, "Stock data for month" + monthNo + " is: " + monthData);
                monthNo++;
            }
            stocks.setValue(stock);
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred while JSON parsing: ", e);
        }
    }
}
