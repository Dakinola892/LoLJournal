package com.danielakinola.loljournal.championselect;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

//TODO: rename all viewmodels to 'viewmodel'

public class ChampionSelectActivity extends AppCompatActivity {

    public static final String LANE = "LANE";
    public static final String CHAMP_NAME = "CHAMP_NAME";
    @Inject
    ViewModelFactory viewModelFactory;
    private ChampionSelectViewModel championSelectViewModel;
    ChampionListAdapter championListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_select);
        setupViewModel();
        setupToolbar();
        setupRecylerView();
        setupFAB();
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_confirm_selection);
        fab.setOnClickListener(v -> championSelectViewModel.applyChampionSelection());
    }

    private void setupRecylerView() {
        championListAdapter = new ChampionListAdapter(championSelectViewModel);
        RecyclerView recyclerView = findViewById(R.id.champion_select_recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(ChampionSelectActivity.this, 3));
        recyclerView.setAdapter(championListAdapter);
    }

    private void setupViewModel() {
        int lane = getIntent().getIntExtra(LANE, -1);
        String champName = getIntent().getStringExtra(CHAMP_NAME);

        championSelectViewModel = ViewModelProviders.of(this, viewModelFactory).get(ChampionSelectViewModel.class);

        championSelectViewModel.initialize(lane, champName,
                getResources().getStringArray(R.array.lanes_array)[lane],
                getResources().getIntArray(R.array.lane_icons)[lane]);

        championSelectViewModel.getCurrentlySelectedChampions()
                .observe(this, championListAdapter::setCurrentlySelectedChampions);

        championSelectViewModel.getNavigateBackToPreviousActivityEvent()
                .observe(this, aVoid -> navigateBack());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_champion_select);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle(championSelectViewModel.getTitle());
        toolbar.setSubtitle(championSelectViewModel.getSubtitle());
        toolbar.setLogo(championSelectViewModel.getLaneIcon());
    }

    public void navigateBack() {
        setResult(RESULT_OK);
        finish();
    }
}
