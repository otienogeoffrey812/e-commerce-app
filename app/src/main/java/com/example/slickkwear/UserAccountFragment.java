package com.example.slickkwear;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textview.MaterialTextView;

public class UserAccountFragment extends Fragment {

    private MaterialTextView user_account_admin;
    private Button register_link_btn;

    public UserAccountFragment() {
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
       View view = inflater.inflate(R.layout.fragment_user_account, container, false);

       user_account_admin = (MaterialTextView) view.findViewById(R.id.user_account_admin);

       register_link_btn = (Button) view.findViewById(R.id.user_register_link_btn);
       register_link_btn.setOnClickListener(
               new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent(getContext(), RegisterActivity.class);
                       startActivity(intent);
                   }
               }
       );


        TextView verify_link_btn = (TextView) view.findViewById(R.id.account_my_wishlist);
        verify_link_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );



        user_account_admin.setOnClickListener(
               new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Intent intent = new Intent(getContext(), AdminHomeActivity.class);
                       startActivity(intent);
                   }
               }
       );


       return view;
    }
}