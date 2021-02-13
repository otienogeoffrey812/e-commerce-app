package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class AdminAssistantActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Choose android x version
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assistant);

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