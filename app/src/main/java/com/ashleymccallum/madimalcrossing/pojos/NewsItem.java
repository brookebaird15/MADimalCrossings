package com.ashleymccallum.madimalcrossing.pojos;

/**
 * NewsItem class
 * Represents a news result from the API
 */
public class NewsItem {
    private final String publisherName;
    private final String authorName;
    private final String title;
    private final String description;
    private final String articleURL;
    private final String imgURL;
    private final String timestamp;
    private final long lastUpdated;

    public NewsItem(String title, String authorName, String publisherName, String description, String articleURL, String imgURL, String timestamp, long lastUpdated) {
        this.publisherName = publisherName;
        this.authorName = authorName;
        this.title = title;
        this.description = description;
        this.articleURL = articleURL;
        this.imgURL = imgURL;
        this.timestamp = timestamp;
        this.lastUpdated = lastUpdated;
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

    public long getLastUpdated() {
        return lastUpdated;
    }
}
