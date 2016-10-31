package com.codepath.apps.simpletweets;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;

import android.content.Context;

import com.codepath.apps.simpletweets.utils.Constants;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {

	public TwitterClient(Context context) {
			super(context, Constants.REST_API_CLASS, Constants.REST_URL,
                    Constants.REST_CONSUMER_KEY, Constants.REST_CONSUMER_SECRET,
                    Constants.REST_CALLBACK_URL);
	}

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

    //max_id Returns results with an ID less than (that is, older than) or equal to the specified ID.
    public void getHomeTimeline(long maxId, long sinceId, boolean isScrolled, boolean isRefreshed,  AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();

        if(isScrolled) {
            params.put("max_id", maxId);
            params.put("count", Constants.NUM_TWEETS);
        } else if (isRefreshed) {
            params.put("since_id", sinceId);
        } else {
            params.put("count", Constants.NUM_TWEETS);
            params.put("since_id", 1);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getHomeTimeline_short(AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();

        params.put("count", Constants.NUM_TWEETS);
        params.put("since_id", 1);
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", Constants.NUM_TWEETS);
        params.put("since_id", 1); //get latest tweets
        getClient().get(apiUrl, params, handler);
    }

    public void composeTweet(String tweet, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet);
		getClient().post(apiUrl, params, handler);
    }
}