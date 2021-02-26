package com.example.slickkwear;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.slickkwear.Model.SliderItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class UserHomeFragment extends Fragment {


    private BottomNavigationView bottomNavigationView;
    SliderView sliderView;
    private SliderHomeAdapter adapter;

    private FirebaseFirestore productRef;
    private Query query;


    public UserHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        sliderView = view.findViewById(R.id.imageSlider);

        productRef = FirebaseFirestore.getInstance();
        query = productRef.collection("Products");

        SearchView searchView = view.findViewById(R.id.search);
        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getContext(), SearchableActivity.class);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;

            }
        };

        homeSliderBanner();

        return  view;
    }

    private void homeSliderBanner() {

        adapter = new SliderHomeAdapter(requireContext());

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
}