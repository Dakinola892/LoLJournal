package com.danielakinola.loljournal.matchups;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.databinding.ItemMatchupChampionBinding;

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
        });
        return new MatchupViewHolder(itemMatchupChampionBinding);
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
        private final ItemMatchupChampionBinding itemMatchupChampionBinding;

        MatchupViewHolder(ItemMatchupChampionBinding itemMatchupChampionBinding) {
            super(itemMatchupChampionBinding.getRoot());
            this.itemMatchupChampionBinding = itemMatchupChampionBinding;
        }

        void bind(Matchup matchup) {
            itemMatchupChampionBinding.setMatchup(matchup);
            itemMatchupChampionBinding.executePendingBindings();
        }
    }

    public void setMatchups(List<Matchup> matchups) {
        this.matchups = matchups;
    }
}
