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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APIDataViewModel extends ViewModel {
    private static final String TAG = "APIViewModel";
    private static final String API_KEY = "5LDENZCGIC5UU3VX";
    private static final String BASE_URL = "https://www.alphavantage.co/query?";
    public static final String STOCKS_NAME_REGEX = "Symbol\":\\s+\"([^.]+)";
    private static MutableLiveData<Set<String>> stocks;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    public void addStockName(String name) {
        Set<String> updatedStocks = stocks.getValue();
        assert updatedStocks != null;
        updatedStocks.add(name);
        Log.d(TAG, "New stock added = " + name);
        stocks.setValue(updatedStocks);
    }
    public LiveData<Set<String>> getStocksList(Context context) {
        if(stocks == null){
            stocks = new MutableLiveData<>();
            stocks.setValue(new LinkedHashSet<>());
        }
        this.context = context;
        retrieveDataFromAPI();
        return stocks;
    }

    private void retrieveDataFromAPI() {
        AlphaVantageAPICall alphaVantageAPICall = new AlphaVantageAPICall(BASE_URL, API_KEY);
        List<String> params = new ArrayList<>();
        params.add("function=TIME_SERIES_DAILY");
        params.add("symbol=TCS.BSE");
        alphaVantageAPICall.setParams(params);
        RequestQueue queue = Volley.newRequestQueue(this.context);
        Log.d(TAG, "URL: " + alphaVantageAPICall.getURL());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, alphaVantageAPICall.getURL(), new Response.Listener<String>() {
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
    //Helper method to extract relevant stocks data from JSON response
    private void extractJSON(String response) {
        Pattern pattern = Pattern.compile(STOCKS_NAME_REGEX);
        Matcher stocksMatcher = pattern.matcher(response);
        while(stocksMatcher.find()){
            Log.d(TAG, "fetched value: " + stocksMatcher.group(1));
            addStockName(stocksMatcher.group(1));
        }
        Log.d(TAG, "List in view model: " + stocks.getValue());
    }

}
