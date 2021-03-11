package com.example.slickkwear.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slickkwear.Interface.ItemClickListener;
import com.example.slickkwear.R;

public class HomeTrendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtTrendingName, txtTrendingPrice;
    public ImageView txtTrendingImage;
    public ItemClickListener listener;

    public HomeTrendingViewHolder(@NonNull View itemView) {
        super(itemView);

        txtTrendingImage = (ImageView) itemView.findViewById(R.id.user_home_trending_image);
        txtTrendingName = (TextView) itemView.findViewById(R.id.user_home_trending_name);
        txtTrendingPrice = (TextView) itemView.findViewById(R.id.user_home_trending_price);
    }
    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
