package com.codepath.apps.simpletweets.models;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONException;

/**
 * Created by mkhade on 10/27/2016.
 */

public class Owner {

    private String ownerTwitterHandle;
    private String ownerName;
    private String ownerProfileImageUrl;

    private Owner(){}

    public static Owner fromJSONObject(JsonObject jsonObject){
        Owner owner = new Owner();
        if (jsonObject == null) {
            Log.e("ERROR!!", "Cannot be null");
        }
        try {
            if (jsonObject.has("name"))
                owner.setOwnerName(jsonObject.get("name").getAsString());
            if (jsonObject.has("screen_name"))
                owner.setOwnerName(jsonObject.get("screen_name").getAsString());
            if (jsonObject.has("profile_image_url"))
                    owner.setOwnerProfileImageUrl(jsonObject.get("profile_image_url").getAsString());
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return owner;
    }

    public String getOwnerTwitterHandle() {
        return ownerTwitterHandle;
    }

    public void setOwnerTwitterHandle(String ownerTwitterHandle) {
        this.ownerTwitterHandle = ownerTwitterHandle;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerProfileImageUrl() {
        return ownerProfileImageUrl;
    }

    public void setOwnerProfileImageUrl(String ownerProfileImageUrl) {
        this.ownerProfileImageUrl = ownerProfileImageUrl;
    }
}
