package cegepst.example.shareparks.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cegepst.example.shareparks.R;
import cegepst.example.shareparks.models.Constants;

public class AppSettingsFragment extends Fragment {

    private boolean isDarkModeOn;

    public static AppSettingsFragment newInstance() {
        AppSettingsFragment appSettingsFragment = new AppSettingsFragment();
        return appSettingsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        isDarkModeOn = sharedPreferences.getBoolean(Constants.PREF_DARK_MODE, false);
        ((Switch) getView().findViewById(R.id.darkModeSwitch)).setChecked(isDarkModeOn);
    }

    public boolean getIsDarkModeOn() {
        return ((Switch) getView().findViewById(R.id.darkModeSwitch)).isChecked();
    }
}
