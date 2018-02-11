package com.danielakinola.loljournal.matchups;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.SingleLiveEvent;
import com.danielakinola.loljournal.SnackbarMessage;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Matchup;

import java.util.List;

//TODO: FINALIZE, check Dagger2 solution to constructor variables for factory

public class MatchupsViewModel extends AndroidViewModel {

    private final MatchupRepository matchupRepository;
    private final Integer lane;
    private final LiveData<Champion> champion;
    private final String laneSubtitle;

    private LiveData<List<Matchup>> matchups;
    private SingleLiveEvent<String> openMatchupDetailEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> editMatchupsEvent = new SingleLiveEvent<>();
    private SnackbarMessage snackbarMessage = new SnackbarMessage();

    public MatchupsViewModel(@NonNull Application application, MatchupRepository matchupRepository, String championId) {
        super(application);
        this.matchupRepository = matchupRepository;
        champion = matchupRepository.getChampion(championId);
        lane = champion.getValue().getLane();
        laneSubtitle = application.getResources().getStringArray(R.array.lanes_array)[lane];
        matchups = matchupRepository.getMatchups(lane, champion.getValue().getName());
    }

    public Integer getLane() {
        return lane;
    }

    public LiveData<List<Matchup>> getMatchups() {
        return matchups;
    }

    public LiveData<Champion> getChampion() {
        return champion;
    }

    public String getLaneSubtitle() {
        return laneSubtitle;
    }

    public SingleLiveEvent<String> getOpenMatchupDetailEvent() {
        return openMatchupDetailEvent;
    }

    public SingleLiveEvent<String> getEditMatchupsEvent() {
        return editMatchupsEvent;
    }

    public SnackbarMessage getSnackbarMessage() {
        return snackbarMessage;
    }

    public void navigateToMatchupDetail(String id) {
        openMatchupDetailEvent.setValue(id);
    }

    private void editMatchups() {
        editMatchupsEvent.setValue(champion.getValue().getId());
    }

    public void updateFavourited(Matchup matchup) {
        matchupRepository.changeMatchupStarred(matchup.getId(), !matchup.isStarred());
    }

    public void deleteMatchup(Matchup matchup) {
        matchupRepository.deleteMatchup(matchup);
    }

    public void navigateToMatchupSelect() {
        editMatchups();
    }
}
