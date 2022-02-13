package com.pkasemer.spacelounge.Utils;

import com.pkasemer.spacelounge.Models.FoodDBModel;
import com.pkasemer.spacelounge.Models.SelectedCategoryMenuItemResult;

public interface MenuDetailListener {
    void retryPageLoad();
    void incrementqtn(int qty, FoodDBModel foodDBModel);
    void decrementqtn(int qty, FoodDBModel foodDBModel);

    void addToCartbtn(SelectedCategoryMenuItemResult selectedCategoryMenuItemResult);
    void orderNowMenuBtn(SelectedCategoryMenuItemResult selectedCategoryMenuItemResult);

}
