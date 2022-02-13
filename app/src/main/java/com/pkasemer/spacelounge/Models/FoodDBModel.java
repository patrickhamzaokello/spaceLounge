package com.pkasemer.spacelounge.Models;


public class FoodDBModel {

    private Integer menuId;

    private String menuName;

    private Integer price;

    private String description;

    private Integer menuTypeId;

    private String menuImage;

    private String backgroundImage;

    private String ingredients;

    private Integer menuStatus;

    private String created;

    private String modified;

    private Integer rating;

    private Integer quantity;

    private Integer orderstatus;

    public FoodDBModel(Integer menuId, String menuName, Integer price, String description, Integer menuTypeId, String menuImage, String backgroundImage, String ingredients, Integer menuStatus, String created, String modified, Integer rating, Integer quantity, Integer orderstatus) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.price = price;
        this.description = description;
        this.menuTypeId = menuTypeId;
        this.menuImage = menuImage;
        this.backgroundImage = backgroundImage;
        this.ingredients = ingredients;
        this.menuStatus = menuStatus;
        this.created = created;
        this.modified = modified;
        this.rating = rating;
        this.quantity = quantity;
        this.orderstatus = orderstatus;
    }


    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMenuTypeId() {
        return menuTypeId;
    }

    public void setMenuTypeId(Integer menuTypeId) {
        this.menuTypeId = menuTypeId;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Integer getMenuStatus() {
        return menuStatus;
    }

    public void setMenuStatus(Integer menuStatus) {
        this.menuStatus = menuStatus;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(Integer orderstatus) {
        this.orderstatus = orderstatus;
    }

}
