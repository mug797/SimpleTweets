package com.codepath.apps.simpletweets.utils;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/**
 * Created by mkhade on 10/25/2016.
 */

public final class Constants {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
    public static final String API_URL = "statuses/home_timeline.json"; // Change this, base API URL

    //my account
    //public static final String REST_CONSUMER_KEY = "IeNqK8GqcBOnY9haOlu0FeYEF";       // Change this
    //public static final String REST_CONSUMER_SECRET = "ggIWARlMM7k3F5bLquiKSQgqpC5E2fA7SUQOGPRFLXPFY297Ic"; // Change this

    //jane797doe
    public static final String REST_CONSUMER_KEY = "rfxCHjWcEGYMsGMOA1XJwU0EW";       // Change this
    public static final String REST_CONSUMER_SECRET = "cOHsM0OgJtu8JWW6SwJ10Ka3xU8KVrybYk7bit3m7wzzZi0JaU"; // Change this

    public static final String REST_CALLBACK_URL = "oauth://simpletweets"; // Change this (here and in manifest)
    public static final long NUM_TWEETS = 50;

    private Constants() {}

}
