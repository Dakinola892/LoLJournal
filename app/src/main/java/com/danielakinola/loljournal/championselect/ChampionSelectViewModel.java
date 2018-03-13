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

import io.reactivex.annotations.Nullable;

public class ChampionSelectViewModel extends ViewModel {
    private final List<String> INTIALLY_SELECTED_CHAMPIONS = new ArrayList<>();
    private int LANE;
    private String TITLE;
    private String SUBTITLE;
    private int LANE_ICON;
    private final MatchupRepository MATCHUP_REPOSITORY;
    private String CHAMP_NAME;
    private final MutableLiveData<List<String>> CURRENTLY_SELECTED_CHAMPIONS = new MutableLiveData<>();
    private final SingleLiveEvent<Void> NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT = new SingleLiveEvent<>();


    @Inject
    public ChampionSelectViewModel(MatchupRepository matchupRepository) {
        this.MATCHUP_REPOSITORY = matchupRepository;
    }

    public void initialize(int lane, @Nullable String champName, String laneTitle, int laneIcon) {
        this.LANE = lane;
        this.LANE_ICON = laneIcon;
        this.CHAMP_NAME = champName;

        if (champName == null) {
            this.INTIALLY_SELECTED_CHAMPIONS.addAll(MATCHUP_REPOSITORY.getChampNames(lane));
            this.TITLE = "Champion Select";
            this.SUBTITLE = laneTitle;
        } else {
            this.INTIALLY_SELECTED_CHAMPIONS.addAll(MATCHUP_REPOSITORY.getMatchupNames(lane, champName));
            this.TITLE = "Matchup Select";
            this.SUBTITLE = laneTitle + champName;
        }
    }


    public void applyChampionSelection() {
        List<String> championsOrMatchups = CURRENTLY_SELECTED_CHAMPIONS.getValue();
        if (CHAMP_NAME == null) {
            Champion[] champions = new Champion[championsOrMatchups.size()];
            for (int i = 0; i < championsOrMatchups.size(); i++) {
                champions[i] = new Champion(championsOrMatchups.get(i), LANE);
            }
            MATCHUP_REPOSITORY.addChampion(champions);
        } else {
            Matchup[] matchups = new Matchup[championsOrMatchups.size()];
            for (int i = 0; i < championsOrMatchups.size(); i++) {
                matchups[i] = new Matchup(CHAMP_NAME, championsOrMatchups.get(i), LANE);
            }
            MATCHUP_REPOSITORY.addMatchup(matchups);
        }
        NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT.setValue(null);
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

    public SingleLiveEvent<Void> getNavigateBackToPreviousActivityEvent() {
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
        if (champions.contains(name)) {
            champions.remove(name);
        } else {
            champions.add(name);
        }
        CURRENTLY_SELECTED_CHAMPIONS.setValue(champions);
    }
}
