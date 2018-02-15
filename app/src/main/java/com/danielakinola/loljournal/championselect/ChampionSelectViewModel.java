package com.danielakinola.loljournal.championselect;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.data.MatchupRepository;

import java.util.List;

public class ChampionSelectViewModel extends AndroidViewModel {
    private final int LANE;
    private final String TITLE;
    private final String SUBTITLE;
    private final int LANE_ICON;
    private final MatchupRepository matchupRepository;
    private final List<String> intiallySelectedChampions;
    private final MutableLiveData<List<String>> currentlySelectedChampions = new MutableLiveData<>();


    public ChampionSelectViewModel(int lane, MatchupRepository matchupRepository, String champName, Application application) {
        super(application);
        this.LANE = lane;
        this.matchupRepository = matchupRepository;
        this.intiallySelectedChampions = matchupRepository.getMatchupNames(lane, champName);
        this.TITLE = "Matchup Select";
        this.SUBTITLE = application.getResources().getStringArray(R.array.lanes_array)[lane] + champName;
        this.LANE_ICON = application.getResources().getIntArray(R.array.lane_icons)[lane];
    }

    //TODO: PROVIDE resources
    public ChampionSelectViewModel(int lane, MatchupRepository matchupRepository, Application application) {
        super(application);
        this.LANE = lane;
        this.matchupRepository = matchupRepository;
        this.intiallySelectedChampions = matchupRepository.getChampNames(lane);
        this.TITLE = "Champion Select";
        this.SUBTITLE = application.getResources().getStringArray(R.array.lanes_array)[lane];
        this.LANE_ICON = application.getResources().getIntArray(R.array.lane_icons)[lane];
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

    public int getLANE() {
        return LANE;
    }

    public List<String> getIntiallySelectedChampions() {
        return intiallySelectedChampions;
    }

    public LiveData<List<String>> getCurrentlySelectedChampions() {
        return currentlySelectedChampions;
    }

    public void onViewHolderClick(String name) {
        List<String> champions = currentlySelectedChampions.getValue();
        if (champions.contains(name)) {
            champions.remove(name);
        } else {
            champions.add(name);
        }
        currentlySelectedChampions.setValue(champions);
    }
}
