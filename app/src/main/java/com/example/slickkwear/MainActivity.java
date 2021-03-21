package com.example.slickkwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.smarteist.autoimageslider.SliderView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    SliderView sliderView;
    private SliderHomeAdapter adapter;

    private FirebaseFirestore productRef;
    private Query query;

    private EditText search_edit_text;
    private LinearLayout search_interface;
    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        userSearch();
         bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
         bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserHomeFragment())
                .commit();

        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        search_interface = (LinearLayout) findViewById(R.id.search_interface);

        search_edit_text.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {

                        search_interface.setVisibility(View.VISIBLE);

                    }
                }
        );

//        homeSliderBanner();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search_menu, menu);
//
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);
//
//        return true;
//    }

//    private void userSearch() {
//
//        String[] list = new String[] {"one", "two", "three"};
//
////        searchView = (MaterialSearchView) findViewById(R.id.search_view);
////        searchView.setSuggestions(list);
//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchView.setSuggestions(list);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //Do some magic
//                searchView.setSuggestions(list);
//                return false;
//            }
//        });
//
//        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewShown() {
////                searchView.showSuggestions();
//                //Do some magic
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//                //Do some magic
//            }
//        });
//    }

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

}