
package com.pkasemer.spacelounge.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SelectedCategoryMenuItem {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<SelectedCategoryMenuItemResult> selectedCategoryMenuItemResults = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<SelectedCategoryMenuItemResult> getResults() {
        return selectedCategoryMenuItemResults;
    }

    public void setResults(List<SelectedCategoryMenuItemResult> selectedCategoryMenuItemResults) {
        this.selectedCategoryMenuItemResults = selectedCategoryMenuItemResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

}
