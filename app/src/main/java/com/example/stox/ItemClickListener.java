package com.example.stox;

import android.view.View;

public interface ItemClickListener {

    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}
