package com.example.stox;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TimeDurationFragmentAdapter extends FragmentStateAdapter {
    private static final String TAG = "TimeDurFragmentAdapter";

    public TimeDurationFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0){
            return new DailyTimeFragment();
        }else if(position == 1){
            Log.e(TAG,"Fragment is Week" );
            return new WeeklyTimeFragment();
        }
        Log.e(TAG,"Fragment is Month" );
        return new MonthlyTimeFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
