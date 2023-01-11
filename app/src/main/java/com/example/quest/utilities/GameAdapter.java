package com.example.quest.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quest.R;
import com.example.quest.fragments.MainMenuFragment;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> games;
    static View.OnClickListener myOnClickListener;

    public GameAdapter(List<Game> games){
        this.games = games;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card, parent, false);
        return new GameViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        holder.setGameData(games.get(position));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private ImageView thumbImageView;

        GameViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.game_title);
            thumbImageView = itemView.findViewById(R.id.game_thumb);
            myOnClickListener = new MyOnClickListener(itemView.getContext());
            itemView.setOnClickListener(myOnClickListener);
        }

        public void setGameData(Game game) {
            titleTextView.setText(game.title);
            thumbImageView.setImageResource(game.image);
            itemView.setId(game.id);
            itemView.setOnClickListener(myOnClickListener);
        }
    }


    private static class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;

        }

        @Override
        public void onClick(View v) {
            MainMenuFragment.changeToGameFragment(v.getId());
        }
    }

}
