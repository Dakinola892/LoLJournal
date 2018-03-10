package com.danielakinola.loljournal.championselect;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.SingleLiveEvent;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Matchup;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

public class ChampionSelectViewModel extends ViewModel {
    private final int LANE;
    private final String TITLE;
    private final String SUBTITLE;
    private final int LANE_ICON;
    private final String CHAMP_NAME;
    private final MatchupRepository MATCHUP_REPOSITORY;
    private final List<String> INTIALLY_SELECTED_CHAMPIONS;
    private final MutableLiveData<List<String>> CURRENTLY_SELECTED_CHAMPIONS = new MutableLiveData<>();
    private final SingleLiveEvent<Void> NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT = new SingleLiveEvent<>();


    @Inject
    public ChampionSelectViewModel(@Named("lane") int lane, MatchupRepository matchupRepository, @Named("champName") String champName, String[] laneTitles, int[] laneIcons) {
        this.LANE = lane;
        this.CHAMP_NAME = champName;
        this.MATCHUP_REPOSITORY = matchupRepository;
        this.INTIALLY_SELECTED_CHAMPIONS = matchupRepository.getMatchupNames(lane, champName);
        this.TITLE = "Matchup Select";
        this.SUBTITLE = laneTitles[lane] + champName;
        this.LANE_ICON = laneIcons[lane];
    }

    @Inject
    public ChampionSelectViewModel(@Named("lane") int lane, MatchupRepository matchupRepository, String[] laneTitles, int[] laneIcons) {
        this.LANE = lane;
        this.CHAMP_NAME = null;
        this.MATCHUP_REPOSITORY = matchupRepository;
        this.INTIALLY_SELECTED_CHAMPIONS = matchupRepository.getChampNames(lane);
        this.TITLE = "Champion Select";
        this.SUBTITLE = laneTitles[lane];
        this.LANE_ICON = laneIcons[lane];
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
