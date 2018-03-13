package com.danielakinola.loljournal.dependencyinjection;

import com.danielakinola.loljournal.champpool.ChampPoolActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ChampPoolActivityModule {
    @Provides
    ChampPoolActivity.LanePagerAdapter provideLanePageAdapter(
            ChampPoolActivity champPoolActivity, @Named("laneTitles") String[] laneTitles) {
        return new ChampPoolActivity.LanePagerAdapter(
                champPoolActivity.getSupportFragmentManager(), laneTitles);
    }
}
