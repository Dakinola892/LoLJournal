package com.danielakinola.loljournal.champpool;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.databinding.ActivityChampPoolBinding;
import com.danielakinola.loljournal.matchups.MatchupsActivity;
import com.danielakinola.loljournal.utils.SnackbarMessage;
import com.danielakinola.loljournal.utils.SnackbarUtils;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

//todo: fix scrolling problem with viewpager
public class ChampPoolActivity extends DaggerAppCompatActivity {

    public static final int REQUEST_EDIT_CHAMP_POOL = RESULT_FIRST_USER + 1;
    public static final String PLAYER_CHAMPION_ID = "PLAYER_CHAMPION_ID";
    private ChampPoolViewModel champPoolViewModel;
    @Inject
    LanePagerAdapter lanePagerAdapter;
    @Inject
    ViewModelFactory viewModelFactory;
    private int lane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        if (savedInstanceState != null) {
            lane = savedInstanceState.getInt(ChampionSelectActivity.LANE, 0);
        } else {
            lane = getIntent().getIntExtra(ChampionSelectActivity.LANE, 0);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ_pool);
        setupViewModel();
        setupDataBinding();
        setupViewPager();
        setupFAB();
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_change_champ_pool);
        fab.setOnClickListener(v -> champPoolViewModel.editChampPool());
    }


    private void setupDataBinding() {
        ActivityChampPoolBinding activityChampPoolBinding = ActivityChampPoolBinding.inflate(getLayoutInflater());
        activityChampPoolBinding.setViewmodel(champPoolViewModel);
    }

    private void setupViewModel() {
        ImageView icon = findViewById(R.id.img_lane_icon);
        TextView title = findViewById(R.id.text_lane_title);

        champPoolViewModel = ViewModelProviders.of(this, viewModelFactory).get(ChampPoolViewModel.class);
        champPoolViewModel.setCurrentLane(lane);
        champPoolViewModel.getCurrentLane().observe(this, newLane -> lane = newLane);
        champPoolViewModel.getLaneTitle().observe(this, title::setText);
        champPoolViewModel.getLaneIcon().observe(this, icon::setImageResource);
        champPoolViewModel.getEditChampPoolEvent().observe(this, aVoid -> openChampionSelect());
        champPoolViewModel.getChampionDetailEvent().observe(this, this::openChosenChampion);
        champPoolViewModel.getSnackbarMessage().observe(this, (SnackbarMessage.SnackbarObserver) laneString ->
                SnackbarUtils.showSnackbar(findViewById(R.id.coordinator_layout), getString(R.string.champ_pool_edited, getString(laneString))));
    }

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(lanePagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                champPoolViewModel.setCurrentLane(position);
            }
        });
        viewPager.setCurrentItem(lane, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_champ_pool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openChosenChampion(String championId) {
        Intent intent = new Intent(this, MatchupsActivity.class);
        intent.putExtra(PLAYER_CHAMPION_ID, championId);
        startActivity(intent);
    }

    public void openChampionSelect() {
        Intent intent = new Intent(this, ChampionSelectActivity.class);
        intent.putExtra(ChampionSelectActivity.LANE, lane);
        startActivityForResult(intent, REQUEST_EDIT_CHAMP_POOL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        champPoolViewModel.onEdit(resultCode);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ChampionSelectActivity.LANE, lane);
    }

    public static class LanePagerAdapter extends FragmentPagerAdapter {

        private String[] names;

        @Inject
        public LanePagerAdapter(FragmentManager fm, @Named("laneTitles") String[] names) {
            super(fm);
            this.names = names;
        }

        @Override
        public Fragment getItem(int position) {
            return ChampPoolFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return names[position];
        }
    }
}
