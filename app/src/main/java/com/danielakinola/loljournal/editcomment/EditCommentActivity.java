package com.danielakinola.loljournal.editcomment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;

import javax.inject.Inject;

public class EditCommentActivity extends AppCompatActivity {
    public static final String CATEGORY = "CATEGORY";
    public static final String COMMENT_ID = "COMMENT_ID";
    public static final String MATCHUP_ID = "MATCHUP_ID";

    @Inject
    private ViewModelFactory viewModelFactory;
    private EditCommentViewModel editCommentViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        setupFAB();

        editCommentViewModel = ViewModelProviders.of(this, viewModelFactory).get(EditCommentViewModel.class);
        editCommentViewModel.getConfirmationEvent().observe(this, aVoid -> onConfirm());
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_confirm_comment_edit);
        fab.setOnClickListener(v -> editCommentViewModel.onConfirm());
    }

    private void onConfirm() {
        setResult(RESULT_OK);
        finish();
    }

}
