package com.danielakinola.loljournal.dependencyinjection;

import com.danielakinola.loljournal.champpool.ChampPoolFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuilder {
    @ContributesAndroidInjector
    abstract ChampPoolFragment champPoolFragment();
}
