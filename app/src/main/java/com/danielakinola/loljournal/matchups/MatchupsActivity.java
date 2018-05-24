package com.danielakinola.loljournal.matchups;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.champpool.ChampPoolActivity;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.editcomment.EditCommentActivity;
import com.danielakinola.loljournal.matchupdetail.MatchupDetailActivity;
import com.danielakinola.loljournal.utils.ScreenUtils;
import com.danielakinola.loljournal.utils.SnackbarMessage;
import com.danielakinola.loljournal.utils.SnackbarUtils;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MatchupsActivity extends AppCompatActivity {

    public static final int REQUEST_EDIT_MATCHUPS = RESULT_OK + 2;

    @Inject
    ViewModelFactory viewModelFactory;
    private MatchupsViewModel matchupsViewModel;
    private int logo;
    private String champName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchups);
        String championId = getIntent().getStringExtra(ChampPoolActivity.PLAYER_CHAMPION_ID);
        if (savedInstanceState != null) {
            championId = savedInstanceState.getString(ChampPoolActivity.PLAYER_CHAMPION_ID, "NO_ID");
        }
        setupViewModel(championId);
        setupToolbars();
        setupRecyclerView();
        setupFAB();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.matchups_recylcer_view);
        View emptyState = findViewById(R.id.empty_state);
        int spanCount = ScreenUtils.calculateNoOfColumns(Objects.requireNonNull(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        MatchupAdapter matchupAdapter = new MatchupAdapter(matchupsViewModel);
        recyclerView.setAdapter(matchupAdapter);

        matchupsViewModel.getMatchups().observe(this, matchups -> {
            if (matchups == null || matchups.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                emptyState.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                matchupAdapter.setMatchups(matchups);
                matchupAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_add_matchups);
        fab.setOnClickListener(v -> matchupsViewModel.navigateToMatchupSelect());
    }

    private void setupToolbars() {

        TextView championNameView = findViewById(R.id.text_champion_name_title);
        ImageView championDiagonalPortrait = findViewById(R.id.diagonalImageView);
        TextView laneSubtitleView = findViewById(R.id.text_lane_subtitle);

        matchupsViewModel.getChampion().observe(this, champion -> {
            assert champion != null;
            championNameView.setText(champion.getName());
            championDiagonalPortrait.setImageResource(champion.getImageResource());
            champName = champion.getName();
        });

        matchupsViewModel.getLaneSubtitle().observe(this, laneSubtitleView::setText);
        matchupsViewModel.getLogo().observe(this, logoResource -> logo = logoResource);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.coltoolbar);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            final int switchHeight = 160;
            boolean isShow = true;
            int scrollRange = -1;

            //todo: custom animations for visibility changes
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }

                if (scrollRange + verticalOffset < switchHeight) {
                    collapsingToolbarLayout.setTitle(champName);
                    championNameView.setVisibility(View.GONE);
                    laneSubtitleView.setVisibility(View.GONE);
                    toolbar.setLogo(logo);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    championNameView.setVisibility(View.VISIBLE);
                    laneSubtitleView.setVisibility(View.VISIBLE);
                    toolbar.setLogo(R.drawable.empty);
                    isShow = false;
                }
            }
        });

    }

    private void setupViewModel(String championId) {
        matchupsViewModel = ViewModelProviders.of(this, viewModelFactory).get(MatchupsViewModel.class);
        matchupsViewModel.initialize(championId);
        matchupsViewModel.getEditMatchupsEvent().observe(this, this::openMatchupSelect);
        matchupsViewModel.getOpenMatchupDetailEvent().observe(this, this::openMatchupDetail);
        matchupsViewModel.getSnackbarMessage().observe(this,
                (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId ->
                        SnackbarUtils.showSnackbar(findViewById(R.id.matchups_coordinator_layout),
                                getString(snackbarMessageResourceId)));
    }

    private void openMatchupSelect(Champion champion) {
        Intent intent = new Intent(this, ChampionSelectActivity.class);
        intent.putExtra(ChampionSelectActivity.LANE, champion.getLane());
        intent.putExtra(ChampionSelectActivity.CHAMP_NAME, champion.getName());
        intent.putExtra(ChampionSelectActivity.PLAYER_CHAMPION_ID, champion.getId());
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
        matchupsViewModel.onEdit(resultCode);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ChampPoolActivity.PLAYER_CHAMPION_ID, matchupsViewModel.getChampionId());
    }

}
