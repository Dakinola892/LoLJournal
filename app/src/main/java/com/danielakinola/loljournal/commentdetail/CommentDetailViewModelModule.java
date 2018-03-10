package com.danielakinola.loljournal.commentdetail;

import com.danielakinola.loljournal.editcomment.EditCommentActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class CommentDetailViewModelModule {
    @Provides
    @Named("commentId")
    String provideCommentId(CommentDetailActivity commentDetailActivity) {
        return commentDetailActivity.getIntent().getStringExtra(EditCommentActivity.COMMENT_ID);
    }
}
