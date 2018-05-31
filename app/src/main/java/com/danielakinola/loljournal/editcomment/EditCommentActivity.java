package com.danielakinola.loljournal.editcomment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class EditCommentActivity extends AppCompatActivity {
    public static final String CATEGORY = "CATEGORY";
    public static final String COMMENT_ID = "COMMENT_ID";
    public static final String MATCHUP_ID = "MATCHUP_ID";

    @Inject
    ViewModelFactory viewModelFactory;
    private EditCommentViewModel editCommentViewModel;
    private TextInputEditText commentTitle;
    private TextInputEditText commentDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        commentTitle = findViewById(R.id.edit_text_comment_title);
        commentDetail = findViewById(R.id.edit_text_comment_detail);
        setupViewModel();
        setupFAB();
        loadData();
    }

    private void setupViewModel() {
        int commentId = getIntent().getIntExtra(COMMENT_ID, -1);
        String matchupId = getIntent().getStringExtra(MATCHUP_ID);
        int category = getIntent().getIntExtra(CATEGORY, -1);

        editCommentViewModel = ViewModelProviders.of(this, viewModelFactory).get(EditCommentViewModel.class);
        editCommentViewModel.initialize(commentId, matchupId, category);
    }

    private void loadData() {
        editCommentViewModel.getConfirmationEvent().observe(this, this::onConfirm);
        editCommentViewModel.getTitle().observe(this, Objects.requireNonNull(getSupportActionBar())::setTitle);
        editCommentViewModel.getSubtitle().observe(this, getSupportActionBar()::setSubtitle);
        editCommentViewModel.getComment().observe(this, comment -> {
            assert comment != null;
            commentTitle.setText(comment.getTitle());
            commentDetail.setText(comment.getDetail());
        });
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_confirm_comment_edit);
        fab.setOnClickListener(v -> {
            String newCommentTitle = commentTitle.getText().toString();
            String newCommentDetail = commentDetail.getText().toString();
            editCommentViewModel.saveComment(newCommentTitle, newCommentDetail);
        });
    }

    private void onConfirm(int result) {
        Intent intent = new Intent();
        intent.putExtra(CATEGORY, editCommentViewModel.getComment().getValue().getCategory());
        setResult(result, intent);
        finish();
    }

}
