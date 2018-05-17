package com.danielakinola.loljournal.matchupdetail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.commentdetail.CommentDetailActivity;
import com.danielakinola.loljournal.editcomment.EditCommentActivity;
import com.danielakinola.loljournal.utils.SnackbarMessage;
import com.danielakinola.loljournal.utils.SnackbarUtils;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class MatchupDetailActivity extends DaggerAppCompatActivity {

    public static final int REQUEST_ADD_COMMENT = RESULT_FIRST_USER + 3;
    @Inject
    ViewModelFactory viewModelFactory;
    private MatchupDetailViewModel matchupDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup_detail);
        setupViewPager();
        setupFAB();
        setupViewModel();
        setupAppBar();

        //TODO: RENAME FROM PLAYERCHAMPION TO USERCHAMPION
        //TODO: databind these?


    }

    private void setupAppBar() {
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.coltoolbar);
        ImageView playerChampionPortrait = findViewById(R.id.img_player_champion);
        ImageView enemyChampionPortrait = findViewById(R.id.img_enemy_champion);

        matchupDetailViewModel.getMatchup().observe(this, matchup -> {
            assert matchup != null;
            String title = String.format("%s vs. %s", matchup.getPlayerChampion(), matchup.getEnemyChampion());
            collapsingToolbarLayout.setTitle(title);
            playerChampionPortrait.setImageResource(matchup.getPlayerChampionImageResource());
            enemyChampionPortrait.setImageResource(matchup.getEnemyChampionImageResource());
        });
    }

    private void setupViewModel() {
        String matchupId = getIntent().getStringExtra(EditCommentActivity.MATCHUP_ID);

        matchupDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(MatchupDetailViewModel.class);
        matchupDetailViewModel.initialize(matchupId);
        matchupDetailViewModel.getAddCommentEvent().observe(this, this::addNewComment);
        matchupDetailViewModel.getCommentDetailEvent().observe(this, this::navigateToCommentDetail);
        matchupDetailViewModel.getSnackbarMessage().observe(this, (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId ->
                SnackbarUtils.showSnackbar(findViewById(R.id.matchup_detail_coordinator_layout), getString(snackbarMessageResourceId)));

    }

    private void navigateToCommentDetail(int commentId) {
        Intent intent = new Intent(this, CommentDetailActivity.class);
        intent.putExtra(EditCommentActivity.COMMENT_ID, commentId);
        startActivity(intent);
    }

    private void addNewComment(int category) {
        Intent intent = new Intent(this, EditCommentActivity.class);
        intent.putExtra(EditCommentActivity.CATEGORY, category);
        intent.putExtra(EditCommentActivity.MATCHUP_ID, Objects.requireNonNull(matchupDetailViewModel.getMatchup().getValue()).getId());
        intent.putExtra(getString(R.string.request_code), REQUEST_ADD_COMMENT);
        startActivityForResult(intent, REQUEST_ADD_COMMENT);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_add_comment);
        fab.setOnClickListener(v -> matchupDetailViewModel.addComment());
    }

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager_matchup_detail);
        viewPager.setAdapter(new MatchupDetailPagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(R.array.comment_categories)));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                matchupDetailViewModel.setCurrentPage(position);
            }
        });

        TabLayout tabLayout = findViewById(R.id.tabs_matchup_detail);
        tabLayout.setupWithViewPager(viewPager);
    }

    class MatchupDetailPagerAdapter extends FragmentPagerAdapter {

        String[] categories;

        MatchupDetailPagerAdapter(FragmentManager fm, String[] categories) {
            super(fm);
            this.categories = categories;
        }

        @Override
        public Fragment getItem(int position) {
            return MatchupDetailFragment.newInstance(position, categories[position]);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return categories[position];
        }
    }
}
