package com.pkasemer.spacelounge.Adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.pkasemer.spacelounge.HelperClasses.CartItemHandlerListener;
import com.pkasemer.spacelounge.Models.FoodDBModel;
import com.pkasemer.spacelounge.MyMenuDetail;
import com.pkasemer.spacelounge.R;
import com.pkasemer.spacelounge.RootActivity;
import com.pkasemer.spacelounge.localDatabase.SenseDBHelper;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by Suleiman on 19/10/16.
 */

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private static final String BASE_URL_IMG = "";

    private List<FoodDBModel> foodDBModelList;
    SenseDBHelper db;


    private final Context context;

    private final boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;



    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    private String errorMsg;

    private final CartItemHandlerListener mCartListener;


    public CartAdapter(Context context, List<FoodDBModel>tweetList, CartItemHandlerListener mCartListener) {
        this.context = context;
        this.foodDBModelList = tweetList;
        this.mCartListener = mCartListener;
    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.pagination_item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.cart_item_design, parent, false);
        viewHolder = new CartDesignVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        FoodDBModel foodDBModel = foodDBModelList.get(position); // Food
        db = new SenseDBHelper(holder.itemView.getContext());



        switch (getItemViewType(position)) {
            case ITEM:
                final CartDesignVH movieVH = (CartDesignVH) holder;

                movieVH.cart_product_name.setText(foodDBModel.getMenuName());

                movieVH.cart_product_price.setText("Ugx " + NumberFormat.getNumberInstance(Locale.US).format(foodDBModel.getPrice()));

                movieVH.itemQuanEt.setText((foodDBModel.getQuantity()).toString());
                movieVH.fooditemtotal.setText("Ugx " + NumberFormat.getNumberInstance(Locale.US).format((foodDBModel.getPrice())*(foodDBModel.getQuantity())));

                movieVH.cart_item_Rating.setText(  "Rating "+ foodDBModel.getRating()
                        + " | "
                        + "5");

                Glide
                        .with(context)
                        .load(BASE_URL_IMG + foodDBModel.getMenuImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .transition(withCrossFade(factory))
                        .into(movieVH.cart_product_image);


                //show toast on click of show all button
                movieVH.cart_product_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context.getApplicationContext(), MyMenuDetail.class);
                        //PACK DATA
                        i.putExtra("SENDER_KEY", "MenuDetails");
                        i.putExtra("selectMenuId", foodDBModel.getMenuId());
                        i.putExtra("category_selected_key", foodDBModel.getMenuTypeId());
                        context.startActivity(i);
                    }
                });

                movieVH.btnCartItemRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCartListener.deletemenuitem(String.valueOf(foodDBModel.getMenuId()), foodDBModel);
                    }
                });


                movieVH.cart_addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCartListener.increment(foodDBModel.getQuantity()+1, foodDBModel);
                        updateFood(foodDBModel);

                    }
                });

                movieVH.cart_removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            mCartListener.decrement(foodDBModel.getQuantity()-1, foodDBModel);
                        updateFood(foodDBModel);

                    }
                });


                break;

            case LOADING:
//                Do nothing
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }

                break;
        }



    }



    @Override
    public int getItemCount() {
        return foodDBModelList == null ? 0 : foodDBModelList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == foodDBModelList.size() - 1 && isLoadingAdded) ?
                LOADING : ITEM;
    }


    public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof RootActivity) {
            RootActivity mainActivity = (RootActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag, "MenuDetails");
        }

    }




    public void remove(FoodDBModel r) {
        int position = foodDBModelList.indexOf(r);
        if (position > -1) {
            foodDBModelList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateFood(FoodDBModel r) {
        int position = foodDBModelList.indexOf(r);
        if (position > -1) {
            db = new SenseDBHelper(context.getApplicationContext());
            foodDBModelList = db.listTweetsBD();
            notifyDataSetChanged();
        }
    }





    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(foodDBModelList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }



    protected class CartDesignVH extends RecyclerView.ViewHolder {
        private TextView id_strView;
        private final TextView cart_product_name;
        private final TextView cart_product_price;
        private final TextView itemQuanEt;
        private final TextView fooditemtotal;
        private final TextView cart_item_Rating;
        private final ImageView cart_product_image;
        private final Button btnCartItemRemove;
        private final Button cart_addBtn;
        private final Button cart_removeBtn;
        private final ProgressBar mProgress;

        public CartDesignVH(View itemView) {
            super(itemView);
            cart_product_name = itemView.findViewById(R.id.cart_product_name);
            cart_product_price = itemView.findViewById(R.id.cart_product_price);
            cart_product_image = itemView.findViewById(R.id.cart_product_image);
            itemQuanEt = itemView.findViewById(R.id.itemQuanEt);
            btnCartItemRemove = itemView.findViewById(R.id.btnCartItemRemove);
            mProgress = itemView.findViewById(R.id.cart_progress);
            fooditemtotal = itemView.findViewById(R.id.fooditemtotal);
            cart_item_Rating = itemView.findViewById(R.id.cart_item_Rating);
            cart_addBtn = itemView.findViewById(R.id.cart_addBtn);
            cart_removeBtn = itemView.findViewById(R.id.cart_removeBtn);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ProgressBar mProgressBar;
        private final ImageButton mRetryBtn;
        private final TextView mErrorTxt;
        private final LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:
                    showRetry(false, null);
                    break;
            }
        }
    }







}