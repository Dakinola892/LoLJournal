package com.danielakinola.loljournal.championselect;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.champpool.ChampPoolActivity;

//TODO: FINALIZE VIEWMODELS if possible

public class ChampionSelectActivity extends AppCompatActivity {
    public static final int RESULT_EDIT_OK = RESULT_FIRST_USER + 1;

    private final int LANE = getIntent().getIntExtra(ChampPoolActivity.LANE, 0);
    private ChampionSelectViewModel championSelectViewModel = ViewModelProviders.of(this).get(ChampionSelectViewModel.class);
    private final ChampionListAdapter championListAdapter = new ChampionListAdapter(championSelectViewModel);
    private final String PLAYER_CHAMPION_ID = getIntent().getStringExtra(ChampPoolActivity.PLAYER_CHAMPION_ID);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_select);

        Toolbar toolbar = findViewById(R.id.toolbar_champion_select);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle(championSelectViewModel.getTITLE());
        toolbar.setSubtitle(championSelectViewModel.getSUBTITLE());
        toolbar.setLogo(championSelectViewModel.getLANE_ICON());

        RecyclerView recyclerView = findViewById(R.id.champion_select_recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(ChampionSelectActivity.this, 3));
        recyclerView.setAdapter(championListAdapter);

        championSelectViewModel.getCurrentlySelectedChampions().observe(this, championListAdapter::setCurrentlySelectedChampions);

    }

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }
}
