package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slickkwear.Model.Products;
import com.example.slickkwear.Model.SliderItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    SliderView sliderView;
    private SliderHomeAdapter adapter;

    private FirebaseFirestore productRef;
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productRef = FirebaseFirestore.getInstance();
        query = productRef.collection("Products");

         bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
         bottomNavigationView.setOnNavigationItemSelectedListener(this);


        homeSliderBanner();
    }

    private void homeSliderBanner() {

        sliderView = findViewById(R.id.imageSlider);

        adapter = new SliderHomeAdapter(this);

        query.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot result : queryDocumentSnapshots)
                        {
                            SliderItem sliderItem = new SliderItem();
                            sliderItem.setDescription(result.getString("ProductName"));
                            sliderItem.setImageUrl(result.getString("ProductImage"));
                            adapter.addItem(sliderItem);
                        }

                    }
                }
        );

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
//        sliderView.setIndicatorUnselectedColor(R.attr.colorPrimary);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.bottom_nav_home)
        {
            Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show();
        }
        else if (item.getItemId() == R.id.bottom_nav_category)
        {
            Intent intent = new Intent(getApplicationContext(), AdminOrdersActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.bottom_nav_cart)
        {
            Intent intent = new Intent(getApplicationContext(), AdminCategoriesActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.bottom_nav_account)
        {
            Intent intent = new Intent(getApplicationContext(), AdminCategoriesActivity.class);
            startActivity(intent);
        }
        return false;
    }
}