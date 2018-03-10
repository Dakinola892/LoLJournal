package com.danielakinola.loljournal.championselect;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ChampSelectViewModelModule {
    @Provides
    @Named("lane")
    int provideLane(ChampionSelectActivity championSelectActivity) {
        return championSelectActivity.getIntent().getIntExtra(ChampionSelectActivity.LANE, 0);
    }

    @Provides
    @Named("champName")
    String provideChampName(ChampionSelectActivity championSelectActivity) {
        return championSelectActivity.getIntent().getStringExtra(ChampionSelectActivity.CHAMP_NAME);
    }
}
