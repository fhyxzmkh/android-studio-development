package com.example.demo3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("SetTextI18n")
public class FileWriteActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText et_name; // 姓名输入框
    private EditText et_age; // 年龄输入框
    private EditText et_height; // 身高输入框
    private EditText et_weight; // 体重输入框
    private boolean isMarried = false; // 婚否状态
    private String[] typeArray = {"未婚", "已婚"}; // 婚否状态文本数组
    private String mPath; // 文件保存路径
    private TextView tv_path; // 显示文件路径的文本视图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_write);

        // 初始化视图组件
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        tv_path = findViewById(R.id.tv_path);
        CheckBox ck_married = findViewById(R.id.ck_married);

        // 设置婚否复选框的监听器
        ck_married.setOnCheckedChangeListener(this);

        // 设置保存按钮的点击监听器
        findViewById(R.id.btn_save).setOnClickListener(this);

        // 根据传入的参数决定文件保存路径
        if (getIntent().getBooleanExtra("is_external", false)) {
            // 获取公共下载目录路径
            mPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        } else {
            // 获取私有下载目录路径
            mPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
        }
    }

    // 婚否复选框状态改变时的回调
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        isMarried = isChecked; // 更新婚否状态
    }

    // 点击事件处理
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            // 获取用户输入的数据
            String name = et_name.getText().toString();
            String age = et_age.getText().toString();
            String height = et_height.getText().toString();
            String weight = et_weight.getText().toString();

            // 检查输入是否为空
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "请先填写姓名", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(age)) {
                Toast.makeText(this, "请先填写年龄", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(height)) {
                Toast.makeText(this, "请先填写身高", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(weight)) {
                Toast.makeText(this, "请先填写体重", Toast.LENGTH_SHORT).show();
                return;
            }

            // 构建文件内容
            String content = String.format("　姓名：%s\n　年龄：%s\n　身高：%scm\n　体重：%skg\n　婚否：%s\n　注册时间：%s\n",
                    name, age, height, weight, typeArray[isMarried?1:0], DateUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss"));

            // 生成文件路径
            String file_path = mPath + DateUtil.getNowDateTime("") + ".txt";

            // 保存文件内容到指定路径
            FileUtil.saveText(file_path, content);

            // 显示文件保存路径
            tv_path.setText("用户注册信息文件的保存路径为：\n" + file_path);

            // 提示用户文件已保存
            Toast.makeText(this, "数据已写入存储卡文件", Toast.LENGTH_SHORT).show();
        }
    }
}