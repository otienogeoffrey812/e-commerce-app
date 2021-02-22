package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.smarteist.autoimageslider.SliderView;

public class MainActivity extends AppCompatActivity {

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
         bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserHomeFragment())
                .commit();


//        homeSliderBanner();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId())
            {
                case R.id.bottom_nav_home:
                    selectedFragment = new UserHomeFragment();
                    break;
                case R.id.bottom_nav_category:
                    selectedFragment = new UserCategoryFragment();
                    break;
                case R.id.bottom_nav_cart:
                    selectedFragment = new UserCartFragment();
                    break;
                case R.id.bottom_nav_account:
                    selectedFragment = new UserAccountFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }
    };

//    private void homeSliderBanner() {
//
//        sliderView = findViewById(R.id.imageSlider);
//
//        adapter = new SliderHomeAdapter(this);
//
//        query.get().addOnSuccessListener(
//                new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                        for (QueryDocumentSnapshot result : queryDocumentSnapshots)
//                        {
//                            SliderItem sliderItem = new SliderItem();
//                            sliderItem.setDescription(result.getString("ProductName"));
//                            sliderItem.setImageUrl(result.getString("ProductImage"));
//                            adapter.addItem(sliderItem);
//                        }
//
//                    }
//                }
//        );
//
//        sliderView.setSliderAdapter(adapter);
//
//        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
//        sliderView.setIndicatorSelectedColor(Color.WHITE);
////        sliderView.setIndicatorUnselectedColor(R.attr.colorPrimary);
//        sliderView.setScrollTimeInSec(3);
//        sliderView.setAutoCycle(true);
//        sliderView.startAutoCycle();
//    }
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        if(item.getItemId() == R.id.bottom_nav_home)
//        {
//            Toast.makeText(this, "Home Clicked", Toast.LENGTH_SHORT).show();
//        }
//        else if (item.getItemId() == R.id.bottom_nav_category)
//        {
//            Toast.makeText(this, "Category Clicked", Toast.LENGTH_SHORT).show();
//        }
//        else if (item.getItemId() == R.id.bottom_nav_cart)
//        {
//            Toast.makeText(this, "Cart Clicked", Toast.LENGTH_SHORT).show();
//        }
//        else if (item.getItemId() == R.id.bottom_nav_account)
//        {
//            Toast.makeText(this, "Account Clicked", Toast.LENGTH_SHORT).show();
//        }
//        return true;
//    }
}