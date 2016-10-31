package com.codepath.apps.simpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.simpletweets.R;
import com.codepath.apps.simpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.media.CamcorderProfile.get;
import static com.squareup.picasso.Picasso.with;

/**
 * Created by mkhade on 10/26/2016.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private List<Tweet> tweets;
    private Context context;

    @BindView(R.id.tvUserName) TextView tvUserName;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.ivProfileImage) ImageView ivProfileImageView;
    @BindView(R.id.tvTwitterHandle) TextView tvTwitterHandle;
    @BindView(R.id.tvRelativeTime) TextView tvRelativeTime;


    public TweetViewHolder(Context context, View view, List<Tweet> tweets) {
        super(view);
        this.tweets = tweets;
        this.context = context;
        view.setOnClickListener(this);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "CLICKED", Toast.LENGTH_SHORT).show();
    }
}