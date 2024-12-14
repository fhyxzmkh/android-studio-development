package com.example.demo2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // UI组件
    private EditText keyInput;
    private EditText valueInput;
    private TextView dataTextView;
    private Button insertButton;
    private Button deleteButton;
    private Button updateButton;
    private Button queryButton;
    private Button clearButton;

    // 数据库相关
    MySQLiteOpenHelper mySQLDatabase;
    SQLiteDatabase sqlDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // 启用全屏模式
        setContentView(R.layout.activity_main);

        // 初始化UI组件
        keyInput = findViewById(R.id.keyInput);
        valueInput = findViewById(R.id.valueInput);
        dataTextView = findViewById(R.id.dataTextView);
        insertButton = findViewById(R.id.insertButton);
        deleteButton = findViewById(R.id.deleteButton);
        updateButton = findViewById(R.id.updateButton);
        queryButton = findViewById(R.id.queryButton);
        clearButton = findViewById(R.id.clearButton);

        // 设置按钮点击监听
        insertButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        queryButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        // 初始化数据库帮助类并打开数据库
        mySQLDatabase = new MySQLiteOpenHelper(MainActivity.this, "demo2_db");
        sqlDatabase = mySQLDatabase.getReadableDatabase();

        // 设置窗口内边距以适应系统栏
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 显示数据库中的所有数据
        dataTextView.setText(printDatabase());
    }

    // 查询并返回数据库中的所有数据
    private String printDatabase() {
        StringBuilder result = new StringBuilder();
        Cursor cursor = sqlDatabase.query("my_map_table", null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int keyIndex = cursor.getColumnIndex("my_key");
                int valueIndex = cursor.getColumnIndex("my_value");

                if (keyIndex != -1 && valueIndex != -1) {
                    String queriedKey = cursor.getString(keyIndex);
                    String queriedValue = cursor.getString(valueIndex);
                    result.append("Key: ").append(queriedKey).append(", Value: ").append(queriedValue).append("\n");
                } else {
                    result.append("Error: Column not found\n");
                }
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            result.append("No data found");
        }

        return result.toString();
    }

    @Override
    public void onClick(View v) {
        String key = keyInput.getText().toString();
        String value = valueInput.getText().toString();

        // 根据按钮ID执行不同操作
        if (v.getId() == R.id.insertButton) {
            // 插入数据
            ContentValues contentValues = new ContentValues();
            contentValues.put("my_key", key);
            contentValues.put("my_value", value);
            long result = sqlDatabase.insert("my_map_table", null, contentValues);
            if (result != -1) {
                Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.deleteButton) {
            // 删除数据
            String whereClause = "my_key=?";
            String[] whereArgs = {key};
            int result = sqlDatabase.delete("my_map_table", whereClause, whereArgs);
            if (result > 0) {
                Toast.makeText(this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete data", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.updateButton) {
            // 更新数据
            ContentValues contentValues = new ContentValues();
            contentValues.put("my_value", value);
            String whereClause = "my_key=?";
            String[] whereArgs = {key};
            int result = sqlDatabase.update("my_map_table", contentValues, whereClause, whereArgs);
            if (result > 0) {
                Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update data", Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.queryButton) {
            // 查询数据
            if (key.isEmpty()) {
                Toast.makeText(this, "Please enter a key to query", Toast.LENGTH_SHORT).show();
                return;
            }

            // 查询 my_key 对应的 my_value
            Cursor cursor = sqlDatabase.query(
                    "my_map_table",
                    new String[]{"my_value"}, // 只查询 my_value 列
                    "my_key=?",              // 查询条件
                    new String[]{key},       // 查询条件的参数
                    null, null, null
            );

            StringBuilder result = new StringBuilder();
            if (cursor != null && cursor.moveToFirst()) {
                int valueIndex = cursor.getColumnIndex("my_value");
                if (valueIndex != -1) {
                    String queriedValue = cursor.getString(valueIndex);
                    result.append("Value for key '").append(key).append("': ").append(queriedValue);
                } else {
                    result.append("Error: Column not found");
                }
                cursor.close();
            } else {
                result.append("No value found for key '").append(key).append("'");
            }

            dataTextView.setText(result.toString());
            return;
        } else if (v.getId() == R.id.clearButton) {
            // 清空表
            int rowsDeleted = sqlDatabase.delete("my_map_table", null, null);
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Table cleared successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to clear table", Toast.LENGTH_SHORT).show();
            }

            // 更新显示
            dataTextView.setText("No data found");
        }

        // 更新显示数据库中的所有数据
        dataTextView.setText(printDatabase());
    }
}