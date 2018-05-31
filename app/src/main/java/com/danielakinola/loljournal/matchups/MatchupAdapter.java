package com.danielakinola.loljournal.matchups;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.databinding.ItemMatchupChampionBinding;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;

import java.util.ArrayList;
import java.util.List;

public class MatchupAdapter extends RecyclerView.Adapter<MatchupAdapter.MatchupViewHolder> {
    private List<Matchup> matchups = new ArrayList<>();
    private MatchupsViewModel matchupsViewModel;

    public MatchupAdapter(MatchupsViewModel matchupsViewModel) {
        this.matchupsViewModel = matchupsViewModel;
    }

    @Override
    public MatchupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMatchupChampionBinding itemMatchupChampionBinding = ItemMatchupChampionBinding.inflate(inflater, parent, false);
        itemMatchupChampionBinding.setListener(new MatchupItemActionListener() {
            @Override
            public void onMatchupClicked(Matchup matchup) {
                matchupsViewModel.navigateToMatchupDetail(matchup.getId());
            }

            @Override
            public void onFavouriteChanged(Matchup matchup, View v) {
                matchupsViewModel.updateFavourited(matchup);
            }

            @Override
            public void onDeleteClicked(Matchup matchup) {
                matchupsViewModel.getDeleteMatchupEvent().setValue(matchup);
            }
        });

        MatchupViewHolder holder = new MatchupViewHolder(itemMatchupChampionBinding);

        MaterialFavoriteButton favoriteButton = itemMatchupChampionBinding.getRoot().findViewById(R.id.matchup_favorite_button);
        favoriteButton.setOnClickListener(v -> {
            Matchup matchup = itemMatchupChampionBinding.getMatchup() != null ? itemMatchupChampionBinding.getMatchup() : matchups.get(holder.getAdapterPosition());
            matchupsViewModel.updateFavourited(matchup);
        });
        holder.setItemMatchupChampionBinding(itemMatchupChampionBinding);

        return holder;
    }

    @Override
    public void onBindViewHolder(MatchupViewHolder holder, int position) {
        holder.bind(matchups.get(position));
    }

    @Override
    public int getItemCount() {
        return matchups.size();
    }

    class MatchupViewHolder extends RecyclerView.ViewHolder {
        private ItemMatchupChampionBinding itemMatchupChampionBinding;
        private MaterialFavoriteButton materialFavoriteButton;

        MatchupViewHolder(ItemMatchupChampionBinding itemMatchupChampionBinding) {
            super(itemMatchupChampionBinding.getRoot());
            this.itemMatchupChampionBinding = itemMatchupChampionBinding;
            this.materialFavoriteButton = itemMatchupChampionBinding.getRoot().findViewById(R.id.matchup_favorite_button);
        }

        void bind(Matchup matchup) {
            itemMatchupChampionBinding.setMatchup(matchup);
            itemMatchupChampionBinding.executePendingBindings();
            materialFavoriteButton.setFavorite(matchup.isStarred(), false);
        }

        public void setItemMatchupChampionBinding(ItemMatchupChampionBinding itemMatchupChampionBinding) {
            this.itemMatchupChampionBinding = itemMatchupChampionBinding;
        }
    }

    public void setMatchups(List<Matchup> matchups) {
        this.matchups = matchups;
    }
}
