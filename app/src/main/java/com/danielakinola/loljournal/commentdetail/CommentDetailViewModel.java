package com.danielakinola.loljournal.commentdetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.res.TypedArray;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.utils.SingleLiveEvent;
import com.danielakinola.loljournal.utils.SnackbarMessage;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CommentDetailViewModel extends ViewModel {
    private final MatchupRepository matchupRepository;
    private final SingleLiveEvent<Comment> editCommentEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Integer> deleteCommentEvent = new SingleLiveEvent<>();
    private final SnackbarMessage snackbarMessage = new SnackbarMessage();
    private LiveData<String> title;
    private LiveData<String> subtitle;
    private LiveData<Integer> logo;
    private LiveData<Matchup> matchup;
    private LiveData<Comment> comment;
    private int commentId;
    private String[] commentCategories;
    private TypedArray laneIcons;
    private String versusString;

    @Inject
    public CommentDetailViewModel(MatchupRepository matchupRepository,
                                  @Named("commentCategories") String[] commentCategories,
                                  @Named("actionBarIcons") TypedArray laneIcons,
                                  @Named("versus") String versusString) {
        this.matchupRepository = matchupRepository;
        this.commentCategories = commentCategories;
        this.laneIcons = laneIcons;
        this.versusString = versusString;
    }

    public void initialize(int commentId) {
        this.commentId = commentId;
        this.comment = matchupRepository.getComment(commentId);
        this.matchup = Transformations.switchMap(this.comment, comment -> matchupRepository.getMatchup(comment.getMatchupId()));
        this.title = Transformations.map(matchup, matchup -> String.format(versusString, matchup.getPlayerChampion(), matchup.getEnemyChampion()));
        this.subtitle = Transformations.map(comment, comment -> commentCategories[comment.getCategory()]);
        this.logo = Transformations.map(matchup, matchup -> laneIcons.getResourceId(matchup.getLane(), -1));
    }

    public void editComment() {
        editCommentEvent.setValue(comment.getValue());
    }

    public void favouriteComment() {
        Completable.fromAction(() -> matchupRepository.setCommentStarred(commentId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        snackbarMessage.setValue(R.string.comment_favourited);
                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbarMessage.setValue(R.string.error);
                    }
                });
    }

    public void deleteComment() {
        Completable.fromAction(() -> matchupRepository.deleteComment(comment.getValue()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        snackbarMessage.setValue(R.string.comment_deleted);
                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbarMessage.setValue(R.string.error);
                    }
                });
    }

    public SingleLiveEvent<Comment> getEditCommentEvent() {
        return editCommentEvent;
    }

    public SingleLiveEvent<Integer> getDeleteCommentEvent() {
        return deleteCommentEvent;
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

    public LiveData<String> getTitle() {
        return title;
    }

    public LiveData<String> getSubtitle() {
        return subtitle;
    }

    public LiveData<Integer> getLogo() {
        return logo;
    }

    public void onEdit(int resultCode) {
        if (resultCode == 1) {
            snackbarMessage.setValue(R.string.comment_plus_1);
        } else {
            snackbarMessage.setValue(R.string.error);
        }
    }
}
