package com.pkasemer.spacelounge.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.pkasemer.spacelounge.HelperClasses.SharedPrefManager;
import com.pkasemer.spacelounge.LoginMaterial;
import com.pkasemer.spacelounge.ManageOrders;
import com.pkasemer.spacelounge.Models.UserModel;
import com.pkasemer.spacelounge.R;


public class Profile extends Fragment {


    TextView textViewUsername, textViewEmail,address_text,full_name_text,card_email_text,card_address_text,card_phone_text;


    MaterialCardView manageOrders;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(getContext()).isLoggedIn()) {
//            finish();
            startActivity(new Intent(getContext(), LoginMaterial.class));
        }


        textViewUsername = view.findViewById(R.id.full_name);
        textViewEmail = view.findViewById(R.id.email_text);
        address_text = view.findViewById(R.id.address_text);


        full_name_text = view.findViewById(R.id.full_name_text);
        card_email_text = view.findViewById(R.id.card_email_text);
        card_address_text = view.findViewById(R.id.card_address_text);
        card_phone_text = view.findViewById(R.id.card_phone_text);
        manageOrders = view.findViewById(R.id.manageOrders);

        //getting the current user
        UserModel userModel = SharedPrefManager.getInstance(getContext()).getUser();

        //setting the values to the textviews
        textViewUsername.setText(userModel.getUsername());
        textViewEmail.setText(userModel.getEmail());
        address_text.setText("Location: " + userModel.getAddress());


        full_name_text.setText(userModel.getFullname());
        card_email_text.setText(userModel.getEmail());
        card_address_text.setText(userModel.getAddress());
        card_phone_text.setText(userModel.getPhone());


        //when the user presses logout button
        //calling the logout method
        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManager.getInstance(getContext()).logout();

            }
        });

        manageOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i("show past order ", "orders" );
                Intent i = new Intent(getContext(), ManageOrders.class);
                startActivity(i);
            }
        });




        return view;
    }
}