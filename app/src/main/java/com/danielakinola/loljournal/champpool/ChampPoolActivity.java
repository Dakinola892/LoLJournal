package com.danielakinola.loljournal.champpool;

import android.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.SnackbarMessage;
import com.danielakinola.loljournal.SnackbarUtils;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.championselect.ChampionSelectActivity;
import com.danielakinola.loljournal.databinding.ActivityChampPoolBinding;
import com.danielakinola.loljournal.matchups.MatchupsActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasFragmentInjector;

//TODO: Support fragments for Dagger
public class ChampPoolActivity extends AppCompatActivity implements HasFragmentInjector {

    public static final int REQUEST_EDIT_CHAMP_POOL = RESULT_FIRST_USER + 1;
    public static final String LANE = "LANE";
    public static final String PLAYER_CHAMPION_ID = "PLAYER_CHAMPION_ID";
    public final String[] lanes = getResources().getStringArray(R.array.lanes_array);

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelFactory viewModelFactory;

    private LanePagerAdapter lanePagerAdapter = new LanePagerAdapter(getSupportFragmentManager(), lanes);
    private ChampPoolViewModel champPoolViewModel = ViewModelProviders.of(this).get(ChampPoolViewModel.class); //TODO: factory
    private int lane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_champ_pool);

        ActivityChampPoolBinding activityChampPoolBinding = ActivityChampPoolBinding.inflate(getLayoutInflater());
        activityChampPoolBinding.setViewmodel(champPoolViewModel);

        if (savedInstanceState != null) {
            lane = savedInstanceState.getInt(LANE, 0);
        }

        champPoolViewModel.getCurrentLane().observe(this, integer -> integer = lane);
        champPoolViewModel.getEditChampPoolEvent().observe(this, aVoid -> openChampionSelect());
        champPoolViewModel.getChampionDetailEvent().observe(this, this::openChosenChampion);
        champPoolViewModel.getSnackbarMessage().observe(this, (SnackbarMessage.SnackbarObserver) laneString -> {
            SnackbarUtils.showSnackbar(findViewById(R.id.coordinator_layout), getString(R.string.champ_pool_edited, getString(laneString)));
        });

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(lanePagerAdapter);
        viewPager.setCurrentItem(lane, true);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                champPoolViewModel.setCurrentLane(position);
            }
        });

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
        intent.putExtra(LANE, lane);
        intent.putExtra(getString(R.string.request_code), REQUEST_EDIT_CHAMP_POOL);
        startActivityForResult(intent, REQUEST_EDIT_CHAMP_POOL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_CHAMP_POOL && resultCode == RESULT_OK) {
            champPoolViewModel.onChampPoolEdit();
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> fragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public static class LanePagerAdapter extends FragmentPagerAdapter {

        private String[] names;

        LanePagerAdapter(FragmentManager fm, String[] names) {
            super(fm);
            this.names = names;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
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
