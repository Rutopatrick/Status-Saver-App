package com.nadinegb.free;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nadinegb.free.adapter.WallCleanerAdapter;
import com.nadinegb.free.fragments.Utils;
import com.nadinegb.free.model.CleanerFileModel;
import com.nadinegb.free.util.AdsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WallCleanerActivity extends AppCompatActivity implements WallCleanerAdapter.OnCheckboxListener {

    ImageView backIV;
    LinearLayout delete;
    TextView txt;
    TextView emptyTxt;
    File file;
    ArrayList<CleanerFileModel> filesToDelete = new ArrayList<>();
    WallCleanerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView recyclerView;
    ArrayList<CleanerFileModel> statusImageList = new ArrayList<>();
    String folderPath;
    String category;
    CheckBox selectAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_cleaner);

        AdsManager.load(this);

        category = getIntent().getStringExtra("category");
        folderPath = getIntent().getStringExtra("folderPath");

        backIV = findViewById(R.id.backIV);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        emptyTxt = findViewById(R.id.emptyTxt);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);

        loadMedia(folderPath);

        txt = findViewById(R.id.txt);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AdsManager.showONLY(WallCleanerActivity.this);
                if (!filesToDelete.isEmpty()) {
                    new AlertDialog.Builder(WallCleanerActivity.this)
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
                                        Toast.makeText(WallCleanerActivity.this, "Couldn't delete some files", Toast.LENGTH_SHORT).show();
                                    } else if (success == 1) {
                                        Toast.makeText(WallCleanerActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
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

        selectAll = findViewById(R.id.selectAll);
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

        AdsManager.showBanner(this);
    }


    public void loadMedia(String path) {
        file = new File(path);
        statusImageList.clear();
        if (file.isDirectory()) {
            displayfiles(file, recyclerView);
        }
    }

    public File[] dirListByAscendingDate(File folder) {
        File[] sortedByDate;
        if (category.equals(Utils.VOICE)) {
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
                statusImageList.add(new CleanerFileModel(listfilemedia[i].getAbsolutePath(), listfilemedia[i].getName(), Formatter.formatShortFileSize(this, listfilemedia[i].length())));
            }
        }
        if (statusImageList.isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
        } else {
            emptyTxt.setVisibility(View.GONE);
        }
        mAdapter = new WallCleanerAdapter(this, statusImageList, category, this);
        mRecyclerView.setAdapter(mAdapter);
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
            String size = Formatter.formatShortFileSize(this, totalFileSize);
            txt.setText("Delete Selected Items (" + size + ")");
            txt.setTextColor(Color.parseColor("#5AA061"));
            return;
        }
        txt.setText(R.string.delete_items_blank);
        txt.setTextColor(getResources().getColor(R.color.h_btn_text_color));
        selectAll.setChecked(false);
    }
}
