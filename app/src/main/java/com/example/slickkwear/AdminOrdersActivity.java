package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminOrdersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Choose android x version
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

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


        viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setAdapter(new AdminOrdersPagerAdapter(this));
        tabLayout = findViewById(R.id.tab_layout);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                switch (position)
                {
                    case 0: {
                        tab.setText("New");
                        tab.setIcon(R.drawable.ic_baseline_person_24);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.design_default_color_error)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(50);
                        badgeDrawable.setMaxCharacterCount(10);
                        break;
                    }
                    case 1: {
                        tab.setText("Dispatch");
                        tab.setIcon(R.drawable.ic_baseline_directions_bike_24);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.design_default_color_error)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(50);
                        badgeDrawable.setMaxCharacterCount(10);
                        break;
                    }
                    case 2: {
                        tab.setText("All");
                        tab.setIcon(R.drawable.ic_baseline_local_grocery_store_24);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.design_default_color_error)
                        );
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(50);
                        badgeDrawable.setMaxCharacterCount(10);
                        break;
                    }

                }

            }
        }
        );
        tabLayoutMediator.attach();

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