package com.example.stox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
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
    private RequestQueue queue;
    private StockCustomAdapter arrayAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = StocksFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stockSet = new LinkedHashSet<>();
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Stocks", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        String savedJSON = sharedPreferences.getString("Stocks", "Empty Stock");
        Type type = new TypeToken<List<Stock>>(){}.getType();
        Gson gson = new Gson();
        Log.d(TAG, "data saved in shared prefs: " + savedJSON);
        if(savedJSON != null && !Objects.equals(savedJSON, "Empty Stock")){
            stockSet.addAll(gson.fromJson(savedJSON, type));
        }
        ListView listView = view.findViewById(R.id.stocks_list_view);
        arrayAdapter = new StockCustomAdapter(
                requireActivity(),
                R.layout.custom_stock_text_view,
                new ArrayList<>()
        );
        listView.setAdapter(arrayAdapter);
        arrayAdapter.addAll(stockSet);
        APIDataViewModel apiDataViewModel = new ViewModelProvider(this).get(APIDataViewModel.class);
        //callAPIService(editor, apiDataViewModel);
    }

    private void callAPIService(SharedPreferences.Editor editor, APIDataViewModel apiDataViewModel) {
        apiDataViewModel.getStocksList(getActivity()).observe(getViewLifecycleOwner(), new Observer<Set<Stock>>() {
            @Override
            public void onChanged(Set<Stock> updatedStocks) {
                Log.d(TAG, "final list : " + updatedStocks);
                Gson gson = new Gson();
                stockSet.addAll(updatedStocks);
                String updatedJSON = gson.toJson(stockSet);
                Log.d(TAG, "updated JSON: " + updatedJSON);
                editor.putString("Stocks", updatedJSON);
                editor.apply();
                arrayAdapter.clear();
                arrayAdapter.addAll(stockSet);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if(queue != null){
            queue.cancelAll(TAG);
        }
    }

}