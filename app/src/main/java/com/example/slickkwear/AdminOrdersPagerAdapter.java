package com.example.slickkwear;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdminOrdersPagerAdapter extends FragmentStateAdapter {

    public AdminOrdersPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position) {
           case 0:
               return new AdminNewOrdersFragment();
           case 1:
               return new AdminDispatchedOrdersFragment();
           default:
               return new AdminAllOrdersFragment();
       }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
