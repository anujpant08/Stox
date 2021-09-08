package com.example.stox;

import static com.example.stox.StockDetailedViewActivity.setListenerToInterface;

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
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;
import java.util.List;

public class DailyTimeFragment extends Fragment implements CallToFragmentInterface {
    private static Stock stock;
    private static final String TAG = "DailyTimeFragment";
    public StockDayCustomAdapter stockDayCustomAdapter;
    private StockDetailedViewModel stockDetailedViewModel;
    private LinearProgressIndicator linearProgressIndicator;
    private ShimmerFrameLayout shimmerFrameLayout;

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
        setListenerToInterface(this);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_daily);
        shimmerFrameLayout.startShimmer();
        ListView stockDetailedListView = view.findViewById(R.id.stock_detailed_listview);
        List<String> days = new ArrayList<>();
        days.add("Day1");
        days.add("Day2");
        days.add("Day3");
        days.add("Day4");
        days.add("Day5");
        stock.setRequestType("TIME_SERIES_DAILY");
        stockDayCustomAdapter = new StockDayCustomAdapter(
                requireActivity(),
                R.layout.stock_day_custom_view,
                days
        );
        stockDetailedViewModel = new ViewModelProvider(this).get(StockDetailedViewModel.class);
        stockDetailedViewModel.getStocksList(getActivity(), stock).observe(getViewLifecycleOwner(), new Observer<Stock>() {
            @Override
            public void onChanged(Stock updatedStock) {
                if(updatedStock == null){
                    stock = null;
                    Log.e(TAG, "Got null stock!");
                }
                else {
                    Log.e(TAG, "results fetched: " + updatedStock.isResultFetched());
                    if (!updatedStock.isResultFetched()) {
                        Log.e(TAG, "Network error");
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.hideShimmer();
                    } else {
                        stock = updatedStock;
                        if (stock.getDayData5() != null) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.hideShimmer();
                        }
                    }
                }
                stockDayCustomAdapter.notifyDataSetChanged();
            }
        });
        stockDetailedListView.setAdapter(stockDayCustomAdapter);
    }

    public void clearData(){
        stockDetailedViewModel.setStocks(null);
    }
    @Override
    public void onResume() {
        super.onResume();
        setChipsChecked();
        if(!shimmerFrameLayout.isShimmerStarted() || !shimmerFrameLayout.isShimmerVisible()){
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
        }
    }

    private void setChipsChecked() {
        Chip dailyChip = requireActivity().findViewById(R.id.daily_chip);
        dailyChip.setChecked(true);
    }
}