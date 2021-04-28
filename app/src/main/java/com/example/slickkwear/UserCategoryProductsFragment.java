package com.example.slickkwear;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.slickkwear.Model.Products;
import com.example.slickkwear.ViewHolder.HomeProductsViewHolder;
import com.example.slickkwear.ViewHolder.UserCategoryProductsViewHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class UserCategoryProductsFragment extends Fragment {

    private View view;
    private FirebaseFirestore productRef;
    private Query queryProducts;
    private RecyclerView recyclerViewProducts;
    private RecyclerView.LayoutManager layoutManagerProducts;
    private String categoryUniqueID, min_filter, max_filter;
    private TextView backToPrevBtn, category_products_sort_btn, category_products_filter_btn, category_products_change_layout_btn;
    private TextView category_products_most_popular_sort,category_products_latest_sort,category_products_lowest_price_sort,category_products_best_rating_sort, category_products_highest_price_sort;
    private LinearLayout category_products_sort_layout, translucent_layout_sort, translucent_layout_filter;
    private RelativeLayout category_products_filter_layout, category_products_null_products_layout;
    private TextInputLayout category_products_filter_min, category_products_filter_max;
    private Button category_products_filter_reset_btn, category_products_filter_apply_btn;
    private int count;
    private LottieAnimationView lottieAnimationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_category_products, container, false);

        lottieAnimationView = view.findViewById(R.id.category_products_empty_animation);

//        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.fragment_container2);
//        frameLayout.setVisibility(View.VISIBLE);
        BottomNavigationView bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setVisibility(View.GONE);

        backToPrevBtn = (TextView) view.findViewById(R.id.search_view_back_to_prev_btn);
        backToPrevBtn.setVisibility(View.VISIBLE);

        category_products_sort_layout = (LinearLayout) view.findViewById(R.id.category_products_sort_layout);
        category_products_sort_btn = (TextView) view.findViewById(R.id.category_products_sort_btn);

        category_products_most_popular_sort = (TextView) view.findViewById(R.id.category_products_most_popular_sort);
        category_products_latest_sort = (TextView) view.findViewById(R.id.category_products_latest_sort);
        category_products_best_rating_sort = (TextView) view.findViewById(R.id.category_products_best_rating_sort);
        category_products_lowest_price_sort = (TextView) view.findViewById(R.id.category_products_lowest_price_sort);
        category_products_highest_price_sort = (TextView) view.findViewById(R.id.category_products_highest_price_sort);

        translucent_layout_sort = (LinearLayout) view.findViewById(R.id.translucent_layout_sort);

        category_products_filter_btn = (TextView) view.findViewById(R.id.category_products_filter_btn);
        category_products_filter_layout = (RelativeLayout) view.findViewById(R.id.category_products_filter_layout);
        translucent_layout_filter = (LinearLayout) view.findViewById(R.id.translucent_layout_filter);

        category_products_filter_min = (TextInputLayout) view.findViewById(R.id.category_products_filter_min);
        category_products_filter_max = (TextInputLayout) view.findViewById(R.id.category_products_filter_max);

        category_products_filter_reset_btn = (Button) view.findViewById(R.id.category_products_filter_reset_btn);
        category_products_filter_apply_btn = (Button) view.findViewById(R.id.category_products_filter_apply_btn);


        backToPrevBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // RETURN TO PREVIOUS FRAGMENT

                        getActivity().onBackPressed();

                    }
                }
        );
        category_products_null_products_layout = (RelativeLayout) view.findViewById(R.id.category_products_null_products_layout);

//        category_products_change_layout_btn = (TextView) view.findViewById(R.id.category_products_change_layout_btn);

//        changeProductsLayout();

        sortProducts();

        filterProducts();

        categoryUniqueID = getArguments().getString("categoryUniqueID");


        productRef = FirebaseFirestore.getInstance();
        queryProducts = productRef.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID);

        recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview);
        recyclerViewProducts.setHasFixedSize(false);
        recyclerViewProducts.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(layoutManagerProducts);

        productsRView();

        return view;
    }

    //CHANGE PRODUCT LAYOUT BTN GRID AND LINEAR
//    private void changeProductsLayout() {
//        count = 0;
//        category_products_change_layout_btn.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        queryProducts = productRef.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID);
//
////                        if(count == 0){
//                            recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview);
//                            recyclerViewProducts.setHasFixedSize(false);
//                            recyclerViewProducts.setNestedScrollingEnabled(false);
//                            layoutManagerProducts = new LinearLayoutManager(getContext());
//                            category_products_change_layout_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_list_24, 0);
//                            recyclerViewProducts.setLayoutManager(layoutManagerProducts);
////                            count ++;
////                        }
////                        else {
////                            recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview);
////                            recyclerViewProducts.setHasFixedSize(false);
////                            recyclerViewProducts.setNestedScrollingEnabled(false);
////                            layoutManagerProducts = new GridLayoutManager(getContext(), 2);
////                            category_products_change_layout_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_grid_24, 0);
////                            recyclerViewProducts.setLayoutManager(layoutManagerProducts);
////                            count--;
////                        }
//
//                        productsRView();
//
//                    }
//                }
//        );
//    }

    private void filterProducts() {

        Animation slide_in_right = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_in_right);

        Animation slide_out_right = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_out_right);

        // Start animation
        // SHOW FILTER LAYOUT
        count = 0;
        category_products_filter_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (count == 0) {
                            category_products_filter_layout.startAnimation(slide_out_right);
                            translucent_layout_filter.setVisibility(View.VISIBLE);
                            category_products_filter_layout.setVisibility(View.VISIBLE);
                            category_products_filter_layout.startAnimation(slide_in_right);
                            count += 1;
                        }
                        else
                        {
                            category_products_filter_layout.startAnimation(slide_out_right);
                            category_products_filter_layout.setVisibility(View.GONE);
                            count -= 1;
                        }
                    }
                }
        );

        //DISMISS FILTER LAYOUT
        translucent_layout_filter.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        category_products_filter_layout.startAnimation(slide_out_right);
                        category_products_filter_layout.setVisibility(View.GONE);
                        count -= 1;

                        translucent_layout_filter.setVisibility(View.GONE);

                    }
                }
        );

        category_products_filter_apply_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        min_filter = category_products_filter_min.getEditText().getText().toString();
                        max_filter = category_products_filter_max.getEditText().getText().toString();

                        if (min_filter.isEmpty())
                        {
                            category_products_filter_min.setError("Cannot be empty!");
                        }
                        else if (max_filter.isEmpty())
                        {
                            category_products_filter_max.setError("Cannot be empty!");
                        }
                        else if (Integer.parseInt(min_filter) < 0)
                        {
                            category_products_filter_min.setError("Min value is 0 !");
                        }
                        else if (Integer.parseInt(min_filter) >  Integer.parseInt(max_filter))
                        {
                            category_products_filter_max.setError("Must be greater than min. value !");
                        }
                        else{
                            category_products_filter_layout.startAnimation(slide_out_right);
                            category_products_filter_layout.setVisibility(View.GONE);
                            count -= 1;

                            translucent_layout_filter.setVisibility(View.GONE);

                            category_products_filter_min.setError(null);
                            category_products_filter_max.setError(null);

                            filterProductsMinMax();
                        }
                    }
                }
        );

        category_products_filter_reset_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        category_products_filter_max.getEditText().setText("");
                        category_products_filter_min.getEditText().setText("");
                        category_products_filter_min.setError(null);
                        category_products_filter_max.setError(null);

                        category_products_filter_layout.startAnimation(slide_out_right);
                        category_products_filter_layout.setVisibility(View.GONE);
                        count -= 1;

                        translucent_layout_filter.setVisibility(View.GONE);

                        sortMostPopular();

                    }
                }
        );

    }

    // FILTER PRODUCTS BASED ON PRICE
    private void filterProductsMinMax() {

        int new_min = Integer.parseInt(min_filter);
        int new_max = Integer.parseInt(max_filter);

        queryProducts = productRef.collection("Products").
                whereGreaterThanOrEqualTo("ProductPrice", new_min)
                .whereLessThanOrEqualTo("ProductPrice", new_max)
                .orderBy("ProductPrice", Query.Direction.ASCENDING);

        //Check if empty products result
        queryProducts.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty())
                        {
                            category_products_null_products_layout.setVisibility(View.VISIBLE);
                        }
                        else {
                            category_products_null_products_layout.setVisibility(View.GONE);
                        }
                    }
                }
        );


        recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview);
        recyclerViewProducts.setHasFixedSize(false);
        recyclerViewProducts.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(layoutManagerProducts);

        productsRView();

    }

    private void sortProducts() {

        Animation slide_down = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getContext(),
                R.anim.slide_up);

        category_products_sort_layout.startAnimation(slide_down);

//        translucent_layout_sort.setVisibility(View.GONE);

// Start animation
        // SHOW SORT LAYOUT
        count = 0;
        category_products_sort_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (count == 0) {
                            translucent_layout_sort.setVisibility(View.VISIBLE);
                            category_products_sort_layout.setVisibility(View.VISIBLE);
                            category_products_sort_layout.startAnimation(slide_up);
                            category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);
                            count += 1;
                        }
                        else
                        {
                            category_products_sort_layout.startAnimation(slide_down);
                            category_products_sort_layout.setVisibility(View.GONE);
                            category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                            count -= 1;
                        }
                    }
                }
        );

        //DISMISS SORT LAYOUT
        translucent_layout_sort.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        category_products_sort_layout.startAnimation(slide_down);
                        category_products_sort_layout.setVisibility(View.GONE);
                        category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                        count -= 1;

                        translucent_layout_sort.setVisibility(View.GONE);

                    }
                }
        );

        category_products_most_popular_sort.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        category_products_most_popular_sort
                                .setCompoundDrawablesRelativeWithIntrinsicBounds
                                        (0, 0, R.drawable.ic_baseline_check_24, 0);

                        category_products_latest_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_best_rating_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_lowest_price_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_highest_price_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);

                        category_products_sort_layout.startAnimation(slide_down);
                        category_products_sort_layout.setVisibility(View.GONE);
                        category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                        category_products_sort_btn.setText("Most Popular");

                        count -= 1;

                        translucent_layout_sort.setVisibility(View.GONE);

                        sortMostPopular();
                    }
                }
        );

        category_products_latest_sort.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        category_products_latest_sort
                                .setCompoundDrawablesRelativeWithIntrinsicBounds
                                        (0, 0, R.drawable.ic_baseline_check_24, 0);

                        category_products_most_popular_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_best_rating_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_lowest_price_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_highest_price_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);

                        category_products_sort_layout.startAnimation(slide_down);
                        category_products_sort_layout.setVisibility(View.GONE);
                        category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                        category_products_sort_btn.setText("Latest");
                        count -= 1;

                        translucent_layout_sort.setVisibility(View.GONE);

                        sortLatest();
                    }
                }
        );

        category_products_best_rating_sort.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        category_products_best_rating_sort
                                .setCompoundDrawablesRelativeWithIntrinsicBounds
                                        (0, 0, R.drawable.ic_baseline_check_24, 0);

                        category_products_latest_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_most_popular_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_lowest_price_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_highest_price_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);

                        category_products_sort_layout.startAnimation(slide_down);
                        category_products_sort_layout.setVisibility(View.GONE);
                        category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                        category_products_sort_btn.setText("Best Rating");
                        count -= 1;

                        translucent_layout_sort.setVisibility(View.GONE);

                        sortBestRating();
                    }
                }
        );

        category_products_lowest_price_sort.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        category_products_lowest_price_sort
                                .setCompoundDrawablesRelativeWithIntrinsicBounds
                                        (0, 0, R.drawable.ic_baseline_check_24, 0);

                        category_products_latest_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_best_rating_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_most_popular_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_highest_price_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);

                        category_products_sort_layout.startAnimation(slide_down);
                        category_products_sort_layout.setVisibility(View.GONE);
                        category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                        category_products_sort_btn.setText("Lowest Price");
                        count -= 1;

                        translucent_layout_sort.setVisibility(View.GONE);

                        sortLowestPrice();
                    }
                }
        );

        category_products_highest_price_sort.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        category_products_highest_price_sort
                                .setCompoundDrawablesRelativeWithIntrinsicBounds
                                        (0, 0, R.drawable.ic_baseline_check_24, 0);

                        category_products_latest_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_best_rating_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_lowest_price_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);
                        category_products_most_popular_sort.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0);

                        category_products_sort_layout.startAnimation(slide_down);
                        category_products_sort_layout.setVisibility(View.GONE);
                        category_products_sort_btn.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
                        category_products_sort_btn.setText("Highest Price");
                        count -= 1;

                        translucent_layout_sort.setVisibility(View.GONE);

                        sortHighestPrice();
                    }
                }
        );

    }

    private void sortMostPopular() {
        queryProducts = productRef.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID);

        recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview);
        recyclerViewProducts.setHasFixedSize(false);
        recyclerViewProducts.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(layoutManagerProducts);


        productsRView();
    }


    private void sortLatest() {
        queryProducts = productRef.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID)
                        .orderBy("DateCreated", Query.Direction.DESCENDING);

        recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview);
        recyclerViewProducts.setHasFixedSize(false);
        recyclerViewProducts.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(layoutManagerProducts);


        productsRView();
    }

    private void sortBestRating() {
        queryProducts = productRef.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID);

        recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview);
        recyclerViewProducts.setHasFixedSize(false);
        recyclerViewProducts.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(layoutManagerProducts);

        productsRView();
    }

    private void sortLowestPrice() {
        queryProducts = productRef.collection("Products")
                        .whereEqualTo("ProductCategory", categoryUniqueID)
                        .orderBy("ProductPrice", Query.Direction.ASCENDING);

        recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview);
        recyclerViewProducts.setHasFixedSize(false);
        recyclerViewProducts.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(layoutManagerProducts);

        productsRView();
    }

    private void sortHighestPrice() {
        queryProducts = productRef.collection("Products")
                .whereEqualTo("ProductCategory", categoryUniqueID)
                .orderBy("ProductPrice", Query.Direction.DESCENDING);

        recyclerViewProducts = view.findViewById(R.id.category_products_recyclerview);
        recyclerViewProducts.setHasFixedSize(false);
        recyclerViewProducts.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(layoutManagerProducts);

        productsRView();
    }


    private void productsRView() {


        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
//                .setEnablePlaceholders(false)
//                .setPrefetchDistance(5)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<Products> options = new FirestorePagingOptions.Builder<Products>()
                .setQuery(queryProducts,config, Products.class)
                .build();

        FirestorePagingAdapter<Products, UserCategoryProductsViewHolder> adapter = new FirestorePagingAdapter<Products, UserCategoryProductsViewHolder>(options) {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull UserCategoryProductsViewHolder holder, int position, @NonNull Products model) {

                String product_name = model.getProductName();
                if (product_name.length() > 20)
                {
                    product_name = product_name.substring(0, 19);
                    product_name = product_name + "...";
                }

                holder.txtCategoryProductsName.setText(product_name);
                holder.txtCategoryProductsPrice.setText("Ksh " + model.getProductPrice());
                Picasso.get().load(model.getProductImage()).into(holder.txtCategoryProductsImage);

//                Onclick listener to take the user to the products details
                holder.itemView.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getContext(), UserProductsDetailsActivity.class);
                                intent.putExtra("ProductUniqueID", model.getProductUniqueID());
                                startActivity(intent);
                            }
                        }
                );
//                Onclick listener to take the user to the products details

            }

            @NonNull
            @Override
            public UserCategoryProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_category_products_layout, parent, false);
                UserCategoryProductsViewHolder holder = new UserCategoryProductsViewHolder(view);
                return holder;
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
//                        main_loading_bar.setVisibility(View.GONE);
//                        progressBar1.setVisibility(View.GONE);
//                        main_loading_bar.setVisibility(View.GONE);
                        break;
                    case LOADING_MORE:
                        // Do your loading animation
//                        mSwipeRefreshLayout.setRefreshing(true);
//                        progressBar2.setVisibility(View.VISIBLE);
                        break;

                    case LOADED:
                        // Stop Animation
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        progressBar2.setVisibility(View.GONE);
                        break;

                    case FINISHED:
                        //Reached end of Data set
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        Toast.makeText(AdminProductsActivity.this, "Finished", Toast.LENGTH_SHORT).show();
//                        progressBar2.setVisibility(View.GONE);
                        break;

                    case ERROR:
                        retry();
                        break;

                }
            }

        };

        recyclerViewProducts.setAdapter(adapter);
        adapter.startListening();
//        progressBar1.setVisibility(View.GONE);
    }


}