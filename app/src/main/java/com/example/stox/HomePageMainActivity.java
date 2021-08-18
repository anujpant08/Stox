package com.example.stox;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import com.example.stox.databinding.ActivityHomePageMainBinding;
import com.google.android.material.tabs.TabLayout;

public class HomePageMainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.stox.databinding.ActivityHomePageMainBinding binding = ActivityHomePageMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar mainToolbar = findViewById(R.id.material_toolbar);
        mainToolbar.setTitle("Home");
        mainToolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(mainToolbar);
        tabLayout = findViewById(R.id.tabsLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        setupFragments();
        //retrieveDataFromAPI();
    }



    private void setupFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentAdapter = new FragmentAdapter(fragmentManager, getLifecycle());
        viewPager2.setAdapter(fragmentAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("Stocks"));
        tabLayout.addTab(tabLayout.newTab().setText("Index"));
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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }
    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}