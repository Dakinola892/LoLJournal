package com.danielakinola.loljournal.dependencyinjection;

import com.danielakinola.loljournal.championselect.ChampSelectViewModelModule;
import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.champpool.ChampPoolActivity;
import com.danielakinola.loljournal.commentdetail.CommentDetailActivity;
import com.danielakinola.loljournal.commentdetail.CommentDetailViewModelModule;
import com.danielakinola.loljournal.editcomment.EditCommentActivity;
import com.danielakinola.loljournal.editcomment.EditCommentViewModelModule;
import com.danielakinola.loljournal.matchupdetail.MatchupDetailActivity;
import com.danielakinola.loljournal.matchupdetail.MatchupDetailViewModelModule;
import com.danielakinola.loljournal.matchups.MatchupsActivity;
import com.danielakinola.loljournal.matchups.MatchupsViewModelModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = ChampPoolFragmentBuilder.class)
    abstract ChampPoolActivity contributeChampPoolActivity();

    @ContributesAndroidInjector(modules = MatchupsViewModelModule.class)
    abstract MatchupsActivity contributeMatchupsActivity();

    @ContributesAndroidInjector(modules = ChampSelectViewModelModule.class)
    abstract ChampionSelectActivity contributeChampionListActivity();

    @ContributesAndroidInjector(modules = {MatchupDetailViewModelModule.class, MatchupDetailFragmentBuilder.class})
    abstract MatchupDetailActivity contributeMatchupDetailActivity();

    @ContributesAndroidInjector(modules = CommentDetailViewModelModule.class)
    abstract CommentDetailActivity contributeCommentDetailActivity();

    @ContributesAndroidInjector(modules = EditCommentViewModelModule.class)
    abstract EditCommentActivity contributeEditCommentActivity();

}
