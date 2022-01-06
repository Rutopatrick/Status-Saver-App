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
import com.nadinegb.free.adapter.RecWappAdapter;
import com.nadinegb.free.model.StatusModel;
import com.nadinegb.free.util.AdsManager;
import com.nadinegb.free.util.Utils;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecentWapp extends Fragment implements RecWappAdapter.OnCheckboxListener {

    GridView imagegrid;
    ArrayList<StatusModel> f = new ArrayList<>();
    RecWappAdapter myAdapter;
    ArrayList<StatusModel> filesToDelete = new ArrayList<>();
    LinearLayout actionLay, downloadIV, deleteIV;
    CheckBox selectAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sta_photos, container, false);
        imagegrid = (GridView) rootView.findViewById(R.id.WorkImageGrid);
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

        downloadIV = rootView.findViewById(R.id.downloadIV);
        downloadIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdsManager.showONLY(requireActivity());
                if (!filesToDelete.isEmpty()) {

                    int success = -1;
                    ArrayList<StatusModel> deletedFiles = new ArrayList<>();

                    for (StatusModel details : filesToDelete) {
                        File file = new File(details.getFilePath());
                        if (file.exists()) {
                            if (Utils.download(getActivity(), details.getFilePath())) {
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
                        f.contains(deletedFile.selected = false);
//						deletedFile.selected = false;
//						f.add(deletedFile);
                    }
                    myAdapter.notifyDataSetChanged();
                    if (success == 0) {
                        Toast.makeText(getContext(), "Couldn't Saved Some Files", Toast.LENGTH_SHORT).show();
                    } else if (success == 1) {
                        Toast.makeText(getActivity(), "All Status Are Saved Successfully", Toast.LENGTH_SHORT).show();
                    }
                    actionLay.setVisibility(View.GONE);
                    selectAll.setChecked(false);
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
        myAdapter = new RecWappAdapter(this, f, this);
        imagegrid.setAdapter(myAdapter);
    }

    public void getFromSdcard() {
        File file = getWhatsupFolder();
        f = new ArrayList<>();
        if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            if (listFile != null) {
                Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                for (int i = 0; i < listFile.length; i++) {
                    if (!listFile[i].getAbsolutePath().contains(".nomedia"))
                        f.add(new StatusModel(listFile[i].getAbsolutePath()));
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        myAdapter.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 10) {
            myAdapter.notifyDataSetChanged();

            populateGrid();
            actionLay.setVisibility(View.GONE);
            selectAll.setChecked(false);
        }
    }


    public File getWhatsupFolder() {
        return new File(Environment.getExternalStorageDirectory() + File.separator + "WhatsApp" + File.separator + "Media" + File.separator + ".Statuses");
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
