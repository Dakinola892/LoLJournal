package com.danielakinola.loljournal.matchups;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.SnackbarMessage;
import com.danielakinola.loljournal.SnackbarUtils;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.editcomment.EditCommentActivity;
import com.danielakinola.loljournal.matchupdetail.MatchupDetailActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MatchupsActivity extends AppCompatActivity {

    public static final int REQUEST_EDIT_MATCHUPS = RESULT_OK + 2;
    @Inject
    private ViewModelFactory viewModelFactory;
    private MatchupsViewModel matchupsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchups);
        matchupsViewModel = ViewModelProviders.of(this, viewModelFactory).get(MatchupsViewModel.class);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab_add_matchups);
        fab.setOnClickListener(v -> matchupsViewModel.navigateToMatchupSelect());

        matchupsViewModel.getEditMatchupsEvent().observe(this, this::openMatchupSelect);

        matchupsViewModel.getOpenMatchupDetailEvent().observe(this, this::openMatchupDetail);
        matchupsViewModel.getSnackbarMessage().observe(this, (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId -> {
            SnackbarUtils.showSnackbar(findViewById(R.id.matchups_coordinator_layout), getString(snackbarMessageResourceId));
        });
    }

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
        super.onActivityResult(requestCode, resultCode, data);
    }
}
