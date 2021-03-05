package com.example.slickkwear.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slickkwear.Interface.ItemClickListener;
import com.example.slickkwear.R;

public class HomeCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCategoryName;
    public ImageView txtCategoryImage;
    public ItemClickListener listener;

    public HomeCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        txtCategoryImage = (ImageView) itemView.findViewById(R.id.user_home_category_image);
        txtCategoryName = (TextView) itemView.findViewById(R.id.user_home_category_name);
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
