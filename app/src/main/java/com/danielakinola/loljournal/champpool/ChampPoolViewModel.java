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
    private final MatchupRepository matchupRepository;
    private final String[] laneTitles;
    private final TypedArray laneIcons;

    private final MutableLiveData<Integer> currentLane;
    private final LiveData<String> laneTitle;
    private LiveData<Integer> laneIcon;

    private final LiveData<List<Champion>>[] champions;

    private final SnackbarMessage snackbarMessage;
    private final SingleLiveEvent<Void> editChampPoolEvent;
    private final SingleLiveEvent<String> championDetailEvent;
    private String messageArgument;


    @Inject
    public ChampPoolViewModel(MatchupRepository matchupRepository,
                              SnackbarMessage snackbarMessage,
                              SingleLiveEvent<Void> editChampPoolEvent,
                              SingleLiveEvent<String> championDetailEvent,
                              @Named("champPoolArray") LiveData[] champions,
                              @Named("laneTitles") String[] laneTitles,
                              @Named("laneIcons") TypedArray laneIcons, MutableLiveData<Integer> currentLane) {
        this.matchupRepository = matchupRepository;
        this.laneTitles = laneTitles;
        this.laneIcons = laneIcons;
        this.currentLane = currentLane;
        this.champions = champions;
        this.laneTitle = Transformations.map(currentLane, newLane -> laneTitles[newLane]);
        this.laneIcon = Transformations.map(currentLane, newLane -> laneIcons.getResourceId(newLane, 0));
        this.editChampPoolEvent = editChampPoolEvent;
        this.snackbarMessage = snackbarMessage;
        this.championDetailEvent = championDetailEvent;
        getChampPoolData();
    }

    private void getChampPoolData() {
        for (int i = 0; i < champions.length; i++) {
            this.champions[i] = this.matchupRepository.getChampPool(i);
        }
    }

    public String getMessageArgument() {
        return messageArgument;
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

    public void updateFavourited(Champion champion) {
        Completable.fromAction(() -> matchupRepository.setChampionStarred(champion.getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        if (!champion.isStarred()) {
                            messageArgument = laneTitle.getValue() + " " + champion.getName();
                            snackbarMessage.setValue(R.string.champion_favourited);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        snackbarMessage.setValue(R.string.error);
                    }
                });

    }

    public void onEdit(int resultCode) {
        if (resultCode == 1) {
            messageArgument = laneTitle.getValue();
            snackbarMessage.setValue(R.string.champ_pool_edited);
        } else {
            snackbarMessage.setValue(R.string.error);
        }
    }
}
