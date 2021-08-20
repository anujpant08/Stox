package com.example.stox;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.example.stox.databinding.StocksFragmentBinding;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class StocksFragment extends Fragment {

    private static final String TAG = "StocksFragment";
    private StocksFragmentBinding binding;
    private TextView textView;
    private ListView listView;
    private List<String> stocks;
    private Set<String> stockSet;
    private RequestQueue queue;
    private APIDataViewModel apiDataViewModel;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = StocksFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stocks = new LinkedList<>();
        stockSet = new LinkedHashSet<>();
        // For testing set of stocks

//        stockSet.add("RIL");
//        stockSet.add("SAIL");
//        stockSet.add("ITC");

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Stocks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        stockSet = sharedPreferences.getStringSet("Stocks", stockSet);
        textView = view.findViewById(R.id.textview_first);
        listView = view.findViewById(R.id.stocks_list_view);
        arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                stocks
        );
        listView.setAdapter(arrayAdapter);
        arrayAdapter.addAll(stockSet);
        apiDataViewModel = new ViewModelProvider(this).get(APIDataViewModel.class);
        apiDataViewModel.getStocksList(getActivity()).observe(getViewLifecycleOwner(), new Observer<Set<String>>() {
            @Override
            public void onChanged(Set<String> updatedStocks) {
               /* if(stocks != null){
                    Log.d(TAG, "changed value: " + updatedStocks);
                    //stocks.clear();
                }else{
                    stocks = new LinkedList<>();
                }*/
                Log.d(TAG, "final list : " + updatedStocks);
                //stocks.addAll(updatedStocks);
                stockSet.addAll(updatedStocks);
                editor.putStringSet("Stocks", stockSet);
                editor.apply();
                arrayAdapter.clear();
                arrayAdapter.addAll(stockSet);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if(queue != null){
            queue.cancelAll(TAG);
        }
    }

}