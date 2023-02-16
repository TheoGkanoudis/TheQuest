package com.example.quest.utilities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quest.fragments.ChatFragment;
import com.example.quest.fragments.HandbookFragment;
import com.example.quest.fragments.MapFragment;

public class LobbyViewPagerAdapter extends FragmentStateAdapter {
    public LobbyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MapFragment();
            case 1:
                return new ChatFragment();
            case 2:
                return new HandbookFragment();
            default:
                return new ChatFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
