package com.example.slickkwear;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slickkwear.Model.Products;
import com.example.slickkwear.ViewHolder.HomeProductsViewHolder;
import com.example.slickkwear.ViewHolder.UserCategoryProductsViewHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class UserCategoryProductsFragment extends Fragment {

    private View view;
    private FirebaseFirestore productRef;
    private Query queryProducts;
    private RecyclerView recyclerViewProducts;
    private RecyclerView.LayoutManager layoutManagerProducts;
    private String categoryUniqueID;
    private TextView backToPrevBtn, category_products_sort_btn;
    private TextView category_products_most_popular_sort,category_products_latest_sort,category_products_lowest_price_sort,category_products_best_rating_sort, category_products_highest_price_sort;
    private LinearLayout category_products_sort_layout, translucent_layout_sort;
    private int count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_category_products, container, false);

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

        backToPrevBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // RETURN TO PREVIOUS FRAGMENT

                        getActivity().onBackPressed();

                    }
                }
        );

        sortProducts();

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
                    }
                }
        );

    }


    private void sortLatest() {
        queryProducts = productRef.collection("Products").whereEqualTo("ProductCategory", categoryUniqueID)
                        .orderBy("TimeCreated", Query.Direction.DESCENDING);

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