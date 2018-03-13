package com.danielakinola.loljournal.commentdetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.utils.SingleLiveEvent;
import com.danielakinola.loljournal.utils.SnackbarMessage;

import javax.inject.Inject;

public class CommentDetailViewModel extends ViewModel {
    private final MatchupRepository MATCHUP_REPOSITORY;
    private final SingleLiveEvent<Comment> EDIT_COMMENT_EVENT = new SingleLiveEvent<>();
    private LiveData<Comment> COMMENT;
    private int COMMENT_ID;
    private final SingleLiveEvent<Integer> DELETE_COMMENT_EVENT = new SingleLiveEvent<>();
    private final SnackbarMessage snackbarMessage = new SnackbarMessage();

    @Inject
    public CommentDetailViewModel(MatchupRepository matchupRepository) {
        this.MATCHUP_REPOSITORY = matchupRepository;
    }

    public void initialize(int commentId) {
        this.COMMENT_ID = commentId;
        this.COMMENT = MATCHUP_REPOSITORY.getComment(commentId);
    }

    public void editComment() {
        EDIT_COMMENT_EVENT.setValue(COMMENT.getValue());
    }

    public void favouriteComment() {
        MATCHUP_REPOSITORY.setCommentStarred(COMMENT_ID);
    }

    public SingleLiveEvent<Comment> getEditCommentEvent() {
        return EDIT_COMMENT_EVENT;
    }

    public SingleLiveEvent<Integer> getDeleteCommentEvent() {
        return DELETE_COMMENT_EVENT;
    }

    public void onSuccessfulEdit() {
        snackbarMessage.setValue(R.string.comment_plus_1);
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public LiveData<Comment> getComment() {
        return COMMENT;
    }

}
