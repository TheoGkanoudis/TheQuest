package com.example.quest.activities;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.TabWidget;

import com.example.quest.R;
import com.example.quest.fragments.ChatFragment;
import com.example.quest.utilities.LobbyViewPagerAdapter;
import com.example.quest.utilities.PreferenceManager;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class GameLobbyActivity extends AppCompatActivity {

    public static PreferenceManager preferenceManager;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    LobbyViewPagerAdapter lobbyViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_lobby);
        preferenceManager = MainActivity.preferenceManager;

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.lobby_view_pager);
        lobbyViewPagerAdapter = new LobbyViewPagerAdapter(this);
        viewPager2.setAdapter(lobbyViewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
        viewPager2.setCurrentItem(1);
    }
}