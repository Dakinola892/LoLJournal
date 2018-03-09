package com.danielakinola.loljournal.dependencyinjection;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.championselect.ChampionSelectViewModel;
import com.danielakinola.loljournal.champpool.ChampPoolViewModel;
import com.danielakinola.loljournal.commentdetail.CommentDetailViewModel;
import com.danielakinola.loljournal.editcomment.EditCommentViewModel;
import com.danielakinola.loljournal.matchupdetail.MatchupDetailViewModel;
import com.danielakinola.loljournal.matchups.MatchupsViewModel;

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
    @IntoMap
    @ViewModelKey(MatchupsViewModel.class)
    abstract ViewModel bindMatchupsViewModel(MatchupsViewModel matchupsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MatchupDetailViewModel.class)
    abstract ViewModel bindMatchupDetailViewModel(MatchupDetailViewModel matchupDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CommentDetailViewModel.class)
    abstract ViewModel bindCommentDetailViewModel(CommentDetailViewModel commentDetailViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChampionSelectViewModel.class)
    abstract ViewModel bindChampionSelectViewModel(ChampionSelectViewModel championSelectViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(EditCommentViewModel.class)
    abstract ViewModel bindEditCommentViewModel(EditCommentViewModel editCommentViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
