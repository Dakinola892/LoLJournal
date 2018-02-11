package com.danielakinola.loljournal.dependencyinjection;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.champpool.ChampPoolViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChampPoolViewModel.class)
    abstract ViewModel bindChampPoolViewModel(ChampPoolViewModel champPoolViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
