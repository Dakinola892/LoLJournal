package com.danielakinola.loljournal.matchups;

import com.danielakinola.loljournal.champpool.ChampPoolActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MatchupsViewModelModule {
    @Provides
    @Named("championId")
    String provideChampionId(MatchupsActivity matchupsActivity) {
        return matchupsActivity.getIntent().getStringExtra(ChampPoolActivity.PLAYER_CHAMPION_ID);
    }

}
