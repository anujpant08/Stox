package com.example.stox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class StockDetailedViewActivity extends AppCompatActivity {
    private static final String TAG = "StockDetailActivity";
    private Stock stock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detailed_view);
        Intent intent = getIntent();
        String jsonStockData = intent.getStringExtra("Stock");
        Gson gson = new Gson();
        stock = gson.fromJson(jsonStockData, Stock.class );
        TextView stockCodeTextView = findViewById(R.id.stock_code);
        stockCodeTextView.setText(stock.getStockSymbol());
        TextView stockNameTextView = findViewById(R.id.stock_name);
        stockNameTextView.setText(stock.getStockName());
        FragmentManager fragmentManager = getSupportFragmentManager();
        TimeDurationFragmentAdapter timeDurationFragmentAdapter = new TimeDurationFragmentAdapter(fragmentManager, getLifecycle());
        ViewPager2 timeViewPager = findViewById(R.id.time_viewpager2);
        timeViewPager.setAdapter(timeDurationFragmentAdapter);

        DailyTimeFragment.setStock(stock);

        ChipGroup chipGroup = findViewById(R.id.chip_group);
        Chip dailyChip = findViewById(R.id.daily_chip);
        Integer dailyChipId = dailyChip.getId();
        Chip weeklyChip = findViewById(R.id.weekly_chip);
        Integer weeklyChipId = weeklyChip.getId();
        Chip monthlyChip = findViewById(R.id.monthly_chip);
        Integer monthlyChipId = monthlyChip.getId();
        List<Integer> chipIds = new ArrayList<>();
        chipIds.add(dailyChipId);
        chipIds.add(weeklyChipId);
        chipIds.add(monthlyChipId);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = findViewById(checkedId);
                if(chip != null){
                    Chip uncheckChip = null;
                    for(Integer chipID : chipIds){
                        if(chipID != checkedId){
                            uncheckChip = findViewById(chipID);
                            uncheckChip.setChipBackgroundColorResource(R.color.white);
                        }
                    }
                    chip.setChipBackgroundColorResource(R.color.primary_color);
                    if(chip.getText().equals("Daily")){
                        //daily fragment
                        Log.d(TAG, "Clicked on checkedId: " + chip.getText());
                        timeViewPager.setCurrentItem(0, true);
                    }else if(chip.getText().equals("Weekly")){
                        //weekly fragment
                        Log.d(TAG, "Clicked on checkedId: " + chip.getText());
                        timeViewPager.setCurrentItem(1, true);
                    }else{
                        //monthly fragment
                        Log.d(TAG, "Clicked on checkedId: " + chip.getText());
                        timeViewPager.setCurrentItem(2, true);
                    }
                }
            }
        });
    }
}