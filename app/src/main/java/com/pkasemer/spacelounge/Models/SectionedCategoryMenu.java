
package com.pkasemer.spacelounge.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class SectionedCategoryMenu {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("sectioned_category_results")
    @Expose
    private List<SectionedCategoryResult> sectionedCategoryResults = null;
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

    public List<SectionedCategoryResult> getSectionedCategoryResults() {
        return sectionedCategoryResults;
    }

    public void setSectionedCategoryResults(List<SectionedCategoryResult> sectionedCategoryResults) {
        this.sectionedCategoryResults = sectionedCategoryResults;
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
