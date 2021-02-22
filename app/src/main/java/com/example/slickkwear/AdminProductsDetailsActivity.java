package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slickkwear.Model.Products;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class AdminProductsDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ShapeableImageView productImage;
    private Button deleteProductButton, editProductButton;
    private MaterialTextView productName, productPrice, productQuantity,ProductAvailability, productDescription;
    private String productID;

    private FirebaseFirestore productRef;
    private DocumentReference productItem;

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
        productRef = FirebaseFirestore.getInstance();
        productItem = productRef.collection("Products").document(productID);

        productItem.get()
                .addOnSuccessListener(
                        new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if(documentSnapshot.exists())
                                {
//                                    Products products = documentSnapshot.getValue(Products.class);
                                    productName.setText(documentSnapshot.getString("ProductName"));
                                    productPrice.setText(documentSnapshot.getString("ProductPrice"));
                                    productDescription.setText(documentSnapshot.getString("ProductDescription"));
                                    productQuantity.setText(documentSnapshot.getString("ProductStatus"));
                                    Picasso.get().load(documentSnapshot.getString("ProductImage")).into(productImage);
                                }
                                else {
                                    Toast.makeText(AdminProductsDetailsActivity.this, "Product Doesn't exist!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminProductsDetailsActivity.this, "Loading product details unsuccessful !", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}