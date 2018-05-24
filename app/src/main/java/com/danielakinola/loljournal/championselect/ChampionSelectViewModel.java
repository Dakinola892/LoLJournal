package com.danielakinola.loljournal.championselect;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.utils.SingleLiveEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChampionSelectViewModel extends ViewModel {
    private final List<String> intiallySelectedChampions = new ArrayList<>();
    private final MatchupRepository matchupRepository;
    private final SingleLiveEvent<Integer> navigateBackToPreviousActivityEvent = new SingleLiveEvent<>();
    private final MutableLiveData<List<String>> currentlySelectedChampions = new MutableLiveData<>();
    private int lane;
    private String title;
    private String subtitle;
    private int laneIcon;
    private String champName;
    private String championId;


    @Inject
    public ChampionSelectViewModel(MatchupRepository matchupRepository) {
        this.matchupRepository = matchupRepository;
    }

    public void initialize(int lane, @Nullable String champName, String laneTitle, int laneIcon, String championId) {
        this.lane = lane;
        this.laneIcon = laneIcon;
        this.championId = championId;

        if (champName == null) {
            this.title = "Champion Select";
            this.subtitle = laneTitle;
            List<String> result = matchupRepository.getChampNames(lane).getValue();
            if (result != null) {
                this.intiallySelectedChampions.addAll(result);
            }
        } else {
            this.champName = champName;
            this.title = champName + "Matchup Select";
            this.subtitle = laneTitle;
            List<String> result = matchupRepository.getMatchupNames(championId).getValue();
            if (result != null) {
                this.intiallySelectedChampions.addAll(result);
            }
        }
    }


    //TODO: properly learn RxJava2
    //TODO: modularize
    public void applyChampionSelection() {
        List<String> championsOrMatchups = currentlySelectedChampions.getValue();

        if (championsOrMatchups != null) {
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
                //matchupRepository.addMatchup(matchups);
            }
        }


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
                        navigateBackToPreviousActivityEvent.setValue(1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        navigateBackToPreviousActivityEvent.setValue(-1);
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
                        navigateBackToPreviousActivityEvent.setValue(1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        navigateBackToPreviousActivityEvent.setValue(-1);
                    }
                });
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getLaneIcon() {
        return laneIcon;
    }

    public int getLane() {
        return lane;
    }

    public SingleLiveEvent<Integer> getNavigateBackToPreviousActivityEvent() {
        return navigateBackToPreviousActivityEvent;
    }

    public List<String> getIntiallySelectedChampions() {
        return intiallySelectedChampions;
    }

    public LiveData<List<String>> getCurrentlySelectedChampions() {
        return currentlySelectedChampions;
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
