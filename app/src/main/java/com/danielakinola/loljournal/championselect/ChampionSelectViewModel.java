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
    private final List<String> INTIALLY_SELECTED_CHAMPIONS = new ArrayList<>();
    private int LANE;
    private String TITLE;
    private String SUBTITLE;
    private int LANE_ICON;
    private final MatchupRepository MATCHUP_REPOSITORY;
    private String CHAMP_NAME;
    private final SingleLiveEvent<Integer> NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT = new SingleLiveEvent<>();
    private final MutableLiveData<List<String>> CURRENTLY_SELECTED_CHAMPIONS = new MutableLiveData<>();
    private String championId;


    @Inject
    public ChampionSelectViewModel(MatchupRepository matchupRepository) {
        this.MATCHUP_REPOSITORY = matchupRepository;
    }

    public void initialize(int lane, @Nullable String champName, String laneTitle, int laneIcon, String championId) {
        this.LANE = lane;
        this.LANE_ICON = laneIcon;
        this.CHAMP_NAME = champName;
        this.championId = championId;

        if (champName == null) {
            this.TITLE = "Champion Select";
            this.SUBTITLE = laneTitle;
            List<String> result = MATCHUP_REPOSITORY.getChampNames(lane).getValue();
            if (result != null) {
                this.INTIALLY_SELECTED_CHAMPIONS.addAll(result);
            }
        } else {
            this.TITLE = "Matchup Select";
            this.SUBTITLE = String.format("%s %s", laneTitle, champName);
            List<String> result = MATCHUP_REPOSITORY.getMatchupNames(championId).getValue();
            if (result != null) {
                this.INTIALLY_SELECTED_CHAMPIONS.addAll(result);
            }
        }
    }


    //TODO: properly learn RxJava2
    //TODO: modularize
    public void applyChampionSelection() {
        List<String> championsOrMatchups = CURRENTLY_SELECTED_CHAMPIONS.getValue();

        if (championsOrMatchups != null) {
            if (CHAMP_NAME == null) {
                Champion[] champions = new Champion[championsOrMatchups.size()];
                for (int i = 0; i < championsOrMatchups.size(); i++) {
                    champions[i] = new Champion(championsOrMatchups.get(i), LANE);
                }
                addChampions(champions);
            } else {
                Matchup[] matchups = new Matchup[championsOrMatchups.size()];
                for (int i = 0; i < championsOrMatchups.size(); i++) {
                    matchups[i] = new Matchup(CHAMP_NAME, championsOrMatchups.get(i), LANE, championId);
                }
                addMatchups(matchups);
                //MATCHUP_REPOSITORY.addMatchup(matchups);
            }
        }


    }

    private void addMatchups(Matchup... matchups) {
        Completable.fromAction(() -> MATCHUP_REPOSITORY.addMatchup(matchups))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT.setValue(1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT.setValue(-1);
                    }
                });
    }

    private void addChampions(Champion... champions) {
        Completable.fromAction(() -> MATCHUP_REPOSITORY.addChampion(champions))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT.setValue(1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT.setValue(-1);
                    }
                });
    }

    public String getTitle() {
        return TITLE;
    }

    public String getSubtitle() {
        return SUBTITLE;
    }

    public int getLaneIcon() {
        return LANE_ICON;
    }

    public int getLane() {
        return LANE;
    }

    public SingleLiveEvent<Integer> getNavigateBackToPreviousActivityEvent() {
        return NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT;
    }

    public List<String> getIntiallySelectedChampions() {
        return INTIALLY_SELECTED_CHAMPIONS;
    }

    public LiveData<List<String>> getCurrentlySelectedChampions() {
        return CURRENTLY_SELECTED_CHAMPIONS;
    }

    public void onViewHolderClick(String name) {
        List<String> champions = CURRENTLY_SELECTED_CHAMPIONS.getValue();

        if (champions == null) {
            champions = new ArrayList<>();
        }

        if (champions.contains(name)) {
            champions.remove(name);
        } else {
            champions.add(name);
        }
        CURRENTLY_SELECTED_CHAMPIONS.setValue(champions);
    }
}
