package com.example.stox;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class WeeklyTimeFragment extends Fragment implements CallToFragmentInterface {
    private static Stock stock;
    private static final String TAG = "WeeklyTimeFragment";
    private StockWeekCustomAdapter stockWeekCustomAdapter;

    public static void setStock(Stock stock) {
        WeeklyTimeFragment.stock = stock;
    }

    public static Stock getStock() {
        return stock;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekly_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmer_weekly);
        shimmerFrameLayout.startShimmer();
        ListView stockWeekListView = view.findViewById(R.id.stock_week_list_view);
        List<String> weeks = new ArrayList<>();
        weeks.add("Week1");
        weeks.add("Week2");
        weeks.add("Week3");
        weeks.add("Week4");
        weeks.add("Week5");
        stock.setRequestType("TIME_SERIES_WEEKLY");
        stockWeekCustomAdapter = new StockWeekCustomAdapter(
                requireActivity(),
                R.layout.stock_week_custom_view,
                weeks
        );
        StockDetailedViewModel stockDetailedViewModel = new ViewModelProvider(this).get(StockDetailedViewModel.class);
        stockDetailedViewModel.getStocksList(getActivity(), stock).observe(getViewLifecycleOwner(), new Observer<Stock>() {
            @Override
            public void onChanged(Stock updatedStock) {
                if(updatedStock == null){
                    stock = null;
                    Log.e(TAG, "Got null stock!");
                }else{
                    stock = updatedStock;
                    if(stock.getWeekData5() != null){
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.hideShimmer();
                    }
                }
                stockWeekCustomAdapter.notifyDataSetChanged();
            }
        });
        stockWeekListView.setAdapter(stockWeekCustomAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setChipsChecked();
    }

    private void setChipsChecked() {
        Chip weeklyChip = requireActivity().findViewById(R.id.weekly_chip);
        weeklyChip.setChecked(true);
    }

    @Override
    public void clearData() {
        stockWeekCustomAdapter.clear();
        stockWeekCustomAdapter.notifyDataSetChanged();
    }
}