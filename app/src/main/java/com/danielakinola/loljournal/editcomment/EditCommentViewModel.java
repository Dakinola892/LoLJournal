package com.danielakinola.loljournal.editcomment;

import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.SingleLiveEvent;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;

import javax.inject.Inject;
import javax.inject.Named;

public class EditCommentViewModel extends ViewModel {

    private final MatchupRepository matchupRepository;
    private SingleLiveEvent<Void> confirmationEvent = new SingleLiveEvent<>();
    private final Comment editableComment;
    private final boolean NEW_COMMENT;


    @Inject
    public EditCommentViewModel(MatchupRepository matchupRepository, @Named("editCommentId") int commentId) {
        this.matchupRepository = matchupRepository;
        this.editableComment = matchupRepository.getComment(commentId).getValue();
        this.NEW_COMMENT = false;
    }

    @Inject
    public EditCommentViewModel(MatchupRepository matchupRepository, @Named("newCommentMatchupId") String matchupId, @Named("category") int category) {
        this.matchupRepository = matchupRepository;
        this.editableComment = new Comment(matchupId, category);
        this.NEW_COMMENT = true;
    }

    public Comment getComment() {
        return editableComment;
    }

    public SingleLiveEvent<Void> getConfirmationEvent() {
        return confirmationEvent;
    }

    public void onConfirm() {
        saveComment();
        confirmationEvent.setValue(null);
    }

    private void saveComment() {
        if (NEW_COMMENT) {
            matchupRepository.saveComment(editableComment);
        } else {
            matchupRepository.updateComment(editableComment);
        }
    }
}
