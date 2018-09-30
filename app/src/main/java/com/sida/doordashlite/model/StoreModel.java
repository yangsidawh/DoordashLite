package com.sida.doordashlite.model;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class StoreModel {

    private int id;

    private String name;

    private String description;

    private String coverImageUrl;

    private String status;

    private String tagString;

    private List<String> tags;

    private int deliveryFee;

    public StoreModel(int _id, @NonNull String _name, String _description, String _imageUrl, String _status, int _fee, List<String> _tags) {
       id = _id;
       name = _name;
       description = _description;
       coverImageUrl = _imageUrl;
       status = _status;
       deliveryFee = _fee;
       tags = _tags;

       StringBuilder sb = new StringBuilder();
       for (String tag: tags) {
           sb.append(tag);
           sb.append(", ");
       }
       if (sb.length() > 0) {
           sb.setLength(sb.length() - 2);
       }
       tagString = sb.toString();
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public String getStatus() {
        return status;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public String getTagString() {
        return tagString;
    }
}
