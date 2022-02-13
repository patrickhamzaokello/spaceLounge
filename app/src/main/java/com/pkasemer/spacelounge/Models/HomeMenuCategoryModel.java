
package com.pkasemer.spacelounge.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class HomeMenuCategoryModel {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<HomeMenuCategoryModelResult> homeMenuCategoryModelResults = null;
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

    public List<HomeMenuCategoryModelResult> getResults() {
        return homeMenuCategoryModelResults;
    }

    public void setResults(List<HomeMenuCategoryModelResult> homeMenuCategoryModelResults) {
        this.homeMenuCategoryModelResults = homeMenuCategoryModelResults;
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
