package com.danielakinola.loljournal.champpool;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.databinding.ItemChampionBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by white on 31/12/2017.
 */

public class ChampionAdapter extends RecyclerView.Adapter<ChampionAdapter.StarredChampionViewHolder> {
    private final ChampPoolViewModel champPoolViewModel;
    private ArrayList<Champion> champions = new ArrayList<>();

    public ChampionAdapter(ChampPoolViewModel champPoolViewModel) {
        this.champPoolViewModel = champPoolViewModel;
    }

    public void setChampions(List<Champion> champions) {
        this.champions = (ArrayList<Champion>) champions;
    }

    @NonNull
    @Override
    public StarredChampionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemChampionBinding itemChampionBinding = ItemChampionBinding.inflate(inflater, parent, false);
        itemChampionBinding.setListener(new ChampionItemActionListener() {
            @Override
            public void onChampionClicked(Champion champion) {
                champPoolViewModel.getChampionDetailEvent().setValue(champion.getId());
            }

            @Override
            public void onFavouriteChanged(Champion champion, View v) {
                champPoolViewModel.updateFavourited(champion);
            }
        });
        return new StarredChampionViewHolder(itemChampionBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull StarredChampionViewHolder holder, int position) {
        holder.bind(champions.get(position));
    }

    @Override
    public int getItemCount() {
        return champions == null ? 0 : champions.size();
    }


    class StarredChampionViewHolder extends RecyclerView.ViewHolder {

        private final ItemChampionBinding itemChampionBinding;

        StarredChampionViewHolder(ItemChampionBinding itemChampionBinding) {
            super(itemChampionBinding.getRoot());
            this.itemChampionBinding = itemChampionBinding;
        }

        void bind(Champion champion) {
            itemChampionBinding.setChampion(champion);
            itemChampionBinding.executePendingBindings();
        }

    }
}
