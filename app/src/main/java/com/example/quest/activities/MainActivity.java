package com.example.quest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;

import com.example.quest.R;
import com.example.quest.fragments.LoginSignupFragment;
import com.example.quest.fragments.MainMenuFragment;
import com.example.quest.utilities.FragmentHelper;
import com.example.quest.utilities.PreferenceManager;
import com.example.quest.utilities.Variables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.prefs.PreferenceChangeEvent;

public class MainActivity extends AppCompatActivity{

    public static PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        setContentView(R.layout.activity_main);

        preferenceManager = new PreferenceManager(getApplicationContext());
        getSupportFragmentManager().beginTransaction().add(R.id.container, new LoginSignupFragment()).commit();
        if(preferenceManager.getBoolean(Variables.KEY_IS_SIGNED_IN)){
            MainMenuFragment mainMenuFragment = new MainMenuFragment();
            FragmentHelper.changeToFragment(getSupportFragmentManager(), mainMenuFragment, String.valueOf(R.string.main_menu_fragment_name));
        }
    }


}