package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.slickkwear.Model.Categories;
import com.example.slickkwear.Model.Products;
import com.example.slickkwear.ViewHolder.CategoryViewHolder;
import com.example.slickkwear.ViewHolder.ProductViewHolder;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class AdminCategoriesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Choose android x version
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialButton addCategoryButton;

    private FirebaseFirestore categoriesRef;
    private Query query;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar1, progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categories);

        toolbar = findViewById(R.id.main_toolbar);
//        toolbar.setTitle("Admin");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);

        addCategoryButton = (MaterialButton) findViewById(R.id.admin_go_to_add_category_page_btn);

        addCategoryButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), AdminCategoriesAddEditActivity.class);
                        startActivity(intent);
                    }
                }
        );


        categoriesRef = FirebaseFirestore.getInstance();
        query = categoriesRef.collection("Categories");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.hasNestedScrollingParent();
//        layoutManager = new LinearLayoutManager(this);
        layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(3)
//                .setEnablePlaceholders(false)
//                .setPrefetchDistance(5)
                .setPageSize(2)
                .build();

        FirestorePagingOptions<Categories> options = new FirestorePagingOptions.Builder<Categories>()
                .setQuery(query,config, Categories.class)
                .build();

        FirestorePagingAdapter<Categories, CategoryViewHolder> adapter = new FirestorePagingAdapter<Categories, CategoryViewHolder>(options) {
            @NonNull
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder holder, int position, @NonNull Categories model) {

                holder.txtCategoryName.setText(model.getCategoryName());
                holder.txtCategoryStatus.setText(model.getCategoryStatus());
                Picasso.get().load(model.getCategoryImage()).into(holder.txtCategoryImage);

//                Onclick listener to take the user to the products details
                holder.itemView.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent(getApplicationContext(), AdminProductsDetailsActivity.class);
                                intent.putExtra("ProductUniqueID", model.getCategoryUniqueID());
                                startActivity(intent);
                            }
                        }
                );
//                Onclick listener to take the user to the products details

            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_category_display_layout, parent, false);
                CategoryViewHolder holder = new CategoryViewHolder(view);
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
                        progressBar2.setVisibility(View.VISIBLE);
                        break;

                    case LOADED:
                        // Stop Animation
//                        mSwipeRefreshLayout.setRefreshing(false);
                        progressBar2.setVisibility(View.GONE);
                        break;

                    case FINISHED:
                        //Reached end of Data set
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        Toast.makeText(AdminProductsActivity.this, "Finished", Toast.LENGTH_SHORT).show();
                        progressBar2.setVisibility(View.GONE);
                        break;

                    case ERROR:
                        retry();
                        break;

                }
            }

        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
//        progressBar1.setVisibility(View.GONE);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.admin_home_menu)
        {
            Intent intent = new Intent(getApplicationContext(), AdminHomeActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.admin_orders_menu)
        {
            Intent intent = new Intent(getApplicationContext(), AdminOrdersActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.admin_categories_menu)
        {
            Intent intent = new Intent(getApplicationContext(), AdminCategoriesActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.admin_products_menu)
        {
            Intent intent = new Intent(getApplicationContext(), AdminProductsActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.admin_delivery_menu)
        {
            Intent intent = new Intent(getApplicationContext(), AdminDeliveryActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.admin_assistant_menu)
        {
            Intent intent = new Intent(getApplicationContext(), AdminAssistantActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.admin_main_home_menu)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.admin_settings_menu)
        {
            Intent intent = new Intent(getApplicationContext(), AdminAssistantActivity.class);
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.admin_logout_menu)
        {
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}