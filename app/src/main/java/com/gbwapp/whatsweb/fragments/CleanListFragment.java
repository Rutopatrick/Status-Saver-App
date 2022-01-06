package com.nadinegb.free.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import com.nadinegb.free.R;
import com.nadinegb.free.adapter.CleanerAdapter;
import com.nadinegb.free.model.CleanerFileModel;
import com.nadinegb.free.util.AdsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CleanListFragment extends Fragment implements CleanerAdapter.OnCheckboxListener {
    String category;
    LinearLayout delete;
    TextView txt;
    TextView emptyTxt;
    File file;
    ArrayList<CleanerFileModel> filesToDelete = new ArrayList<>();
    CleanerAdapter mAdapter;
    LayoutManager mLayoutManager;
    String path;
    RecyclerView recyclerView;
    ArrayList<CleanerFileModel> statusImageList = new ArrayList<>();
    CheckBox selectAll;

    public static CleanListFragment newInstance(String category, String path) {
        CleanListFragment cleanListFragment = new CleanListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putString("category", category);
        cleanListFragment.setArguments(bundle);
        return cleanListFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clean_list, container, false);
        if (getArguments() != null) {
            this.path = getArguments().getString("path");
            this.category = getArguments().getString("category");
        }

        AdsManager.load(requireActivity());
        this.emptyTxt = view.findViewById(R.id.emptyTxt);
        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.recyclerView.setHasFixedSize(true);
        this.mLayoutManager = new GridLayoutManager(getActivity(), 3);
        this.recyclerView.setLayoutManager(this.mLayoutManager);
        loadMedia();
        this.txt = view.findViewById(R.id.txt);
        this.delete = view.findViewById(R.id.delete);
        this.delete.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AdsManager.showONLY(requireActivity());
                if (!filesToDelete.isEmpty()) {
                    new AlertDialog.Builder(getContext())
                            .setMessage("Are you sure , You want to delete selected files?")
                            .setCancelable(true)
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int success = -1;
                                    ArrayList<CleanerFileModel> deletedFiles = new ArrayList<>();

                                    for (CleanerFileModel details : filesToDelete) {
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
                                    for (CleanerFileModel deletedFile : deletedFiles) {
                                        statusImageList.remove(deletedFile);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                    if (success == 0) {
                                        Toast.makeText(CleanListFragment.this.getContext(), "Couldn't delete some files", Toast.LENGTH_SHORT).show();
                                    } else if (success == 1) {
                                        Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                                    }
                                    txt.setText(R.string.delete_items_blank);
                                    txt.setTextColor(getResources().getColor(R.color.h_btn_text_color));
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

        selectAll = view.findViewById(R.id.selectAll);
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!compoundButton.isPressed()) {
                    return;
                }

                filesToDelete.clear();

                for (int i = 0; i < statusImageList.size(); i++) {
                    if (!statusImageList.get(i).selected) {
                        b = true;
                        break;
                    }
                }

                if (b) {
                    for (int i = 0; i < statusImageList.size(); i++) {
                        statusImageList.get(i).selected = true;
                        filesToDelete.add(statusImageList.get(i));
                    }
                    selectAll.setChecked(true);
                } else {
                    for (int i = 0; i < statusImageList.size(); i++) {
                        statusImageList.get(i).selected = false;
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public void loadMedia() {
        this.file = new File(this.path);
        this.statusImageList.clear();
        if (this.file.isDirectory()) {
            displayfiles(this.file, this.recyclerView);
        }
    }

    public File[] dirListByAscendingDate(File folder) {
        File[] sortedByDate;
        if (this.category.equals(Utils.VOICE)) {
            sortedByDate = listf(folder.getAbsolutePath(), new ArrayList());
        } else {
            sortedByDate = folder.listFiles();
        }
        if (sortedByDate == null || sortedByDate.length <= 1) {
            return sortedByDate;
        }
        return sortedByDate;
    }

    public File[] listf(String directoryName, List<File> files) {
        File[] fList = new File(directoryName).listFiles();
        if (fList != null) {
            for (File file2 : fList) {
                if (file2.isFile()) {
                    files.add(file2);
                } else if (file2.isDirectory() && !file2.getName().equals("Sent")) {
                    listf(file2.getAbsolutePath(), files);
                }
            }
        }
        File[] strArr = new File[files.size()];
        files.toArray(strArr);
        return strArr;
    }

    public void displayfiles(File file2, RecyclerView mRecyclerView) {
        File[] listfilemedia = dirListByAscendingDate(file2);
        for (int i = 0; i < listfilemedia.length; i++) {
            if (!listfilemedia[i].getAbsolutePath().contains(".nomedia") && !listfilemedia[i].isDirectory()) {
                this.statusImageList.add(new CleanerFileModel(listfilemedia[i].getAbsolutePath(), listfilemedia[i].getName(), Formatter.formatShortFileSize(getActivity(), listfilemedia[i].length())));
            }
        }
        if (this.statusImageList.isEmpty()) {
            this.emptyTxt.setVisibility(View.VISIBLE);
        } else {
            this.emptyTxt.setVisibility(View.GONE);
        }
        this.mAdapter = new CleanerAdapter(getActivity(), this.statusImageList, this.category, this);
        mRecyclerView.setAdapter(this.mAdapter);
    }

    @Override
    public void onCheckboxListener(View view, List<CleanerFileModel> updatedFiles) {
        filesToDelete.clear();
        for (CleanerFileModel details : updatedFiles) {
            if (details.isSelected()) {
                filesToDelete.add(details);
            }
        }
        if (filesToDelete.size() == statusImageList.size()) {
            selectAll.setChecked(true);
        }
        if (!filesToDelete.isEmpty()) {
            long totalFileSize = 0;

            for (CleanerFileModel details : filesToDelete) {
                totalFileSize += new File(details.getFilePath()).length();
            }
            String size = Formatter.formatShortFileSize(getActivity(), totalFileSize);
            txt.setText("Delete Selected Items (" + size + ")");
            this.txt.setTextColor(Color.parseColor("#5AA061"));
            return;
        }
        this.txt.setText(R.string.delete_items_blank);
        txt.setTextColor(getResources().getColor(R.color.h_btn_text_color));
        selectAll.setChecked(false);
    }
}
