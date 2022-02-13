package com.pkasemer.spacelounge.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pkasemer.spacelounge.Adapters.TripsAdapter;
import com.pkasemer.spacelounge.Models.Ads;
import com.pkasemer.spacelounge.Models.Item;
import com.pkasemer.spacelounge.Models.News;
import com.pkasemer.spacelounge.Models.Trip;
import com.pkasemer.spacelounge.R;

import java.util.ArrayList;
import java.util.List;


public class Multiview extends Fragment {



    public Multiview() {
        // Required empty public constructor
    }


    public static Cart newInstance(String param1, String param2) {
        Cart fragment = new Cart();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.multyrecyclerView);
        List<Item> items = new ArrayList<>();

        //here types are 0=trip, 1=ads, 2=news



        //trip
        Trip trip1 = new Trip(R.drawable.africanwoman, "Crotia", "summer 2022 -20 days");
        items.add(new Item(0, trip1));

        //ads
        Ads ads1 = new Ads("Kakebe Shop", "Best online Shopping in Lira");
        items.add(new Item(1, ads1));

        //News
        News news1 = new News("Lira, Uganda", "You'll find the most interesting story " +
                "You'll find the most interesting story You'll find the most interesting story " +
                "You'll find the most interesting story You'll find the most interesting story");
        items.add(new Item(2, news1));

        //News
        News news2 = new News("Kampala, Uganda", "You'll find the most interesting story " +
                "You'll find the most interesting story You'll find the most interesting story " +
                "You'll find the most interesting story You'll find the most interesting story");
        items.add(new Item(2, news2));

        //ads
        Ads ads2 = new Ads("Mwonyaa Streams", "Best online Shopping in Lira");
        items.add(new Item(1, ads2));

        recyclerView.setAdapter(new TripsAdapter(items));


        return view;
    }
}