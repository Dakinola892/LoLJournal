package com.danielakinola.loljournal.matchupdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.commentdetail.CommentDetailActivity;
import com.danielakinola.loljournal.editcomment.EditCommentActivity;

public class MatchupDetailActivity extends AppCompatActivity {

    public static final String CATEGORY = "CATEGORY";
    public static final String COMMENT_ID = "COMMENT_ID";
    public static final int REQUEST_ADD_COMMENT = RESULT_FIRST_USER + 4;
    private MatchupDetailViewModel matchupDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchup_detail);
        setupViewPager();
        setupFAB();

        matchupDetailViewModel.getAddCommentEvent().observe(this, this::addNewComment);
        matchupDetailViewModel.getCommentDetailEvent().observe(this, this::navigateToCommentDetail);

    }

    private void navigateToCommentDetail(int commentId) {
        Intent intent = new Intent(this, CommentDetailActivity.class);
        intent.putExtra(COMMENT_ID, commentId);
        startActivity(intent);
    }

    private void addNewComment(int category) {
        Intent intent = new Intent(this, EditCommentActivity.class);
        intent.putExtra("CATEGORY", category);
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
