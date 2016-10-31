package com.codepath.apps.simpletweets.models;

import com.codepath.apps.simpletweets.MyDatabase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.Model;

/**
 * Created by mkhade on 10/26/2016.
 */

@Table(database = MyDatabase.class, name="Users")
public class User extends BaseModel {

    @Column
    private String name;

    @PrimaryKey
    @Column
    private long uid;

    @Column
    private String screenName;

    @Column
    private String profileImageUrl;

    @Column
    private String uid_string;

    public static User fromJson(JsonObject jsonObject) {

        User user = new User();
        try {
            user.name = jsonObject.get("name").getAsString();
            user.uid = jsonObject.get("id").getAsLong();
            user.screenName = jsonObject.get("screen_name").getAsString();
            user.profileImageUrl = jsonObject.get("profile_image_url").getAsString();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setUid_string(String uid_string) {
        this.uid_string = uid_string;
    }

    public String getUid_string() { return uid_string; }

}
