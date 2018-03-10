package com.danielakinola.loljournal.commentdetail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.SnackbarMessage;
import com.danielakinola.loljournal.SnackbarUtils;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.editcomment.EditCommentActivity;

import javax.inject.Inject;

public class CommentDetailActivity extends AppCompatActivity {

    public static final int REQUEST_EDIT_COMMENT = RESULT_FIRST_USER + 4;
    @Inject
    private ViewModelFactory viewModelFactory;
    private CommentDetailViewModel commentDetailViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        commentDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentDetailViewModel.class);
        commentDetailViewModel.getEditCommentEvent().observe(this, this::navigateToEditComment);
        commentDetailViewModel.getSnackbarMessage().observe(this, (SnackbarMessage.SnackbarObserver) snackbarMessageResourceId -> {
            SnackbarUtils.showSnackbar(findViewById(R.id.frame_comment_detail), getString(snackbarMessageResourceId));
        });

        setupToolbar();
        setupFAB();

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_comment_detail);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void navigateToEditComment(int commentId) {
        Intent intent = new Intent(this, EditCommentActivity.class);
        intent.putExtra(EditCommentActivity.COMMENT_ID, commentId);
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
