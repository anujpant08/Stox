package com.example.stox;

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
            String changedDayOpenValue = null;
            String changedDayCloseValue = null;
            String changedDayHighValue = null;
            String changedDayLowValue = null;
            String volumeValue = null;
            switch (position){
                case 0:
                    changedDayOpenValue = stock.getDayData1().getOpen().toString();
                    changedDayCloseValue = stock.getDayData1().getOpen().toString();
                    changedDayHighValue = stock.getDayData1().getOpen().toString();
                    changedDayLowValue = stock.getDayData1().getOpen().toString();
                    volumeValue = stock.getDayData1().getVolume().toString();
                    break;
                case 1:
                    changedDayOpenValue = stock.getDayData2().getOpen().toString();
                    changedDayCloseValue = stock.getDayData2().getOpen().toString();
                    changedDayHighValue = stock.getDayData2().getOpen().toString();
                    changedDayLowValue = stock.getDayData2().getOpen().toString();
                    volumeValue = stock.getDayData2().getVolume().toString();
                    break;
                case 2:
                    changedDayOpenValue = stock.getDayData3().getOpen().toString();
                    changedDayCloseValue = stock.getDayData3().getOpen().toString();
                    changedDayHighValue = stock.getDayData3().getOpen().toString();
                    changedDayLowValue = stock.getDayData3().getOpen().toString();
                    volumeValue = stock.getDayData3().getVolume().toString();
                    break;
                case 3:
                    changedDayOpenValue = stock.getDayData4().getOpen().toString();
                    changedDayCloseValue = stock.getDayData4().getOpen().toString();
                    changedDayHighValue = stock.getDayData4().getOpen().toString();
                    changedDayLowValue = stock.getDayData4().getOpen().toString();
                    volumeValue = stock.getDayData4().getVolume().toString();
                    break;
                case 4:
                    changedDayOpenValue = stock.getDayData5().getOpen().toString();
                    changedDayCloseValue = stock.getDayData5().getOpen().toString();
                    changedDayHighValue = stock.getDayData5().getOpen().toString();
                    changedDayLowValue = stock.getDayData5().getOpen().toString();
                    volumeValue = stock.getDayData5().getVolume().toString();
                    break;
            }
            viewHolder.dayOpenValue.setText(changedDayOpenValue);
            viewHolder.dayCloseValue.setText(changedDayCloseValue);
            viewHolder.dayHighValue.setText(changedDayHighValue);
            viewHolder.dayLowValue.setText(changedDayLowValue);
            viewHolder.volumeValue.setText(volumeValue);
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
    static class ViewHolder{
        TextView dayOpenValue;
        TextView dayCloseValue;
        TextView dayHighValue;
        TextView dayLowValue;
        TextView volumeValue;
    }
}
