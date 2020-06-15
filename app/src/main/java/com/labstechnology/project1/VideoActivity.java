package com.labstechnology.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.labstechnology.project1.models.LiveEvent;

public class VideoActivity extends YouTubeBaseActivity {
    private static final String TAG = "VideoActivity";

    private YouTubePlayerView youTubePlayerView;
    private LiveEvent incomingEvent;
    private String videoId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        youTubePlayerView = findViewById(R.id.youtubePlayer);


        try {
            Intent intent = getIntent();
            incomingEvent = intent.getParcelableExtra("incomingLiveEvent");
            String[] splitedUrl = incomingEvent.getUrl().split("/");
            videoId = splitedUrl[splitedUrl.length - 1];
            Log.d(TAG, "onCreate: Video Id" + videoId);
            youTubePlayerView.initialize(getString(R.string.google_developer_api_key), new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    youTubePlayer.loadVideo(videoId); // your video id
                    youTubePlayer.play();
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }
}