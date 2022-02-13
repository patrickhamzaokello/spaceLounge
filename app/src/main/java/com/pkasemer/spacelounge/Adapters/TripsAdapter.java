package com.pkasemer.spacelounge.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkasemer.spacelounge.Models.Ads;
import com.pkasemer.spacelounge.Models.Item;
import com.pkasemer.spacelounge.Models.News;
import com.pkasemer.spacelounge.Models.Trip;
import com.pkasemer.spacelounge.R;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private final List<Item> items;

    public TripsAdapter(List<Item> items){
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         // here types are: 0 trips,1ads,2news

        if(viewType == 0){
            return new TripViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_container_trip,
                            parent,
                            false
                    )
            );
        } else if(viewType == 1){
            return new AdsViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_container_ads,
                            parent,
                            false
                    )
            );
        }else {
            return new NewsViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.item_container_news,
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == 0){
            Trip trip = (Trip) items.get(position).getObject();
            ((TripViewHolder) holder).setTripData(trip);
        }else if(getItemViewType(position) == 1){
            Ads ads = (Ads) items.get(position).getObject();
            ((AdsViewHolder) holder).setAdsData(ads);
        } else {
            News news = (News) items.get(position).getObject();
            ((NewsViewHolder) holder).setNewsData(news);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position){
        return  items.get(position).getType();
    }


    static  class SliderViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageTrip;
        private final TextView textTripTitle;
        private final TextView textTrip;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTrip = itemView.findViewById(R.id.imageTrip);
            textTripTitle = itemView.findViewById(R.id.textTripTitle);
            textTrip = itemView.findViewById(R.id.textTrip);
        }

        void setSliderData(Trip trip){
            imageTrip.setImageResource(trip.getTripImage());
            textTripTitle.setText(trip.getTripTitle());
            textTrip.setText(trip.getTrip());
        }
    }



    static  class TripViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageTrip;
        private final TextView textTripTitle;
        private final TextView textTrip;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTrip = itemView.findViewById(R.id.imageTrip);
            textTripTitle = itemView.findViewById(R.id.textTripTitle);
            textTrip = itemView.findViewById(R.id.textTrip);
        }

        void setTripData(Trip trip){
            imageTrip.setImageResource(trip.getTripImage());
            textTripTitle.setText(trip.getTripTitle());
            textTrip.setText(trip.getTrip());
        }
    }

    static class AdsViewHolder extends RecyclerView.ViewHolder{
        private final TextView textAdsTitle;
        private final TextView textAds;


        public AdsViewHolder(@NonNull View itemView) {
            super(itemView);
            textAdsTitle = itemView.findViewById(R.id.textAdsTitle);
            textAds = itemView.findViewById(R.id.textAds);
        }

        void setAdsData(Ads ads){
            textAdsTitle.setText(ads.getAdsTitle());
            textAds.setText(ads.getAds());
        }


    }

    static class NewsViewHolder extends RecyclerView.ViewHolder{
        private final TextView textnewsTitle;
        private final TextView textNews;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            textnewsTitle = itemView.findViewById(R.id.textnewsTitle);
            textNews = itemView.findViewById(R.id.textNews);
        }

        void setNewsData(News news){
            textnewsTitle.setText(news.getNewsTitle());
            textNews.setText(news.getNews());
        }
    }
}
