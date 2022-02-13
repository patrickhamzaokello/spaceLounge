package com.pkasemer.spacelounge.Models;

public class Ads {

    private final String adsTitle;
    private final String ads;

    public Ads(String adsTitle, String ads) {
        this.adsTitle = adsTitle;
        this.ads = ads;
    }

    public String getAdsTitle() {
        return adsTitle;
    }

    public String getAds() {
        return ads;
    }
}
