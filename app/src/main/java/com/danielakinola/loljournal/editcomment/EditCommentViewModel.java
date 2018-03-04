package com.danielakinola.loljournal.editcomment;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.danielakinola.loljournal.SingleLiveEvent;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;

public class EditCommentViewModel extends AndroidViewModel {
    private final MatchupRepository matchupRepository;
    private final int REQUEST_CODE;
    //private String CommentId;
    private SingleLiveEvent<Void> confirmationEvent = new SingleLiveEvent<>();
    private Comment commentBeingEditedOrCreated;


    public EditCommentViewModel(@NonNull Application application, MatchupRepository matchupRepository, int commentId, int requestCode) {
        super(application);
        this.matchupRepository = matchupRepository;
        this.REQUEST_CODE = requestCode;
    }

    public Comment getComment() {
        return commentBeingEditedOrCreated;
    }

    public SingleLiveEvent<Void> getConfirmationEvent() {
        return confirmationEvent;
    }

    public void onConfirm() {
        confirmationEvent.setValue(null);
    }
}
