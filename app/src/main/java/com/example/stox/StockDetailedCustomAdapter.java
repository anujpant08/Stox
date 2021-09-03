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

public class StockDetailedCustomAdapter extends ArrayAdapter<String> {
    private static final String TAG = "StockDetailedCusAdapter";
    private final int resourceID;
    private final Context context;

    public StockDetailedCustomAdapter(@NonNull Context context, int resourceID, @NonNull List<String> stocksList) {
        super(context, resourceID, stocksList);
        this.resourceID = resourceID;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;
        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.dateText = (TextView)row.findViewById(R.id.date_text);
            viewHolder.dayOpenValue = (TextView)row.findViewById(R.id.day_open_value);
            viewHolder.dayCloseValue = (TextView)row.findViewById(R.id.day_close_value);
            viewHolder.dayHighValue = (TextView)row.findViewById(R.id.day_high_value);
            viewHolder.dayLowValue = (TextView)row.findViewById(R.id.day_low_value);
            viewHolder.volumeValue = (TextView)row.findViewById(R.id.volume_value);
            row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) row.getTag();
        }
        Stock stock = DailyTimeFragment.getStock();
        if(stock != null && stock.getDayData1() != null && stock.getDayData2() != null
                && stock.getDayData3() != null && stock.getDayData4() != null && stock.getDayData5() != null ){
            Log.d(TAG, "Got stock: " + stock);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM");
            DecimalFormat decimalFormat = new DecimalFormat("##,###.##");
            String changedDayOpenValue = null;
            String changedDayCloseValue = null;
            String changedDayHighValue = null;
            String changedDayLowValue = null;
            String volumeValue = null;
            String dateFormatted = null;
            switch (position){
                case 0:
                    changedDayOpenValue = decimalFormat.format(stock.getDayData1().getOpen());
                    changedDayCloseValue = decimalFormat.format(stock.getDayData1().getClose());
                    changedDayHighValue = decimalFormat.format(stock.getDayData1().getDayHigh());
                    changedDayLowValue = decimalFormat.format(stock.getDayData1().getDayLow());
                    volumeValue = getFormattedVolume(stock.getDayData1().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getDayData1().getDate());
                    break;
                case 1:
                    changedDayOpenValue = decimalFormat.format(stock.getDayData2().getOpen());
                    changedDayCloseValue = decimalFormat.format(stock.getDayData2().getClose());
                    changedDayHighValue = decimalFormat.format(stock.getDayData2().getDayHigh());
                    changedDayLowValue = decimalFormat.format(stock.getDayData2().getDayLow());
                    volumeValue = getFormattedVolume(stock.getDayData2().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getDayData2().getDate());
                    break;
                case 2:
                    changedDayOpenValue = decimalFormat.format(stock.getDayData3().getOpen());
                    changedDayCloseValue = decimalFormat.format(stock.getDayData3().getClose());
                    changedDayHighValue = decimalFormat.format(stock.getDayData3().getDayHigh());
                    changedDayLowValue = decimalFormat.format(stock.getDayData3().getDayLow());
                    volumeValue = getFormattedVolume(stock.getDayData3().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getDayData3().getDate());
                    break;
                case 3:
                    changedDayOpenValue = decimalFormat.format(stock.getDayData4().getOpen());
                    changedDayCloseValue = decimalFormat.format(stock.getDayData4().getClose());
                    changedDayHighValue = decimalFormat.format(stock.getDayData4().getDayHigh());
                    changedDayLowValue = decimalFormat.format(stock.getDayData4().getDayLow());
                    volumeValue = getFormattedVolume(stock.getDayData4().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getDayData4().getDate());
                    break;
                case 4:
                    changedDayOpenValue = decimalFormat.format(stock.getDayData5().getOpen());
                    changedDayCloseValue = decimalFormat.format(stock.getDayData5().getClose());
                    changedDayHighValue = decimalFormat.format(stock.getDayData5().getDayHigh());
                    changedDayLowValue = decimalFormat.format(stock.getDayData5().getDayLow());
                    volumeValue = getFormattedVolume(stock.getDayData5().getVolume());
                    dateFormatted = simpleDateFormat.format(stock.getDayData5().getDate());
                    break;
            }
            viewHolder.dayOpenValue.setText(changedDayOpenValue);
            viewHolder.dayCloseValue.setText(changedDayCloseValue);
            viewHolder.dayHighValue.setText(changedDayHighValue);
            viewHolder.dayLowValue.setText(changedDayLowValue);
            viewHolder.volumeValue.setText(volumeValue);
            viewHolder.dateText.setText(dateFormatted);
            assert changedDayOpenValue != null;
            if(stock.getChangeValue().startsWith("-")){
                viewHolder.dayOpenValue.setTextColor(Color.parseColor("#ED7373"));
                viewHolder.dayCloseValue.setTextColor(Color.parseColor("#ED7373"));
                viewHolder.dayHighValue.setTextColor(Color.parseColor("#ED7373"));
                viewHolder.dayLowValue.setTextColor(Color.parseColor("#ED7373"));
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
        TextView dayOpenValue;
        TextView dayCloseValue;
        TextView dayHighValue;
        TextView dayLowValue;
        TextView volumeValue;
    }
}
