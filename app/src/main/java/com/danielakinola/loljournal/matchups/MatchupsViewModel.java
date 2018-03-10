package com.danielakinola.loljournal.matchups;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.SingleLiveEvent;
import com.danielakinola.loljournal.SnackbarMessage;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Matchup;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

//TODO: FINALIZE, check Dagger2 solution to constructor variables for factory

public class MatchupsViewModel extends ViewModel {

    private final MatchupRepository matchupRepository;
    private final int lane;
    private final LiveData<Champion> champion;
    private final String laneSubtitle;

    private LiveData<List<Matchup>> matchups;
    private SingleLiveEvent<String> openMatchupDetailEvent = new SingleLiveEvent<>();
    private SingleLiveEvent<String> editMatchupsEvent = new SingleLiveEvent<>();
    private SnackbarMessage snackbarMessage = new SnackbarMessage();

    @Inject
    public MatchupsViewModel(@Named("laneTitles") String[] laneTitles, MatchupRepository matchupRepository, @Named("championId") String championId) {
        this.matchupRepository = matchupRepository;
        champion = matchupRepository.getChampion(championId);
        lane = Objects.requireNonNull(champion.getValue()).getLane();
        matchups = matchupRepository.getMatchups(lane, champion.getValue().getName());
        laneSubtitle = laneTitles[lane];
    }

    public int getLane() {
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
        editMatchupsEvent.setValue(champion.getValue().getName());
    }

    public void updateFavourited(Matchup matchup) {
        matchupRepository.changeMatchupStarred(matchup.getId());
    }

    public void deleteMatchup(Matchup matchup) {
        matchupRepository.deleteMatchup(matchup);
    }

    public void showSnackbar() {
        snackbarMessage.setValue(R.string.matchup_updated);
    }

    public void navigateToMatchupSelect() {
        editMatchups();
    }
}
