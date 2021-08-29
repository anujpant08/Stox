package com.example.stox;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

public class DailyTimeFragment extends Fragment {
    private static Stock stock;

    public static void setStock(Stock stock) {
        DailyTimeFragment.stock = stock;
    }

    public static Stock getStock() {
        return stock;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView stockDetailedListView = view.findViewById(R.id.stock_detailed_listview);
        List<String> days = new ArrayList<>();
        days.add("Day1");
        days.add("Day2");
        days.add("Day3");
        days.add("Day4");
        days.add("Day5");
        StockDetailedCustomAdapter stockDetailedCustomAdapter = new StockDetailedCustomAdapter(
                requireActivity(),
                R.layout.stock_detailed_custom_view,
                days
        );
        StockDetailedViewModel stockDetailedViewModel = new ViewModelProvider(this).get(StockDetailedViewModel.class);
        stockDetailedViewModel.getStocksList(getActivity(), stock).observe(getViewLifecycleOwner(), new Observer<Stock>() {
            @Override
            public void onChanged(Stock updatedStock) {
                stock = updatedStock;
                stockDetailedCustomAdapter.notifyDataSetChanged();
            }
        });
        stockDetailedListView.setAdapter(stockDetailedCustomAdapter);
    }
}