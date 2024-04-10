package com.example.appbao2.models;

import java.io.Serializable;

public class NewsHeadlines implements Serializable {
    Source source = null;
    private String title;
    private String author;
    private String publishedAt;
    private String description;
    private String content;
    private String urlToImage;
    private String userId;

    public NewsHeadlines() {
        // Default constructor required for Firebase
    }

    public NewsHeadlines(String title, String author, String publishedAt, String description, String content, String urlToImage, String userId) {
        this.title = title;
        this.author = author;
        this.publishedAt = publishedAt;
        this.description = description;
        this.content = content;
        this.urlToImage = urlToImage;

    }


    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "NewsHeadline{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                '}';
    }


}
