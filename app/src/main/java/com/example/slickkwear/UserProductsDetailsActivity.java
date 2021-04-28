package com.example.slickkwear;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class UserProductsDetailsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseFirestore productRef;
    private DocumentReference productItem;
    private MaterialTextView user_product_details_name, user_product_details_price, user_product_details_description;

    private ShapeableImageView productImage;

    private String productID;
    private LinearLayout user_product_details_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_products_details);

        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);

        productID = getIntent().getStringExtra("ProductUniqueID");

        productImage = (ShapeableImageView) findViewById(R.id.user_product_details_image);
        user_product_details_name = (MaterialTextView) findViewById(R.id.user_product_details_name);
        user_product_details_price = (MaterialTextView) findViewById(R.id.user_product_details_price);
        user_product_details_description = (MaterialTextView) findViewById(R.id.user_product_details_description);

        user_product_details_size = (LinearLayout) findViewById(R.id.user_product_details_size);


        productSize();

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
                                    user_product_details_name.setText(documentSnapshot.getString("ProductName"));
                                    user_product_details_price.setText("Ksh " + (String.valueOf(documentSnapshot.getLong("ProductPrice"))));
                                    user_product_details_description.setText(documentSnapshot.getString("ProductDescription"));

                                }
                                else
                                {
                                    Toast.makeText(UserProductsDetailsActivity.this, "Product doesn't exists!!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }
                );
    }


    private void productSize() {

        for (int i =36; i <= 42; i++){
//            String t_id = "val"+i;
            TextView valueTV  = new TextView(getApplicationContext());
//            valueTV.setText("36"+i);
            valueTV.setText(Html.fromHtml("<strike><font color=\'#757575\'>" + i + "</font></strike>"));
            valueTV.setId(Integer.parseInt("102"+i));
            valueTV.setPadding(15, 15, 15, 15);
            valueTV.setBackgroundResource(R.drawable.user_product_details_size_bg);
            valueTV.setGravity(Gravity.CENTER);
//            valueTV.setBackgroundColor(Color.GRAY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5,5,5,5);
            valueTV.setLayoutParams(params);

            user_product_details_size.addView(valueTV);
        }


    }


}