package com.pkasemer.spacelounge.Apis;

import com.pkasemer.spacelounge.Models.HomeBannerModel;
import com.pkasemer.spacelounge.Models.HomeMenuCategoryModel;
import com.pkasemer.spacelounge.Models.OrderRequest;
import com.pkasemer.spacelounge.Models.OrderResponse;
import com.pkasemer.spacelounge.Models.SectionedCategoryMenu;
import com.pkasemer.spacelounge.Models.SelectedCategoryMenuItem;
import com.pkasemer.spacelounge.Models.UserOrders;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface MovieService {

    @GET("menus/menupages.php")
    Call<SelectedCategoryMenuItem> getTopRatedMovies(
            @Query("category") int menu_category_id,
            @Query("page") int pageIndex
    );

    @GET("menus/topmenuitems.php")
    Call<SelectedCategoryMenuItem> getTopMenuItems(
            @Query("page") int pageIndex
    );

    @GET("menucategory/readPaginated.php")
    Call<HomeMenuCategoryModel> getMenuCategories();

    @GET("menucategory/readSectionedMenu.php")
    Call<SectionedCategoryMenu> getMenuCategoriesSection();

    @GET("banner/read.php")
    Call<HomeBannerModel> getHomeBanners();

//    menus/menudetails.php?menuId=9&category=3&page=1
    @GET("menus/menudetails.php")
    Call<SelectedCategoryMenuItem> getMenuDetails(
            @Query("menuId") int menu_id,
            @Query("category") int menu_category_id,
            @Query("page") int pageIndex
    );


    @POST("orders/create_order.php")
    Call<OrderResponse> postCartOrder(
            @Body OrderRequest orderRequest
    );



    //fetch past orders
    @GET("orders/userOrders.php")
    Call<UserOrders> getUserOrders(
            @Query("customerId") int customerID,
            @Query("page") int pageIndex
    );



}