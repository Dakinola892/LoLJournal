package com.danielakinola.loljournal.editcomment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.utils.SingleLiveEvent;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditCommentViewModel extends ViewModel {

    private final MatchupRepository matchupRepository;
    private final String[] commentCategories;
    private final String[] laneTitles;
    private final SingleLiveEvent<Integer> confirmationEvent;
    private LiveData<String> title;
    private LiveData<String> subtitle;
    private LiveData<Comment> editableComment;
    private LiveData<Matchup> matchup;
    private boolean newComment;


    @Inject
    EditCommentViewModel(MatchupRepository matchupRepository, @Named("laneTitles") String[] laneTitles,
                         @Named("commentCategoriesSingular") String[] commentCategories,
                         SingleLiveEvent<Integer> confirmationEvent) {
        this.matchupRepository = matchupRepository;
        this.commentCategories = commentCategories;
        this.laneTitles = laneTitles;
        this.confirmationEvent = confirmationEvent;
    }

    public void initialize(int commentId, String matchupId, int category) {
        this.matchup = matchupRepository.getMatchup(matchupId);
        if (commentId == -1) {
            editableComment = new MutableLiveData<>();
            ((MutableLiveData<Comment>) editableComment).setValue(new Comment(matchupId, category));
            newComment = true;
        } else {
            editableComment = matchupRepository.getComment(commentId);
            newComment = false;
        }

        this.title = Transformations.map(editableComment, comment -> {
            String titleStart = newComment ? "Adding new Matchup " : "Editing Matchup ";
            String categoryString = commentCategories[comment.getCategory()];
            return titleStart + categoryString;
        });
        this.subtitle = Transformations.map(matchup, matchup -> {
            String laneString = laneTitles[matchup.getLane()];
            return String.format("%s vs. %s %s", matchup.getPlayerChampion(), matchup.getEnemyChampion(), laneString);
        });
    }

    public LiveData<Comment> getComment() {
        return editableComment;
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

    public SingleLiveEvent<Integer> getConfirmationEvent() {
        return confirmationEvent;
    }

    public void saveComment(String title, String detail) {

        Comment comment = editableComment.getValue();
        assert comment != null;
        comment.setTitle(title);
        comment.setDetail(detail);

        Completable saveComment = newComment ? Completable.fromAction(() -> matchupRepository.addComment(comment)) : Completable.fromAction(() -> matchupRepository.updateComment(comment));
        saveComment
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        confirmationEvent.setValue(1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        confirmationEvent.setValue(-1);
                    }
                });

    }
}
