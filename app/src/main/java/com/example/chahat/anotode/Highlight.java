package com.example.chahat.anotode;

/**
 * Created by chahat on 3/9/16.
 */
public class Highlight {
    private String time,url,title,notedtext,comment,tag,category;

    public Highlight() {
    }

    public Highlight(String time,String url,String title,String notedtext,String comment, String tag, String category) {
        this.title = title;
        this.time = time;
        this.url = url;
        this.notedtext=notedtext;
        this.category=category;
        this.comment = comment;
        this.tag = tag;
    }
    public Highlight(String title,String notedtext, String tag, String category) {
        this.title = title;
        this.notedtext=notedtext;
        this.category=category;
        this.tag = tag;
    }



    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getNotedtext() {
        return notedtext;
    }

    public String getComment() {
        return comment;
    }

    public String getCategory() {
        return category;
    }

    public String getTag() {
        return tag;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNotedtext(String notedtext) {
        this.notedtext = notedtext;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}