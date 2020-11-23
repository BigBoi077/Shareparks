package cegepst.example.shareparks.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.User;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 1;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private User user;
    private Bitmap image;
    private String userPath;

    private void initDrawerNavigation() {
        DrawerLayout drawerLayout = findViewById(R.id.menuDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.action_open, R.string.action_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        NavigationView navigationView = findViewById(R.id.menuDrawerNav);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.idHome:
                    return true;
                case R.id.idSettings:
                    return true;
                default:
                    return false;
            }
        });
    }

    private void initBottomNavigation() {
        BottomNavigationView navigationView = findViewById(R.id.menuBottom);
        navigationView.setSelectedItemId(R.id.idHome);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.idHome:
                    return true;
                case R.id.idMap:
                    return true;
                case R.id.idPost:
                    startPostFragment();
                    return true;
                case R.id.idMarket:
                    return true;
                case R.id.idAccount:
                    return true;
                default:
                    return false;
            }
        });
    }

    private void startPostFragment() {
        CreatePostFragment createPostFragment = CreatePostFragment.newInstance(user.getUsername());
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, createPostFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.actionAcountManagement:
                return true;
            case R.id.actionReportBug:
                return true;
            case R.id.actionTerminateSession:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getIntent().hasExtra("user_path")) {
            userPath = getIntent().getStringExtra("user_path");
            getCurrentUser();
            saveConnectionInformation(true);
        }
        initBottomNavigation();
        initDrawerNavigation();
    }

    public void startCameraActivity(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE_CAMERA || resultCode != RESULT_OK) {
            return;
        }
        Bundle extras = data.getExtras();
        image = (Bitmap) extras.get("data");
    }

    private void saveConnectionInformation(boolean isConnected) {
        try {
            FileOutputStream fileOutputStream = openFileOutput(userPath, MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            user.setConnected(isConnected);
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
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