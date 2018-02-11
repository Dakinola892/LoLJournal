package com.danielakinola.loljournal;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.danielakinola.loljournal.champpool.ChampPoolViewModel;
import com.danielakinola.loljournal.data.MatchupRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final MatchupRepository matchupRepository;
    private final Application application;

    @Inject
    public ViewModelFactory(MatchupRepository matchupRepository, Application application) {
        this.matchupRepository = matchupRepository;
        this.application = application;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChampPoolViewModel.class)) {
            return (T) new ChampPoolViewModel(application, matchupRepository);
        } else {
            throw new IllegalArgumentException("Cannot locate relevant ViewModel");
        }
    }
}
