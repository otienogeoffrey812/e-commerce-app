package com.example.slickkwear;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;

public class UserAccountFragment extends Fragment {

    private MaterialTextView user_account_admin;

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