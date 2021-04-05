package com.example.slickkwear;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.slickkwear.Model.Categories;
import com.example.slickkwear.Model.Products;
import com.example.slickkwear.Model.SliderItem;
import com.example.slickkwear.ViewHolder.CategoryViewHolder;
import com.example.slickkwear.ViewHolder.HomeCategoryViewHolder;
import com.example.slickkwear.ViewHolder.HomeDealsViewHolder;
import com.example.slickkwear.ViewHolder.HomeProductsViewHolder;
import com.example.slickkwear.ViewHolder.HomeTrendingViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

public class UserHomeFragment extends Fragment {

    SliderView sliderView;
    private SliderHomeAdapter adapter;

    private FirebaseFirestore productRef;
    private Query query, queryCategory, queryDeals, queryTrending, queryProducts;

    private RecyclerView recyclerViewCategory, recyclerViewDeals, recyclerViewTrending, recyclerViewProducts;
    private RecyclerView.LayoutManager layoutManagerCategory, layoutManagerDeals, layoutManagerTrending, layoutManagerProducts;
    private View view;
    MaterialSearchView searchView;

    private ProgressBar loadingBar;

    LottieAnimationView lottieAnimationView;

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
        view = inflater.inflate(R.layout.fragment_user_home, container, false);


        lottieAnimationView = view.findViewById(R.id.lottie);
        sliderView = view.findViewById(R.id.imageSlider);

        productRef = FirebaseFirestore.getInstance();
        query = productRef.collection("Products");


//        userSearch();

//        loadingBar = (ProgressBar) view.findViewById(R.id.)
//        getInstrumentation().waitForIdleSync()

        homeSliderBanner();

        queryCategory = productRef.collection("Categories");

        recyclerViewCategory = view.findViewById(R.id.recycler_view_category);
        recyclerViewCategory.setHasFixedSize(false);
        recyclerViewCategory.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerCategory = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManagerCategory);


        queryDeals = productRef.collection("Products").orderBy("ProductName", Query.Direction.ASCENDING).limit(3);

        recyclerViewDeals = view.findViewById(R.id.recycler_view_today_deals);
        recyclerViewDeals.setHasFixedSize(false);
        recyclerViewDeals.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerDeals = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerViewDeals.setLayoutManager(layoutManagerDeals);


        queryTrending = productRef.collection("Products");

        recyclerViewTrending = view.findViewById(R.id.recycler_view_home_trending);
        recyclerViewTrending.setHasFixedSize(false);
        recyclerViewTrending.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerTrending = new GridLayoutManager(getContext(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerViewTrending.setLayoutManager(layoutManagerTrending);

        queryProducts = productRef.collection("Products");

        recyclerViewProducts = view.findViewById(R.id.recycler_view_home_products);
        recyclerViewProducts.setHasFixedSize(false);
        recyclerViewProducts.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManagerProducts = new LinearLayoutManager(getContext());
        layoutManagerProducts = new GridLayoutManager(getContext(), 2);
        recyclerViewProducts.setLayoutManager(layoutManagerProducts);

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


    @Override
    public void onStart() {
        super.onStart();

        homeCategory();

        homeDeals();
//
        homeTrending();

        homeProducts();
    }


    private void homeCategory() {

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
//                .setEnablePlaceholders(false)
//                .setPrefetchDistance(5)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<Categories> options = new FirestorePagingOptions.Builder<Categories>()
                .setQuery(queryCategory,config, Categories.class)
                .build();

        FirestorePagingAdapter<Categories, HomeCategoryViewHolder> adapter = new FirestorePagingAdapter<Categories, HomeCategoryViewHolder>(options) {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull HomeCategoryViewHolder holder, int position, @NonNull Categories model) {

                holder.txtCategoryName.setText(model.getCategoryName());
                Picasso.get().load(model.getCategoryImage()).into(holder.txtCategoryImage);

//                Onclick listener to take the user to the products details
                holder.itemView.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getContext(), UserCategoryFragment.class);
                                intent.putExtra("CategoryUniqueID", model.getCategoryUniqueID());
                                startActivity(intent);
                            }
                        }
                );
//                Onclick listener to take the user to the products details

            }

            @NonNull
            @Override
            public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_home_category_layout, parent, false);
                HomeCategoryViewHolder holder = new HomeCategoryViewHolder(view);
                return holder;
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
//                        Toast.makeText(AdminProductsActivity.this, "Initial Data Loaded", Toast.LENGTH_SHORT).show();
//                        progressBar1.setVisibility(View.GONE);
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

        recyclerViewCategory.setAdapter(adapter);
        adapter.startListening();
//        progressBar1.setVisibility(View.GONE);
    }


    private void homeDeals() {

        FirestoreRecyclerOptions<Products> options = new FirestoreRecyclerOptions.Builder<Products>()
                .setQuery(queryDeals, Products.class)
                .build();

        FirestoreRecyclerAdapter<Products, HomeDealsViewHolder> adapter = new FirestoreRecyclerAdapter<Products, HomeDealsViewHolder>(options) {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull HomeDealsViewHolder holder, int position, @NonNull Products model) {

                holder.txtDealName.setText(model.getProductName());

                String initialPrice = "Ksh " + model.getProductPrice();
//                String text2 = "<strike><font color=\'#757575\'>" + initialPrice + "</font></strike>";
////                textview.setText(Html.fromHtml(text));
                holder.txtDealPriceInitial.setText(Html.fromHtml("<strike><font color=\'#757575\'>" + initialPrice + "</font></strike>"));
                holder.txtDealPriceDiscounted.setText("Ksh " + model.getProductPrice());
                Picasso.get().load(model.getProductImage()).into(holder.txtDealImage);

//                Onclick listener to take the user to the products details
                holder.itemView.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getContext(), AdminProductsDetailsActivity.class);
                                intent.putExtra("ProductUniqueID", model.getProductUniqueID());
                                startActivity(intent);
                            }
                        }
                );
//                Onclick listener to take the user to the products details

            }

            @NonNull
            @Override
            public HomeDealsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_home_deals_layout, parent, false);
                HomeDealsViewHolder holder = new HomeDealsViewHolder(view);
                return holder;
            }

        };
        recyclerViewDeals.setAdapter(adapter);
        adapter.startListening();
    }

    private void homeTrending(){

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
//                .setEnablePlaceholders(false)
//                .setPrefetchDistance(5)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<Products> options = new FirestorePagingOptions.Builder<Products>()
                .setQuery(queryTrending,config, Products.class)
                .build();

        FirestorePagingAdapter<Products, HomeTrendingViewHolder> adapter = new FirestorePagingAdapter<Products, HomeTrendingViewHolder>(options) {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull HomeTrendingViewHolder holder, int position, @NonNull Products model) {

                holder.txtTrendingName.setText(model.getProductName());
                holder.txtTrendingPrice.setText("Ksh " + model.getProductPrice());
                Picasso.get().load(model.getProductImage()).into(holder.txtTrendingImage);

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
            public HomeTrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_home_trending_layout, parent, false);
                HomeTrendingViewHolder holder = new HomeTrendingViewHolder(view);
                return holder;
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
//                        Toast.makeText(AdminProductsActivity.this, "Initial Data Loaded", Toast.LENGTH_SHORT).show();
//                        progressBar1.setVisibility(View.GONE);
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

        recyclerViewTrending.setAdapter(adapter);
        adapter.startListening();
//        progressBar1.setVisibility(View.GONE);
    }

    private void homeProducts(){

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
//                .setEnablePlaceholders(false)
//                .setPrefetchDistance(5)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<Products> options = new FirestorePagingOptions.Builder<Products>()
                .setQuery(queryProducts,config, Products.class)
                .build();

        FirestorePagingAdapter<Products, HomeProductsViewHolder> adapter = new FirestorePagingAdapter<Products, HomeProductsViewHolder>(options) {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull HomeProductsViewHolder holder, int position, @NonNull Products model) {

                holder.txtProductsName.setText(model.getProductName());
                holder.txtProductsPrice.setText("Ksh " + model.getProductPrice());
                Picasso.get().load(model.getProductImage()).into(holder.txtProductsImage);

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
            public HomeProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_home_products_layout, parent, false);
                HomeProductsViewHolder holder = new HomeProductsViewHolder(view);
                return holder;
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
//                        Toast.makeText(AdminProductsActivity.this, "Initial Data Loaded", Toast.LENGTH_SHORT).show();
//                        progressBar1.setVisibility(View.GONE);
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