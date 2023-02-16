package com.example.quest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.quest.R;
import com.example.quest.Models.Game;


public class GameOverviewFragment extends Fragment {

    ImageView gameImage;
    TextView gameTitle;
    TextView location;
    TextView description;
    ImageView dif1;
    ImageView dif2;
    ImageView dif3;
    ImageButton back;
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
        back = view.findViewById(R.id.back_button);

        //populate the views\
        location.setText(String.format(" %s",game.location));
        gameImage.setImageResource(Integer.parseInt(game.image));
        gameTitle.setText(game.title);
        description.setText(R.string.lorem50);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        if(Integer.parseInt(game.difficulty)>0) dif1.setForeground(ContextCompat.getDrawable(getContext(),R.drawable.selected_dot2));
        if(Integer.parseInt(game.difficulty)>1) dif2.setForeground(ContextCompat.getDrawable(getContext(),R.drawable.selected_dot2));
        if(Integer.parseInt(game.difficulty)>2) dif3.setForeground(ContextCompat.getDrawable(getContext(),R.drawable.selected_dot2));

        return view;
    }
}
