package com.nadinegb.free.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import com.nadinegb.free.R;
import com.nadinegb.free.adapter.StaPhotoAdapter;
import com.nadinegb.free.model.StatusModel;
import com.nadinegb.free.util.AdsManager;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaVideos extends Fragment implements StaPhotoAdapter.OnCheckboxListener {

    ArrayList<StatusModel> fileList = null;
    GridView gridView;
    StaPhotoAdapter adapter;
    int save = 10;
    ArrayList<StatusModel> filesToDelete = new ArrayList<>();
    LinearLayout actionLay, deleteIV;
    CheckBox selectAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sta_videos, container, false);
        gridView = rootView.findViewById(R.id.videoGrid);
        populateVideo();

        AdsManager.load(requireActivity());

        actionLay = rootView.findViewById(R.id.actionLay);
        deleteIV = rootView.findViewById(R.id.deleteIV);
        deleteIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdsManager.showONLY(requireActivity());
                if (!filesToDelete.isEmpty()) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Are you sure , You want to delete selected files?")
                            .setCancelable(true)
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int success = -1;
                                    ArrayList<StatusModel> deletedFiles = new ArrayList<>();

                                    for (StatusModel details : filesToDelete) {
                                        File file = new File(details.getFilePath());
                                        if (file.exists()) {
                                            if (file.delete()) {
                                                deletedFiles.add(details);
                                                if (success == 0) {
                                                    return;
                                                }
                                                success = 1;
                                            } else {
                                                success = 0;
                                            }
                                        } else {
                                            success = 0;
                                        }
                                    }

                                    filesToDelete.clear();
                                    for (StatusModel deletedFile : deletedFiles) {
                                        fileList.remove(deletedFile);
                                    }
                                    adapter.notifyDataSetChanged();
                                    if (success == 0) {
                                        Toast.makeText(getContext(), "Couldn't delete some files", Toast.LENGTH_SHORT).show();
                                    } else if (success == 1) {
                                        Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                                    }
                                    actionLay.setVisibility(View.GONE);
                                    selectAll.setChecked(false);
                                }
                            })
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();
                }
            }
        });

        selectAll = rootView.findViewById(R.id.selectAll);
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!compoundButton.isPressed()) {
                    return;
                }

                filesToDelete.clear();

                for (int i = 0; i < fileList.size(); i++) {
                    if (!fileList.get(i).selected) {
                        b = true;
                        break;
                    }
                }

                if (b) {
                    for (int i = 0; i < fileList.size(); i++) {
                        fileList.get(i).selected = true;
                        filesToDelete.add(fileList.get(i));
                    }
                    selectAll.setChecked(true);
                } else {
                    for (int i = 0; i < fileList.size(); i++) {
                        fileList.get(i).selected = false;
                    }
                    actionLay.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    public void populateVideo() {
        updateSongList();

        if (fileList != null) {
            adapter = new StaPhotoAdapter(this, fileList, this);
            gridView.setAdapter(adapter);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.onActivityResult(requestCode, resultCode, data);
        if (requestCode == save && resultCode == save) {
            adapter.notifyDataSetChanged();

            populateVideo();
            actionLay.setVisibility(View.GONE);
            selectAll.setChecked(false);
        }
    }

    public void updateSongList() {
        File videoFiles = new File(Environment
                .getExternalStorageDirectory().toString() + File.separator + getResources().getString(R.string.app_name) + "/Videos");

        if (videoFiles.isDirectory()) {
            fileList = new ArrayList<>();
            if (videoFiles.isDirectory()) {
                File[] listFile = videoFiles.listFiles();
                Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                for (int i = 0; i < listFile.length; i++) {

                    fileList.add(new StatusModel(listFile[i].getAbsolutePath()));

                }
            }
        }

    }

    @Override
    public void onCheckboxListener(View view, List<StatusModel> list) {
        filesToDelete.clear();
        for (StatusModel details : list) {
            if (details.isSelected()) {
                filesToDelete.add(details);
            }
        }
        if (filesToDelete.size() == fileList.size()) {
            selectAll.setChecked(true);
        }
        if (!filesToDelete.isEmpty()) {
            actionLay.setVisibility(View.VISIBLE);
            return;
        }
        selectAll.setChecked(false);
        actionLay.setVisibility(View.GONE);
    }
}
