package com.danielakinola.loljournal.dependencyinjection;

import com.danielakinola.loljournal.matchupdetail.MatchupDetailFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MatchupDetailFragmentBuilder {
    @ContributesAndroidInjector
    abstract MatchupDetailFragment contributeMatchupDetailFragment();
}
