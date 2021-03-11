package com.example.slickkwear.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slickkwear.Interface.ItemClickListener;
import com.example.slickkwear.R;

public class HomeProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductsName, txtProductsPrice;
    public ImageView txtProductsImage;
    public ItemClickListener listener;

    public HomeProductsViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductsImage = (ImageView) itemView.findViewById(R.id.user_home_products_image);
        txtProductsName = (TextView) itemView.findViewById(R.id.user_home_products_name);
        txtProductsPrice = (TextView) itemView.findViewById(R.id.user_home_products_price);
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
