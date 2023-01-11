package com.example.quest.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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
import com.example.quest.utilities.Game;
import com.example.quest.utilities.GameAdapter;
import com.example.quest.utilities.Variables;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainMenuFragment extends Fragment {

    String username;
    String welcomeMessage;

    private static GameAdapter gameAdapter;
    private static ViewPager2 gamesViewPager;

    int gamesAvailable;

    TextView welcomeTextView;
    FrameLayout mm_top_frame;
    FrameLayout mm_games_tabs;

    public List<Game> games;
    public static Game viewedGame;

    static FragmentManager fragmentManager;



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

        username = MainActivity.preferenceManager.getString(Variables.KEY_USERNAME);
        welcomeMessage = String.format("Welcome, <b>%s</b>!", username);
        welcomeTextView = view.findViewById(R.id.welcome_user);
        welcomeTextView.setText(Html.fromHtml(welcomeMessage,Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH));


        //ViewPager2 functionality
        games = new ArrayList<>();
        mm_games_tabs = view.findViewById(R.id.mm_games_tabs);

        gamesFinder();

        gamesViewPager = view.findViewById(R.id.game_cards_viewpager);
        gameAdapter = new GameAdapter(games);

        gamesViewPager.setAdapter(gameAdapter);
        gamesViewPager.setClipToPadding(false);
        gamesViewPager.setClipChildren(false);
        gamesViewPager.setOffscreenPageLimit(Variables.OFFSCREEN_PAGE_LIMIT);
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

        return view;
    }

    //finds local games and sets the games tabs visibility
    //TODO: get dynamic data for games
    private void gamesFinder() {
        gamesAvailable = 5;
        for (int i = 0; i < gamesAvailable; i++){
            Game game = new Game();
            game.id = i;
            game.title = "Game Number " + String.valueOf(i+1);
            game.difficulty = Math.floorMod(i,3)+1;
            game.location = "Ermoupolis";
            String drawableName = "ic_launcher_background"+String.valueOf(i+1);
            String packageName = getContext().getPackageName();
            game.image = getResources().getIdentifier(drawableName, "drawable", packageName);
            games.add(game);
        }
        mm_games_tabs.setVisibility(gamesAvailable>0?View.VISIBLE:View.GONE);
    }

    public static void changeToGameFragment(int id) {
        GameOverviewFragment gameOverviewFragment = new GameOverviewFragment();
        FragmentHelper.changeToFragment(fragmentManager, gameOverviewFragment, String.valueOf(R.string.game_fragment_name));
    }
}
