package com.danielakinola.loljournal.matchups;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.champpool.ChampPoolActivity;
import com.danielakinola.loljournal.editcomment.EditCommentActivity;
import com.danielakinola.loljournal.matchupdetail.MatchupDetailActivity;
import com.danielakinola.loljournal.utils.SnackbarMessage;
import com.danielakinola.loljournal.utils.SnackbarUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MatchupsActivity extends AppCompatActivity {

    public static final int REQUEST_EDIT_MATCHUPS = RESULT_OK + 2;
    @Inject
    ViewModelFactory viewModelFactory;
    private MatchupsViewModel matchupsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchups);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab_add_matchups);
        fab.setOnClickListener(v -> matchupsViewModel.navigateToMatchupSelect());

        setupViewModel();
    }

    private void setupViewModel() {
        int lane = getIntent().getIntExtra(ChampionSelectActivity.LANE, -1);
        String championId = getIntent().getStringExtra(ChampPoolActivity.PLAYER_CHAMPION_ID);
        String laneSubtitle = getResources().getStringArray(R.array.lanes_array)[lane];
        matchupsViewModel = ViewModelProviders.of(this, viewModelFactory).get(MatchupsViewModel.class);
        matchupsViewModel.initialize(championId, laneSubtitle);
        matchupsViewModel.getEditMatchupsEvent().observe(this, this::openMatchupSelect);
        matchupsViewModel.getOpenMatchupDetailEvent().observe(this, this::openMatchupDetail);
        matchupsViewModel.getSnackbarMessage().observe(this,
                (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId ->
                        SnackbarUtils.showSnackbar(findViewById(R.id.matchups_coordinator_layout),
                                getString(snackbarMessageResourceId)));
    }


    //TODO: CHECK IF REQUEST_CODE PARAMETER IS NECESSARY
    private void openMatchupSelect(String champName) {
        Intent intent = new Intent(this, ChampionSelectActivity.class);
        intent.putExtra(ChampionSelectActivity.LANE, matchupsViewModel.getLane());
        intent.putExtra(ChampionSelectActivity.CHAMP_NAME, champName);
        intent.putExtra(getString(R.string.request_code), REQUEST_EDIT_MATCHUPS);
        startActivityForResult(intent, REQUEST_EDIT_MATCHUPS);
    }

    private void openMatchupDetail(String matchupId) {
        Intent intent = new Intent(this, MatchupDetailActivity.class);
        intent.putExtra(EditCommentActivity.MATCHUP_ID, matchupId);
        startActivity(intent);
    }


    //TODO: setup with correct Snackbar showing
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_MATCHUPS && resultCode == RESULT_OK) {
            matchupsViewModel.showSnackbar();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ChampPoolActivity.PLAYER_CHAMPION_ID, matchupsViewModel.getChampionId());
        super.onSaveInstanceState(outState);
    }
}
