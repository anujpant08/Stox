package com.example.stox;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.stox.databinding.StocksFragmentBinding;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class StocksFragment extends Fragment implements ItemClickListener{

    private static final String TAG = "StocksFragment";
    private StocksFragmentBinding binding;
    private Set<Stock> stockSet;
    private StockCustomAdapter arrayAdapter;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private List<Stock> sortedList;
    private RelativeLayout noStocksRelativeLayout;
    private ExtendedFloatingActionButton searchFAB;
    private SwipeRefreshLayout swipeRefreshLayout;
    private APIDataViewModel apiDataViewModel;
    private SharedPreferences.Editor editor;
    private FloatingActionButton cancelFloatingButton;
    private boolean isDeleteEnabled = false;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = StocksFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            noStocksRelativeLayout = view.findViewById(R.id.no_stocks);
            searchFAB = requireActivity().findViewById(R.id.search_floating_button);
            //cancelButton = requireActivity().findViewById(R.id.cancel_selection);
            cancelFloatingButton = view.findViewById(R.id.cancel_fab);
            cancelFloatingButton.animate().translationY(200f).setDuration(100).setListener(null);
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.primary_color, requireActivity().getTheme()));
            }
            stockSet = new HashSet<>();
            sharedPreferences = requireActivity().getSharedPreferences("Stocks", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            String savedJSON = sharedPreferences.getString("Stocks", "Empty Stock");
            Type type = new TypeToken<LinkedList<Stock>>() {
            }.getType();
            Gson gson = new Gson();
            Log.d(TAG, "data saved in shared prefs: " + savedJSON);
            if (savedJSON != null && !Objects.equals(savedJSON, "Empty Stock")) {
                stockSet.addAll(gson.fromJson(savedJSON, type));
            }
            if (stockSet.size() > 0) {
                searchFAB.setVisibility(View.VISIBLE);
                noStocksRelativeLayout.setVisibility(View.INVISIBLE);
            } else {
                searchFAB.setVisibility(View.INVISIBLE);
                noStocksRelativeLayout.setVisibility(View.VISIBLE);
            }
            sortedList = new ArrayList<>(stockSet);
            Collections.sort(sortedList, Collections.reverseOrder());
            recyclerView = view.findViewById(R.id.stocks_list_view);
            arrayAdapter = new StockCustomAdapter(this, sortedList);
            arrayAdapter.setItemClickListener(this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(arrayAdapter);
            apiDataViewModel = new ViewModelProvider(this).get(APIDataViewModel.class);
            for (Stock stock : stockSet) {
                callAPIService(editor, apiDataViewModel, stock.getStockSymbol());
            }
            /*cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    arrayAdapter.setChecked(false);
                    //arrayAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(arrayAdapter);
                    removeFavButton.setVisibility(View.GONE);
                    cancelButton.setVisibility(View.GONE);
                }
            });*/
            cancelFloatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isDeleteEnabled) {
                        arrayAdapter.setChecked(false);
                        //arrayAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(arrayAdapter);
                        cancelFloatingButton.animate().translationY(200f).setDuration(200).setListener(null);
                        //cancelFloatingButton.setVisibility(View.GONE);
                        searchFAB.animate().translationY(0f).setDuration(200).setListener(null);
                    }else {
                        cancelFloatingButton.animate().translationY(200f).setDuration(200).setListener(null);
                        List<Stock> checkedItems = new ArrayList<>(arrayAdapter.getCheckedItems());
                        stockSet.removeAll(checkedItems);
                        sortedList.clear();
                        sortedList.addAll(stockSet);
                        Collections.sort(sortedList, Collections.reverseOrder());
                        arrayAdapter.notifyDataSetChanged();
                        saveStocksInJson();
                        arrayAdapter.setChecked(false);
                        recyclerView.setAdapter(arrayAdapter);
                        Snackbar snackbar = Snackbar.make(view, "Removed from favourites", Snackbar.LENGTH_LONG);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            snackbar.setActionTextColor(getResources().getColor(R.color.primary_color, requireActivity().getTheme()));
                        }
                        snackbar.setAnchorView(searchFAB);
                        snackbar.setAction("Undo", new View.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(View view) {
                                stockSet.addAll(checkedItems);
                                sortedList.clear();
                                sortedList.addAll(stockSet);
                                Collections.sort(sortedList, Collections.reverseOrder());
                                arrayAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(arrayAdapter);
                                saveStocksInJson();
                            }
                        });
                        searchFAB.animate().translationY(0f).setDuration(200).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                snackbar.show();
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                    }
                    //Resetting drawable back to cancel
                    cancelFloatingButton.setImageDrawable(getResources().getDrawable(R.drawable.cancel));
                }
            });
            ImageButton noFavsButton = view.findViewById(R.id.no_favs);
            noFavsButton.setOnClickListener(view12 -> {
                Intent intent = new Intent(requireActivity(), SearchActivity.class);
                startActivity(intent);
            });
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred in homepage: ", e);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (Stock stock : stockSet) {
                    callAPIService(editor, apiDataViewModel, stock.getStockSymbol());
                }
            }
        });
    }

    private void saveStocksInJson() {
        Gson gson = new Gson();
        String updatedJSON = gson.toJson(stockSet);
        Log.d(TAG, "updated JSON: " + updatedJSON);
        editor.putString("Stocks", updatedJSON);
        editor.apply();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void callAPIService(SharedPreferences.Editor editor, APIDataViewModel apiDataViewModel, String stockSymbol) {
        apiDataViewModel.getStocksList(getActivity(), stockSymbol, stockSet).observe(getViewLifecycleOwner(), updatedStocks -> {
            Log.d(TAG, "final list : " + updatedStocks);
            swipeRefreshLayout.setRefreshing(false);
            Gson gson = new Gson();
            stockSet = updatedStocks;
            String updatedJSON = gson.toJson(stockSet);
            Log.d(TAG, "updated JSON: " + updatedJSON);
            editor.putString("Stocks", updatedJSON);
            editor.apply();
            sortedList.clear();
            sortedList.addAll(stockSet);
            Collections.sort(sortedList, Collections.reverseOrder());
            arrayAdapter.notifyDataSetChanged();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onResume() {
        try {
            super.onResume();
            stockSet.clear();
            sharedPreferences = requireActivity().getSharedPreferences("Stocks", Context.MODE_PRIVATE);
            String savedJSON = sharedPreferences.getString("Stocks", "Empty Stock");
            Type type = new TypeToken<List<Stock>>() {
            }.getType();
            Gson gson = new Gson();
            Log.d(TAG, "data saved in shared prefs: " + savedJSON);
            if (savedJSON != null && !Objects.equals(savedJSON, "Empty Stock")) {
                stockSet.addAll(gson.fromJson(savedJSON, type));
            }
            if (stockSet.size() > 0) {
                searchFAB.setVisibility(View.VISIBLE);
                noStocksRelativeLayout.setVisibility(View.INVISIBLE);
            } else {
                searchFAB.setVisibility(View.INVISIBLE);
                noStocksRelativeLayout.setVisibility(View.VISIBLE);
            }
            sortedList.clear();
            sortedList.addAll(stockSet);
            Collections.sort(sortedList, Collections.reverseOrder());
            arrayAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(arrayAdapter);
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred while resuming frag: ", e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view, int position) {
        Stock clickedStock = sortedList.get(position);
        Intent intent = new Intent(StocksFragment.this.requireActivity(), StockDetailedViewActivity.class);
        Gson gson1 = new Gson();
        String jsonStockObject = gson1.toJson(clickedStock);
        intent.putExtra("Search", jsonStockObject);
        StocksFragment.this.startActivity(intent);
    }

    @Override
    public void onLongClick(View view, int position) {
        cancelFloatingButton.setVisibility(View.VISIBLE);
        cancelFloatingButton.animate().translationY(-200f).setDuration(200).setListener(null);
        searchFAB.animate().translationY(200f).setDuration(200).setListener(null);
        arrayAdapter.setChecked(true);
        recyclerView.setAdapter(arrayAdapter);
    }
    public void updateFABIcon(boolean areAllUnChecked){
        if(areAllUnChecked){
            cancelFloatingButton.setImageDrawable(getResources().getDrawable(R.drawable.cancel));
            isDeleteEnabled = false;
        }else{
            cancelFloatingButton.setImageDrawable(getResources().getDrawable(R.drawable.delete));
            isDeleteEnabled = true;
        }
    }
}