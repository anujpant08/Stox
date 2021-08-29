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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StockDetailedViewModel extends ViewModel {
    private static final String TAG = "StockDetailedViewModel";
    private static final String API_KEY = "5LDENZCGIC5UU3VX";
    private static final String BASE_URL = "https://www.alphavantage.co/query?";
    public static final String DAY = "Day";
    private static MutableLiveData<Stock> stocks;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private Stock stock;
    public LiveData<Stock> getStocksList(Context context, Stock stock) {
        this.stock  = stock;
        Log.d(TAG, "Got stock in view model: " + stock);
        if(stocks == null){
            stocks = new MutableLiveData<>();
            stocks.setValue(this.stock);
        }
        this.context = context;
        retrieveDataFromAPI();
        return stocks;
    }

    private void retrieveDataFromAPI() {
        //globalQuoteAPICall();
        AlphaVantageAPICall alphaVantageAPICall = new AlphaVantageAPICall(BASE_URL, API_KEY);
        List<String> params = new ArrayList<>();
        params.add("function=TIME_SERIES_DAILY");
        params.add("symbol=" + stock.getStockSymbol());
        alphaVantageAPICall.setParams(params);
        RequestQueue queue = Volley.newRequestQueue(this.context);
        String finalURL = alphaVantageAPICall.getURL();
        Log.d(TAG, "URL: " + finalURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                extractJSON(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "exception occurred: ", error);
            }
        });
        stringRequest.setTag(TAG);
        queue.add(stringRequest);
    }

//    private void globalQuoteAPICall() {
//        AlphaVantageAPICall alphaVantageAPICall = new AlphaVantageAPICall(BASE_URL, API_KEY);
//        List<String> params = new ArrayList<>();
//        params.add("function=GLOBAL_QUOTE");
//        params.add("symbol=" + stock.getStockSymbol());
//        alphaVantageAPICall.setParams(params);
//        RequestQueue queue = Volley.newRequestQueue(this.context);
//        String finalURL = alphaVantageAPICall.getURL();
//        Log.d(TAG, "URL: " + finalURL);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalURL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                extractJSONForGlobalQuote(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "exception occurred: ", error);
//            }
//        });
//        stringRequest.setTag(TAG);
//        queue.add(stringRequest);
//    }
//
//    private void extractJSONForGlobalQuote(String response) {
//        try{
//            Log.e(TAG, "Response for global quote: " + response);
//            JSONObject jsonObject = new JSONObject(response);
//            JSONObject globalQuote = jsonObject.getJSONObject("Global Quote");
//            stock.setChangeValue(globalQuote.getString("09. change"));
//        }catch (Exception exception){
//            Log.e(TAG, "An exception occurred while parsing JSON: ", exception);
//        }
//    }

    private void extractJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject timeSeriesDaily = jsonObject.getJSONObject("Time Series (Daily)");
            Iterator<String> keysIterator = timeSeriesDaily.keys();
            String key = null;
            JSONObject eachDay = null;
            DayData dayData;
            int dayNo = 1;
            while(keysIterator.hasNext() && dayNo <= 5){
                key = keysIterator.next();
                eachDay = timeSeriesDaily.getJSONObject(key);
                dayData = new DayData(DAY + dayNo);
                dayData.setDayHigh(eachDay.getDouble("2. high"));
                dayData.setDayLow(eachDay.getDouble("3. low"));
                dayData.setOpen(eachDay.getDouble("1. open"));
                dayData.setClose(eachDay.getDouble("4. close"));
                dayData.setVolume(eachDay.getDouble("5. volume"));
                switch(DAY + dayNo){
                    case "Day1":
                        stock.setDayData1(dayData);
                        break;
                    case  "Day2":
                        stock.setDayData2(dayData);
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
            stocks.setValue(stock);
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred while JSON parsing: ", e);
        }

    }
}
