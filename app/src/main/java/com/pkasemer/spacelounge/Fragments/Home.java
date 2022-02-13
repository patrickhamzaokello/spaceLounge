package com.pkasemer.spacelounge.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.pkasemer.spacelounge.Adapters.HomeMenuCategoryAdapter;
import com.pkasemer.spacelounge.Adapters.HomeSectionedRecyclerViewAdapter;
import com.pkasemer.spacelounge.Adapters.HomeSliderAdapter;
import com.pkasemer.spacelounge.Apis.MovieApi;
import com.pkasemer.spacelounge.Apis.MovieService;
import com.pkasemer.spacelounge.Models.Banner;
import com.pkasemer.spacelounge.Models.HomeBannerModel;
import com.pkasemer.spacelounge.Models.HomeMenuCategoryModel;
import com.pkasemer.spacelounge.Models.HomeMenuCategoryModelResult;
import com.pkasemer.spacelounge.Models.SectionedCategoryMenu;
import com.pkasemer.spacelounge.Models.SectionedCategoryResult;
import com.pkasemer.spacelounge.R;
import com.pkasemer.spacelounge.RootActivity;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment  {

    public Home() {
        // Required empty public constructor
    }

    private static final String TAG = "MainActivity";
    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError,categorylable,category_desc;
    SliderView sliderView;
    CardView welcome_card_layout;

    RecyclerView sectionedmenurecyclerView;
    List<HomeMenuCategoryModelResult> homeMenuCategoryModelResults;
    List<SectionedCategoryResult> sectionedCategoryResults;
    List<Banner> banners;
    HomeMenuCategoryAdapter homeMenuCategoryAdapter;
    private MovieService movieService;
    private Object PaginationAdapterCallback;




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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // new
        rv = view.findViewById(R.id.home_main_recycler);
        progressBar = view.findViewById(R.id.home_main_progress);
        errorLayout = view.findViewById(R.id.error_layout);
        btnRetry = view.findViewById(R.id.error_btn_retry);
        txtError = view.findViewById(R.id.error_txt_cause);


        categorylable = view.findViewById(R.id.categorylable);
        category_desc = view.findViewById(R.id.category_desc);
        sliderView = view.findViewById(R.id.home_slider);
        welcome_card_layout = view.findViewById(R.id.welcome_card_layout);


        homeMenuCategoryAdapter = new HomeMenuCategoryAdapter(getContext());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        rv.setLayoutManager(staggeredGridLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(homeMenuCategoryAdapter);

        //init service and load data
        movieService = MovieApi.getClient(getContext()).create(MovieService.class);

        setUpHomeSectionRecyclerView(view);


        btnRetry.setOnClickListener(v -> {
            loadFirstPage();
        });


        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        loadFirstPage();

    }




    private void loadFirstPage() {

        class LoadFirstPage extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d(TAG, "loadFirstPage: ");

                // To ensure list is visible when retry button in error view is clicked
                hideErrorView();

                homeslider();
                populateHomeCategories();
                populateHomeSectionRecyclerView();
            }

            @Override
            protected String doInBackground(Void... voids) {
                return "done";
            }
        }

        LoadFirstPage ulLoadFirstPage = new LoadFirstPage();
        ulLoadFirstPage.execute();
    }

    private void populateHomeCategories() {
        callGetMenuCategoriesApi().enqueue(new Callback<HomeMenuCategoryModel>() {
            @Override
            public void onResponse(Call<HomeMenuCategoryModel> call, Response<HomeMenuCategoryModel> response) {
                hideErrorView();
                // Got data. Send it to adapter
                homeMenuCategoryModelResults = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                if (homeMenuCategoryModelResults.isEmpty()) {
                    return;
                } else {
                    homeMenuCategoryAdapter.addAll(homeMenuCategoryModelResults);
                    sliderView.setVisibility(View.VISIBLE);
                    categorylable.setVisibility(View.VISIBLE);
                    category_desc.setVisibility(View.VISIBLE);
                    welcome_card_layout.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<HomeMenuCategoryModel> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }

    //populate Sectioned view
    private void populateHomeSectionRecyclerView() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        callGetSectionedCategoriesApi().enqueue(new Callback<SectionedCategoryMenu>() {
            @Override
            public void onResponse(Call<SectionedCategoryMenu> call, Response<SectionedCategoryMenu> response) {
                hideErrorView();
                // Got data. Send it to adapter
                sectionedCategoryResults = fetchSectionedResults(response);
                progressBar.setVisibility(View.GONE);
                if (sectionedCategoryResults.isEmpty()) {
                    return;
                } else {
                    HomeSectionedRecyclerViewAdapter adapter = new HomeSectionedRecyclerViewAdapter(getContext(), sectionedCategoryResults);
                    sectionedmenurecyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<SectionedCategoryMenu> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });

    }

    public void homeslider() {

        callGetHomeBanners().enqueue(new Callback<HomeBannerModel>() {
            @Override
            public void onResponse(Call<HomeBannerModel> call, Response<HomeBannerModel> response) {
                hideErrorView();
                // Got data. Send it to adapter
                banners = fetchBannerResults(response);
                progressBar.setVisibility(View.GONE);
                if (banners.isEmpty()) {
                    return;
                } else {
                    // initializing the slider view.
                    sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                    sliderView.setScrollTimeInSec(3);
                    sliderView.setAutoCycle(true);
                    sliderView.startAutoCycle();
                    // passing this array list inside our adapter class.
                    HomeSliderAdapter adapter = new HomeSliderAdapter(getContext(), banners);
                    sliderView.setSliderAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<HomeBannerModel> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });

    }


    private List<HomeMenuCategoryModelResult> fetchResults(Response<HomeMenuCategoryModel> response) {
        HomeMenuCategoryModel homeMenuCategoryModel = response.body();
        int TOTAL_PAGES = homeMenuCategoryModel.getTotalPages();
        System.out.println("total pages cat" + TOTAL_PAGES);
        return homeMenuCategoryModel.getResults();
    }

    private List<SectionedCategoryResult> fetchSectionedResults(Response<SectionedCategoryMenu> response) {
        SectionedCategoryMenu sectionedCategoryMenu = response.body();
        int TOTAL_PAGES = sectionedCategoryMenu.getTotalPages();
        System.out.println("total pages cat" + TOTAL_PAGES);
        return sectionedCategoryMenu.getSectionedCategoryResults();
    }

    private List<Banner> fetchBannerResults(Response<HomeBannerModel> response) {
        HomeBannerModel homeBannerModel = response.body();
        int TOTAL_PAGES = homeBannerModel.getTotalPages();
        System.out.println("total pages banners" + TOTAL_PAGES);
        return homeBannerModel.getBanners();
    }


    /**
     * Performs a Retrofit call to the callGetMenuCategoriesApi API.
     * Same API call for Pagination.
     */
    private Call<HomeBannerModel> callGetHomeBanners() {
        return movieService.getHomeBanners();
    }

    private Call<HomeMenuCategoryModel> callGetMenuCategoriesApi() {
        return movieService.getMenuCategories();
    }

    private Call<SectionedCategoryMenu> callGetSectionedCategoriesApi() {
        return movieService.getMenuCategoriesSection();
    }


    private void setUpHomeSectionRecyclerView(View view) {
        sectionedmenurecyclerView = view.findViewById(R.id.menu_sectioned_recyclerView);
        sectionedmenurecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        sectionedmenurecyclerView.setLayoutManager(linearLayoutManager);
    }





    /**
     * @param throwable required for {@link #fetchErrorMessage(Throwable)}
     * @return
     */
    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));

            showCategoryErrorView();

        }
    }

    private void showCategoryErrorView() {

        progressBar.setVisibility(View.GONE);

        AlertDialog.Builder android = new AlertDialog.Builder(getContext());
        android.setTitle("No Internet Connection");
        android.setIcon(R.drawable.africanwoman);
        android.setMessage("Check your Internet Connection and  Try again.!  Error Code: Zodongo4M301.")
//                .setCancelable(false)

                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //go to activity
                        Intent intent = new Intent(getActivity(), RootActivity.class);
                        startActivity(intent);
                    }
                });

        android.create().show();

    }


    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    // Helpers -------------------------------------------------------------------------------------


    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}