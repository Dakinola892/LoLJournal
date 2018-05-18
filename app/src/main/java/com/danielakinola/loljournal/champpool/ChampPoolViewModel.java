package com.danielakinola.loljournal.champpool;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.res.TypedArray;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.utils.SingleLiveEvent;
import com.danielakinola.loljournal.utils.SnackbarMessage;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
    private final TypedArray laneIcons;
    private final MatchupRepository matchupRepository;

    private final MutableLiveData<Integer> currentLane = new MutableLiveData<>();
    private LiveData<String> laneTitle;
    private LiveData<Integer> laneIcon;
    private LiveData<Integer> emptyStateVisibility;

    private LiveData<List<Champion>>[] champions = new LiveData[5];
    private LiveData<Boolean>[] noChampions = new LiveData[5];

    private SnackbarMessage snackbarMessage = new SnackbarMessage();
    private SingleLiveEvent<Void> editChampPoolEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> championDetailEvent = new SingleLiveEvent<>();


    @Inject
    public ChampPoolViewModel(MatchupRepository matchupRepository, @Named("laneTitles") String[] laneTitles, @Named("laneIcons") TypedArray laneIcons/*, @Named("visibilities") int[] visibilities*/) {
        this.matchupRepository = matchupRepository;
        this.laneTitles = laneTitles;
        this.laneIcons = laneIcons;
        /*this.visibilityValues = visibilities;*/
        getChampPoolData();
        subscribeToLaneChanges();
    }

    private void getChampPoolData() {
        for (int i = 0; i < champions.length; i++) {
            this.champions[i] = this.matchupRepository.getChampPool(i);
        }
    }

    private void subscribeToLaneChanges() {

        this.laneTitle = Transformations.map(currentLane, newLane -> {
            return laneTitles[newLane];
        });

        this.laneIcon = Transformations.map(currentLane, newLane -> {
            return laneIcons.getResourceId(newLane, 0);
        });

        for (int i = 0; i < noChampions.length; i++) {
            this.noChampions[i] = Transformations.map(champions[i], newChampions -> {
                return (newChampions.isEmpty() || newChampions == null) ? true : false;
            });
        }

        //TODO: GIANT SWITCH STATEMENT?
        /*for (int i = 0; i < emptyStateVisibilities.length; i++) {
            this.emptyStateVisibilities[i] = Transformations.map(champions[i], newChampions -> {
                if(newChampions.isEmpty()) {
                    return visibilityValues[0];
                } else{
                    return visibilityValues[1];
                }
            });

            this.recyclerViewVisibilities[i] = Transformations.map(emptyStateVisibilities[i], newValue -> {
                if(newValue == visibilityValues[0]) {
                    return visibilityValues[1];
                } else{
                    return visibilityValues[0];
                }
            });
        }*/

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

    /*public LiveData<Integer> getEmptyStateVisibility(int lane) {
        return emptyStateVisibilities[lane];
    }

    public LiveData<Integer> getRecyclerViewVisibility(int lane) {
        return recyclerViewVisibilities[lane];
    }*/

    public LiveData<Boolean> getNoChampions(int lane) {
        return noChampions[lane];
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

    public void showSnackbarMessage() {
        snackbarMessage.setValue(R.string.champ_pool_edited);
    }

    public void updateFavourited(Champion champion) {
        update(champion);
    }

    //TODO: fix because it isn't an int array its a string array

    //TODO: RxJava-ify for thread safety
    //TODO: CHANGE SO ONLY NEED ID
    private void update(Champion champion) {
        Completable.fromAction(() -> matchupRepository.setChampionStarred(champion.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        if (!champion.isStarred()) showSnackbarMessage();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

    }
}
