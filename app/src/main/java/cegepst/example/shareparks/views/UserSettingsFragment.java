package cegepst.example.shareparks.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.User;

public class UserSettingsFragment extends Fragment {

    private User user;

    public static UserSettingsFragment newInstance(User user) {
        UserSettingsFragment userSettingsFragment = new UserSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        userSettingsFragment.setArguments(args);
        return userSettingsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.user =
                (User) getArguments().getSerializable("user");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public String getFirstNameInput() {
        String input = ((EditText) getView().findViewById(R.id.firstNameInput)).getText().toString();
        return input.equals("") ? user.getFirstName() : input;
    }

    public String getLastNameInput() {
        String input = ((EditText) getView().findViewById(R.id.lastNameInput)).getText().toString();
        return input.equals("") ? user.getLastName() : input;
    }

    public String getNewPasswordInput() {
        String input = ((EditText) getView().findViewById(R.id.passwordInput)).getText().toString();
        return input.equals("") ? user.getPassword() : input;
    }

    public String getPersonalizedMessage() {
        String input = ((EditText) getView().findViewById(R.id.personalizedMessageInput)).getText().toString();
        return input;
    }
}
