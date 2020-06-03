package com.artsiom_homan.youtubeplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String GOOGLE_API_KEY = "PASTE GOOGLE API KEY HERE";
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSingle = findViewById(R.id.btnPlayVideo);
        Button btnStandalone = findViewById(R.id.btnPlayList);


        btnSingle.setOnClickListener(this);
        btnStandalone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        EditText link = findViewById(R.id.editText);
        String text_link = link.getText().toString();

        if (text_link.equals("")) {
            Toast.makeText(this, "You didn't enter the link!", Toast.LENGTH_LONG).show();
        }

        String pattern = "^(http(s)?://)?((w){3}.)?youtu(be.com|.be)?/.+";

        if (text_link.matches(pattern)) {
            String video_id;
            String playlist_id;
            try {
                boolean isPlaylist = text_link.contains("list="); // true
                boolean isIndex = text_link.contains("&index"); // true
                boolean isMobileVideo = text_link.contains("youtu.be"); // true
                boolean isVideo = text_link.contains("?v="); // true

                Intent intent = null;

                switch (view.getId()) {
                    case R.id.btnPlayVideo:
                        if (isMobileVideo) {
                            video_id = text_link.substring(text_link.indexOf("youtu.be") + 9);
                            intent = YouTubeStandalonePlayer.createVideoIntent(MainActivity.this, GOOGLE_API_KEY, video_id, 0, true, true);
                        }
                        if (isVideo) {
                            video_id = text_link.substring(text_link.indexOf("v=") + 2);
                            video_id = video_id.substring(0, 11);
                            intent = YouTubeStandalonePlayer.createVideoIntent(MainActivity.this, GOOGLE_API_KEY, video_id, 0, true, true);
                        } else {
                            Toast.makeText(this, "Your link is not a valid video link!", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.btnPlayList:
                        if (isPlaylist) {
                            int index = 0;
                            playlist_id = text_link.substring(text_link.indexOf("list=") + 5);
                            if (isIndex) {
                                playlist_id = playlist_id.substring(0, playlist_id.indexOf("&index"));
                                index = Integer.parseInt(text_link.substring(text_link.length() - 1));
                            }
                            intent = YouTubeStandalonePlayer.createPlaylistIntent(MainActivity.this, GOOGLE_API_KEY, playlist_id, index - 1, 0, true, true);
                        } else {
                            Toast.makeText(this, "Your link is not a valid playlist link!", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                }
                if (intent != null) {
                    startActivity(intent);
                }
            } catch (Exception e) {
                Log.e(TAG, "onClick: " + e.getMessage());
            }
        } else {
            // Not Valid youtube URL
            Toast.makeText(this, "Your link is not youtube link!", Toast.LENGTH_LONG).show();
        }
    }
}

