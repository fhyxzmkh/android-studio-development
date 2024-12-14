package com.example.storagepractice;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText keyInput;
    private EditText valueInput;

    private TextView dataTextView;

    private Button insertButton;
    private Button deleteButton;
    private Button updateButton;
    private Button queryButton;
    private Button clearButton;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public static final String PATH = "/data/data/com.example.storagepractice/shared_prefs/demo.xml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        keyInput = findViewById(R.id.keyInput);
        valueInput = findViewById(R.id.valueInput);

        dataTextView = findViewById(R.id.dataTextView);

        insertButton = findViewById(R.id.insertButton);
        deleteButton = findViewById(R.id.deleteButton);
        updateButton = findViewById(R.id.updateButton);
        queryButton = findViewById(R.id.queryButton);
        clearButton = findViewById(R.id.clearButton);

        insertButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        updateButton.setOnClickListener(this);
        queryButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        // 初始化SharedPreferences对象
        sp = getSharedPreferences("demo", MODE_PRIVATE);
        // 初始化Editor对象
        editor = sp.edit();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataTextView.setText(printXML());

    }

    private String printXML() {
        StringBuilder stringBuffer = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(PATH)));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuffer.append(str).append("\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }


    @Override
    public void onClick(View v) {

        String key = keyInput.getText().toString();
        String value = valueInput.getText().toString();

        if (v.getId() == R.id.insertButton) {
            editor.putString(key, value);
            editor.commit();
        } else if (v.getId() == R.id.deleteButton) {
            editor.remove(key);
            editor.commit();
        } else if (v.getId() == R.id.updateButton) {
            editor.putString(key, value);
            editor.commit();
        } else if (v.getId() == R.id.queryButton) {
            String result = sp.getString(key, "No such key");
            dataTextView.setText("key=" + key + ",value=" + result);
            return;
        } else if (v.getId() == R.id.clearButton) {
            editor.clear();
            editor.commit();
        }

        dataTextView.setText(MainActivity.this.printXML());

    }
}