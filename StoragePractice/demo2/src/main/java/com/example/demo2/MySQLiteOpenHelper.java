package com.example.demo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import androidx.annotation.Nullable;

import java.io.File;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    // 将数据库文件存放在内存的SQLiteTest目录中，便于后续调试、操作等
    public static final String FILE_DIR = Environment.getExternalStorageDirectory().getPath() + "/SQLiteTest/";
    // 数据库版本号
    public static final int DATABASE_VERSION = 1;
    // 数据库表名
    public static final String TABLE_NAME = "my_map_table";


    public MySQLiteOpenHelper(@Nullable Context context, String name) {
        super(context, name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        File dir = new File(FILE_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }

        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (my_key TEXT PRIMARY KEY, my_value TEXT)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sql);
            onCreate(db);
        }
    }
}
