package com.example.stox;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    private static final String ENDPOINT_URL = "https://www.alphavantage.co/query?";
    private static final String API_KEY = "5LDENZCGIC5UU3VX";
    private static final String TAG = "SearchActivity";
    private Set<String> searchList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_layout);
        Toolbar mainToolbar = findViewById(R.id.material_toolbar);
        mainToolbar.setTitle("Browse Stocks");
        mainToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(mainToolbar);
        searchList = new LinkedHashSet<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new LinkedList<>(searchList));
        ListView listView = findViewById(R.id.search_list_view);
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if(searchList != null){
                searchList.clear();
            }
            performSearch(query);
            listView.setAdapter(arrayAdapter);
            //arrayAdapter.addAll(searchList);
        }
    }

    private void performSearch(String query) {
        AlphaVantageAPICall alphaVantageAPICall = new AlphaVantageAPICall(ENDPOINT_URL, API_KEY);
        List<String> params = new ArrayList<>();
        params.add("function=SYMBOL_SEARCH");
        params.add("keywords=" + query);
        alphaVantageAPICall.setParams(params);
        String finalURL = alphaVantageAPICall.getURL();
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(TAG, "URL: " + finalURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Get list of all stock in search results.
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

    private void extractJSON(String response) {
        try {
            searchList.clear();
            Log.e(TAG, "response: " + response);
            JSONObject jsonObject = new JSONObject(response);
            JSONArray bestMatchesArray = jsonObject.getJSONArray("bestMatches");
            JSONObject bestMatchObject = null;
            for(int index = 0; index < bestMatchesArray.length(); index ++){
                bestMatchObject = (JSONObject) bestMatchesArray.get(index);
                Stock stock = new Stock();
                stock.setStockSymbol(bestMatchObject.get("1. symbol").toString());
                stock.setStockName(bestMatchObject.get("2. name").toString());
                stock.setMatchPercentage(Double.parseDouble(bestMatchObject.get("9. matchScore").toString()));
                searchList.add(stock.getStockSymbol() + " " + stock.getStockName());
            }
            Log.d(TAG, "Final search results: " + searchList);
            arrayAdapter.addAll(searchList);
        } catch (Exception e) {
            Log.e(TAG,"An exception occurred while trying to parse JSON: ", e);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_items, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                arrayAdapter.clear();
                performSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //arrayAdapter.getFilter().filter(s);
                return false;
            }
        });
        searchView.setIconifiedByDefault(true);
        return true;
    }
}