package com.danielakinola.loljournal.matchupdetail;

import com.danielakinola.loljournal.editcomment.EditCommentActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MatchupDetailViewModelModule {
    @Provides
    @Named("commentMatchupId")
    String provideMatchupId(MatchupDetailActivity matchupDetailActivity) {
        return matchupDetailActivity.getIntent().getStringExtra(EditCommentActivity.MATCHUP_ID);
    }
}
