package com.example.stox;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    private static final String ENDPOINT_URL = "https://www.alphavantage.co/query?";
    private static final String API_KEY = "5LDENZCGIC5UU3VX";
    private static final String TAG = "SearchActivity";
    private Set<Stock> searchList;
    private SearchStockCustomAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity_layout);
        Toolbar mainToolbar = findViewById(R.id.material_toolbar);
        mainToolbar.setTitle("Search");
        mainToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(mainToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        searchList = new LinkedHashSet<>();
        arrayAdapter = new SearchStockCustomAdapter(this, R.layout.custom_search_stock_layout, new LinkedList<>());
        ListView listView = findViewById(R.id.search_list_view);
        listView.setAdapter(arrayAdapter);
        SearchAPIViewModel apiDataViewModel = new ViewModelProvider(this).get(SearchAPIViewModel.class);
        EditText editText = findViewById(R.id.search_edit_text);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean result = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String value = textView.getText().toString();
                    if(arrayAdapter != null && !arrayAdapter.isEmpty() && !searchList.isEmpty()){
                        searchList.clear();
                        arrayAdapter.clear();
                    }
                    performSearch(value, apiDataViewModel);
                    result = true;
                }
                return result;
            }
        });
    }

    private void performSearch(String query, SearchAPIViewModel apiViewModel) {
        apiViewModel.getStocksList(this, query).observe(this, new Observer<Set<Stock>>() {
            @Override
            public void onChanged(Set<Stock> updatedStocks) {
               /* if(stocks != null){
                    Log.d(TAG, "changed value: " + updatedStocks);
                    //stocks.clear();
                }else{
                    stocks = new LinkedList<>();
                }*/
                Log.d(TAG, "final list : " + updatedStocks);
                if(!arrayAdapter.isEmpty()){
                    arrayAdapter.clear();
                }
                arrayAdapter.addAll(updatedStocks);
            }
        });
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
                searchList.add(stock);
            }
            Log.d(TAG, "Final search results: " + searchList);
            arrayAdapter.addAll(searchList);
        } catch (Exception e) {
            Log.e(TAG,"An exception occurred while trying to parse JSON: ", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}