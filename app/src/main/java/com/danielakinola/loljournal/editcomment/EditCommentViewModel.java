package com.danielakinola.loljournal.editcomment;

import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.utils.SingleLiveEvent;

import javax.inject.Inject;

import io.reactivex.annotations.Nullable;

public class EditCommentViewModel extends ViewModel {

    private final MatchupRepository matchupRepository;
    private SingleLiveEvent<Void> confirmationEvent = new SingleLiveEvent<>();
    private Comment editableComment;
    private boolean NEW_COMMENT;


    /*@Inject
    public EditCommentViewModel(MatchupRepository matchupRepository, @Named("editCommentId") int commentId) {
        this.matchupRepository = matchupRepository;
        this.editableComment = matchupRepository.getComment(commentId).getValue();
        this.NEW_COMMENT = false;
    }*/

    @Inject
    public EditCommentViewModel(MatchupRepository matchupRepository) {
        this.matchupRepository = matchupRepository;
    }

    public void initialize(int commentId, @Nullable String matchupId, int category) {
        if (commentId == -1) {
            this.editableComment = new Comment(matchupId, category);
            this.NEW_COMMENT = true;
        } else {
            this.editableComment = matchupRepository.getComment(commentId).getValue();
            this.NEW_COMMENT = false;
        }
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
