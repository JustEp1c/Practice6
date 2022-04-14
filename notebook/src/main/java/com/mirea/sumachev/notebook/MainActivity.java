package com.mirea.sumachev.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private EditText editFileName;
    private EditText editText;
    private Button btnSave;
    private SharedPreferences sharedPreferences;
    private boolean isWork = false;
    private static final int REQUEST_CODE_PERMISSION_STORAGE = 100;
    private SharedPreferences preferences;
    final String SAVED_TEXT = "saved_text";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_PERMISSION_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isWork = true;
            } else {
                isWork = false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        editFileName = findViewById(R.id.editFileName);
        btnSave = findViewById(R.id.btnSave);

        preferences = getPreferences(MODE_PRIVATE);

        int storagePermissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
           ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION_STORAGE);
        }

        String path = preferences.getString(SAVED_TEXT, "Empty");
        if (isExternalStorageReadable()) {
            File file = new File(path);

            try {
                FileInputStream fin = openFileInput(file.getName());
                byte[] bytes = new byte[fin.available()];
                fin.read(bytes);
                String text = new String(bytes);
                fin.close();
                editText.setText(text);
            } catch (IOException e) {
                Log.e("error", e.getMessage());
            }

        }
    }

    public void onClick(View view) {

        if (isWork && isExternalStorageWritable()) {
            String fileName = editFileName.getText().toString();
            String text = editText.getText().toString();
            File file = getAlbumStorageDir(fileName);

            try {
                FileOutputStream outputStream = openFileOutput(file.getName(), Context.MODE_PRIVATE);
                outputStream.write(text.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getAlbumStorageDir(String albumName) {

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), albumName);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVED_TEXT, file.getAbsolutePath());
        editor.apply();
        if (!file.mkdirs()) {
            Log.e("error", "Directory not created");
        }
        return file;
    }
}