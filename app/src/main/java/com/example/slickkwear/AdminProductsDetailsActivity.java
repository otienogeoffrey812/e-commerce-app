package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.slickkwear.Model.Products;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminProductsDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ShapeableImageView productImage;
    private Button deleteProductButton, editProductButton;
    private MaterialTextView productName, productPrice, productQuantity,ProductAvailability, productDescription;
    private String productID;

    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products_details);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productID = getIntent().getStringExtra("ProductUniqueID");

        productName =(MaterialTextView) findViewById(R.id.product_details_name);
        productPrice =(MaterialTextView) findViewById(R.id.product_details_price);
        productQuantity =(MaterialTextView) findViewById(R.id.product_details_quantity);
        productDescription =(MaterialTextView) findViewById(R.id.product_details_description);
        deleteProductButton = (Button) findViewById(R.id.delete_product);
        editProductButton = (Button) findViewById(R.id.edit_product);
        productImage = (ShapeableImageView) findViewById(R.id.product_details_image);


        getProductDetails(productID);
    }

    private void getProductDetails(String productID) {
        productRef = FirebaseDatabase.getInstance().getReference("Products");

        productRef.child(productID).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                        {
                            Products products = snapshot.getValue(Products.class);
                            productName.setText(products.getProductName());
                            productPrice.setText(products.getProductPrice());
                            productDescription.setText(products.getProductDescription());
                            productQuantity.setText(products.getProductStatus());
                            Picasso.get().load(products.getProductImage()).into(productImage);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
}