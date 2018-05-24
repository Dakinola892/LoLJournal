package com.danielakinola.loljournal.matchups;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.res.TypedArray;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.utils.SingleLiveEvent;
import com.danielakinola.loljournal.utils.SnackbarMessage;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//TODO: inject Snackbar Messages & SingleLiveEvents?

public class MatchupsViewModel extends ViewModel {

    private final MatchupRepository matchupRepository;
    private final String[] laneTitles;
    private final TypedArray laneIcons;
    private final SingleLiveEvent<String> openMatchupDetailEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Champion> editMatchupsEvent = new SingleLiveEvent<>();
    private final SnackbarMessage snackbarMessage = new SnackbarMessage();
    private String championId;
    private LiveData<String> laneSubtitle;
    private LiveData<Integer> logo;
    private LiveData<Champion> champion;
    private LiveData<List<Matchup>> matchups;

    @Inject
    MatchupsViewModel(MatchupRepository matchupRepository,
                      @Named("laneTitles") String[] laneTitles,
                      @Named("actionBarIcons") TypedArray laneIcons) {
        this.matchupRepository = matchupRepository;
        this.laneTitles = laneTitles;
        this.laneIcons = laneIcons;
    }


    public void initialize(String championId) {
        this.championId = championId;
        champion = matchupRepository.getChampion(championId);
        matchups = Transformations.switchMap(champion, champion ->
                matchupRepository.getMatchups(champion.getLane(), champion.getName()));
        this.laneSubtitle = Transformations.map(champion, champion -> laneTitles[champion.getLane()]);
        this.logo = Transformations.map(champion,
                champion -> laneIcons.getResourceId(champion.getLane(), -1));
    }

    public LiveData<String> getLaneSubtitle() {
        return laneSubtitle;
    }

    public LiveData<Integer> getLogo() {
        return logo;
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

    public String getChampionId() {
        return championId;
    }

    public SingleLiveEvent<String> getOpenMatchupDetailEvent() {
        return openMatchupDetailEvent;
    }

    public SingleLiveEvent<Champion> getEditMatchupsEvent() {
        return editMatchupsEvent;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public void navigateToMatchupDetail(String id) {
        openMatchupDetailEvent.setValue(id);
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
                        if (!matchup.isStarred()) showSuccessSnackbar();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void deleteMatchup(Matchup matchup) {
        Completable.fromAction(() -> matchupRepository.deleteMatchup(matchup))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        showSuccessSnackbar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorSnackbar();
                    }
                });
    }

    void showSuccessSnackbar() {
        snackbarMessage.setValue(R.string.matchup_updated);
    }

    private void showErrorSnackbar() {
        snackbarMessage.setValue(R.string.error);
    }

    public void navigateToMatchupSelect() {
        editMatchupsEvent.setValue(champion.getValue());
    }

    public void onEdit(int resultCode) {
        if (resultCode == 1) {
            showSuccessSnackbar();
        } else {
            showErrorSnackbar();
        }
    }
}
