package com.codepath.apps.simpletweets.models;

import android.text.format.DateUtils;

import com.codepath.apps.simpletweets.MyDatabase;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by mkhade on 10/26/2016.
 */

@Table(database = MyDatabase.class, name="Tweets")
public class Tweet extends BaseModel{

    //@PrimaryKey
    //@Column
    //private long uid;

    @Column
    private String body;

    //@Column
    private User user;

    @Column
    private String createdAt;

    @Column
    private String idStr;

    @PrimaryKey
    @Column
    private  long user_id;


    public static Tweet fromJSONObject(JsonObject jobj){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jobj.get("text").getAsString();
            //tweet.uid = jobj.get("id").getAsLong();
            tweet.createdAt = jobj.get("created_at").getAsString();
            if (jobj.has("user")) {
                User newUser = User.fromJson(jobj.get("user").getAsJsonObject());
                tweet.user = newUser;
                tweet.setUser_id(newUser.getUid());
            }
            tweet.idStr = jobj.get("id_str").getAsString();
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return tweet;
    }

/*
    public static List<Tweet> fromJSONArray(JSONArray jsonArray){
        List<Tweet> tweets = new LinkedList<>();

        for (int i = 0; i < jsonArray.length(); i++){
            try {
                JsonObject tweetJson = jsonArray.getJ(i);
                Tweet tweet = Tweet.fromJSONObject(tweetJson);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
*/


    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getBody() {
        return body;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser(){
        return this.user;
    }

    public void setBody(String body) {
        this.body = body;
    }

/*
    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
*/

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getIdStr(){ return idStr; }


}
