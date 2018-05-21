package com.danielakinola.loljournal.matchups;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.utils.SingleLiveEvent;
import com.danielakinola.loljournal.utils.SnackbarMessage;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//TODO: FINALIZE, check Dagger2 solution to constructor variables for factory

public class MatchupsViewModel extends ViewModel {

    private final MatchupRepository matchupRepository;
    private String championId;
    private LiveData<Champion> champion;
    //private String laneSubtitle;

    private LiveData<List<Matchup>> matchups;
    private SingleLiveEvent<String> openMatchupDetailEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> editMatchupsEvent = new SingleLiveEvent<>();
    private SnackbarMessage snackbarMessage = new SnackbarMessage();

    @Inject
    public MatchupsViewModel(MatchupRepository matchupRepository) {
        this.matchupRepository = matchupRepository;
    }


    public void initialize(String championId) {
        this.championId = championId;
        //this.laneSubtitle = laneSubtitle;
        champion = matchupRepository.getChampion(championId);
        matchups = Transformations.switchMap(champion, newChampion -> matchupRepository.getMatchups(newChampion.getLane(), newChampion.getName()));
    }

    public int getLane() {
        return Objects.requireNonNull(champion.getValue()).getLane();
    }

    public LiveData<List<Matchup>> getMatchups() {
        return matchups;
    }

    public LiveData<Champion> getChampion() {
        return champion;
    }

    /*public String getLaneSubtitle() {
        return laneSubtitle;
    }*/

    public String getChampionId() {
        return championId;
    }

    public SingleLiveEvent<String> getOpenMatchupDetailEvent() {
        return openMatchupDetailEvent;
    }

    public SingleLiveEvent<String> getEditMatchupsEvent() {
        return editMatchupsEvent;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public void navigateToMatchupDetail(String id) {
        openMatchupDetailEvent.setValue(id);
    }

    private void editMatchups() {
        editMatchupsEvent.setValue(Objects.requireNonNull(champion.getValue()).getName());
    }

    public void updateFavourited(Matchup matchup) {
        Completable.fromAction(() -> matchupRepository.changeMatchupStarred(matchup.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        if (!matchup.isStarred()) showSnackbar();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });


    }

    public void deleteMatchup(Matchup matchup) {
        matchupRepository.deleteMatchup(matchup);
    }

    public void showSnackbar() {
        snackbarMessage.setValue(R.string.matchup_updated);
    }

    public void navigateToMatchupSelect() {
        editMatchups();
    }
}
