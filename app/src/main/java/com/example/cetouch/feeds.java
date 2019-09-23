package com.example.cetouch;

public class feeds {

    public feeds(){}


    public feeds(String id,String title,String content, String image,String department, String object)
    {
        feed_id=id;
        feed_title=title;
        feed_content=content;
        feed_image=image;
        feed_department=department;
        feed_object=object;
    }

    private String feed_id;
    private String feed_title;
    private String feed_content;
    private String feed_department;
    private String feed_image;

    public void setFeed_department(String feed_department) {
        this.feed_department = feed_department;
    }

    public String getFeed_department() {
        return feed_department;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public void setFeed_title(String feed_title) {
        this.feed_title = feed_title;
    }

    public void setFeed_content(String feed_content) {
        this.feed_content = feed_content;
    }

    public void setFeed_image(String feed_image) {
        this.feed_image = feed_image;
    }

    public void setFeed_object(String feed_object) {
        this.feed_object = feed_object;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public String getFeed_title() {
        return feed_title;
    }

    public String getFeed_content() {
        return feed_content;
    }

    public String getFeed_image() {
        return feed_image;
    }

    public String getFeed_object() {
        return feed_object;
    }

    private String feed_object;


}