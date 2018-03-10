package com.danielakinola.loljournal.editcomment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class EditCommentViewModelModule {

    @Provides
    @Named("editCommentId")
    String provideCommentId(EditCommentActivity editCommentActivity) {
        return editCommentActivity.getIntent().getStringExtra(EditCommentActivity.COMMENT_ID);
    }

    @Provides
    @Named("newCommentMatchupId")
    String provideMatchupId(EditCommentActivity editCommentActivity) {
        return editCommentActivity.getIntent().getStringExtra(EditCommentActivity.MATCHUP_ID);
    }

    @Provides
    @Named("category")
    int provideCategory(EditCommentActivity editCommentActivity) {
        return editCommentActivity.getIntent().getIntExtra(EditCommentActivity.CATEGORY, 0);
    }
}
