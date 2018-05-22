package com.danielakinola.loljournal.commentdetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.utils.SingleLiveEvent;
import com.danielakinola.loljournal.utils.SnackbarMessage;

import javax.inject.Inject;

public class CommentDetailViewModel extends ViewModel {
    private final MatchupRepository matchupRepository;
    private final SingleLiveEvent<Comment> editCommentEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> deleteCommentEvent = new SingleLiveEvent<>();
    private LiveData<Comment> comment;
    private int commentId;
    private final SnackbarMessage snackbarMessage = new SnackbarMessage();
    private LiveData<Matchup> matchup;

    @Inject
    public CommentDetailViewModel(MatchupRepository matchupRepository) {
        this.matchupRepository = matchupRepository;
    }

    public void initialize(int commentId) {
        this.commentId = commentId;
        this.comment = matchupRepository.getComment(commentId);
        this.matchup = Transformations.switchMap(this.comment, comment -> matchupRepository.getMatchup(comment.getMatchupId()));
    }

    public void editComment() {
        editCommentEvent.setValue(comment.getValue());
    }

    public void favouriteComment() {
        matchupRepository.setCommentStarred(commentId);
    }

    public SingleLiveEvent<Comment> getEditCommentEvent() {
        return editCommentEvent;
    }

    public SingleLiveEvent<Integer> getDeleteCommentEvent() {
        return deleteCommentEvent;
    }

    public void onSuccessfulEdit() {
        snackbarMessage.setValue(R.string.comment_plus_1);
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public LiveData<Comment> getComment() {
        return comment;
    }

    public LiveData<Matchup> getMatchup() {
        return matchup;
    }

}
