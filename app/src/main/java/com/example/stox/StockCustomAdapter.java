package com.example.stox;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StockCustomAdapter extends RecyclerView.Adapter<StockCustomAdapter.StockViewHolder> {
    private static final String TAG = "StockCustomAdapter";
    private final List<Stock> stocksList;
    public StockViewHolder stockViewHolder;
    public boolean isChecked = false;
    public static ItemClickListener itemClickListener;
    public List<Stock> checkedItems = new ArrayList<>();
    public int lastPosition = -1;
    private Context context;
    private StocksFragment stocksFragment;

    public StockCustomAdapter(StocksFragment stocksFragment, @NonNull List<Stock> stocksList) {
        this.stocksList = stocksList;
        this.stocksFragment = stocksFragment;
    }

    /*@NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resourceID, parent, false);
            stockViewHolder.stockSymbol = (TextView)row.findViewById(R.id.stock_text);
            stockViewHolder.stockName = (TextView)row.findViewById(R.id.stock_detail_text);
            stockViewHolder.lastTradePrice = (TextView)row.findViewById(R.id.last_trade_price);
            stockViewHolder.changeValue = (TextView)row.findViewById(R.id.change_value);
            stockViewHolder.checkBox = (MaterialCheckBox)row.findViewById(R.id.checkbox);
            row.setTag(stockViewHolder);
        }else{
            stockViewHolder = (StockViewHolder) row.getTag();
        }


        return row;
    }*/

    public void setChecked(boolean checked) {
        isChecked = checked;
        if (!checked) {
            checkedItems.clear();
        }
    }

    public List<Stock> getCheckedItems() {
        return checkedItems;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        StockCustomAdapter.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_stock_text_view, parent, false);
        return new StockViewHolder(view, itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder stockViewHolder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("##,###.##");
        Stock stock = this.stocksList.get(position);
        if (isChecked) {
            stockViewHolder.checkBox.setVisibility(View.VISIBLE);
            stockViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (compoundButton.isChecked()) {
                        checkedItems.add(stock);
                        stocksFragment.updateFABIcon(false);
                        Log.e(TAG, "Checked stock :" + stock.getStockSymbol() + " final list: " + checkedItems.size());
                    } else {
                        checkedItems.remove(stock);
                        if(checkedItems.isEmpty()){
                            stocksFragment.updateFABIcon(true);
                        }
                        Log.e(TAG, "Un-checked stock :" + stock.getStockSymbol() + " final list: " + checkedItems.size());
                    }
                }
            });
        } else {
            stockViewHolder.checkBox.setVisibility(View.GONE);
        }
        stockViewHolder.stockSymbol.setText(stock.getStockSymbol());
        stockViewHolder.stockName.setText(stock.getStockName());
        stockViewHolder.lastTradePrice.setText(decimalFormat.format(stock.getLastTradePrice()));
        String changedValue = decimalFormat.format(stock.getChangeValue());
        stockViewHolder.changeValue.setText(changedValue);
        if (stock.getChangeValue() < 0.0) {
            stockViewHolder.changeValue.setTextColor(Color.parseColor("#ED7373"));
            stockViewHolder.lastTradePrice.setTextColor(Color.parseColor("#ED7373"));
        } else {
            stockViewHolder.changeValue.setText("+" + changedValue);
        }
        if (position > lastPosition) {
            setFadeAnimation(stockViewHolder.itemView);
            lastPosition = position;
        }
    }

    private void setFadeAnimation(View view) {
        Animation anim = new TranslateAnimation(0f, 0f, 100f, 0f);
        anim.setFillAfter(true);
        anim.setDuration(600);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return this.stocksList.size();
    }

    public static class StockViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView stockSymbol;
        TextView stockName;
        TextView lastTradePrice;
        TextView changeValue;
        MaterialCheckBox checkBox;

        public StockViewHolder(@NonNull View itemView, ItemClickListener itemClickListener) {
            super(itemView);
            stockSymbol = (TextView) itemView.findViewById(R.id.stock_text);
            stockName = (TextView) itemView.findViewById(R.id.stock_detail_text);
            lastTradePrice = (TextView) itemView.findViewById(R.id.last_trade_price);
            changeValue = (TextView) itemView.findViewById(R.id.change_value);
            checkBox = (MaterialCheckBox) itemView.findViewById(R.id.checkbox);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onClick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (itemClickListener != null) {
                Log.e(TAG, "Long click at pos ");
                itemClickListener.onLongClick(view, getAdapterPosition());
            }
            return true;
        }
    }
}
