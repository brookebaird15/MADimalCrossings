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

    public NewsItem(String publisherName, String authorName, String title, String description, String articleURL, String imgURL, String timestamp) {
        this.publisherName = publisherName;
        this.authorName = authorName;
        this.title = title;
        this.description = description;
        this.articleURL = articleURL;
        this.imgURL = imgURL;
        this.timestamp = timestamp;
    }

    public NewsItem() {
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticleURL() {
        return articleURL;
    }

    public void setArticleURL(String articleURL) {
        this.articleURL = articleURL;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
