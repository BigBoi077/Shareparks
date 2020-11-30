package cegepst.example.shareparks.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.appcompat.app.AppCompatDelegate;
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
import cegepst.example.shareparks.models.Constants;
import cegepst.example.shareparks.models.Post;
import cegepst.example.shareparks.models.User;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CreatePostFragment createPostFragment;
    private FeedFragment feedFragment;
    private UserSettingsFragment userSettingsFragment;
    private AppSettingsFragment appSettingsFragment;
    private User user;
    private Bitmap image;
    private String userPath;
    private String customMessage;
    private ArrayList<Post> posts;
    private boolean isDarkModeOn;

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
                    refreshFeed();
                    return true;
                case R.id.idSettings:
                    startAppSettingsFragment();
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
                    refreshFeed();
                    return true;
                case R.id.idMap:
                    return true;
                case R.id.idPost:
                    startPostFragment();
                    return true;
                case R.id.idMarket:
                    return true;
                case R.id.idAccount:
                    startUserSettingsFragment();
                    return true;
                default:
                    return false;
            }
        });
    }

    private void startAppSettingsFragment() {
        appSettingsFragment = AppSettingsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, appSettingsFragment).commit();
    }

    private void startUserSettingsFragment() {
        userSettingsFragment = UserSettingsFragment.newInstance(user);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, userSettingsFragment).commit();
    }

    private void refreshFeed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, feedFragment).commit();
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
                startUserSettingsFragment();
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
            toggleLogInMessage();
        }
        initBottomNavigation();
        initDrawerNavigation();
        initFeedContent();
    }

    private void toggleLogInMessage() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        customMessage = preferences.getString(Constants.PREF_MESSAGES, "");
        if (customMessage.equals("")) {
            alert(user.getUsername() + " is connected!");
        } else {
            alert(customMessage);
        }
    }

    private void initFeedContent() {
        posts = new ArrayList<>();
        feedFragment = FeedFragment.newInstance(posts);
        refreshFeed();
    }

    public void actionPost(View view) {
        posts.add(createPostFragment.makePost());
        refreshFeed();
    }

    public void toggleDarkMode(View view) {
        saveDarkModePreference();
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void saveDarkModePreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        isDarkModeOn = sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false);
        editor.putBoolean(Constants.PREF_DARK_MODE, appSettingsFragment.getIsDarkModeOn());
        editor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == Constants.REQUEST_IMAGES_SELECTION) {
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

    public void saveUserInformation(View view) {
        try {
            FileOutputStream fileOutputStream = openFileOutput(userPath, MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            user.setFirstName(userSettingsFragment.getFirstNameInput());
            user.setLastName(userSettingsFragment.getLastNameInput());
            user.setPassword(userSettingsFragment.getNewPasswordInput());
            customMessage = userSettingsFragment.getPersonalizedMessage();
            managePersonalizedMessage();
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            alert("Something did not go as expected please try again . . .");
            e.printStackTrace();
        }
        alert("New information saved");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, feedFragment).commit();
    }

    private void managePersonalizedMessage() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREF_MESSAGES, customMessage);
        editor.apply();
    }

    public void startCameraActivity(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
    }

    public void launchPhotoSelection(View view) {
        Intent photoSelection = new Intent();
        photoSelection.setAction(Intent.ACTION_PICK);
        photoSelection.setDataAndType(Uri.parse("content://media/external/images/media"), "image/*");
        startActivityForResult(photoSelection, Constants.REQUEST_IMAGES_SELECTION);
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