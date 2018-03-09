package com.danielakinola.loljournal.championselect;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.champpool.ChampPoolActivity;

//TODO: FINALIZE VIEWMODELS if possible

public class ChampionSelectActivity extends AppCompatActivity {

    private final int LANE = getIntent().getIntExtra(ChampPoolActivity.LANE, 0);
    private ChampionSelectViewModel championSelectViewModel = ViewModelProviders.of(this).get(ChampionSelectViewModel.class);
    private final ChampionListAdapter championListAdapter = new ChampionListAdapter(championSelectViewModel);
    private final String PLAYER_CHAMPION_ID = getIntent().getStringExtra(ChampPoolActivity.PLAYER_CHAMPION_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_select);

        setupToolbar();

        RecyclerView recyclerView = findViewById(R.id.champion_select_recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(ChampionSelectActivity.this, 3));
        recyclerView.setAdapter(championListAdapter);

        championSelectViewModel.getCURRENTLY_SELECTED_CHAMPIONS()
                .observe(this, championListAdapter::setCurrentlySelectedChampions);

        //TODO: See if referenceing static int in actvity causes leak

        championSelectViewModel.getNavigateBackToPreviousActivityEvent()
                .observe(this, aVoid -> navigateBack(getIntent()
                        .getIntExtra(getString(R.string.request_code), -1)));

        FloatingActionButton fab = findViewById(R.id.fab_confirm_selection);
        fab.setOnClickListener(v -> championSelectViewModel.applyChampionSelection());

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_champion_select);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle(championSelectViewModel.getTITLE());
        toolbar.setSubtitle(championSelectViewModel.getSUBTITLE());
        toolbar.setLogo(championSelectViewModel.getLANE_ICON());
    }

    public void navigateBack(int requestCode) {
        setResult(RESULT_OK);
        finish();
    }
}
