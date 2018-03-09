package com.danielakinola.loljournal.matchups;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.champpool.ChampPoolActivity;
import com.danielakinola.loljournal.matchupdetail.MatchupDetailActivity;

public class MatchupsActivity extends AppCompatActivity {

    public static final int REQUEST_EDIT_MATCHUPS = RESULT_OK + 2;
    public static final String MATCHUP_ID = "MATCHUP_ID";
    private MatchupsViewModel matchupsViewModel = ViewModelProviders.of(this).get(MatchupsViewModel.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchups);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab_add_matchups);
        fab.setOnClickListener(v -> matchupsViewModel.navigateToMatchupSelect());

        matchupsViewModel.getEditMatchupsEvent()
                .observe(this, this::openMatchupSelect);

        matchupsViewModel.getOpenMatchupDetailEvent().observe(this, this::openMatchupDetail);
    }

    private void openMatchupSelect(String championId) {
        Intent intent = new Intent(this, ChampionSelectActivity.class);
        intent.putExtra(ChampPoolActivity.PLAYER_CHAMPION_ID, championId);
        intent.putExtra(getString(R.string.request_code), REQUEST_EDIT_MATCHUPS);
        startActivityForResult(intent, REQUEST_EDIT_MATCHUPS);
    }

    private void openMatchupDetail(String matchupId) {
        Intent intent = new Intent(this, MatchupDetailActivity.class);
        intent.putExtra(MATCHUP_ID, matchupId);
        startActivity(intent);
    }

}
