package com.nadinegb.free.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.nadinegb.free.VideoPreviewActivity;
import com.nadinegb.free.R;
import com.nadinegb.free.model.StatusModel;
import com.nadinegb.free.util.Utils;

import java.io.File;
import java.util.ArrayList;

public class FullscreenImageAdapter extends PagerAdapter {
    Activity activity;
    ArrayList<StatusModel> imageList;
    private File file;

    public FullscreenImageAdapter(Activity activity, ArrayList<StatusModel> imageList) {
        this.activity = activity;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(activity).inflate(R.layout.preview_list_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        ImageView iconplayer = itemView.findViewById(R.id.iconplayer);

        this.file = new File( imageList.get(position).getFilePath());
        if (!this.file.isDirectory()) {
            if (!Utils.getBack( imageList.get(position).getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                try {
                    iconplayer.setVisibility(View.VISIBLE);
                    Glide.with(this.activity).load(this.file).into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (!Utils.getBack( imageList.get(position).getFilePath(), "((\\.jpg|\\.png|\\.gif|\\.jpeg|\\.bmp)$)").isEmpty()) {
                iconplayer.setVisibility(View.GONE);
                Glide.with(this.activity).load(this.file).into(imageView);
            }
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.getBack( imageList.get(position).getFilePath(), "((\\.mp4|\\.webm|\\.ogg|\\.mpK|\\.avi|\\.mkv|\\.flv|\\.mpg|\\.wmv|\\.vob|\\.ogv|\\.mov|\\.qt|\\.rm|\\.rmvb\\.|\\.asf|\\.m4p|\\.m4v|\\.mp2|\\.mpeg|\\.mpe|\\.mpv|\\.m2v|\\.3gp|\\.f4p|\\.f4a|\\.f4b|\\.f4v)$)").isEmpty()) {
                    com.nadinegb.free.fragments.Utils.mPath = imageList.get(position).getFilePath();
                    activity.startActivity(new Intent(activity, VideoPreviewActivity.class));
                }
            }
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }
}
