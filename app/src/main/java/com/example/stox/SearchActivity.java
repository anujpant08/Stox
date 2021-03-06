package com.example.stox;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private SearchStockCustomAdapter arrayAdapter;
    private ListView listView;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout noSignalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.search_activity_layout);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            shimmerFrameLayout = findViewById(R.id.search_shimmer);
            noSignalLayout = findViewById(R.id.no_signal_layout);
            noSignalLayout.setVisibility(View.INVISIBLE);
            LinearLayout linearLayout = findViewById(R.id.placeholder_container_layout);
            for (int i = 1; i <= 10; i++) {
                View view = inflater.inflate(R.layout.placeholder_textview_for_shimmer, null);
                linearLayout.addView(view);
            }
            setupToolbar();
            handleAPICalls();
            final Stock[] listStock = {null};
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    listStock[0] = (Stock) listView.getItemAtPosition(position);
                    Intent intent = new Intent(SearchActivity.this, StockDetailedViewActivity.class);
                    Gson gson = new Gson();
                    String jsonStockObject = gson.toJson(listStock[0]);
                    intent.putExtra("Search", jsonStockObject);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred in Search activity: ", e);
        }
    }

    private void handleAPICalls() {
        arrayAdapter = new SearchStockCustomAdapter(this, R.layout.custom_search_stock_layout, new LinkedList<>());
        listView = findViewById(R.id.search_list_view);
        listView.setAdapter(arrayAdapter);
        SearchAPIViewModel apiDataViewModel = new ViewModelProvider(this).get(SearchAPIViewModel.class);
        EditText editText = findViewById(R.id.search_edit_text);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean result = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    shimmerFrameLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.startShimmer();
                    String value = textView.getText().toString();
                    if (arrayAdapter != null) {
                        arrayAdapter.clear();
                        arrayAdapter.notifyDataSetChanged();
                    }
                    performSearch(value, apiDataViewModel);
                    result = true;
                }
                return result;
            }
        });
    }

    private void setupToolbar() {
        Toolbar mainToolbar = findViewById(R.id.material_toolbar);
        mainToolbar.setTitle("Search");
        mainToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(mainToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void performSearch(String query, SearchAPIViewModel apiViewModel) {
        apiViewModel.getStocksList(this, query).observe(this, new Observer<Set<Stock>>() {
            @Override
            public void onChanged(Set<Stock> updatedStocks) {
                Log.d(TAG, "final list : " + updatedStocks);
                if(updatedStocks == null){
                    arrayAdapter.clear();
                    Snackbar snackbar = Snackbar.make(shimmerFrameLayout,"Network error.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    //shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.INVISIBLE);
                    noSignalLayout.setVisibility(View.VISIBLE);
                }else{
                    if(updatedStocks.size() > 0){
                        noSignalLayout.setVisibility(View.INVISIBLE);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.INVISIBLE);
                        arrayAdapter.clear();
                        arrayAdapter.addAll(updatedStocks);
                    }
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            arrayAdapter.clear();
            arrayAdapter.notifyDataSetChanged();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}