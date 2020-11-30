package cegepst.example.shareparks.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.Constants;
import cegepst.example.shareparks.models.User;

public class LogInActivity extends AppCompatActivity {

    private User user;
    private String filename;
    private boolean isPasswordOk;
    private boolean isDarkModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        toggleDarkMode();
        if (isLastUserConnected()) {
            Log.d("Was connected", "true");
            startMain();
        }
        user = new User();
    }

    private boolean isLastUserConnected() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String lastUser = preferences.getString(Constants.PREF_LAST_USER, "");
        try {
            FileInputStream fileInputStream = openFileInput(lastUser);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            user = (User) objectInputStream.readObject();
            filename = "user_" + user.getUsername().toUpperCase();
            Log.d("user", String.valueOf(user.isConnected()));
            return user.isConnected();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void onOpenBrowser(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://shareparks.toutsapprend.com/"));
        startActivity(browserIntent);
    }

    public void onLaunchSignUp(View view) {
        Intent signUp = new Intent(this, SignUpActivity.class);
        startActivity(signUp);
    }

    public void onLogIn(View view) {
        EditText usernameInput = findViewById(R.id.usernameInput);
        String username = usernameInput.getText().toString();
        EditText passwordInput = findViewById(R.id.passwordInput);
        String password = passwordInput.getText().toString();
        if (fileExist(username, password) && isPasswordOk) {
            startMain();
        }
    }

    private boolean fileExist(String username, String password) {
        filename = "user_" + username.toUpperCase();
        try {
            FileInputStream fileInputStream = openFileInput(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            user = (User) objectInputStream.readObject();
            String filePassword = user.getPassword();
            comparePasswords(password, filePassword);
            fileInputStream.close();
            objectInputStream.close();
            return true;
        } catch (ClassNotFoundException | IOException e) {
            alert("User does not exist...");
            return false;
        }
    }

    private void comparePasswords(String password, String filePassword) {
        if (filePassword.equals(password)) {
            isPasswordOk = true;
            savePreferences();
        } else {
            isPasswordOk = false;
            alert("The password is incorrect...");
        }
    }

    private void savePreferences() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.PREF_LAST_USER, filename);
        editor.apply();
    }

    private void alert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void startMain() {
        Intent main = new Intent(this, MainActivity.class);
        main.putExtra("user_path", filename);
        startActivity(main);
    }

    public void toggleDarkMode() {
        getDartkModePreference();
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void getDartkModePreference() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isDarkModeOn = sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false);
    }
}