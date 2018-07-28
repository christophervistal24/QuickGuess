package com.example.michellebiol.sampleapp.Models;

public class CategoriesItem {

    private String head;
    private String desc;
    private String id;


    public CategoriesItem(String id , String head , String desc)
    {
        this.head = head;
        this.desc = desc;
        this.id = id;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }

    public String getId() {
        return id;
    }
}
