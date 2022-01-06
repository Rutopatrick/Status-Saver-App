package com.nadinegb.free;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nadinegb.free.fragments.Utils;
import com.nadinegb.free.util.AdsManager;

import java.io.File;

public class CleanOptionActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backIV;
    RelativeLayout imgBtn, videoBtn, docBtn, audioBtn, voiceBtn, wallBtn, gifBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_option);
        init();

        AdsManager.load(this);
        LinearLayout adContainer = findViewById(R.id.banner_container);
        AdsManager.showBanner(this);
    }

    void init() {
        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(this);

        imgBtn = findViewById(R.id.imgBtn);
        imgBtn.setOnClickListener(this);

        videoBtn = findViewById(R.id.videoBtn);
        videoBtn.setOnClickListener(this);

        docBtn = findViewById(R.id.docBtn);
        docBtn.setOnClickListener(this);

        audioBtn = findViewById(R.id.audioBtn);
        audioBtn.setOnClickListener(this);

        voiceBtn = findViewById(R.id.voiceBtn);
        voiceBtn.setOnClickListener(this);

        wallBtn = findViewById(R.id.wallBtn);
        wallBtn.setOnClickListener(this);

        gifBtn = findViewById(R.id.gifBtn);
        gifBtn.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.iswApp) {
            ((TextView) findViewById(R.id.txt1)).setText(Formatter.formatShortFileSize(this,
                    folderSize(new File(Utils.imagesReceivedPath)) + folderSize(new File(Utils.imagesSentPath))));
            ((TextView) findViewById(R.id.txt2)).setText(Formatter.formatShortFileSize(this,
                    folderSize(new File(Utils.videosReceivedPath)) + folderSize(new File(Utils.videosSentPath))));
            ((TextView) findViewById(R.id.txt3)).setText(Formatter.formatShortFileSize(this,
                    folderSize(new File(Utils.documentsReceivedPath)) + folderSize(new File(Utils.documentsSentPath))));
            ((TextView) findViewById(R.id.txt4)).setText(Formatter.formatShortFileSize(this,
                    folderSize(new File(Utils.audiosReceivedPath)) + folderSize(new File(Utils.audiosSentPath))));
            ((TextView) findViewById(R.id.txt5)).setText(Formatter.formatShortFileSize(this, folderSize(new File(Utils.voiceReceivedPath))));
            ((TextView) findViewById(R.id.txt6)).setText(Formatter.formatShortFileSize(this, folderSize(new File(Utils.wallReceivedPath))));
            ((TextView) findViewById(R.id.txt7)).setText(Formatter.formatShortFileSize(this,
                    folderSize(new File(Utils.gifReceivedPath)) + folderSize(new File(Utils.gifSentPath))));
        } else {
            ((TextView) findViewById(R.id.txt1)).setText(Formatter.formatShortFileSize(this,
                    folderSize(new File(Utils.wbImagesReceivedPath)) + folderSize(new File(Utils.wbImagesSentPath))));
            ((TextView) findViewById(R.id.txt2)).setText(Formatter.formatShortFileSize(this,
                    folderSize(new File(Utils.wbVideosReceivedPath)) + folderSize(new File(Utils.wbVideosSentPath))));
            ((TextView) findViewById(R.id.txt3)).setText(Formatter.formatShortFileSize(this,
                    folderSize(new File(Utils.wbDocumentsReceivedPath)) + folderSize(new File(Utils.wbDocumentsSentPath))));
            ((TextView) findViewById(R.id.txt4)).setText(Formatter.formatShortFileSize(this,
                    folderSize(new File(Utils.wbAudiosReceivedPath)) + folderSize(new File(Utils.wbAudiosSentPath))));
            ((TextView) findViewById(R.id.txt5)).setText(Formatter.formatShortFileSize(this, folderSize(new File(Utils.wbVoiceReceivedPath))));
            ((TextView) findViewById(R.id.txt6)).setText(Formatter.formatShortFileSize(this, folderSize(new File(Utils.wbWallReceivedPath))));
            ((TextView) findViewById(R.id.txt7)).setText(Formatter.formatShortFileSize(this,
                    folderSize(new File(Utils.wbGifReceivedPath)) + folderSize(new File(Utils.wbGifSentPath))));
        }
    }

    public static long folderSize(File directory) {
        long j;
        long length = 0;
        if (directory.listFiles() == null) {
            return length;
        } else {
            for (File file : directory.listFiles()) {
                if (file.isFile()) {
                    j = file.length();
                } else {
                    j = folderSize(file);
                }
                length += j;
            }
            return length;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backIV:
                onBackPressed();
                break;

            case R.id.imgBtn:
                if (Utils.iswApp) {
                    onTapBtn(Utils.IMAGE, Utils.imagesReceivedPath, Utils.imagesSentPath);
                } else {
                    onTapBtn(Utils.IMAGE, Utils.wbImagesReceivedPath, Utils.wbImagesSentPath);
                }
                break;

            case R.id.videoBtn:
                if (Utils.iswApp) {
                    onTapBtn(Utils.VIDEO, Utils.videosReceivedPath, Utils.videosSentPath);
                } else {
                    onTapBtn(Utils.VIDEO, Utils.wbVideosReceivedPath, Utils.wbVideosSentPath);
                }
                break;

            case R.id.docBtn:
                if (Utils.iswApp) {
                    onTapBtn(Utils.DOCUMENT, Utils.documentsReceivedPath, Utils.documentsSentPath);
                } else {
                    onTapBtn(Utils.DOCUMENT, Utils.wbDocumentsReceivedPath, Utils.wbDocumentsSentPath);
                }
                break;

            case R.id.audioBtn:
                if (Utils.iswApp) {
                    onTapBtn(Utils.AUDIO, Utils.audiosReceivedPath, Utils.audiosSentPath);
                } else {
                    onTapBtn(Utils.AUDIO, Utils.wbAudiosReceivedPath, Utils.wbAudiosSentPath);
                }
                break;

            case R.id.voiceBtn:
                if (Utils.iswApp) {
                    onTapOther(Utils.VOICE, Utils.voiceReceivedPath);
                } else {
                    onTapOther(Utils.VOICE, Utils.wbVoiceReceivedPath);
                }
                break;

            case R.id.wallBtn:
                if (Utils.iswApp) {
                    onTapOther(Utils.WALLPAPER, Utils.wallReceivedPath);
                } else {
                    onTapOther(Utils.WALLPAPER, Utils.wbWallReceivedPath);
                }
                break;

            case R.id.gifBtn:
                if (Utils.iswApp) {
                    onTapBtn(Utils.GIF, Utils.gifSentPath, Utils.gifReceivedPath);
                } else {
                    onTapBtn(Utils.GIF, Utils.wbGifSentPath, Utils.wbGifReceivedPath);
                }
                break;

            default:
                break;
        }
    }

    void onTapBtn(String category, String receivePath, String sentPath) {
        Intent intent = new Intent(this, CleanDataActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("receivePath", receivePath);
        intent.putExtra("sentPath", sentPath);
        AdsManager.showNext(this,intent);

    }

    void onTapOther(String category, String path) {
        Intent intent = new Intent(this, WallCleanerActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("folderPath", path);
        AdsManager.showNext(this,intent);

    }
}
