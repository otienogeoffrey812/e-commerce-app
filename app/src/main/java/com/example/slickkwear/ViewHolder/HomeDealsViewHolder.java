package com.example.slickkwear.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slickkwear.Interface.ItemClickListener;
import com.example.slickkwear.R;

public class HomeDealsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtDealName, txtDealPriceInitial,txtDealPriceDiscounted ;
    public ImageView txtDealImage;
    public ItemClickListener listener;

    public HomeDealsViewHolder(@NonNull View itemView) {
        super(itemView);

        txtDealImage = (ImageView) itemView.findViewById(R.id.user_home_deals_image);
        txtDealName = (TextView) itemView.findViewById(R.id.user_home_deals_name);
        txtDealPriceInitial = (TextView) itemView.findViewById(R.id.user_home_deals_price_initial);
        txtDealPriceDiscounted = (TextView) itemView.findViewById(R.id.user_home_deals_price_discounted);
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
