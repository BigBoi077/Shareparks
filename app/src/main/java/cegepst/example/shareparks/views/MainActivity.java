package cegepst.example.shareparks.views;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.Post;
import cegepst.example.shareparks.models.User;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_IMAGES_SELECTION = 2;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CreatePostFragment createPostFragment;
    private FeedFragment feedFragment;
    private User user;
    private Bitmap image;
    private String userPath;
    private ArrayList<Post> posts;

    private void initDrawerNavigation() {
        DrawerLayout drawerLayout = findViewById(R.id.menuDrawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.actionOpen, R.string.actionClose);
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, feedFragment).commit();
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

    private void disconnectUserDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.disconnectSession)
                .setMessage(R.string.terminateSessionMessage)
                .setPositiveButton(R.string.close_session_true, (dialogInterface, i) -> disconnectUser())
                .setNegativeButton(R.string.close_session_false, null).show();
    }

    private void reportBugDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.report_bug_dialog, null))
                .setPositiveButton(R.string.reportBug, (dialog, id) -> {
                    alert("Bug has been reported to the developpers !");
                })
                .setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    private void startPostFragment() {
        createPostFragment = CreatePostFragment.newInstance(user.getUsername());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, createPostFragment).commit();
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
                reportBugDialog();
                return true;
            case R.id.actionTerminateSession:
                disconnectUserDialog();
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
            alert(user.getUsername() + " is connected!");
        }
        initBottomNavigation();
        initDrawerNavigation();
        initFeedContent();
    }

    private void initFeedContent() {
        posts = new ArrayList<>();
        feedFragment = FeedFragment.newInstance(posts);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, feedFragment).commit();

    }

    public void actionPost(View view) {
        posts.add(createPostFragment.makePost());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, feedFragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_IMAGES_SELECTION) {
            mangePhotoSelection(data);
            return;
        }
        Bundle extras = data.getExtras();
        image = (Bitmap) extras.get("data");
        createPostFragment.passImage(image);
        createPostFragment.getDescription();
    }

    private void mangePhotoSelection(@Nullable Intent data) {
        try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ImageView imageView = createPostFragment.getView().findViewById(R.id.imageView);
            imageView.setImageBitmap(selectedImage);
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            image = drawable.getBitmap();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        createPostFragment.passImage(image);
        createPostFragment.getDescription();
    }

    public void startCameraActivity(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }

    public void launchPhotoSelection(View view) {
        Intent photoSelection = new Intent();
        photoSelection.setAction(Intent.ACTION_PICK);
        photoSelection.setDataAndType(Uri.parse("content://media/external/images/media"), "image/*");
        startActivityForResult(photoSelection, REQUEST_IMAGES_SELECTION);
    }

    private void disconnectUser() {
        saveConnectionInformation(false);
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
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

    private void alert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}