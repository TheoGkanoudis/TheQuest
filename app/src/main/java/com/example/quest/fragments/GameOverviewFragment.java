package com.example.quest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.quest.R;
import com.example.quest.utilities.Game;


public class GameOverviewFragment extends Fragment {

    ImageView gameImage;
    TextView gameTitle;
    TextView location;
    TextView description;
    ImageView dif1;
    ImageView dif2;
    ImageView dif3;
    Game game;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_overview, container, false);

        //set the views
        gameImage = view.findViewById(R.id.game_image);
        gameTitle = view.findViewById(R.id.game_title);
        location = view.findViewById(R.id.location);
        description = view.findViewById(R.id.game_description);
        dif1 = view.findViewById(R.id.difficulty1);
        dif2 = view.findViewById(R.id.difficulty2);
        dif3 = view.findViewById(R.id.difficulty3);
        game = MainMenuFragment.viewedGame;

        //populate the views\
        location.setText(String.format(" %s",game.location));
        gameImage.setImageResource(game.image);
        gameTitle.setText(game.title);
        description.setText(R.string.lorem50);

        if(game.difficulty>0) dif1.setForeground(ContextCompat.getDrawable(getContext(),R.drawable.selected_dot2));
        if(game.difficulty>1) dif2.setForeground(ContextCompat.getDrawable(getContext(),R.drawable.selected_dot2));
        if(game.difficulty>2) dif3.setForeground(ContextCompat.getDrawable(getContext(),R.drawable.selected_dot2));

        return view;
    }
}
