package com.nadinegb.free;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.nadinegb.free.adapter.FullscreenImageAdapter;
import com.nadinegb.free.model.StatusModel;
import com.nadinegb.free.util.AdsManager;
import com.nadinegb.free.util.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;

public class PagerPreviewActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<StatusModel> imageList;
    int position;


    ImageView shareIV, deleteIV, wAppIV;
    LinearLayout downloadIV;
    FullscreenImageAdapter fullscreenImageAdapter;
    String statusdownload;
    ImageView backIV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_preview);

        AdsManager.load(this);
        backIV = findViewById(R.id.backIV);


        viewPager = findViewById(R.id.viewPager);

        shareIV = findViewById(R.id.shareIV);

        downloadIV = findViewById(R.id.downloadIV);

        deleteIV = findViewById(R.id.deleteIV);
        wAppIV = findViewById(R.id.wAppIV);


        imageList = getIntent().getParcelableArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);
        statusdownload = getIntent().getStringExtra("statusdownload");

        if (statusdownload.equals("download")) {
            downloadIV.setVisibility(View.GONE);
        } else {
            downloadIV.setVisibility(View.VISIBLE);
        }

        fullscreenImageAdapter = new FullscreenImageAdapter(PagerPreviewActivity.this, imageList);
        viewPager.setAdapter(fullscreenImageAdapter);
        viewPager.setCurrentItem(position);

        downloadIV.setOnClickListener(clickListener);
        shareIV.setOnClickListener(clickListener);
        deleteIV.setOnClickListener(clickListener);
        backIV.setOnClickListener(clickListener);
        wAppIV.setOnClickListener(clickListener);

        AdsManager.showBanner(this);

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backIV:
                    onBackPressed();
                    break;
                case R.id.downloadIV:
                    if (imageList.size() > 0) {
                        AdsManager.showONLY(PagerPreviewActivity.this);
                        try {
                            download(imageList.get(viewPager.getCurrentItem()).getFilePath());
                        } catch (Exception e) {
                            Toast.makeText(PagerPreviewActivity.this, "Sorry we can't move file.try with other file.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        finish();
                    }
                    break;

                case R.id.shareIV:
                    if (imageList.size() > 0) {
                        if (isImageFile(imageList.get(viewPager.getCurrentItem()).getFilePath())) {
                            File imageFileToShare = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());

                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            share.setType("image/*");
                            Uri photoURI = FileProvider.getUriForFile(
                                    getApplicationContext(), getApplicationContext()
                                            .getPackageName() + ".provider", imageFileToShare);
                            share.putExtra(Intent.EXTRA_STREAM,
                                    photoURI);
                            startActivity(Intent.createChooser(share, "Share via"));

                        } else if (isVideoFile(imageList.get(viewPager.getCurrentItem()).getFilePath())) {

                            Uri videoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext()
                                    .getPackageName() + ".provider", new File(imageList.get(viewPager.getCurrentItem()).getFilePath()));
                            Intent videoshare = new Intent(Intent.ACTION_SEND);
                            videoshare.setType("*/*");
                            videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            videoshare.putExtra(Intent.EXTRA_STREAM, videoURI);

                            startActivity(videoshare);
                        }
                    } else {
                        finish();
                    }
                    break;

                case R.id.deleteIV:
                    if (imageList.size() > 0) {
                        AdsManager.showONLY(PagerPreviewActivity.this);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PagerPreviewActivity.this);
                        alertDialog.setTitle("Confirm Delete....");
                        alertDialog.setMessage("Are you sure, You Want To Delete This Status?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                int currentItem = 0;

                                File file = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());
                                if (file.exists()) {
                                    boolean del = file.delete();
                                    if (imageList.size() > 0 && viewPager.getCurrentItem() < imageList.size()) {
                                        currentItem = viewPager.getCurrentItem();
                                    }
                                    imageList.remove(viewPager.getCurrentItem());
                                    fullscreenImageAdapter = new FullscreenImageAdapter(PagerPreviewActivity.this, imageList);
                                    viewPager.setAdapter(fullscreenImageAdapter);

                                    Intent intent = new Intent();
                                    setResult(10, intent);

                                    if (imageList.size() > 0) {
                                        viewPager.setCurrentItem(currentItem);
                                    } else {
                                        finish();
                                    }
                                }
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.show();
                    } else {
                        finish();
                    }
                    break;

                case R.id.wAppIV:
                    if (isImageFile(imageList.get(viewPager.getCurrentItem()).getFilePath())) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/*");
                        share.setPackage("com.whatsapp");
                        File imageFileToShare = new File(imageList.get(viewPager.getCurrentItem()).getFilePath());
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext()
                                .getPackageName() + ".provider", imageFileToShare);
                        share.putExtra(Intent.EXTRA_STREAM, photoURI);
                        startActivity(Intent.createChooser(share, "Share Image!"));
                        AdsManager.showONLY(PagerPreviewActivity.this);
                    } else if (isVideoFile(imageList.get(viewPager.getCurrentItem()).getFilePath())) {
                        Uri videoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext()
                                .getPackageName() + ".provider", new File(imageList.get(viewPager.getCurrentItem()).getFilePath()));
                        Intent videoshare = new Intent(Intent.ACTION_SEND);
                        videoshare.setType("*/*");
                        videoshare.setPackage("com.whatsapp");
                        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        videoshare.putExtra(Intent.EXTRA_STREAM, videoURI);

                        AdsManager.showNext(PagerPreviewActivity.this,videoshare);

                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void download(String path) {
        if (copyFileInSavedDir(path)) {
            Toast.makeText(PagerPreviewActivity.this, "Status is Saved Successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PagerPreviewActivity.this, "Failed to Download", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }


    public boolean copyFileInSavedDir(String file) {
        try {
            if (isImageFile(file)) {
                FileUtils.copyFileToDirectory(new File(file), getDir("Images"));
                Utils.mediaScanner(PagerPreviewActivity.this, getDir("Images") + "/", file, "image/*");
            } else {
                FileUtils.copyFileToDirectory(new File(file), getDir("Videos"));
                Utils.mediaScanner(PagerPreviewActivity.this, getDir("Videos") + "/", file, "video/*");
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public File getDir(String folder) {

        File rootFile = new File(Environment.getExternalStorageDirectory().toString() + File.separator + getResources().getString(R.string.app_name) + File.separator + folder);
        rootFile.mkdirs();

        return rootFile;

    }

}
