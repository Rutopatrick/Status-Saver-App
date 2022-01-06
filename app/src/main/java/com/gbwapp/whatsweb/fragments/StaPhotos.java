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

public class StaPhotos extends Fragment implements StaPhotoAdapter.OnCheckboxListener {

    GridView imagegrid;
    ArrayList<StatusModel> f = new ArrayList<>();
    StaPhotoAdapter myAdapter;
    int save = 10;
    ArrayList<StatusModel> filesToDelete = new ArrayList<>();
    LinearLayout actionLay, deleteIV;
    CheckBox selectAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sta_videos, container, false);
        imagegrid = rootView.findViewById(R.id.videoGrid);
        populateGrid();

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
                                        f.remove(deletedFile);
                                    }
                                    myAdapter.notifyDataSetChanged();
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

                for (int i = 0; i < f.size(); i++) {
                    if (!f.get(i).selected) {
                        b = true;
                        break;
                    }
                }

                if (b) {
                    for (int i = 0; i < f.size(); i++) {
                        f.get(i).selected = true;
                        filesToDelete.add(f.get(i));
                    }
                    selectAll.setChecked(true);
                } else {
                    for (int i = 0; i < f.size(); i++) {
                        f.get(i).selected = false;
                    }
                    actionLay.setVisibility(View.GONE);
                }
                myAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    public void populateGrid() {
        getFromSdcard();
        myAdapter = new StaPhotoAdapter(this, f, this);
        imagegrid.setAdapter(myAdapter);
    }

    public void getFromSdcard() {
        File file = new File(
                Environment
                        .getExternalStorageDirectory().toString() + File.separator + getResources().getString(R.string.app_name) + "/Images");
        f = new ArrayList<>();
        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (int i = 0; i < listFile.length; i++) {
                f.add(new StatusModel(listFile[i].getAbsolutePath()));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myAdapter.onActivityResult(requestCode, resultCode, data);
        if (requestCode == save && resultCode == save) {
            myAdapter.notifyDataSetChanged();

            populateGrid();
            actionLay.setVisibility(View.GONE);
            selectAll.setChecked(false);
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
        if (filesToDelete.size() == f.size()) {
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
