package com.example.stox;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.List;

public class StockCustomAdapter extends ArrayAdapter<Stock> {
    private final List<Stock> stocksList;
    private final Context context;
    private final int resourceID;
    public StockCustomAdapter(@NonNull Context context, int resourceID, @NonNull List<Stock> stocksList) {
        super(context, resourceID, stocksList);
        this.context = context;
        this.resourceID = resourceID;
        this.stocksList = stocksList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;
        DecimalFormat decimalFormat = new DecimalFormat("##,###.##");
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.stockSymbol = (TextView)row.findViewById(R.id.stock_text);
            viewHolder.stockName = (TextView)row.findViewById(R.id.stock_detail_text);
            viewHolder.lastTradePrice = (TextView)row.findViewById(R.id.last_trade_price);
            viewHolder.changeValue = (TextView)row.findViewById(R.id.change_value);
            row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) row.getTag();
        }
        Stock stock = stocksList.get(position);
        viewHolder.stockSymbol.setText(stock.getStockSymbol());
        viewHolder.stockName.setText(stock.getStockName());
        viewHolder.lastTradePrice.setText(decimalFormat.format(stock.getLastTradePrice()));
        String changedValue = decimalFormat.format(stock.getChangeValue());
        viewHolder.changeValue.setText(changedValue);
        if(changedValue.startsWith("-")){
            viewHolder.changeValue.setTextColor(Color.parseColor("#ED7373"));
            viewHolder.lastTradePrice.setTextColor(Color.parseColor("#ED7373"));
        }else{
            viewHolder.changeValue.setText("+" + changedValue);
        }
        return row;
    }

    static class ViewHolder{
        TextView stockSymbol;
        TextView stockName;
        TextView lastTradePrice;
        TextView changeValue;
    }
}
