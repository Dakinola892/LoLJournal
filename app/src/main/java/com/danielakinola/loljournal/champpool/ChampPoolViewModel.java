package com.danielakinola.loljournal.champpool;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.NonNull;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.SingleLiveEvent;
import com.danielakinola.loljournal.SnackbarMessage;
import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.matchups.MatchupsActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dakin on 13/01/2018.
 */

@SuppressWarnings("ALL")
public class ChampPoolViewModel extends AndroidViewModel {
    private static final int TOP_LANE = 0;
    private static final int JUNGLE = 1;
    private static final int MID_LANE = 2;
    private static final int BOT_LANE = 3;
    private static final int SUPPORT = 4;

    private Context context;
    private MatchupRepository matchupRepository;

    private MutableLiveData<Integer> currentLane;
    private LiveData<String> laneTitle;
    private LiveData<Integer> laneIcon;

    private LiveData<List<Champion>>[] champions = new LiveData[5];
    private LiveData<Boolean>[] empty = new LiveData[5];


    private SnackbarMessage snackbarMessage = new SnackbarMessage();
    private SingleLiveEvent<Void> editChampPoolEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> championDetailEvent = new SingleLiveEvent<>();


    @Inject
    public ChampPoolViewModel(@NonNull Application application,
                              MatchupRepository matchupRepository) {
        super(application);
        if (currentLane == null) {
            this.currentLane.setValue(TOP_LANE);
        }
        this.context = application;
        this.matchupRepository = matchupRepository;
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
            return context.getResources().getStringArray(R.array.lanes_array)[newLane];
        });

        this.laneIcon = Transformations.map(this.currentLane, newLane -> {
            switch (newLane) {
                case TOP_LANE:
                    return R.drawable.ic_top_lane;
                case JUNGLE:
                    return R.drawable.ic_jungle;
                case MID_LANE:
                    return R.drawable.ic_mid_lane;
                case BOT_LANE:
                    return R.drawable.ic_bot_lane;
                case SUPPORT:
                    return R.drawable.ic_support;
                default:
                    return R.drawable.ic_top_lane;
            }
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

    public void handleActivityResult(int requestCode, int resultCode) {
        if (requestCode == ChampPoolActivity.REQUEST_EDIT_CHAMP_POOL && resultCode == ChampionSelectActivity.RESULT_EDIT_OK) {
            showSnackbarMessage(R.string.champ_pool_edited);
        }

        if (requestCode == MatchupsActivity.REQUEST_EDIT_MATCHUPS && resultCode == ChampionSelectActivity.RESULT_EDIT_OK) {
            showSnackbarMessage(R.string.matchup_updated);
        }
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
