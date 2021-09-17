package com.example.stox;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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

public class MonthlyTimeFragment extends Fragment implements CallToFragmentInterface{
    private static Stock stock;
    private static final String TAG = "MonthlyTimeFragment";
    private StockMonthCustomAdapter stockMonthCustomAdapter;
    private ShimmerFrameLayout shimmerFrameLayout;

    public static void setStock(Stock stock) {
        MonthlyTimeFragment.stock = stock;
    }

    public static Stock getStock() {
        return stock;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monthly_time, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Animation anim = new TranslateAnimation(0f, 0f, 50f, 0f);
        anim.setFillAfter(true);
        anim.setDuration(600);
        view.startAnimation(anim);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_monthly);
        shimmerFrameLayout.startShimmer();
        ListView stockMonthListView = view.findViewById(R.id.stock_month_list_view);
        List<String> months = new ArrayList<>();
        months.add("Month1");
        months.add("Month2");
        months.add("Month3");
        months.add("Month4");
        months.add("Month5");
        stock.setRequestType("TIME_SERIES_MONTHLY");
        stockMonthCustomAdapter = new StockMonthCustomAdapter(
                requireActivity(),
                R.layout.stock_month_custom_view,
                months
        );
        StockDetailedViewModel stockDetailedViewModel = new ViewModelProvider(this).get(StockDetailedViewModel.class);
        stockDetailedViewModel.getStocksList(getActivity(), stock).observe(getViewLifecycleOwner(), new Observer<Stock>() {
            @Override
            public void onChanged(Stock updatedStock) {
                if(updatedStock == null){
                    //stock = null;
                    Log.e(TAG, "Got null stock!");
                }
                else {
                    if (!updatedStock.isResultFetched()) {
                        Log.e(TAG, "Network error");
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.hideShimmer();
                    } else {
                        stock = updatedStock;
                        if (stock.getMonthData5() != null) {
                            shimmerFrameLayout.stopShimmer();
                            shimmerFrameLayout.hideShimmer();
                        }
                    }
                }
                stockMonthCustomAdapter.notifyDataSetChanged();
            }
        });
        stockMonthListView.setAdapter(stockMonthCustomAdapter);
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
        Chip monthlyChip = requireActivity().findViewById(R.id.monthly_chip);
        monthlyChip.setChecked(true);
    }

    @Override
    public void clearData() {
        stockMonthCustomAdapter.clear();
        stockMonthCustomAdapter.notifyDataSetChanged();
    }
}