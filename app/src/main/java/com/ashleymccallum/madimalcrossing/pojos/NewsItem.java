package com.ashleymccallum.madimalcrossing.pojos;

/**
 * NewsItem class
 * Represents a news result from the API
 */
public class NewsItem {
    private String publisherName;
    private String authorName;
    private String title;
    private String description;
    private String articleURL;
    private String imgURL;
    private String timestamp;
    private int lastUpdated;

    public NewsItem(String publisherName, String authorName, String title, String description, String articleURL, String imgURL, String timestamp, int lastUpdated) {
        this.publisherName = publisherName;
        this.authorName = authorName;
        this.title = title;
        this.description = description;
        this.articleURL = articleURL;
        this.imgURL = imgURL;
        this.timestamp = timestamp;
        this.lastUpdated = lastUpdated;
    }

    public NewsItem() {
    }

    public String getPublisherName() {
        return publisherName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getArticleURL() {
        return articleURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getLastUpdated() {
        return lastUpdated;
    }
}
