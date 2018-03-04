package com.danielakinola.loljournal.championselect;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.SingleLiveEvent;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Matchup;

import java.util.List;

public class ChampionSelectViewModel extends AndroidViewModel {
    private final int LANE;
    private final String TITLE;
    private final String SUBTITLE;
    private final int LANE_ICON;
    private final String CHAMP_NAME;
    private final MatchupRepository matchupRepository;
    private final List<String> INTIALLY_SELECTED_CHAMPIONS;
    private final MutableLiveData<List<String>> CURRENTLY_SELECTED_CHAMPIONS = new MutableLiveData<>();
    private final SingleLiveEvent<Void> NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT = new SingleLiveEvent<>();


    public ChampionSelectViewModel(int lane, MatchupRepository matchupRepository, String champName, Application application) {
        super(application);
        this.LANE = lane;
        this.CHAMP_NAME = champName;
        this.matchupRepository = matchupRepository;
        this.INTIALLY_SELECTED_CHAMPIONS = matchupRepository.getMatchupNames(lane, champName);
        this.TITLE = "Matchup Select";
        this.SUBTITLE = application.getResources().getStringArray(R.array.lanes_array)[lane] + champName;
        this.LANE_ICON = application.getResources().getIntArray(R.array.lane_icons)[lane];
    }

    //TODO: PROVIDE resources
    public ChampionSelectViewModel(int lane, MatchupRepository matchupRepository, Application application) {
        super(application);
        this.LANE = lane;
        this.CHAMP_NAME = null;
        this.matchupRepository = matchupRepository;
        this.INTIALLY_SELECTED_CHAMPIONS = matchupRepository.getChampNames(lane);
        this.TITLE = "Champion Select";
        this.SUBTITLE = application.getResources().getStringArray(R.array.lanes_array)[lane];
        this.LANE_ICON = application.getResources().getIntArray(R.array.lane_icons)[lane];
    }


    public void applyChampionSelection() {
        List<String> championsOrMatchups = CURRENTLY_SELECTED_CHAMPIONS.getValue();
        if (CHAMP_NAME == null) {
            Champion[] champions = new Champion[championsOrMatchups.size()];
            for (int i = 0; i < championsOrMatchups.size(); i++) {
                champions[i] = new Champion(championsOrMatchups.get(i), LANE);
            }
            matchupRepository.addChampion(champions);
        } else {
            Matchup[] matchups = new Matchup[championsOrMatchups.size()];
            for (int i = 0; i < championsOrMatchups.size(); i++) {
                matchups[i] = new Matchup(CHAMP_NAME, championsOrMatchups.get(i), LANE);
            }
            matchupRepository.addMatchup(matchups);
        }
        NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT.setValue(null);
    }

    public String getTITLE() {
        return TITLE;
    }

    public String getSUBTITLE() {
        return SUBTITLE;
    }

    public int getLANE_ICON() {
        return LANE_ICON;
    }

    public int getLANE() {
        return LANE;
    }

    public SingleLiveEvent<Void> getNavigateBackToPreviousActivityEvent() {
        return NAVIGATE_BACK_TO_PREVIOUS_ACTIVITY_EVENT;
    }

    public List<String> getIntiallySelectedChampions() {
        return INTIALLY_SELECTED_CHAMPIONS;
    }

    public LiveData<List<String>> getCURRENTLY_SELECTED_CHAMPIONS() {
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
