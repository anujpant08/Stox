package com.example.stox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TimeDurationFragmentAdapter extends FragmentStateAdapter {

    public TimeDurationFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0){
            return new DailyTimeFragment();
        }else if(position == 1){
            return new WeeklyTimeFragment();
        }
        return new MonthlyTimeFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
