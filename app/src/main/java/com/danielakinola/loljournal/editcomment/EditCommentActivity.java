package com.danielakinola.loljournal.editcomment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;
import com.danielakinola.loljournal.databinding.ActivityEditCommentBinding;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class EditCommentActivity extends AppCompatActivity {
    public static final String CATEGORY = "CATEGORY";
    public static final String COMMENT_ID = "COMMENT_ID";
    public static final String MATCHUP_ID = "MATCHUP_ID";

    @Inject
    ViewModelFactory viewModelFactory;
    private EditCommentViewModel editCommentViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        ActivityEditCommentBinding activityEditCommentBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_comment);
        setupFAB();
        setupViewModel();
        activityEditCommentBinding.setViewmodel(editCommentViewModel);
    }

    private void setupViewModel() {
        int commentId = getIntent().getIntExtra(COMMENT_ID, -1);
        String matchupId = getIntent().getStringExtra(MATCHUP_ID);
        int category = getIntent().getIntExtra(CATEGORY, -1);

        editCommentViewModel = ViewModelProviders.of(this, viewModelFactory).get(EditCommentViewModel.class);
        editCommentViewModel.initialize(commentId, matchupId, category);
        editCommentViewModel.getConfirmationEvent().observe(this, aVoid -> onConfirm());
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_confirm_comment_edit);
        EditText title = findViewById(R.id.edit_text_comment_title);
        EditText description = findViewById(R.id.edit_text_comment_detail);

        fab.setOnClickListener(v -> {
            editCommentViewModel.onConfirm(title.getText().toString(), description.getText().toString());
            //editCommentViewModel.onConfirm("test", "test");
        });
    }

    private void onConfirm() {
        setResult(RESULT_OK);
        finish();
    }

}
