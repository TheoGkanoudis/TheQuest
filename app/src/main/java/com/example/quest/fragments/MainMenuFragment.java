package com.example.quest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quest.R;
import com.example.quest.activities.MainActivity;
import com.example.quest.utilities.FragmentHelper;
import com.example.quest.Models.Game;
import com.example.quest.utilities.GameAdapter;
import com.example.quest.utilities.Constants;
import com.example.quest.utilities.PreferenceManager;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainMenuFragment extends Fragment {

    String username;
    String welcomeMessage;

    private static GameAdapter gameAdapter;
    private static ViewPager2 gamesViewPager;

    int gamesAvailable;

    TextView welcomeTextView;
    FrameLayout mm_games_tabs;

    ImageButton signOutButton;

    public List<Game> games;
    public static Game viewedGame;

    static FragmentManager fragmentManager;

    public static PreferenceManager preferenceManager;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        fragmentManager = getParentFragmentManager();
        new TabLayoutMediator(tabLayout, gamesViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        // Use the adapter to set the text and icon for the tab
                        tab.setIcon(null);
                    }
                }).attach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_menu, container, false);
        welcomeTextView = view.findViewById(R.id.welcome_user);
        signOutButton = view.findViewById(R.id.my_profile);
        mm_games_tabs = view.findViewById(R.id.mm_games_tabs);
        gamesViewPager = view.findViewById(R.id.game_cards_viewpager);

        loadUserInfo();
        getToken();
        setListeners();
        gamesFinder();
        setViewPager();

        return view;
    }

    private void loadUserInfo(){
        preferenceManager = MainActivity.preferenceManager;
        username = preferenceManager.getString(Constants.KEY_USERNAME);
        welcomeMessage = String.format("Welcome, <b>%s</b>!", username);
        welcomeTextView.setText(Html.fromHtml(welcomeMessage,Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));
    }

    private void setListeners(){
        signOutButton.setOnClickListener(v -> signOut());
    }

    private void setViewPager(){
        gameAdapter = new GameAdapter(games);

        gamesViewPager.setAdapter(gameAdapter);
        gamesViewPager.setClipToPadding(false);
        gamesViewPager.setClipChildren(false);
        gamesViewPager.setOffscreenPageLimit(Constants.OFFSCREEN_PAGE_LIMIT);
        gamesViewPager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        gamesViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewedGame = games.get(position);
            }
        });

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = Math.abs(position);
                page.setScaleY(1 - 0.1f * r);
                page.setAlpha(1 - 0.5f * r);
                page.setElevation(10 - r * 5);
            }
        });


        gamesViewPager.setPageTransformer(compositePageTransformer);
    }

    private void showToast(String message){
        //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(getString(R.string.db_users_collection)).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unused -> showToast("Token update successful"))
                .addOnFailureListener(runnable -> showToast("Unable to update token"));
    }

    //finds local games and sets the games tabs visibility
    private void gamesFinder() {
        games = new ArrayList<>();

        //TODO: get dynamic data for games
        gamesAvailable = 5;
        for (int i = 0; i < gamesAvailable; i++){
            Game game = new Game();
            game.id = String.valueOf(i);
            game.title = "Game Number " + String.valueOf(i+1);
            game.difficulty = String.valueOf(Math.floorMod(i,3)+1);
            game.location = "Ermoupolis";
            String drawableName = "ic_launcher_background"+String.valueOf(i+1);
            String packageName = getContext().getPackageName();
            game.image = String.valueOf(getResources().getIdentifier(drawableName, "drawable", packageName));
            games.add(game);
        }
        mm_games_tabs.setVisibility(gamesAvailable>0?View.VISIBLE:View.GONE);
    }

    public static void changeToGameFragment() {
        GameOverviewFragment gameOverviewFragment = new GameOverviewFragment();
        FragmentHelper.changeToFragment(fragmentManager, gameOverviewFragment, String.valueOf(R.string.game_fragment_name), true);
    }


    private void signOut(){
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(getString(R.string.db_users_collection)).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getContext(), MainActivity.class));
                    getActivity().finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }

}
