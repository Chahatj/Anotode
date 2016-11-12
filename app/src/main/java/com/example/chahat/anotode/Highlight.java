package com.example.chahat.anotode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 3/9/16.
 */
public class Highlight {
    private String id,time,url,title,notedtext,comment,tag1,tag2,tag3,tag4,category;


    public Highlight() {
    }

    public Highlight(String id,String time,String url,String title,String notedtext,String comment,String tag1,String tag2,String tag3,String tag4,String category) {
       this.id= id;
        this.title = title;
        this.time = time;
        this.url = url;
        this.notedtext=notedtext;
        this.category=category;
        this.comment = comment;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.tag4 = tag4;
    }
    public Highlight(String id,String title,String notedtext,String tag1,String tag2,String tag3,String tag4, String category) {
        this.id = id;
        this.title = title;
        this.notedtext=notedtext;
        this.category=category;
        this.tag1 = tag1;
        this.tag2 = tag2;
        this.tag3 = tag3;
        this.tag4 = tag4;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTag1() {
        return tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public String getTag4() {
        return tag4;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setNotedtext(String notedtext) {
        this.notedtext = notedtext;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public void setTag4(String tag4) {
        this.tag4 = tag4;
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