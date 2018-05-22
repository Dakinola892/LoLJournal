package com.danielakinola.loljournal.commentdetail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.editcomment.EditCommentActivity;
import com.danielakinola.loljournal.utils.SnackbarMessage;
import com.danielakinola.loljournal.utils.SnackbarUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class CommentDetailActivity extends AppCompatActivity {

    public static final int REQUEST_EDIT_COMMENT = RESULT_FIRST_USER + 4;
    @Inject
    ViewModelFactory viewModelFactory;
    private CommentDetailViewModel commentDetailViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        /*ActivityCommentDetailBinding activityCommentDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment_detail);*/
        setContentView(R.layout.activity_comment_detail);
        setupViewModel();
        setupFAB();
        /*activityCommentDetailBinding.setViewmodel(commentDetailViewModel);
        activityCommentDetailBinding.executePendingBindings();*/
    }

    private void setupViewModel() {
        int commentId = getIntent().getIntExtra(EditCommentActivity.COMMENT_ID, -1);
        TextView titleView = findViewById(R.id.text_comment_title);
        TextView descriptionView = findViewById(R.id.text_comment_detail);

        commentDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentDetailViewModel.class);

        commentDetailViewModel.initialize(commentId);
        commentDetailViewModel.getEditCommentEvent().observe(this, this::navigateToEditComment);
        commentDetailViewModel.getSnackbarMessage().observe(this,
                (SnackbarMessage.SnackbarObserver) message -> {
                    SnackbarUtils.showSnackbar(findViewById(R.id.frame_comment_detail), getString(message));
                });
        commentDetailViewModel.getComment().observe(this, comment -> {
            assert comment != null;
            titleView.setText(comment.getTitle());
            descriptionView.setText(comment.getDescription());
            setupToolbar(comment.getCategory());
        });
    }

    //TODO: extract string resource for %s vs %s
    //todo: clean up

    private void setupToolbar(int category) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView titleView = findViewById(R.id.comment_detail_title);
        TextView subtitleView = findViewById(R.id.comment_detail_subtitle);

        commentDetailViewModel.getMatchup().observe(this, matchup -> {
            assert matchup != null;
            String categoryString = getResources().getStringArray(R.array.comment_categories)[category];
            titleView.setText(String.format("%s vs. %s ",
                    matchup.getPlayerChampion(), matchup.getEnemyChampion()));
            subtitleView.setText(categoryString);
            getSupportActionBar().setLogo(
                    getResources().obtainTypedArray(R.array.ab_lane_icons).getDrawable(matchup.getLane()));
        });
    }

    //todo: clean up intents & events, see if everything is used/necessary

    private void navigateToEditComment(Comment comment) {
        Intent intent = new Intent(this, EditCommentActivity.class);
        intent.putExtra(EditCommentActivity.COMMENT_ID, comment.getId());
        intent.putExtra(EditCommentActivity.MATCHUP_ID, comment.getMatchupId());
        intent.putExtra(EditCommentActivity.CATEGORY, comment.getCategory());
        intent.putExtra(getString(R.string.request_code), REQUEST_EDIT_COMMENT);
        startActivityForResult(intent, REQUEST_EDIT_COMMENT);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_edit_comment);
        fab.setOnClickListener(v -> commentDetailViewModel.editComment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_COMMENT && resultCode == RESULT_OK) {
            commentDetailViewModel.onSuccessfulEdit();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comment_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:
                Toast.makeText(this, "Delete", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_favourite:
                //viewmodel favourite
                Toast.makeText(this, "Favourite", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "Default", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
