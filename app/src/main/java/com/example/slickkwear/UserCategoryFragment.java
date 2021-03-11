package com.example.slickkwear;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.slickkwear.Model.Categories;
import com.example.slickkwear.ViewHolder.CategoryCategoryViewHolder;
import com.example.slickkwear.ViewHolder.HomeCategoryViewHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class UserCategoryFragment extends Fragment {

    private RecyclerView recyclerViewCategory;
    private RecyclerView.LayoutManager layoutManagerCategories;
    private FirebaseFirestore categoryRef;
    private Query queryCategory;
    private View view;


    public UserCategoryFragment() {
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
        view = inflater.inflate(R.layout.fragment_user_category, container, false);

        categoryRef = FirebaseFirestore.getInstance();
        queryCategory = categoryRef.collection("Categories");

        recyclerViewCategory = (RecyclerView) view.findViewById(R.id.recycler_view_user_category);
        recyclerViewCategory.setHasFixedSize(true);
        recyclerViewCategory.setNestedScrollingEnabled(false);
        layoutManagerCategories = new GridLayoutManager(getContext(), 2);
        recyclerViewCategory.setLayoutManager(layoutManagerCategories);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
//                .setEnablePlaceholders(false)
//                .setPrefetchDistance(5)
                .setPageSize(5)
                .build();

        FirestorePagingOptions<Categories> options = new FirestorePagingOptions.Builder<Categories>()
                .setQuery(queryCategory,config, Categories.class)
                .build();

        FirestorePagingAdapter<Categories, CategoryCategoryViewHolder> adapter = new FirestorePagingAdapter<Categories, CategoryCategoryViewHolder>(options) {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull CategoryCategoryViewHolder holder, int position, @NonNull Categories model) {

                holder.txtCategoryName.setText(model.getCategoryName());
                Picasso.get().load(model.getCategoryImage()).into(holder.txtCategoryImage);

//                Onclick listener to take the user to the products details
                holder.itemView.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getContext(), AdminProductsDetailsActivity.class);
                                intent.putExtra("ProductUniqueID", model.getCategoryUniqueID());
                                startActivity(intent);
                            }
                        }
                );
//                Onclick listener to take the user to the products details

            }

            @NonNull
            @Override
            public CategoryCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_category_category_layout, parent, false);
                CategoryCategoryViewHolder holder = new CategoryCategoryViewHolder(view);
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
}