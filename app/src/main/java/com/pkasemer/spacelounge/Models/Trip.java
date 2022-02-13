package com.pkasemer.spacelounge.Models;

public class Trip {
    private final int tripImage;
    private final String tripTitle;
    private final String trip;

    public Trip(int tripImage, String tripTitle, String trip) {
        this.tripImage = tripImage;
        this.tripTitle = tripTitle;
        this.trip = trip;
    }

    public int getTripImage() {
        return tripImage;
    }

    public String getTripTitle() {
        return tripTitle;
    }

    public String getTrip() {
        return trip;
    }
}
