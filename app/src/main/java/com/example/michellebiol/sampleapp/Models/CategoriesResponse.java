package com.example.michellebiol.sampleapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoriesResponse {

  private String id;
  private String categories;
  private String category_description;

    public CategoriesResponse(String id, String categories, String category_description) {
        this.id = id;
        this.categories = categories;
        this.category_description = category_description;
    }

    public String getId() {
        return id;
    }

    public String getCategories() {
        return categories;
    }

    public String getCategories_description() {
        return category_description;
    }
}
