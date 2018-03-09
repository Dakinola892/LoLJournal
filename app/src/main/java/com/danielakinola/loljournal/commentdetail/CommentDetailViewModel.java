package com.danielakinola.loljournal.commentdetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.SingleLiveEvent;
import com.danielakinola.loljournal.SnackbarMessage;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;

public class CommentDetailViewModel extends ViewModel {
    private final MatchupRepository MATCHUP_REPOSITORY;
    private final LiveData<Comment> COMMENT;
    private final int COMMENT_ID;
    private final SingleLiveEvent<Integer> EDIT_COMMENT_EVENT = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> DELETE_COMMENT_EVENT = new SingleLiveEvent<>();
    private final SnackbarMessage snackbarMessage = new SnackbarMessage();

    public CommentDetailViewModel(MatchupRepository matchupRepository, int commentId) {
        this.MATCHUP_REPOSITORY = matchupRepository;
        this.COMMENT = matchupRepository.getComment(commentId);
        this.COMMENT_ID = commentId;
    }

    public void editComment() {
        EDIT_COMMENT_EVENT.setValue(COMMENT_ID);
    }

    public void favouriteComment() {
        MATCHUP_REPOSITORY.setCommentStarred(COMMENT_ID);
    }

    public SingleLiveEvent<Integer> getEditCommentEvent() {
        return EDIT_COMMENT_EVENT;
    }

    public SingleLiveEvent<Integer> getDeleteCommentEvent() {
        return DELETE_COMMENT_EVENT;
    }

    public void onSuccessfulEdit() {
        snackbarMessage.call();
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public LiveData<Comment> getComment() {
        return COMMENT;
    }
}
