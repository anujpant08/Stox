package com.example.stox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class StockDetailedViewActivity extends AppCompatActivity {
    private static final String TAG = "StockDetailActivity";
    private ViewPager2 timeViewPager;
    private TimeDurationFragmentAdapter timeDurationFragmentAdapter;
    private static CallToFragmentInterface callToFragmentInterface;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detailed_view);
        Intent intent = getIntent();
        String jsonStockData = intent.getStringExtra("Stock");
        final Gson[] gson = {new Gson()};
        List<Stock> savedStocks = new LinkedList<>();
        Stock stock = gson[0].fromJson(jsonStockData, Stock.class);
        TextView stockCodeTextView = findViewById(R.id.stock_code);
        stockCodeTextView.setText(stock.getStockSymbol());
        TextView stockNameTextView = findViewById(R.id.stock_name);
        stockNameTextView.setText(stock.getStockName());
        ImageView favIcon = findViewById(R.id.fav_icon);
        final boolean[] isFilledIcon = {false};
        if (stock.isFav()) {
            isFilledIcon[0] = true;
            favIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_favorite_filled_24));
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("Stocks", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        String savedJSON = sharedPreferences.getString("Stocks", "Empty Stock");
        Type type = new TypeToken<List<Stock>>(){}.getType();
        gson[0] = new Gson();
        Log.d(TAG, "data saved in shared prefs: " + savedJSON);
        savedStocks = gson[0].fromJson(savedJSON, type);
        if(savedStocks.contains(stock)){
            isFilledIcon[0] = true;
            favIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_favorite_filled_24));
        }
        Animation scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        Set<Stock> finalSavedStocks = new HashSet<>(savedStocks);
        favIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                scaleDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.e(TAG, "animation end: " + isFilledIcon[0]);
                        if(!isFilledIcon[0]) {
                            favIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_favorite_filled_24));
                            isFilledIcon[0] = true;
                            stock.setFav(true);
                            Log.e(TAG, "Stock set as fav: " + stock);
                            finalSavedStocks.add(stock);
                            gson[0] = new Gson();
                            String updatedJson = gson[0].toJson(finalSavedStocks);
                            editor.putString("Stocks", updatedJson);
                            editor.apply();
                            Snackbar snackbar = Snackbar.make(view,"Added to Favorites", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            Log.e(TAG, "Added stock to favorites: " + stock);
                        }else{
                            favIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_round_favorite_border_24));
                            isFilledIcon[0] = false;
                            stock.setFav(false);
                            Log.e(TAG, "Stock removed as fav: " + stock);
                            if(finalSavedStocks.remove(stock)){
                                String updatedJson = gson[0].toJson(finalSavedStocks);
                                editor.putString("Stocks", updatedJson);
                                editor.apply();
                                Snackbar snackbar = Snackbar.make(view,"Removed from Favorites", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                Log.e(TAG, "Removed stock from favorites: " + stock);
                            }
                        }
                        view.startAnimation(scaleUp);
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(scaleDown);
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        timeDurationFragmentAdapter = new TimeDurationFragmentAdapter(fragmentManager, getLifecycle());
        timeViewPager = findViewById(R.id.time_viewpager2);
        timeViewPager.setAdapter(timeDurationFragmentAdapter);

        DailyTimeFragment.setStock(stock);
        WeeklyTimeFragment.setStock(stock);
        MonthlyTimeFragment.setStock(stock);

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
                if (chip != null) {
                    Chip uncheckChip = null;
                    for (Integer chipID : chipIds) {
                        if (chipID != checkedId) {
                            uncheckChip = findViewById(chipID);
                            uncheckChip.setChipBackgroundColorResource(R.color.white);
                        }
                    }
                    chip.setChipBackgroundColorResource(R.color.primary_color);
                    if (chip.getText().equals("Daily")) {
                        //daily fragment
                        Log.d(TAG, "Clicked on checkedId: " + chip.getText());
                        timeViewPager.setCurrentItem(0, false);
                    } else if (chip.getText().equals("Weekly")) {
                        //weekly fragment
                        Log.d(TAG, "Clicked on checkedId: " + chip.getText());
                        timeViewPager.setCurrentItem(1, false);
                        Log.e(TAG, "Week current: " + timeViewPager.getCurrentItem());
                    } else {
                        //monthly fragment
                        Log.d(TAG, "Clicked on checkedId: " + chip.getText());
                        timeViewPager.setCurrentItem(2, false);
                        Log.e(TAG, "Month current: " + timeViewPager.getCurrentItem());
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(callToFragmentInterface != null){
            callToFragmentInterface.clearData();
        }
        super.onBackPressed();
    }
    public static void setListenerToInterface(CallToFragmentInterface toFragmentInterface){
        callToFragmentInterface = toFragmentInterface;
    }
}