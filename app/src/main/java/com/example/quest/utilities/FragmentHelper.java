package com.example.quest.utilities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quest.R;
import com.example.quest.fragments.GameOverviewFragment;

public class FragmentHelper {
    public static void changeToFragment(FragmentManager fragmentManager, Fragment fragment, String fragmentName, Boolean addToBackStack){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container,fragment);
        if(addToBackStack) transaction.addToBackStack(fragmentName);
        transaction.commit();
    }

}
