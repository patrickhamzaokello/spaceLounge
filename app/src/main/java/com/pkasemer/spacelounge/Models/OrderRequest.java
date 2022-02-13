
package com.pkasemer.spacelounge.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class OrderRequest {

    @SerializedName("order_address")
    @Expose
    private String orderAddress;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("processed_by")
    @Expose
    private String processedBy;
    @SerializedName("orderItemList")
    @Expose
    private List<FoodDBModel> orderItemList = null;

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    public List<FoodDBModel> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<FoodDBModel> orderItemList) {
        this.orderItemList = orderItemList;
    }

}
