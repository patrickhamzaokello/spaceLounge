package com.pkasemer.spacelounge.Adapters;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.pkasemer.spacelounge.Models.HomeMenuCategoryModelResult;
import com.pkasemer.spacelounge.Models.SelectedCategoryMenuItemResult;
import com.pkasemer.spacelounge.MySelectedCategory;
import com.pkasemer.spacelounge.R;
import com.pkasemer.spacelounge.RootActivity;
import com.pkasemer.spacelounge.Utils.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class HomeMenuCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    //    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";
    private static final String BASE_URL_IMG = "";


    private List<HomeMenuCategoryModelResult> movieHomeMenuCategoryModelResults;
    private final Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    DrawableCrossFadeFactory factory =
            new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

    private String errorMsg;

    public HomeMenuCategoryAdapter(Context context) {
        this.context = context;
        movieHomeMenuCategoryModelResults = new ArrayList<>();
    }

    public List<HomeMenuCategoryModelResult> getMovies() {
        return movieHomeMenuCategoryModelResults;
    }

    public void setMovies(List<HomeMenuCategoryModelResult> movieHomeMenuCategoryModelResults) {
        this.movieHomeMenuCategoryModelResults = movieHomeMenuCategoryModelResults;
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
        View v1 = inflater.inflate(R.layout.home_menu_category_design, parent, false);
        viewHolder = new MovieVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        HomeMenuCategoryModelResult homeMenuCategoryModelResult = movieHomeMenuCategoryModelResults.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;


                Glide
                        .with(context)
                        .load(BASE_URL_IMG + homeMenuCategoryModelResult.getImageCover())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                movieVH.mProgress.setVisibility(View.VISIBLE);
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
                        .into(movieVH.mPosterImg);

                movieVH.mPosterImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context.getApplicationContext(), MySelectedCategory.class);
                        //PACK DATA
                        i.putExtra("SENDER_KEY", "MyFragment");
                        i.putExtra("category_selected_key", homeMenuCategoryModelResult.getId());
                        context.startActivity(i);




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
        return movieHomeMenuCategoryModelResults == null ? 0 : movieHomeMenuCategoryModelResults.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == movieHomeMenuCategoryModelResults.size() - 1 && isLoadingAdded) ?
                LOADING : ITEM;

    }


    public void switchContent(int id, Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof RootActivity) {
            RootActivity mainActivity = (RootActivity) context;
            Fragment frag = fragment;
            mainActivity.switchContent(id, frag, "CategoryDetails");
        }

    }




    /*
   Helpers
   _________________________________________________________________________________________________
    */

    private String formatYearLabel(SelectedCategoryMenuItemResult selectedCategoryMenuItemResult) {
        return "Created | " +
                selectedCategoryMenuItemResult.getCreated().substring(0, 4);
    }

    private RequestBuilder<Drawable> loadImage(@NonNull String posterPath) {
        return GlideApp
                .with(context)
                .load(BASE_URL_IMG + posterPath)
                .centerCrop();
    }

    public void add(HomeMenuCategoryModelResult r) {
        movieHomeMenuCategoryModelResults.add(r);
        notifyItemInserted(movieHomeMenuCategoryModelResults.size() - 1);
    }

    public void addAll(List<HomeMenuCategoryModelResult> moveHomeMenuCategoryModelResults) {
        for (HomeMenuCategoryModelResult homeMenuCategoryModelResult : moveHomeMenuCategoryModelResults) {
            add(homeMenuCategoryModelResult);
        }
    }

    public void remove(HomeMenuCategoryModelResult r) {
        int position = movieHomeMenuCategoryModelResults.indexOf(r);
        if (position > -1) {
            movieHomeMenuCategoryModelResults.remove(position);
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
        add(new HomeMenuCategoryModelResult());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieHomeMenuCategoryModelResults.size() - 1;
        HomeMenuCategoryModelResult homeMenuCategoryModelResult = getItem(position);

        if (homeMenuCategoryModelResult != null) {
            movieHomeMenuCategoryModelResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movieHomeMenuCategoryModelResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public HomeMenuCategoryModelResult getItem(int position) {
        return movieHomeMenuCategoryModelResults.get(position);
    }

    @Override
    public Filter getFilter() {
        return null;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */



    protected class MovieVH extends RecyclerView.ViewHolder {

        private final ImageView mPosterImg;
        private final ProgressBar mProgress;

        public MovieVH(View itemView) {
            super(itemView);
            mPosterImg = itemView.findViewById(R.id.category_product_imageview);
            mProgress = itemView.findViewById(R.id.cat_product_image_progress);
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