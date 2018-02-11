package com.danielakinola.loljournal.dependencyinjection;

import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.champpool.ChampPoolActivity;
import com.danielakinola.loljournal.matchups.MatchupsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = FragmentBuilder.class)
    abstract ChampPoolActivity champPoolActivity();

    @ContributesAndroidInjector
    abstract MatchupsActivity matchupsActivity();

    @ContributesAndroidInjector
    abstract ChampionSelectActivity championListActivity();

}
