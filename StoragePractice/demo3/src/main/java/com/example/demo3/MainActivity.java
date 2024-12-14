package com.example.demo3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btn_file_write).setOnClickListener(this);
        findViewById(R.id.btn_file_read).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        // Intent intent = new Intent(this, FileWriteActivity.class);
        // intent.putExtra("is_external", true); // 设置为公共路径
        // startActivity(intent);

        if (v.getId() == R.id.btn_file_write) {
            startActivity(new Intent(this, FileWriteActivity.class));
        }
        else if (v.getId() == R.id.btn_file_read) {
            startActivity(new Intent(this, FileReadActivity.class));
        }
    }
}