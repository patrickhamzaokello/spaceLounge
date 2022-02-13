package com.pkasemer.spacelounge;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pkasemer.spacelounge.Adapters.CartAdapter;
import com.pkasemer.spacelounge.HelperClasses.CartItemHandlerListener;
import com.pkasemer.spacelounge.Models.FoodDBModel;
import com.pkasemer.spacelounge.localDatabase.SenseDBHelper;

import java.util.List;

public class RootActivity extends AppCompatActivity implements CartItemHandlerListener {

    BottomNavigationView navView;
    SenseDBHelper db;
    List<FoodDBModel> cartitemlist;
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_activity);

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("My new title"); // set the top title
        String title = actionBar.getTitle().toString(); // get the title
        actionBar.hide();



        //Initialize Bottom Navigation View.
        navView = findViewById(R.id.bottomNav_view);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMasage,new IntentFilter(getString(R.string.cartcoutAction)));

        db = new SenseDBHelper(this);

        //Pass the ID's of Different destinations
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_cart, R.id.navigation_profile )
                .build();

        //Initialize NavController.
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navView, navController);
        navView.getOrCreateBadge(R.id.navigation_cart).setBackgroundColor(getResources().getColor(R.color.sweetRed));

        //updating cart counts
        updatecartCount();

    }


    public void switchContent(int id, Fragment fragment, String fragmentname) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment, fragment.toString());
//        ft.addToBackStack(fragmentname);
        ft.commit();
    }


    public BroadcastReceiver mMasage=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String cartcount=intent.getStringExtra(getString(R.string.cartCount));

            if((Integer.parseInt(cartcount)) != 0){
                navView.getOrCreateBadge(R.id.navigation_cart).setNumber(Integer.parseInt(cartcount));
                navView.getOrCreateBadge(R.id.navigation_cart).setVisible(true);

            }else {
                navView.getOrCreateBadge(R.id.navigation_cart).clearNumber();
                navView.getOrCreateBadge(R.id.navigation_cart).setVisible(false);

            }
        }
    };

    private void updatecartCount() {
        int mycartcount = db.countCart();
        if(mycartcount != 0){
            navView.getOrCreateBadge(R.id.navigation_cart).setNumber(mycartcount);
            navView.getOrCreateBadge(R.id.navigation_cart).setVisible(true);

        }else {
            navView.getOrCreateBadge(R.id.navigation_cart).clearNumber();
            navView.getOrCreateBadge(R.id.navigation_cart).setVisible(false);
        }

        refreshcartPage();


    }


    private void refreshcartPage(){

        db = new SenseDBHelper(this);
        cartitemlist = db.listTweetsBD();

        if (cartitemlist.size() > 0) {
            cartAdapter = new CartAdapter(this, cartitemlist, this);
            cartAdapter.notifyDataSetChanged();
        }
        else {
//            recyclerView.setVisibility(View.GONE);
//            emptycartwarning();
        }
    }


    @Override
    public void increment(int qty, FoodDBModel foodDBModel) {

    }

    @Override
    public void decrement(int qty, FoodDBModel foodDBModel) {

    }

    @Override
    public void deletemenuitem(String foodMenu_id, FoodDBModel foodDBModel) {

    }
}