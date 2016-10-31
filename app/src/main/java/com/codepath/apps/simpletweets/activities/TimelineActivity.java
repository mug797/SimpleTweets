package com.codepath.apps.simpletweets.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.simpletweets.MyDatabase;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.TwitterApplication;
import com.codepath.apps.simpletweets.TwitterClient;
import com.codepath.apps.simpletweets.adapters.DividerItemDecoration;
import com.codepath.apps.simpletweets.adapters.RecyclerViewTweetsArrayAdapter;
import com.codepath.apps.simpletweets.fragments.ComposeFragment;
import com.codepath.apps.simpletweets.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.apps.simpletweets.models.Owner;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.loopj.android.http.TextHttpResponseHandler;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import butterknife.BindView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.raizlabs.android.dbflow.config.FlowLog.Level.D;
import static com.raizlabs.android.dbflow.sql.language.SQLite.select;
import static com.raizlabs.android.dbflow.sql.language.property.PropertyFactory.from;

public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    private LinkedList<Tweet> tweets;
    private RecyclerViewTweetsArrayAdapter aTweets;
    private LinearLayoutManager layoutManager;
    public static Owner owner;

    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvTweets) RecyclerView rvTweets;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        tweets = new LinkedList<>();
        aTweets = new RecyclerViewTweetsArrayAdapter(this, tweets);
        rvTweets.setAdapter(aTweets);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvTweets.addItemDecoration(itemDecoration);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.scrollToPosition(0);
        rvTweets.setLayoutManager(layoutManager);

//        getSupportActionBar().setLogo(R.drawable.twitter_logo);

        // This instantiates DBFlow
        FlowManager.init(new FlowConfig.Builder(this).build());
        // add for verbose logging
         FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Toast.makeText(getApplicationContext(), "OnScroll : loading", Toast.LENGTH_SHORT).show();
                populateTimeline(true, false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(false, true);
            }
        });

        client = TwitterApplication.getRestClient();
        //getFromDB(); //Debugging
        populateTimeline(false, false);
    }

    public void populateTimeline(final boolean isScrolled, final boolean isRefreshed) {
        long maxid = !tweets.isEmpty() ? Long.parseLong(tweets.getLast().getIdStr()) - 1 : 1;
        long sinceid = !tweets.isEmpty() ? Long.parseLong(tweets.getFirst().getIdStr()) : 1;
        client.getHomeTimeline(maxid, sinceid, isScrolled, isRefreshed, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("DEBUG: ", + statusCode + " : " + throwable.getMessage());
            }

            //get json, deserialise it, create models, load the models into the adapter
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                List<Tweet> fetchedTweets = new LinkedList<Tweet>();
                if(response == null) {
                    Log.e("ERROR", "returned response is null");
                    return;
                }
                Gson gson = new GsonBuilder().create();
                JsonArray jsonArray = gson.fromJson(response, JsonArray.class);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonTweetObject = jsonArray.get(i).getAsJsonObject();
                        if (jsonTweetObject != null) {
                            fetchedTweets.add(Tweet.fromJSONObject(jsonTweetObject));
                        }
                    }

                    //add to list
                    if (isRefreshed) {
                        for (int i = fetchedTweets.size() - 1; i >= 0; i--) {
                            tweets.addFirst(fetchedTweets.get(i));
                        }
                    } else {
                        tweets.addAll(fetchedTweets);
                    }
                }

                //Log.d("DEBUG", tweets.toString());

                if (isScrolled) {
                    aTweets.notifyItemRangeInserted(aTweets.getItemCount(), fetchedTweets.size());
                } else if (isRefreshed) {
                    aTweets.notifyItemRangeInserted(0, fetchedTweets.size());
                    layoutManager.scrollToPosition(0);
                    swipeRefreshLayout.setRefreshing(false); //remove the symbol
                } else {
                    aTweets.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void addNewComposedTweet(Tweet tweet){
        tweets.addFirst(tweet);
        aTweets.notifyItemRangeInserted(0,1);
        layoutManager.scrollToPosition(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.miComposeTweet:
                triggerComposeTweet();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void triggerComposeTweet() {
        getOwnerDetails();
//            getComposeFragment();
    }

    void getComposeFragment(){
        ComposeFragment composeFragment = new ComposeFragment();
        composeFragment.show(getSupportFragmentManager(), "composeTweet");
    }

    boolean getOwnerDetails(){
        client.getUserTimeline(new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("ERROR", "triggerComposTweet : "  + statusCode + " : " + throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(responseString == null) {
                    Log.e("ERROR", "returned response is null");
                    //should compose tweet frag not be calleD?
                    return;
                }
                Gson gson = new GsonBuilder().create();
                JsonArray jsonArray = gson.fromJson(responseString, JsonArray.class);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jObject = null;
                        try {
                            jObject = jsonArray.get(i).getAsJsonObject();
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        }
                        if (jObject.has("user")) {
                            JsonObject userJObject = null;
                            try {
                                userJObject = jObject.get("user").getAsJsonObject();
                            } catch (JsonParseException e) {
                                e.printStackTrace();
                            }
                            owner = Owner.fromJSONObject(userJObject);
                            if (!TextUtils.isEmpty(owner.getOwnerName())
                                    && !TextUtils.isEmpty(owner.getOwnerProfileImageUrl())
                                    && !TextUtils.isEmpty(owner.getOwnerTwitterHandle())) {
                                break;
                            }
                        }
                    }
                }
                getComposeFragment();
            }
        });
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("DEBUGG", "delete older enteries and update the db.");
        deleteFromDB();
        addToDB();
    }

    public void getFromDB() {
        List<User> usersFromDB = SQLite.select().from(User.class).queryList();
        List<Tweet> tweetsFromDB = SQLite.select().from(Tweet.class).queryList();

        Map<Long, User> userMap = new HashMap<>();

        if (usersFromDB != null) {
            for (User user : usersFromDB) {
                userMap.put(user.getUid(), user);
            }
        }

        if (tweetsFromDB != null) {
            for (Tweet tweet : tweetsFromDB) {
                if (userMap.containsKey(tweet.getUser_id())) {
                    tweet.setUser(userMap.get(tweet.getUser_id()));
                }
                tweets.add(tweet);
            }
            aTweets.notifyDataSetChanged();
        }
    }

    public void deleteFromDB() {
        Delete.tables(User.class, Tweet.class);
    }

    public void addToDB(){
        List<User> usersFromDB = SQLite.select().from(User.class).queryList();
        Set<String> userIds = new HashSet<>();

        for (User user: usersFromDB) {
            userIds.add(user.getUid_string());
        }

        for (Tweet tweet : tweets) {
            if (!userIds.contains(tweet.getUser().getUid_string())) {
                tweet.getUser().save();
                userIds.add(tweet.getUser().getUid_string());
            }
            tweet.save();
        }
    }

    public void addToDB_notWorking(){
        List<User> usersFromDB = SQLite.select().from(User.class).queryList();

        FlowManager.getDatabase(MyDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<User>() {
                            @Override
                            public void processModel(User u) {
                                List<User> usersFromDB = SQLite.select().from(User.class).queryList();
                                Set<String> userIds = new HashSet<>();

                                for (User user: usersFromDB) {
                                    userIds.add(user.getUid_string());
                                }

                                for (Tweet tweet : tweets) {
                                    if (!userIds.contains(tweet.getUser().getUid_string())) {
                                        tweet.getUser().save();
                                        userIds.add(tweet.getUser().getUid_string());
                                    }
                                    tweet.save();
                                }
                            }
                        }).addAll().build())  // add elements (can also handle multiple)
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {

                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {

                    }
                }).build().execute();
    }
}