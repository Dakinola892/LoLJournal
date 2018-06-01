package com.danielakinola.loljournal.championselect;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.res.TypedArray;

import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChampionSelectViewModel extends ViewModel {
    private final MatchupRepository matchupRepository;
    private final SingleLiveEvent<Integer> finishActivityEvent;
    private final MutableLiveData<List<String>> currentlySelectedChampions;
    private final MutableLiveData<Integer> lane;
    private final MutableLiveData<String> title;
    private LiveData<String> subtitle;
    private LiveData<Integer> logo;
    private LiveData<List<String>> initiallySelectedChampions;
    private String champName;
    private String championId;
    private String[] laneTitles;
    private TypedArray laneIcons;


    @Inject
    ChampionSelectViewModel(MatchupRepository matchupRepository,
                            SingleLiveEvent<Integer> finishActivityEvent,
                            MutableLiveData<List<String>> currentlySelectedChampions,
                            MutableLiveData<Integer> lane,
                            MutableLiveData<String> title,
                            @Named("laneTitles") String[] laneTitles,
                            @Named("actionBarIcons") TypedArray laneIcons) {
        this.matchupRepository = matchupRepository;
        this.currentlySelectedChampions = currentlySelectedChampions;
        this.lane = lane;
        this.title = title;
        this.laneTitles = laneTitles;
        this.laneIcons = laneIcons;
        this.finishActivityEvent = finishActivityEvent;
    }

    public void initialize(int lane, @Nullable String champName, String championId) {
        this.lane.setValue(lane);
        this.championId = championId;

        if (champName == null) {
            title.setValue("Champion Select");
            //List<Champion> test = matchupRepository.getChampPool(lane).getValue();
            initiallySelectedChampions = matchupRepository.getChampNames(lane);
        } else {
            this.champName = champName;
            title.setValue(champName + " Matchup Select");
            initiallySelectedChampions = matchupRepository.getMatchupNames(championId);
        }

        this.subtitle = Transformations.map(this.lane, newLane -> laneTitles[newLane]);
        this.logo = Transformations.map(this.lane, newLane -> laneIcons.getResourceId(newLane, -1));
    }



    public void applyChampionSelection() {
        int lane = this.lane.getValue();
        List<String> championsOrMatchups = currentlySelectedChampions.getValue();

        if (championsOrMatchups != null) {
            championsOrMatchups.removeAll(initiallySelectedChampions.getValue());

            if (champName == null) {
                Champion[] champions = new Champion[championsOrMatchups.size()];
                for (int i = 0; i < championsOrMatchups.size(); i++) {
                    champions[i] = new Champion(championsOrMatchups.get(i), lane);
                }
                addChampions(champions);
            } else {
                Matchup[] matchups = new Matchup[championsOrMatchups.size()];
                for (int i = 0; i < championsOrMatchups.size(); i++) {
                    matchups[i] = new Matchup(champName, championsOrMatchups.get(i), lane, championId);
                }
                addMatchups(matchups);
            }
        } else {
            finishActivityEvent.setValue(0);
        }
    }

    public LiveData<List<String>> getInitiallySelectedChampions() {
        return initiallySelectedChampions;
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

    public SingleLiveEvent<Integer> getFinishActivityEvent() {
        return finishActivityEvent;
    }

    public LiveData<List<String>> getCurrentlySelectedChampions() {
        return currentlySelectedChampions;
    }

    private void addMatchups(Matchup... matchups) {
        Completable.fromAction(() -> matchupRepository.addMatchup(matchups))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        finishActivityEvent.setValue(1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        finishActivityEvent.setValue(-1);
                    }
                });
    }

    private void addChampions(Champion... champions) {
        Completable.fromAction(() -> matchupRepository.addChampion(champions))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        finishActivityEvent.setValue(1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        finishActivityEvent.setValue(-1);
                    }
                });
    }

    public void onViewHolderClick(String name) {
        List<String> champions = currentlySelectedChampions.getValue();

        if (champions == null) {
            champions = new ArrayList<>();
        }

        if (champions.contains(name)) {
            champions.remove(name);
        } else {
            champions.add(name);
        }
        currentlySelectedChampions.setValue(champions);
    }
}
