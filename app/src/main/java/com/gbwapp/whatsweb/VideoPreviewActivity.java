package com.nadinegb.free;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.nadinegb.free.fragments.Utils;
import com.nadinegb.free.util.AdsManager;


public class VideoPreviewActivity extends AppCompatActivity {

    VideoView displayVV;
    ImageView backIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_preview);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        displayVV = (VideoView) findViewById(R.id.displayVV);

        displayVV.setVideoPath(Utils.mPath);
        displayVV.start();


        AdsManager.showBanner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayVV.setVideoPath(Utils.mPath);
        displayVV.start();
    }


}
