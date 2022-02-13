package com.pkasemer.spacelounge.Models;

public class News {
    private final String newsTitle;
    private final String news;

    public News(String newsTitle, String news) {
        this.newsTitle = newsTitle;
        this.news = news;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNews() {
        return news;
    }
}
