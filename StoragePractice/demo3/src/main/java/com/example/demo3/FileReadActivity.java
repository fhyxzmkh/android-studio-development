package com.example.demo3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n")
public class FileReadActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "FileReadActivity";
    private TextView tv_content; // 声明一个文本视图对象
    private Spinner spinner_files; // 声明一个下拉框对象
    private String mPath; // 私有目录路径
    private List<File> mFilelist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_read);
        tv_content = findViewById(R.id.tv_content);
        spinner_files = findViewById(R.id.spinner_files);
        findViewById(R.id.btn_delete).setOnClickListener(this);

        if (getIntent().getBooleanExtra("is_external", false)) {
            // 获取当前App的公共下载目录
            mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        } else {
            // 获取当前App的私有下载目录
            mPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        }

        // 初始化文件列表并显示
        showFileContent();
    }

    // 显示最新的文本文件内容
    private void showFileContent() {
        // 获得指定目录下面的所有文本文件
        mFilelist = FileUtil.getFileList(mPath, new String[]{".txt"});

        // 将文件名添加到下拉框
        List<String> fileNames = new ArrayList<>();
        for (File file : mFilelist) {
            fileNames.add(file.getName());
        }

        // 设置下拉框的适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, fileNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_files.setAdapter(adapter);

        // 设置下拉框的选择监听器
        spinner_files.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 显示选中的文件内容
                String file_path = mFilelist.get(position).getAbsolutePath();
                String content = FileUtil.openText(file_path);
                String desc = String.format("选中的文件路径为%s，内容如下：\n%s",
                        file_path, content);
                tv_content.setText(desc);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 如果没有选择任何文件，清空内容
                tv_content.setText("");
            }
        });

        // 如果没有文件，显示提示信息
        if (mFilelist.isEmpty()) {
            tv_content.setText("私有目录下未找到任何文本文件");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_delete) {
            for (int i = 0; i < mFilelist.size(); i++) {
                String file_path = mFilelist.get(i).getAbsolutePath();
                File f = new File(file_path);
                if (!f.delete()) {
                    Log.d(TAG, "file_path=" + file_path + ", delete failed");
                }
            }
            Toast.makeText(this, "已删除私有目录下的所有文本文件", Toast.LENGTH_SHORT).show();
            // 删除后刷新文件列表
            showFileContent();
        }
    }
}