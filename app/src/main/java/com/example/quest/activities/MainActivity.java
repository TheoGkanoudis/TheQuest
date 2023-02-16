package com.example.quest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import com.example.quest.R;
import com.example.quest.fragments.LoginSignupFragment;
import com.example.quest.fragments.MainMenuFragment;
import com.example.quest.utilities.FragmentHelper;
import com.example.quest.utilities.PreferenceManager;
import com.example.quest.utilities.Constants;

public class MainActivity extends AppCompatActivity{

    public static PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(getApplicationContext());
        getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginSignupFragment()).commit();
        if(preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            MainMenuFragment mainMenuFragment = new MainMenuFragment();
            FragmentHelper.changeToFragment(getSupportFragmentManager(), mainMenuFragment, String.valueOf(R.string.main_menu_fragment_name),false);
        }
    }


}