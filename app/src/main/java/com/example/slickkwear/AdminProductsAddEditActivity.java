package com.example.slickkwear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AdminProductsAddEditActivity extends AppCompatActivity {

    private TextInputLayout addProductName, addProductPrice, addProductDescription;
    private MaterialButton addProductButton;
    private ShapeableImageView addProductImage;

    private static final int GalleryPick = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products_add_edit);

        addProductImage = (ShapeableImageView) findViewById(R.id.admin_add_product_image);
        addProductName = (TextInputLayout) findViewById(R.id.admin_add_product_name);
        addProductPrice = (TextInputLayout) findViewById(R.id.admin_add_product_price);
        addProductDescription = (TextInputLayout) findViewById(R.id.admin_add_product_description);

        addProductImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openGallery();
                    }
                }
        );
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("/image*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
}