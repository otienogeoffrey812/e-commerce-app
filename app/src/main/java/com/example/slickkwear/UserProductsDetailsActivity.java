package com.example.slickkwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class UserProductsDetailsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseFirestore productRef;
    private DocumentReference productItem;

    private ShapeableImageView productImage;

    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_products_details);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_icon);

        productID = getIntent().getStringExtra("ProductUniqueID");

        productImage = (ShapeableImageView) findViewById(R.id.user_product_details_image);

        productRef = FirebaseFirestore.getInstance();

        getProductDetails(productID);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Returns to the previous page
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getProductDetails(String productID) {

        productItem = productRef.collection("Products").document(productID);
        productItem.get()
                .addOnSuccessListener(
                        new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if(documentSnapshot.exists())
                                {
                                    Picasso.get().load(documentSnapshot.getString("ProductImage")).into(productImage);

                                }
                                else
                                {
                                    Toast.makeText(UserProductsDetailsActivity.this, "Product doesn't exists!!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );
    }
}