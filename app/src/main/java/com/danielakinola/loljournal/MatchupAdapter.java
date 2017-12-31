package com.danielakinola.loljournal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Guest on 29/12/2017.
 */

public class MatchupAdapter extends RecyclerView.Adapter<MatchupAdapter.MatchupViewHolder> {
    private ArrayList<Champion> matchups;

    public MatchupAdapter(ArrayList<Champion> matchups) {
        this.matchups = matchups;
    }

    @Override
    public MatchupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_champion, parent, false);
        return new MatchupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatchupViewHolder holder, int position) {
        holder.matchupName.setText(matchups.get(position).getName());
        holder.matchupName.setBackgroundResource(R.drawable.annie_splash);
    }

    @Override
    public int getItemCount() {
        return matchups.size();
    }

    class MatchupViewHolder extends RecyclerView.ViewHolder {
        TextView matchupName;
        ImageView matchupImage;

        public MatchupViewHolder(View itemView) {
            super(itemView);
            matchupName = itemView.findViewById(R.id.text_champion_name);
            matchupImage = itemView.findViewById(R.id.img_champion_portrait);
        }
    }
}
