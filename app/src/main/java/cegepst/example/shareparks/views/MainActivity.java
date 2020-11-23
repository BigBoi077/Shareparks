package cegepst.example.shareparks.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.User;

public class MainActivity extends AppCompatActivity {

    private User user;
    private String userPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().hasExtra("user_path")) {
            userPath = getIntent().getStringExtra("user_path");
            getCurrentUser();
        }
    }

    private void getCurrentUser() {
        try {
            FileInputStream fileInputStream = openFileInput(userPath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            user = (User) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}