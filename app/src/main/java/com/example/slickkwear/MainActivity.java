package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.slickkwear.Model.Products;
import com.example.slickkwear.Model.SliderItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SliderView sliderView;
    private SliderHomeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        homeSliderBanner();
    }

    private void homeSliderBanner() {


        sliderView = findViewById(R.id.imageSlider);

        adapter = new SliderHomeAdapter(this);

//        TextView test1 = findViewById(R.id.search_input);

        List<SliderItem> sliderItemList = new ArrayList<>();
        SliderItem sliderItem = new SliderItem();
        //dummy data
        for (int i = 0; i < 5; i++) {
            sliderItem.setDescription("Slider Item ");
            sliderItem.setImageUrl("https://firebasestorage.googleapis.com/v0/b/slickk-wear.appspot.com/o/Product%20Images%2Fimage%3A23897520210214082521jpg?alt=media&token=b08c3abd-4d7c-47f8-8018-656b2a7ce046");
//                        test1.setText();
            sliderItemList.add(sliderItem);
        }

        adapter.renewItems(sliderItemList);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
//        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
    }
}