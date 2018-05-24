package com.danielakinola.loljournal.matchupdetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.utils.SingleLiveEvent;
import com.danielakinola.loljournal.utils.SnackbarMessage;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MatchupDetailViewModel extends ViewModel {
    private final MatchupRepository matchupRepository;
    private final SingleLiveEvent<Comment> addCommentEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> commentDetailEvent = new SingleLiveEvent<>();
    private final SnackbarMessage snackbarMessage = new SnackbarMessage();
    private String matchupId;
    private LiveData<Matchup> matchup;
    private int currentPage;


    @Inject
    MatchupDetailViewModel(MatchupRepository matchupRepository) {
        this.matchupRepository = matchupRepository;
    }

    public void initialize(String matchupId) {
        this.matchupId = matchupId;
        this.matchup = matchupRepository.getMatchup(matchupId);
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public LiveData<Matchup> getMatchup() {
        return matchup;
    }

    public void addComment() {
        addCommentEvent.setValue(new Comment(matchupId, currentPage));
    }

    public SingleLiveEvent<Comment> getAddCommentEvent() {
        return addCommentEvent;
    }

    public SingleLiveEvent<Integer> getCommentDetailEvent() {
        return commentDetailEvent;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public LiveData<List<Comment>> getComments(int category) {
        switch (category) {
            case 0:
                return matchupRepository.getStrengths(matchupId);
            case 1:
                return matchupRepository.getGeneralComments(matchupId);
            default:
                return matchupRepository.getWeaknesses(matchupId);
        }
    }

    public void changeCommentFavourited(Comment comment) {
        Completable.fromAction(() -> matchupRepository.setCommentStarred(comment.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        snackbarMessage.setValue(R.string.champion_favourited);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }

    public void onCommentPlusOned() {
        snackbarMessage.setValue(R.string.comment_plus_1);
    }

    public void onCommentSelected(Comment comment) {
        commentDetailEvent.setValue(comment.getId());
    }
}
