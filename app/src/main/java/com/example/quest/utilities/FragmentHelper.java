package com.example.quest.utilities;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quest.R;

public class FragmentHelper {
    public static void changeToFragment(FragmentManager fragmentManager, Fragment fragment, String fragmentName){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(fragmentName);
        transaction.commit();
    }


}
