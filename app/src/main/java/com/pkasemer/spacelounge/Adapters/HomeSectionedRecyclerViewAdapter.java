package com.pkasemer.spacelounge.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pkasemer.spacelounge.Models.SectionedCategoryResult;
import com.pkasemer.spacelounge.MySelectedCategory;
import com.pkasemer.spacelounge.R;
import com.pkasemer.spacelounge.RootActivity;

import java.util.List;

public class HomeSectionedRecyclerViewAdapter extends RecyclerView.Adapter<HomeSectionedRecyclerViewAdapter.SectionViewHolder> {


    class SectionViewHolder extends RecyclerView.ViewHolder {
        private final TextView sectionLabel;
        private final TextView showAllButton;
        private final RecyclerView itemRecyclerView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            sectionLabel = itemView.findViewById(R.id.section_label);
            showAllButton = itemView.findViewById(R.id.section_show_all_button);
            itemRecyclerView = itemView.findViewById(R.id.item_recycler_view);
        }
    }

    private final Context context;
    List<SectionedCategoryResult> sectionedCategoryResults;

    public HomeSectionedRecyclerViewAdapter(Context context, List<SectionedCategoryResult> sectionedCategoryResults) {
        this.context = context;
        this.sectionedCategoryResults = sectionedCategoryResults;
    }

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section_custom_row_layout, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        final SectionedCategoryResult sectionedCategoryResult = sectionedCategoryResults.get(position);
        holder.sectionLabel.setText(sectionedCategoryResult.getName());

        //recycler view for items
//        holder.itemRecyclerView.setHasFixedSize(true);
        holder.itemRecyclerView.setNestedScrollingEnabled(false);

        /* set layout manager on basis of recyclerview enum type */
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        holder.itemRecyclerView.setLayoutManager(gridLayoutManager);


        HomeSectionedRecyclerViewItemAdapter adapter = new HomeSectionedRecyclerViewItemAdapter(context, sectionedCategoryResult.getSectionedMenuItems());
        holder.itemRecyclerView.setAdapter(adapter);

        //show toast on click of show all button
        holder.showAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context.getApplicationContext(), MySelectedCategory.class);
                //PACK DATA
                i.putExtra("SENDER_KEY", "MyFragment");
                i.putExtra("category_selected_key", sectionedCategoryResult.getId());
                context.startActivity(i);



            }
        });

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

    @Override
    public int getItemCount() {
        return sectionedCategoryResults.size();
    }


}