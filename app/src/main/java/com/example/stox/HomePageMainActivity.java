package com.example.stox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stox.databinding.ActivityHomePageMainBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class HomePageMainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.stox.databinding.ActivityHomePageMainBinding binding = ActivityHomePageMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ExtendedFloatingActionButton searchFAB = findViewById(R.id.search_floating_button);
        searchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageMainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        /*Toolbar mainToolbar = findViewById(R.id.material_toolbar);
        mainToolbar.setTitle("Stocks");
        mainToolbar.setSubtitle("Your favorites");
        setSupportActionBar(mainToolbar);*/
        //tabLayout = findViewById(R.id.tabsLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        setupFragments();
        //retrieveDataFromAPI();
    }
    private void setupFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);
        //Removing tab layout

        /*tabLayout.addTab(tabLayout.newTab().setText("Stocks"));
        //tabLayout.addTab(tabLayout.newTab().setText("Index"));
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });*/
    }
}