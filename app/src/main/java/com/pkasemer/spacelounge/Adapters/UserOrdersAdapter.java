package com.pkasemer.spacelounge.Adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.pkasemer.spacelounge.Models.UserOrdersResult;
import com.pkasemer.spacelounge.R;
import com.pkasemer.spacelounge.Utils.GlideApp;
import com.pkasemer.spacelounge.Utils.PaginationAdapterCallback;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Suleiman on 19/10/16.
 */

public class UserOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;

    //    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";
    private static final String BASE_URL_IMG = "";


    private List<UserOrdersResult> userOrdersResultList;
    private final Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;


    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    private final PaginationAdapterCallback mCallback;
    private String errorMsg;

    public UserOrdersAdapter(Context context, PaginationAdapterCallback callback) {
        this.context = context;
        this.mCallback = callback;
        userOrdersResultList = new ArrayList<>();
    }

    public List<UserOrdersResult> getMovies() {
        return userOrdersResultList;
    }

    public void setMovies(List<UserOrdersResult> userOrdersResultList) {
        this.userOrdersResultList = userOrdersResultList;
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
        View v1 = inflater.inflate(R.layout.order_layout, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        UserOrdersResult userOrdersResult = userOrdersResultList.get(position);// Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                if (userOrdersResult.getOrderId() != 0) {

                    movieVH.order_no.setText("Order No: ZodongoXM34" + userOrdersResult.getOrderId());
                    movieVH.order_date.setText(formatYearLabel(userOrdersResult));
                    movieVH.order_total_price.setText("Ugx " + NumberFormat.getNumberInstance(Locale.US).format(userOrdersResult.getTotalAmount()));
                    movieVH.order_tracking_no.setText("ZF34099112" + userOrdersResult.getOrderId());
                    movieVH.order_address.setText("Address:" + userOrdersResult.getOrderAddress());

                    // check order conditioin
                    /*
                     * 1 - pending --
                     * 2 - processing --
                     * 3 - delivered -- green color
                     * */
                    if ((userOrdersResult.getOrderStatus()) == 1) {
                        movieVH.order_status.setText("Pending");
                        movieVH.order_status.setTextColor(ContextCompat.getColor(context, R.color.pendingstatus));
                    } else if ((userOrdersResult.getOrderStatus()) == 2) {
                        movieVH.order_status.setText("Processing");
                        movieVH.order_status.setTextColor(ContextCompat.getColor(context, R.color.niceBlue));

                    } else {
                        movieVH.order_status.setText("Delivered");
                        movieVH.order_status.setTextColor(ContextCompat.getColor(context, R.color.niceGreen));
                    }


                } else {
                    movieVH.order_layout_card.setVisibility(View.GONE);
                    mCallback.requestfailed();
                }

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
        return userOrdersResultList == null ? 0 : userOrdersResultList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == userOrdersResultList.size() - 1 && isLoadingAdded) ?
                LOADING : ITEM;
    }







    /*
   Helpers
   _________________________________________________________________________________________________
    */

    private String formatYearLabel(UserOrdersResult userOrdersResult) {
        return "Created | " +
                userOrdersResult.getOrderDate().substring(0, 10);
    }

    private RequestBuilder<Drawable> loadImage(@NonNull String posterPath) {
        return GlideApp
                .with(context)
                .load(BASE_URL_IMG + posterPath)
                .centerCrop();
    }

    public void add(UserOrdersResult r) {
        userOrdersResultList.add(r);
        notifyItemInserted(userOrdersResultList.size() - 1);
    }

    public void addAll(List<UserOrdersResult> userOrdersResultList1) {
        for (UserOrdersResult userOrdersResult : userOrdersResultList1) {
            add(userOrdersResult);
        }
    }

    public void remove(UserOrdersResult r) {
        int position = userOrdersResultList.indexOf(r);
        if (position > -1) {
            userOrdersResultList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new UserOrdersResult());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = userOrdersResultList.size() - 1;
        UserOrdersResult userOrdersResult = getItem(position);

        if (userOrdersResult != null) {
            userOrdersResultList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(userOrdersResultList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public UserOrdersResult getItem(int position) {
        return userOrdersResultList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */

    protected class MovieVH extends RecyclerView.ViewHolder {
        private final TextView order_no, order_total_price, order_address, order_status, order_tracking_no, order_date;
        private final RelativeLayout order_layout_card;

        public MovieVH(View itemView) {
            super(itemView);

            order_no = itemView.findViewById(R.id.order_no);
            order_tracking_no = itemView.findViewById(R.id.order_tracking_no);
            order_total_price = itemView.findViewById(R.id.order_total_price);
            order_date = itemView.findViewById(R.id.order_date);
            order_address = itemView.findViewById(R.id.order_address);
            order_status = itemView.findViewById(R.id.order_status);
            order_layout_card = itemView.findViewById(R.id.order_layout_card);


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
                    mCallback.retryPageLoad();
                    break;
            }
        }
    }


}