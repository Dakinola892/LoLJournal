package com.danielakinola.loljournal.editcomment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.data.models.Matchup;
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
    private LiveData<Comment> editableComment;
    private LiveData<Matchup> matchup;
    private boolean newComment;
    /*private String matchupId;
    private int category;*/


    /*@Inject
    public EditCommentViewModel(MatchupRepository matchupRepository, @Named("editCommentId") int commentId) {
        this.matchupRepository = matchupRepository;
        this.editableComment = matchupRepository.getComment(commentId).getValue();
        this.newComment = false;
    }*/

    @Inject
    public EditCommentViewModel(MatchupRepository matchupRepository) {
        this.matchupRepository = matchupRepository;
    }

    public void initialize(int commentId, String matchupId, int category) {
        this.matchup = matchupRepository.getMatchup(matchupId);
        if (commentId == -1) {
            this.editableComment = new MutableLiveData<>();
            ((MutableLiveData<Comment>) this.editableComment).setValue(new Comment(matchupId, category));
            this.newComment = true;
        } else {
            this.editableComment = matchupRepository.getComment(commentId);
            this.newComment = false;
        }
    }

    public LiveData<Comment> getComment() {
        return editableComment;
    }

    public LiveData<Matchup> getMatchup() {
        return matchup;
    }

    public boolean isNewComment() {
        return newComment;
    }

    //TODO: fix public/private acessibility & clean up code order

    public SingleLiveEvent<Void> getConfirmationEvent() {
        return confirmationEvent;
    }

    public void onConfirm(String title, String description) {
        saveComment(title, description);
        confirmationEvent.setValue(null);
    }

    private void saveComment(String title, String description) {

        Comment comment = editableComment.getValue();
        comment.setTitle(title);
        comment.setDescription(description);

        //todo: refactor to use common logic

        if (newComment) {
            Completable.fromAction(() -> matchupRepository.addComment(comment))
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
            Completable.fromAction(() -> matchupRepository.updateComment(comment))
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
