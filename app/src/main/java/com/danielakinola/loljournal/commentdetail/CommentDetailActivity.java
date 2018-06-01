package com.danielakinola.loljournal.commentdetail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.editcomment.EditCommentActivity;
import com.danielakinola.loljournal.utils.SnackbarMessage;
import com.danielakinola.loljournal.utils.SnackbarUtils;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class CommentDetailActivity extends AppCompatActivity {

    public static final int REQUEST_EDIT_COMMENT = RESULT_FIRST_USER + 4;
    public static final int RESULT_SUCCESSFUL_DELETE = RESULT_FIRST_USER + 8;
    @Inject
    ViewModelFactory viewModelFactory;
    private CommentDetailViewModel commentDetailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        setupViewModel();
        setupFAB();
    }

    private void setupViewModel() {
        int commentId = getIntent().getIntExtra(EditCommentActivity.COMMENT_ID, -1);
        TextView titleView = findViewById(R.id.text_comment_title);
        TextView detailView = findViewById(R.id.text_comment_detail);
        View root = findViewById(R.id.frame_comment_detail);

        commentDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(CommentDetailViewModel.class);

        commentDetailViewModel.initialize(commentId);
        commentDetailViewModel.getEditCommentEvent().observe(this, this::navigateToEditComment);
        commentDetailViewModel.getDeleteCommentEvent().observe(this, this::showDeleteDialog);
        commentDetailViewModel.getFinishEvent().observe(this, aVoid -> finish());
        commentDetailViewModel.getSnackbarMessage().observe(this,
                (SnackbarMessage.SnackbarObserver) message -> SnackbarUtils.showSnackbar(root, getString(message)));
        commentDetailViewModel.getComment().observe(this, comment -> {
            if (comment != null) {
                titleView.setText(comment.getTitle());
                detailView.setText(comment.getDetail());
                setupToolbar();
                invalidateOptionsMenu();
            } else {
                onCommentDelete();
            }
        });
    }

    private void onCommentDelete() {
        setResult(RESULT_SUCCESSFUL_DELETE);
        finish();
    }

    private void showDeleteDialog(Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppDialogTheme);
        AlertDialog dialog = builder.setTitle(getString(R.string.delete_dialog_comment_title, comment.getTitle()))
                .setNegativeButton(R.string.cancel, (dialog12, which) -> {
                })
                .setPositiveButton(R.string.delete, (dialog1, which) -> {
                    commentDetailViewModel.getComment().removeObservers(this);
                    commentDetailViewModel.getMatchup().removeObservers(this);
                    commentDetailViewModel.deleteComment();
                })
                .create();

        dialog.setOnShowListener(dialog13 -> dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                .setTextColor(getResources().getColor(R.color.colorAccent)));

        dialog.show();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView titleView = findViewById(R.id.comment_detail_title);
        TextView subtitleView = findViewById(R.id.comment_detail_subtitle);

        commentDetailViewModel.getTitle().observe(this, titleView::setText);
        commentDetailViewModel.getSubtitle().observe(this, subtitleView::setText);
        commentDetailViewModel.getLogo().observe(this, getSupportActionBar()::setLogo);
    }

    private void navigateToEditComment(Comment comment) {
        Intent intent = new Intent(this, EditCommentActivity.class);
        intent.putExtra(EditCommentActivity.COMMENT_ID, comment.getId());
        intent.putExtra(EditCommentActivity.MATCHUP_ID, comment.getMatchupId());
        intent.putExtra(EditCommentActivity.CATEGORY, comment.getCategory());
        startActivityForResult(intent, REQUEST_EDIT_COMMENT);
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_edit_comment);
        fab.setOnClickListener(v -> commentDetailViewModel.editComment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        commentDetailViewModel.onEdit(resultCode);
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
                //open delete dialog
                commentDetailViewModel.showDeleteDialog();
                break;
            case R.id.action_favourite:
                commentDetailViewModel.favouriteComment();
                break;
            case android.R.id.home:
                setResult(0);
                break;
            default:
                Toast.makeText(this, "Default", Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Comment comment = commentDetailViewModel.getComment().getValue();
        int iconResource = Objects.requireNonNull(comment).isStarred() ? R.drawable.ic_favorite_red_24dp : R.drawable.ic_favorite_border_white_24dp;
        menu.findItem(R.id.action_favourite).setIcon(iconResource);
        return super.onPrepareOptionsMenu(menu);
    }
}
