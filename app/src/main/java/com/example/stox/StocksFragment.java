package com.example.stox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.stox.databinding.StocksFragmentBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
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
    private List<Stock> sortedList;
    private RelativeLayout noStocksRelativeLayout;
    private ExtendedFloatingActionButton searchFAB;
    private SwipeRefreshLayout swipeRefreshLayout;
    private APIDataViewModel apiDataViewModel;
    private SharedPreferences.Editor editor;
    private ImageButton cancelButton;
    private ImageButton removeFavButton;

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
            searchFAB = requireActivity().findViewById(R.id.search_floating_button);
            cancelButton = requireActivity().findViewById(R.id.cancel_selection);
            removeFavButton = requireActivity().findViewById(R.id.delete_button);
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.primary_color, requireActivity().getTheme()));
            }
            stockSet = new HashSet<>();
            sharedPreferences = requireActivity().getSharedPreferences("Stocks", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            String savedJSON = sharedPreferences.getString("Stocks", "Empty Stock");
            Type type = new TypeToken<LinkedList<Stock>>() {
            }.getType();
            Gson gson = new Gson();
            Log.d(TAG, "data saved in shared prefs: " + savedJSON);
            if (savedJSON != null && !Objects.equals(savedJSON, "Empty Stock")) {
                stockSet.addAll(gson.fromJson(savedJSON, type));
            }
            if (stockSet.size() > 0) {
                searchFAB.setVisibility(View.VISIBLE);
                noStocksRelativeLayout.setVisibility(View.INVISIBLE);
            } else {
                searchFAB.setVisibility(View.INVISIBLE);
                noStocksRelativeLayout.setVisibility(View.VISIBLE);
            }
            listView = view.findViewById(R.id.stocks_list_view);
            arrayAdapter = new StockCustomAdapter(
                    requireActivity(),
                    R.layout.custom_stock_text_view,
                    new ArrayList<>()
            );
            listView.setAdapter(arrayAdapter);
            sortedList = new ArrayList<>(stockSet);
            Collections.sort(sortedList, Collections.reverseOrder());
            arrayAdapter.addAll(sortedList);
            apiDataViewModel = new ViewModelProvider(this).get(APIDataViewModel.class);
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
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    cancelButton.setVisibility(View.VISIBLE);
                    removeFavButton.setVisibility(View.VISIBLE);
                    Log.e(TAG, "Selected item: " + adapterView.getItemAtPosition(i));
                    arrayAdapter.setChecked(true);
                    arrayAdapter.notifyDataSetChanged();
                    listView.setAdapter(arrayAdapter);
                    return true;
                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    arrayAdapter.setChecked(false);
                    arrayAdapter.notifyDataSetChanged();
                    listView.setAdapter(arrayAdapter);
                    removeFavButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                }
            });
            removeFavButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View favButtonView) {
                    List<Stock> checkedItems = new ArrayList<>(arrayAdapter.getCheckedItems());
                    stockSet.removeAll(checkedItems);
                    sortedList = new ArrayList<>(stockSet);
                    Collections.sort(sortedList, Collections.reverseOrder());
                    arrayAdapter.clear();
                    arrayAdapter.addAll(sortedList);
                    arrayAdapter.notifyDataSetChanged();
                    saveStocksInJson();
                    arrayAdapter.setChecked(false);
                    arrayAdapter.notifyDataSetChanged();
                    listView.setAdapter(arrayAdapter);
                    removeFavButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                    Snackbar snackbar = Snackbar.make(view,"Removed from favourites", Snackbar.LENGTH_LONG);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        snackbar.setActionTextColor(getResources().getColor(R.color.primary_color, requireActivity().getTheme()));
                    }
                    snackbar.setAnchorView(requireActivity().findViewById(R.id.search_floating_button));
                    snackbar.setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            stockSet.addAll(checkedItems);
                            sortedList = new ArrayList<>(stockSet);
                            Collections.sort(sortedList, Collections.reverseOrder());
                            arrayAdapter.clear();
                            arrayAdapter.addAll(sortedList);
                            arrayAdapter.notifyDataSetChanged();
                            listView.setAdapter(arrayAdapter);
                            saveStocksInJson();
                        }
                    });
                    snackbar.show();
                }
            });
            ImageButton noFavsButton = view.findViewById(R.id.no_favs);
            noFavsButton.setOnClickListener(view12 -> {
                Intent intent = new Intent(requireActivity(), SearchActivity.class);
                startActivity(intent);
            });
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred in homepage: ", e);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (Stock stock : stockSet) {
                    callAPIService(editor, apiDataViewModel, stock.getStockSymbol());
                }
            }
        });
    }

    private void saveStocksInJson() {
        Gson gson = new Gson();
        String updatedJSON = gson.toJson(stockSet);
        Log.d(TAG, "updated JSON: " + updatedJSON);
        editor.putString("Stocks", updatedJSON);
        editor.apply();
    }

    private void callAPIService(SharedPreferences.Editor editor, APIDataViewModel apiDataViewModel, String stockSymbol) {
        apiDataViewModel.getStocksList(getActivity(), stockSymbol, stockSet).observe(getViewLifecycleOwner(), updatedStocks -> {
            Log.d(TAG, "final list : " + updatedStocks);
            swipeRefreshLayout.setRefreshing(false);
            Gson gson = new Gson();
            stockSet = updatedStocks;
            String updatedJSON = gson.toJson(stockSet);
            Log.d(TAG, "updated JSON: " + updatedJSON);
            editor.putString("Stocks", updatedJSON);
            editor.apply();
            arrayAdapter.clear();
            sortedList.clear();
            sortedList.addAll(stockSet);
            Collections.sort(sortedList, Collections.reverseOrder());
            arrayAdapter.addAll(sortedList);
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
                searchFAB.setVisibility(View.VISIBLE);
                noStocksRelativeLayout.setVisibility(View.INVISIBLE);
            } else {
                searchFAB.setVisibility(View.INVISIBLE);
                noStocksRelativeLayout.setVisibility(View.VISIBLE);
            }
            Log.e(TAG, "stocks in stocksFragment: " + stockSet);
            arrayAdapter.clear();
            sortedList.clear();
            sortedList.addAll(stockSet);
            Collections.sort(sortedList, Collections.reverseOrder());
            Log.e(TAG, "Sorted list: " + sortedList);
            arrayAdapter.addAll(sortedList);
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