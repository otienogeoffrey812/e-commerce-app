package com.example.slickkwear.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slickkwear.Interface.ItemClickListener;
import com.example.slickkwear.R;

public class UserCategoryProductsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCategoryProductsName, txtCategoryProductsPrice;
    public ImageView txtCategoryProductsImage;
    public ItemClickListener listener;

    public UserCategoryProductsViewHolder(@NonNull View itemView) {
        super(itemView);

        txtCategoryProductsImage = (ImageView) itemView.findViewById(R.id.user_category_products_image);
        txtCategoryProductsName = (TextView) itemView.findViewById(R.id.user_category_products_name);
        txtCategoryProductsPrice = (TextView) itemView.findViewById(R.id.user_category_products_price);
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
