package cegepst.example.shareparks.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.User;

public class SignUpActivity extends AppCompatActivity {

    private String firstName;
    private String lastName;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void onOpenBrowser(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://shareparks.toutsapprend.com/"));
        startActivity(browserIntent);
    }

    public void onSignUp(View view) {
        getEntries();
        if (firstName == null || lastName == null || username == null || password == null) {
            alert("You must fill out each input before proceeding");
            return;
        } else {
            makeUser();
        }
    }

    private void makeUser() {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        makeLocalFile(user);
        goToLogIn();
    }

    private void goToLogIn() {
        Intent main = new Intent(this, LogInActivity.class);
        startActivity(main);
    }

    private void makeLocalFile(User user) {
        String filename = "user_" + user.getUsername().toUpperCase();
        try {
            FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getEntries() {
        firstName = getEntry(R.id.firstNameInput);
        lastName = getEntry(R.id.lastNameInput);
        username = getEntry(R.id.usernameInput);
        password = getEntry(R.id.passwordInput);
    }

    private String getEntry(int inputId) {
        EditText input = findViewById(inputId);
        if (input.equals("")) {
            return null;
        }
        return input.getText().toString();
    }

    private void alert(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}