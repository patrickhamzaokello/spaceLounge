package com.pkasemer.spacelounge.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pkasemer.spacelounge.Adapters.CartAdapter;
import com.pkasemer.spacelounge.HelperClasses.CartItemHandlerListener;
import com.pkasemer.spacelounge.Models.FoodDBModel;
import com.pkasemer.spacelounge.PlaceOrder;
import com.pkasemer.spacelounge.R;
import com.pkasemer.spacelounge.RootActivity;
import com.pkasemer.spacelounge.localDatabase.SenseDBHelper;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class Cart extends Fragment implements CartItemHandlerListener {

    private SenseDBHelper db;
    boolean food_db_itemchecker;
    private Cursor cursor;
    List<FoodDBModel> cartitemlist;
    private ProgressBar progressBar;
    CartAdapter cartAdapter;
    RecyclerView recyclerView;

    TextView grandtotalvalue;
    LinearLayout procceed_checkout_layout;

    Button btnCheckout;


    public Cart() {
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
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        progressBar = view.findViewById(R.id.cart_main_progress);
        grandtotalvalue = view.findViewById(R.id.grandtotalvalue);
        recyclerView = view.findViewById(R.id.cart_main_recycler);
        procceed_checkout_layout = view.findViewById(R.id.procceed_checkout_layout);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
        db = new SenseDBHelper(view.getContext());
        cartitemlist = db.listTweetsBD();

        if (cartitemlist.size() > 0) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            cartAdapter = new CartAdapter(getContext(), cartitemlist, this);
            recyclerView.setAdapter(cartAdapter);
            cartAdapter.notifyDataSetChanged();
            grandtotalvalue();
        } else {
            recyclerView.setVisibility(View.GONE);
            emptycartwarning();
        }

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), PlaceOrder.class);
                startActivity(i);
            }
        });



        return view;
    }


    @Override
    public void increment(int qty, FoodDBModel foodDBModel) {
        food_db_itemchecker = db.checktweetindb(String.valueOf(foodDBModel.getMenuId()));

        if (food_db_itemchecker) {
            return;
            //item doesnt exist
        } else {
            db.updateMenuCount(qty, foodDBModel.getMenuId());
            //item exists
        }

        grandtotalvalue();


    }

    @Override
    public void decrement(int qty, FoodDBModel foodDBModel) {
        if (qty <= 1) {
            qty = 1;
            food_db_itemchecker = db.checktweetindb(String.valueOf(foodDBModel.getMenuId()));

            if (food_db_itemchecker) {
                return;
                //item doesnt exist
            } else {
                db.updateMenuCount(qty, foodDBModel.getMenuId());
                //item exists

            }
        } else {


            food_db_itemchecker = db.checktweetindb(String.valueOf(foodDBModel.getMenuId()));

            if (food_db_itemchecker) {
                return;
                //item doesnt exist
            } else {
                db.updateMenuCount(qty, foodDBModel.getMenuId());
                //item exists

            }
        }
        grandtotalvalue();
//
    }

    @Override
    public void deletemenuitem(String foodModel_ID, FoodDBModel foodDBModel) {

        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Item will be Removed From Cart")
                .setCancelText("No")
                .setConfirmText("Yes,delete it!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        return;
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog
                                .setTitleText("Deleted!")
                                .setContentText("Item has been Removed From Cart")
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                        db.deleteTweet(foodModel_ID);
                        grandtotalvalue();
                        updatecartCount();
                        cartAdapter.remove(foodDBModel);
                    }
                })
                .show();

    }


    public void grandtotalvalue() {
        grandtotalvalue.setText("" + NumberFormat.getNumberInstance(Locale.US).format(db.sumPriceCartItems()));

        if (db.sumPriceCartItems() == 0) {
            emptycartwarning();
        }
    }

    private void updatecartCount() {
        String mycartcount = String.valueOf(db.countCart());
        Intent intent = new Intent(getContext().getResources().getString(R.string.cartcoutAction));
        intent.putExtra(getContext().getResources().getString(R.string.cartCount), mycartcount);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }


    private void emptycartwarning() {
        procceed_checkout_layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        AlertDialog.Builder android = new AlertDialog.Builder(getContext());
        android.setTitle("Empty Cart");
        android.setMessage("Add Items to your cart and check back here later")
                .setCancelable(false)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //go to activity
                        Intent intent = new Intent(getActivity(), RootActivity.class);
                        startActivity(intent);
                    }
                });

        android.create().show();
    }


}