package com.danielakinola.loljournal.champpool;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.SingleLiveEvent;
import com.danielakinola.loljournal.SnackbarMessage;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dakin on 13/01/2018.
 */

@SuppressWarnings("ALL")
public class ChampPoolViewModel extends ViewModel {
    private static final int TOP_LANE = 0;
    private static final int JUNGLE = 1;
    private static final int MID_LANE = 2;
    private static final int BOT_LANE = 3;
    private static final int SUPPORT = 4;

    private final String[] laneTitles;
    private final int[] laneIcons;
    private final MatchupRepository matchupRepository;

    private final MutableLiveData<Integer> currentLane = new MutableLiveData<>();
    private LiveData<String> laneTitle;
    private LiveData<Integer> laneIcon;

    private LiveData<List<Champion>>[] champions = new LiveData[5];
    private LiveData<Boolean>[] empty = new LiveData[5];

    private SnackbarMessage snackbarMessage = new SnackbarMessage();
    private SingleLiveEvent<Void> editChampPoolEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> championDetailEvent = new SingleLiveEvent<>();


    @Inject
    public ChampPoolViewModel(MatchupRepository matchupRepository, String[] laneTitles, int[] laneIcons) {
        //this.currentLane.setValue(0);
        this.matchupRepository = matchupRepository;
        this.laneTitles = laneTitles;
        this.laneIcons = laneIcons;
        getChampPoolData();
        subscribeToLaneChanges();
    }

    private void getChampPoolData() {
        for (int i = 0; i < champions.length; i++) {
            this.champions[i] = this.matchupRepository.getChampPool(i);
        }
    }

    private void subscribeToLaneChanges() {

        this.laneTitle = Transformations.map(this.currentLane, newLane -> {
            return laneTitles[newLane];
        });

        this.laneIcon = Transformations.map(this.currentLane, newLane -> {
            return laneIcons[newLane];
        });

        for (int i = 0; i < empty.length; i++) {
            this.empty[i] = Transformations.map(champions[i], newChampions -> newChampions.isEmpty());
        }

    }

    public LiveData<List<Champion>> getChampions(int lane) {
        return champions[lane];
    }

    public LiveData<Integer> getCurrentLane() {
        return currentLane;
    }

    public void setCurrentLane(int currentLane) {
        this.currentLane.setValue(currentLane);
    }

    public LiveData<Boolean> getEmpty(int lane) {
        return empty[lane];
    }

    public LiveData<String> getLaneTitle() {
        return laneTitle;
    }

    public LiveData<Integer> getLaneIcon() {
        return laneIcon;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public SingleLiveEvent<Void> getEditChampPoolEvent() {
        return editChampPoolEvent;
    }

    public SingleLiveEvent<String> getChampionDetailEvent() {
        return championDetailEvent;
    }

    public void editChampPool() {
        editChampPoolEvent.call();
    }

    public void navigateToChampDetail() {
        championDetailEvent.call();
    }

    private void showSnackbarMessage(Integer message) {
        snackbarMessage.setValue(message);
    }

    public void updateFavourited(Champion champion) {
        update(champion);
    }

    //TODO: fix because it isn't an int array its a string array
    public void onChampPoolEdit() {
        //showSnackbarMessage();
    }

    //TODO: RxJava-ify for thread safety
    //TODO: CHANGE SO ONLY NEED ID
    private void update(Champion champion) {
        Completable.fromAction(() -> matchupRepository.setChampionStarred(champion.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    if (!champion.isStarred()) showSnackbarMessage(R.string.champion_favourited);
                });
    }
}
