package cegepst.example.shareparks.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.User;

public class LogInActivity extends AppCompatActivity {

    private User user;
    private boolean isPasswordOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        user = new User();
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
        String filename = "user_" + username.toUpperCase();
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
        } else {
            isPasswordOk = false;
            alert("The password is incorrect...");
        }
    }

    private void alert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void startMain() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }
}