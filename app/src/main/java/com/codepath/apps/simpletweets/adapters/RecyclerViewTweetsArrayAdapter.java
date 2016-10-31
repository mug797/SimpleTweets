package com.codepath.apps.simpletweets.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.codepath.apps.simpletweets.utils.HelperFunctions;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by mkhade on 10/26/2016.
 */

public class RecyclerViewTweetsArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static List<Tweet> tweets;
    private static Context context;

    //Do not use enums when int can suffice!
    private final int TWEET_NO_LINKS = 0;
    private final int TWEET_WITH_LINKS = 1;

    public RecyclerViewTweetsArrayAdapter(Context context, List<Tweet> tweets){
        this.tweets = tweets;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v1 = inflater.inflate(R.layout.item_tweet, parent, false);
        viewHolder = new TweetViewHolder(context, v1, tweets);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TweetViewHolder vh = (TweetViewHolder) holder;
        Tweet tweet = tweets.get(position);
        vh.tvUserName.setText(tweet.getUser().getName());
        vh.tvBody.setText(tweet.getBody());
        Glide.with(context).load(tweet.getUser().getProfileImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, 10, 0))
                .override(150, 150).into(vh.ivProfileImageView);
        vh.tvTwitterHandle.setText("@"+tweet.getUser().getScreenName());
        vh.tvRelativeTime.setText(HelperFunctions.getRelativeTimeAgo(tweet.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return this.tweets.size();
    }

   /* @Override
    public int getItemViewType(int position) {
        Tweet tweet = tweets.get(position);

        //Get photo from media
        if (tweet.getMedia_type().equals("photo")){
            return TWEET_NO_LINKS;
        }
        return TWEET_WITH_LINKS;
    }*/
}
