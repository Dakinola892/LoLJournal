package com.danielakinola.loljournal.matchupdetail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.ChampionReference;
import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.SingleLiveEvent;
import com.danielakinola.loljournal.SnackbarMessage;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.data.models.Matchup;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class MatchupDetailViewModel extends ViewModel {
    private final MatchupRepository matchupRepository;
    private final String MATCHUP_ID;
    private SingleLiveEvent<Integer> addCommentEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<Integer> commentDetailEvent = new SingleLiveEvent<>();
    private final MutableLiveData<Matchup> matchup;
    private SnackbarMessage snackbarMessage = new SnackbarMessage();
    private int currentPage;
    //TODO: rename to ChampionGallery
    //TODO: finalize all possible variables with correct uppercase
    private final ChampionReference championReference;

    @Inject
    public MatchupDetailViewModel(MatchupRepository matchupRepository, @Named("matchupId") String matchupId, ChampionReference championReference) {
        this.matchupRepository = matchupRepository;
        this.MATCHUP_ID = matchupId;
        this.championReference = championReference;
        this.matchup = (MutableLiveData<Matchup>) matchupRepository.getMatchup(matchupId);
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public LiveData<Matchup> getMatchup() {
        return matchup;
    }

    public void addComment() {
        addCommentEvent.setValue(currentPage);
    }

    public SingleLiveEvent<Integer> getAddCommentEvent() {
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
                return matchupRepository.getStrengths(MATCHUP_ID);
            case 1:
                return matchupRepository.getGeneralComments(MATCHUP_ID);
            default:
                return matchupRepository.getWeaknesses(MATCHUP_ID);
        }
    }

    public void changeCommentFavourited(Comment comment) {
        matchupRepository.setCommentStarred(comment.getId());
    }

    public void onCommentPlusOned() {
        snackbarMessage.setValue(R.string.comment_plus_1);
    }

    public void onCommentSelected(Comment comment) {
        commentDetailEvent.setValue(comment.getId());
    }
}
