package cegepst.example.shareparks.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

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
            Log.d("User", userPath);
        }
    }
}