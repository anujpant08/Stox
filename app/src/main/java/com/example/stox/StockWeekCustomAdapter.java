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

public class StockWeekCustomAdapter extends ArrayAdapter<String> {
    private static final String TAG = "StockWeekCustomAdapter";
    private final int resourceID;
    private final Context context;

    public StockWeekCustomAdapter(@NonNull Context context, int resourceID, @NonNull List<String> stocksList) {
        super(context, resourceID, stocksList);
        this.resourceID = resourceID;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        StockWeekCustomAdapter.ViewHolder viewHolder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.dateText = (TextView)row.findViewById(R.id.week_date_text);
            viewHolder.weekOpenValue = (TextView)row.findViewById(R.id.week_open_value);
            viewHolder.weekCloseValue = (TextView)row.findViewById(R.id.week_close_value);
            viewHolder.weekHighValue = (TextView)row.findViewById(R.id.week_high_value);
            viewHolder.weekLowValue = (TextView)row.findViewById(R.id.week_low_value);
            viewHolder.volumeValue = (TextView)row.findViewById(R.id.week_volume_value);
            row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) row.getTag();
        }
        Stock stock = WeeklyTimeFragment.getStock();
        if(stock != null && stock.getWeekData1() != null && stock.getWeekData2() != null
                && stock.getWeekData3() != null && stock.getWeekData4() != null && stock.getWeekData5() != null ){
            Log.d(TAG, "Got stock: " + stock);
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM");
            DecimalFormat decimalFormat = new DecimalFormat("##,###.##");
            String changedWeekOpenValue = null;
            String changedWeekCloseValue = null;
            String changedWeekHighValue = null;
            String changedWeekLowValue = null;
            String volumeValue = null;
            String dateFormatted = null;
            switch (position){
                case 0:
                    changedWeekOpenValue = decimalFormat.format(stock.getWeekData1().getOpen());
                    changedWeekCloseValue = decimalFormat.format(stock.getWeekData1().getClose());
                    changedWeekHighValue = decimalFormat.format(stock.getWeekData1().getWeekHigh());
                    changedWeekLowValue = decimalFormat.format(stock.getWeekData1().getWeekLow());
                    volumeValue = getFormattedVolume(stock.getWeekData1().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getWeekData1().getDate());
                    break;
                case 1:
                    changedWeekOpenValue = decimalFormat.format(stock.getWeekData2().getOpen());
                    changedWeekCloseValue = decimalFormat.format(stock.getWeekData2().getClose());
                    changedWeekHighValue = decimalFormat.format(stock.getWeekData2().getWeekHigh());
                    changedWeekLowValue = decimalFormat.format(stock.getWeekData2().getWeekLow());
                    volumeValue = getFormattedVolume(stock.getWeekData2().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getWeekData2().getDate());
                    break;
                case 2:
                    changedWeekOpenValue = decimalFormat.format(stock.getWeekData3().getOpen());
                    changedWeekCloseValue = decimalFormat.format(stock.getWeekData3().getClose());
                    changedWeekHighValue = decimalFormat.format(stock.getWeekData1().getWeekHigh());
                    changedWeekLowValue = decimalFormat.format(stock.getWeekData3().getWeekLow());
                    volumeValue = getFormattedVolume(stock.getWeekData3().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getWeekData3().getDate());
                    break;
                case 3:
                    changedWeekOpenValue = decimalFormat.format(stock.getWeekData4().getOpen());
                    changedWeekCloseValue = decimalFormat.format(stock.getWeekData4().getClose());
                    changedWeekHighValue = decimalFormat.format(stock.getWeekData4().getWeekHigh());
                    changedWeekLowValue = decimalFormat.format(stock.getWeekData4().getWeekLow());
                    volumeValue = getFormattedVolume(stock.getWeekData4().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getWeekData4().getDate());
                    break;
                case 4:
                    changedWeekOpenValue = decimalFormat.format(stock.getWeekData5().getOpen());
                    changedWeekCloseValue = decimalFormat.format(stock.getWeekData5().getClose());
                    changedWeekHighValue = decimalFormat.format(stock.getWeekData5().getWeekHigh());
                    changedWeekLowValue = decimalFormat.format(stock.getWeekData5().getWeekLow());
                    volumeValue = getFormattedVolume(stock.getWeekData5().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getWeekData5().getDate());
                    break;
            }
            viewHolder.weekOpenValue.setText(changedWeekOpenValue);
            viewHolder.weekCloseValue.setText(changedWeekCloseValue);
            viewHolder.weekHighValue.setText(changedWeekHighValue);
            viewHolder.weekLowValue.setText(changedWeekLowValue);
            viewHolder.volumeValue.setText(volumeValue);
            viewHolder.dateText.setText(dateFormatted);
            assert changedWeekOpenValue != null;
            if(stock.getChangeValue().startsWith("-")){
                viewHolder.weekOpenValue.setTextColor(Color.parseColor("#ED7373"));
                viewHolder.weekCloseValue.setTextColor(Color.parseColor("#ED7373"));
                viewHolder.weekHighValue.setTextColor(Color.parseColor("#ED7373"));
                viewHolder.weekLowValue.setTextColor(Color.parseColor("#ED7373"));
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
        TextView weekOpenValue;
        TextView weekCloseValue;
        TextView weekHighValue;
        TextView weekLowValue;
        TextView volumeValue;
    }
}
