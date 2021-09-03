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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class APIDataViewModel extends ViewModel {
    private static final String TAG = "APIViewModel";
    private static final String API_KEY = "5LDENZCGIC5UU3VX";
    private static final String BASE_URL = "https://www.alphavantage.co/query?";
    public static MutableLiveData<Set<Stock>> stocks;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    public void addStockName(Stock newStock) {
        Set<Stock> updatedStocks = stocks.getValue();
        assert updatedStocks != null;
        updatedStocks.add(newStock);
        Log.d(TAG, "New stock added = " + newStock);
        stocks.setValue(updatedStocks);
    }
    public LiveData<Set<Stock>> getStocksList(Context context, String stockSymbol) {
        if(stocks == null){
            stocks = new MutableLiveData<>();
            stocks.setValue(new LinkedHashSet<>());
        }
        this.context = context;
        retrieveDataFromAPI(stockSymbol);
        return stocks;
    }

    private void retrieveDataFromAPI(String stockSymbol) {
        AlphaVantageAPICall alphaVantageAPICall = new AlphaVantageAPICall(BASE_URL, API_KEY);
        List<String> params = new ArrayList<>();
        params.add("function=GLOBAL_QUOTE");
        params.add("symbol=" + stockSymbol);
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
            JSONObject jsonObject = new JSONObject(response);
            JSONObject globalQuote = jsonObject.getJSONObject("Global Quote");
            //String lastRefreshed = metaData.getString("3. Last Refreshed");

            Stock stock = new Stock();
            stock.setStockSymbol(globalQuote.getString("01. symbol"));
            stock.setLastTradePrice(globalQuote.getDouble("05. price"));
            stock.setChangeValue(globalQuote.getString("09. change"));
            addStockName(stock);
        }catch (Exception exception){
            Log.e(TAG, "An exception occurred while parsing JSON: ", exception);
        }
        Log.d(TAG, "List in view model: " + stocks.getValue());
    }

}
