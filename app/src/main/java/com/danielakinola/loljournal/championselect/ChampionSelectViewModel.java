package com.danielakinola.loljournal.championselect;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Matchup;

import java.util.ArrayList;
import java.util.List;

public class ChampionSelectViewModel extends AndroidViewModel {
    private final int REQUEST_FLAG;
    private final int LANE;
    private final String TITLE;
    private final String SUBTITLE;
    private final int LANE_ICON;
    private final MatchupRepository matchupRepository;
    private final List<Champion> intiallySelectedChampions;
    private LiveData<List<Champion>> currentlySelectedChampions;

    public ChampionSelectViewModel(int REQUEST_FLAG, int lane, MatchupRepository matchupRepository, String champName, Application application) {
        super(application);
        this.REQUEST_FLAG = REQUEST_FLAG;
        this.LANE = lane;
        this.matchupRepository = matchupRepository;
        this.intiallySelectedChampions = extractMatchupChampions(matchupRepository.getMatchups(lane, champName).getValue());
        this.TITLE = "Matchup Select";
        this.SUBTITLE = application.getResources().getStringArray(R.array.lanes_array)[lane] + champName;
        this.LANE_ICON = application.getResources().getIntArray(R.array.lane_icons)[lane];
    }

    //TODO: PROVIDE resources
    public ChampionSelectViewModel(int request_flag, int lane, MatchupRepository matchupRepository, Application application) {
        super(application);
        this.REQUEST_FLAG = request_flag;
        this.LANE = lane;
        this.matchupRepository = matchupRepository;
        this.intiallySelectedChampions = matchupRepository.getChampPool(lane).getValue();
        this.TITLE = "Champion Select";
        this.SUBTITLE = application.getResources().getStringArray(R.array.lanes_array)[lane];
        this.LANE_ICON = application.getResources().getIntArray(R.array.lane_icons)[lane];
    }

    private List<Champion> extractMatchupChampions(List<Matchup> matchups) {
        List<Champion> initiallySelectedChampions = new ArrayList<>(matchups.size());
        for (Matchup matchup : matchups)
            initiallySelectedChampions.add(new Champion(matchup.getId(), matchup.getEnemyChampion(), LANE));
        return initiallySelectedChampions;
    }

    public LiveData<List<Champion>> getCurrentlySelectedChampions() {
        return currentlySelectedChampions;
    }

    public void applyChampionSelection() {
        //do stuff with @param current champions
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
}
