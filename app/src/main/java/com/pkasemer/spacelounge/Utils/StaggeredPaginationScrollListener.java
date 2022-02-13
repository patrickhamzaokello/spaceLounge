
package com.pkasemer.spacelounge.Utils;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public abstract class StaggeredPaginationScrollListener extends RecyclerView.OnScrollListener {

    StaggeredGridLayoutManager layoutManager;
    private int pastVisibleItems;

    public StaggeredPaginationScrollListener(StaggeredGridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int[] firstVisibleItemPosition = null;
        firstVisibleItemPosition = layoutManager.findFirstVisibleItemPositions(firstVisibleItemPosition);


        if(firstVisibleItemPosition != null && firstVisibleItemPosition.length > 0) {
            pastVisibleItems = firstVisibleItemPosition[0];
        }

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                loadMoreItems();
                Log.d("tag", "LOAD NEXT ITEM");
            }
        }
    }

    protected abstract void loadMoreItems();
    public abstract int getTotalPageCount();
    public abstract boolean isLastPage();
    public abstract boolean isLoading();
}