package com.danielakinola.loljournal.dependencyinjection;

import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.champpool.ChampPoolActivity;
import com.danielakinola.loljournal.commentdetail.CommentDetailActivity;
import com.danielakinola.loljournal.editcomment.EditCommentActivity;
import com.danielakinola.loljournal.matchupdetail.MatchupDetailActivity;
import com.danielakinola.loljournal.matchups.MatchupsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = {ChampPoolFragmentBuilder.class, ChampPoolActivityModule.class})
    abstract ChampPoolActivity contributeChampPoolActivity();

    @ContributesAndroidInjector()
    abstract MatchupsActivity contributeMatchupsActivity();

    @ContributesAndroidInjector()
    abstract ChampionSelectActivity contributeChampionListActivity();

    @ContributesAndroidInjector(modules = MatchupDetailFragmentBuilder.class)
    abstract MatchupDetailActivity contributeMatchupDetailActivity();

    @ContributesAndroidInjector()
    abstract CommentDetailActivity contributeCommentDetailActivity();

    @ContributesAndroidInjector()
    abstract EditCommentActivity contributeEditCommentActivity();

}
