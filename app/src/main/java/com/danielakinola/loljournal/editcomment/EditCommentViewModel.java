package com.danielakinola.loljournal.editcomment;

import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.utils.SingleLiveEvent;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditCommentViewModel extends ViewModel {

    private final MatchupRepository matchupRepository;
    private SingleLiveEvent<Void> confirmationEvent = new SingleLiveEvent<>();
    private Comment editableComment;
    private boolean NEW_COMMENT;
    /*private String matchupId;
    private int category;*/


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

    public void initialize(int commentId, String matchupId, int category) {
        /*this.matchupId = matchupId;
        this.category = category;*/
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

    public void onConfirm(String title, String description) {
        saveComment(title, description);
        confirmationEvent.setValue(null);

    }

    private void saveComment(String title, String description) {

        editableComment.setTitle(title);
        editableComment.setDescription(description);

        if (NEW_COMMENT) {
            Completable.fromAction(() -> matchupRepository.addComment(editableComment))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });


        } else {
            Completable.fromAction(() -> matchupRepository.updateComment(editableComment))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                    });
        }
    }
}
