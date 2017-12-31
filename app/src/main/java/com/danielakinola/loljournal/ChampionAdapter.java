package com.danielakinola.loljournal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by white on 31/12/2017.
 */

public class ChampionAdapter extends RecyclerView.Adapter<ChampionViewHolder> {
    private ArrayList<Champion> champions;

    public ChampionAdapter(Set<Champion> champions) {
        this.champions = new ArrayList<Champion>(champions);
    }

    @Override
    public ChampionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_champion, parent, false);
        return new ChampionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChampionViewHolder holder, int position) {
        holder.textView.setText(champions.get(position).getName());
        holder.imageView.setBackgroundResource(R.drawable.annie_splash);

    }

    @Override
    public int getItemCount() {
        return champions.size();
    }
}
