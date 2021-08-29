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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SearchAPIViewModel extends ViewModel {
    private static final String TAG = "SearchAPIViewModel";
    private static final String API_KEY = "5LDENZCGIC5UU3VX";
    private static final String BASE_URL = "https://www.alphavantage.co/query?";
    public static final String STOCKS_NAME_REGEX = "Symbol\":\\s+\"([^.]+)";
    private static MutableLiveData<Set<Stock>> stocks;
    private Set<Stock> updatedStocks;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    public void addStockName() {
        Log.d(TAG, "New stock list = " + updatedStocks);
        stocks.setValue(updatedStocks);
    }
    public LiveData<Set<Stock>> getStocksList(Context context, String query) {
        if(stocks == null){
            stocks = new MutableLiveData<>();
            stocks.setValue(new LinkedHashSet<>());
        }
        Objects.requireNonNull(stocks.getValue()).clear();
        this.context = context;
        retrieveDataFromAPI(query);
        return stocks;
    }

    private void retrieveDataFromAPI(String query) {
        AlphaVantageAPICall alphaVantageAPICall = new AlphaVantageAPICall(BASE_URL, API_KEY);
        List<String> params = new ArrayList<>();
        params.add("function=SYMBOL_SEARCH");
        params.add("keywords=" + query);
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
    //Helper method to extract relevant stocks data from JSON response
    private void extractJSON(String response) {
        try{
            updatedStocks = new LinkedHashSet<>();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray bestMatchesArray = jsonObject.getJSONArray("bestMatches");
            JSONObject bestMatchObject = null;
            for(int index = 0; index < bestMatchesArray.length(); index ++){
                bestMatchObject = (JSONObject) bestMatchesArray.get(index);
                Stock stock = new Stock();
                stock.setStockSymbol(bestMatchObject.get("1. symbol").toString());
                stock.setStockName(bestMatchObject.get("2. name").toString());
                stock.setMatchPercentage(Double.parseDouble(bestMatchObject.get("9. matchScore").toString()));
                updatedStocks.add(stock);
            }
            addStockName();
        }catch (Exception exception){
            Log.e(TAG, "An exception occurred while parsing JSON: ", exception);
        }
        Log.d(TAG, "List in view model: " + stocks.getValue());
    }

}

