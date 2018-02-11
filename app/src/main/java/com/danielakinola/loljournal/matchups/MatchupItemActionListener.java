package com.danielakinola.loljournal.matchups;

import android.view.View;

import com.danielakinola.loljournal.data.models.Matchup;

public interface MatchupItemActionListener {
    void onMatchupClicked(Matchup matchup);

    void onFavouriteChanged(Matchup matchup, View v);

}
