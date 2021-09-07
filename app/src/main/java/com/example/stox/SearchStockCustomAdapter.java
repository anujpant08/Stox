package com.example.stox;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SearchStockCustomAdapter extends ArrayAdapter<Stock> {
    private final Context context;
    private final int resourceID;
    private final List<Stock> stocksList;
    public SearchStockCustomAdapter(@NonNull Context context, int resourceID, @NonNull List<Stock> stocksList) {
        super(context, resourceID, stocksList);
        this.context = context;
        this.resourceID = resourceID;
        this.stocksList = stocksList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        SearchStockCustomAdapter.ViewHolder viewHolder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceID, parent, false);
            viewHolder = new SearchStockCustomAdapter.ViewHolder();
            viewHolder.stockSymbol = (TextView)row.findViewById(R.id.stock_symbol_search_view);
            viewHolder.stockName = (TextView)row.findViewById(R.id.stock_detail_search_view);
            row.setTag(viewHolder);
        }else{
            viewHolder = (SearchStockCustomAdapter.ViewHolder) row.getTag();
        }
        Stock stock = stocksList.get(position);
        viewHolder.stockSymbol.setText(stock.getStockSymbol());
        viewHolder.stockName.setText(stock.getStockName());
        return row;
    }

    static class ViewHolder{
        TextView stockSymbol;
        TextView stockName;
    }
}
