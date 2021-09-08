package com.example.stox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class StockMonthCustomAdapter extends ArrayAdapter<String> {
    private static final String TAG = "StockMonthCustomAdapter";
    private final int resourceID;
    private final Context context;

    public StockMonthCustomAdapter(@NonNull Context context, int resourceID, @NonNull List<String> stocksList) {
        super(context, resourceID, stocksList);
        this.resourceID = resourceID;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        StockMonthCustomAdapter.ViewHolder viewHolder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.dateText = (TextView)row.findViewById(R.id.month_date_text);
            viewHolder.monthOpenValue = (TextView)row.findViewById(R.id.month_open_value);
            viewHolder.monthCloseValue = (TextView)row.findViewById(R.id.month_close_value);
            viewHolder.monthHighValue = (TextView)row.findViewById(R.id.month_high_value);
            viewHolder.monthLowValue = (TextView)row.findViewById(R.id.month_low_value);
            viewHolder.volumeValue = (TextView)row.findViewById(R.id.month_volume_value);
            row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) row.getTag();
        }
        Stock stock = MonthlyTimeFragment.getStock();
        if(stock != null && stock.getMonthData1() != null && stock.getMonthData2() != null
                && stock.getMonthData3() != null && stock.getMonthData4() != null && stock.getMonthData5() != null ){
            Log.d(TAG, "Got stock: " + stock);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM");
            DecimalFormat decimalFormat = new DecimalFormat("##,###.##");
            String changedMonthOpenValue = null;
            String changedMonthCloseValue = null;
            String changeMonthHighValue = null;
            String changedMonthLowValue = null;
            String volumeValue = null;
            String dateFormatted = null;
            switch (position){
                case 0:
                    changedMonthOpenValue = decimalFormat.format(stock.getMonthData1().getOpen());
                    changedMonthCloseValue = decimalFormat.format(stock.getMonthData1().getClose());
                    changeMonthHighValue = decimalFormat.format(stock.getMonthData1().getMonthHigh());
                    changedMonthLowValue = decimalFormat.format(stock.getMonthData1().getMonthLow());
                    volumeValue = getFormattedVolume(stock.getMonthData1().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getMonthData1().getDate());
                    break;
                case 1:
                    changedMonthOpenValue = decimalFormat.format(stock.getMonthData2().getOpen());
                    changedMonthCloseValue = decimalFormat.format(stock.getMonthData2().getClose());
                    changeMonthHighValue = decimalFormat.format(stock.getMonthData2().getMonthHigh());
                    changedMonthLowValue = decimalFormat.format(stock.getMonthData2().getMonthLow());
                    volumeValue = getFormattedVolume(stock.getMonthData2().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getMonthData2().getDate());
                    break;
                case 2:
                    changedMonthOpenValue = decimalFormat.format(stock.getMonthData3().getOpen());
                    changedMonthCloseValue = decimalFormat.format(stock.getMonthData3().getClose());
                    changeMonthHighValue = decimalFormat.format(stock.getMonthData3().getMonthHigh());
                    changedMonthLowValue = decimalFormat.format(stock.getMonthData3().getMonthLow());
                    volumeValue = getFormattedVolume(stock.getMonthData3().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getMonthData3().getDate());
                    break;
                case 3:
                    changedMonthOpenValue = decimalFormat.format(stock.getMonthData4().getOpen());
                    changedMonthCloseValue = decimalFormat.format(stock.getMonthData4().getClose());
                    changeMonthHighValue = decimalFormat.format(stock.getMonthData4().getMonthHigh());
                    changedMonthLowValue = decimalFormat.format(stock.getMonthData4().getMonthLow());
                    volumeValue = getFormattedVolume(stock.getMonthData4().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getMonthData4().getDate());
                    break;
                case 4:
                    changedMonthOpenValue = decimalFormat.format(stock.getMonthData5().getOpen());
                    changedMonthCloseValue = decimalFormat.format(stock.getMonthData5().getClose());
                    changeMonthHighValue = decimalFormat.format(stock.getMonthData5().getMonthHigh());
                    changedMonthLowValue = decimalFormat.format(stock.getMonthData5().getMonthLow());
                    volumeValue = getFormattedVolume(stock.getMonthData5().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getMonthData5().getDate());
                    break;
            }
            viewHolder.monthOpenValue.setText(changedMonthOpenValue);
            viewHolder.monthCloseValue.setText(changedMonthCloseValue);
            viewHolder.monthHighValue.setText(changeMonthHighValue);
            viewHolder.monthLowValue.setText(changedMonthLowValue);
            viewHolder.volumeValue.setText(volumeValue);
            viewHolder.dateText.setText(dateFormatted);
            assert changedMonthOpenValue != null;
            if(stock.getChangeValue() < 0.0){
                viewHolder.monthOpenValue.setTextColor(Color.parseColor("#ED7373"));
                viewHolder.monthCloseValue.setTextColor(Color.parseColor("#ED7373"));
                viewHolder.monthHighValue.setTextColor(Color.parseColor("#ED7373"));
                viewHolder.monthLowValue.setTextColor(Color.parseColor("#ED7373"));
            }
        }
        return row;
    }

    @SuppressLint("DefaultLocale")
    private String getFormattedVolume(Double volume) {
        if(volume >= 1000000){
            return String.format("%.1fm", volume/ 1000000.0);
        }else if(volume >=1000){
            return String.format("%.1fk", volume/ 1000.0);
        }
        return volume.toString();
    }

    static class ViewHolder{
        TextView dateText;
        TextView monthOpenValue;
        TextView monthCloseValue;
        TextView monthHighValue;
        TextView monthLowValue;
        TextView volumeValue;
    }
}
