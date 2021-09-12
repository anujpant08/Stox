package com.example.stox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.stox.databinding.StocksFragmentBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class StocksFragment extends Fragment {

    private static final String TAG = "StocksFragment";
    private StocksFragmentBinding binding;
    private Set<Stock> stockSet;
    private StockCustomAdapter arrayAdapter;
    private SharedPreferences sharedPreferences;
    private ListView listView;
    private RelativeLayout noStocksRelativeLayout;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = StocksFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            noStocksRelativeLayout = view.findViewById(R.id.no_stocks);
            stockSet = new LinkedHashSet<>();
            sharedPreferences = requireActivity().getSharedPreferences("Stocks", Context.MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
            String savedJSON = sharedPreferences.getString("Stocks", "Empty Stock");
            Type type = new TypeToken<List<Stock>>() {
            }.getType();
            Gson gson = new Gson();
            Log.d(TAG, "data saved in shared prefs: " + savedJSON);
            if (savedJSON != null && !Objects.equals(savedJSON, "Empty Stock")) {
                stockSet.addAll(gson.fromJson(savedJSON, type));
            }
            if (stockSet.size() > 0) {
                noStocksRelativeLayout.setVisibility(View.INVISIBLE);
            } else {
                noStocksRelativeLayout.setVisibility(View.VISIBLE);
            }
            listView = view.findViewById(R.id.stocks_list_view);
            arrayAdapter = new StockCustomAdapter(
                    requireActivity(),
                    R.layout.custom_stock_text_view,
                    new ArrayList<>()
            );
            listView.setAdapter(arrayAdapter);
            arrayAdapter.addAll(stockSet);
            APIDataViewModel apiDataViewModel = new ViewModelProvider(this).get(APIDataViewModel.class);
            for (Stock stock : stockSet) {
                callAPIService(editor, apiDataViewModel, stock.getStockSymbol());
            }
            listView.setOnItemClickListener((adapterView, view1, position, l) -> {
                Stock clickedStock = (Stock) listView.getItemAtPosition(position);
                Intent intent = new Intent(requireActivity(), StockDetailedViewActivity.class);
                Gson gson1 = new Gson();
                String jsonStockObject = gson1.toJson(clickedStock);
                intent.putExtra("Search", jsonStockObject);
                startActivity(intent);
            });
            ImageButton noFavsButton = view.findViewById(R.id.no_favs);
            noFavsButton.setOnClickListener(view12 -> {
                Intent intent = new Intent(requireActivity(), SearchActivity.class);
                startActivity(intent);
            });
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred in homepage: ", e);
        }
    }

    private void callAPIService(SharedPreferences.Editor editor, APIDataViewModel apiDataViewModel, String stockSymbol) {
        apiDataViewModel.getStocksList(getActivity(), stockSymbol, stockSet).observe(getViewLifecycleOwner(), updatedStocks -> {
            Log.d(TAG, "final list : " + updatedStocks);
            Gson gson = new Gson();
            stockSet = updatedStocks;
            String updatedJSON = gson.toJson(stockSet);
            Log.d(TAG, "updated JSON: " + updatedJSON);
            editor.putString("Stocks", updatedJSON);
            editor.apply();
            arrayAdapter.clear();
            arrayAdapter.addAll(stockSet);
            arrayAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        try {
            super.onResume();
            stockSet.clear();
            sharedPreferences = requireActivity().getSharedPreferences("Stocks", Context.MODE_PRIVATE);
            String savedJSON = sharedPreferences.getString("Stocks", "Empty Stock");
            Type type = new TypeToken<List<Stock>>() {
            }.getType();
            Gson gson = new Gson();
            Log.d(TAG, "data saved in shared prefs: " + savedJSON);
            if (savedJSON != null && !Objects.equals(savedJSON, "Empty Stock")) {
                stockSet.addAll(gson.fromJson(savedJSON, type));
            }
            if (stockSet.size() > 0) {
                noStocksRelativeLayout.setVisibility(View.INVISIBLE);
            } else {
                noStocksRelativeLayout.setVisibility(View.VISIBLE);
            }
            Log.e(TAG, "stocks in stocksFragment: " + stockSet);
            arrayAdapter.clear();
            arrayAdapter.addAll(stockSet);
            arrayAdapter.notifyDataSetChanged();
            listView.setAdapter(arrayAdapter);
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred while resuming frag: ", e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}