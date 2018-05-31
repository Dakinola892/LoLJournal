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

public class ChampionSelectActivity extends AppCompatActivity {

    public static final String LANE = "LANE";
    public static final String CHAMP_NAME = "CHAMP_NAME";
    public static final String PLAYER_CHAMPION_ID = "PLAYER_CHAMPION_ID";

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
        setupRecylerView();
        setupToolbar();
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
        championSelectViewModel.getCurrentlySelectedChampions().observe(this,
                currentlySelectedChampions -> {
                    championListAdapter.setCurrentlySelectedChampions(currentlySelectedChampions);
                    championListAdapter.notifyDataSetChanged();
                });
    }

    private void setupViewModel() {
        int lane = getIntent().getIntExtra(LANE, -1);
        String champName = getIntent().getStringExtra(CHAMP_NAME);
        String championId = getIntent().getStringExtra(PLAYER_CHAMPION_ID);

        championSelectViewModel = ViewModelProviders.of(this, viewModelFactory).get(ChampionSelectViewModel.class);
        championSelectViewModel.initialize(lane, champName, championId);
        championSelectViewModel.getFinishActivityEvent()
                .observe(this, this::navigateBack);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_champion_select);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        championSelectViewModel.getTitle().observe(this, getSupportActionBar()::setTitle);
        championSelectViewModel.getSubtitle().observe(this, getSupportActionBar()::setSubtitle);
        championSelectViewModel.getLogo().observe(this, getSupportActionBar()::setLogo);
    }

    public void navigateBack(int result) {
        setResult(result);
        finish();
    }
}
