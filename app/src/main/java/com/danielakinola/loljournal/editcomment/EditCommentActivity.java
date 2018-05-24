package com.danielakinola.loljournal.editcomment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.danielakinola.loljournal.R;
import com.danielakinola.loljournal.ViewModelFactory;

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
        setContentView(R.layout.activity_edit_comment);
        setupFAB();
        setupViewModel();
        //todo: loadData() to split up setupViewModel? - more SOLID
    }


    //todo: possibily include category string in Comment & other model classes
    //todo: change string formats to extracting string resources
    //TODO: decide between comment detail & comment description
    //todo: fix strengths vs strength, weaknesses vs weakness, maybe?

    private void setupViewModel() {
        int commentId = getIntent().getIntExtra(COMMENT_ID, -1);
        String matchupId = getIntent().getStringExtra(MATCHUP_ID);
        int category = getIntent().getIntExtra(CATEGORY, -1);

        TextView commentTitleEditText = findViewById(R.id.edit_text_comment_title);
        TextView commentDetailEditText = findViewById(R.id.edit_text_comment_detail);

        editCommentViewModel = ViewModelProviders.of(this, viewModelFactory).get(EditCommentViewModel.class);
        editCommentViewModel.initialize(commentId, matchupId, category);
        editCommentViewModel.getConfirmationEvent().observe(this, aVoid -> onConfirm());
        editCommentViewModel.getComment().observe(this, comment -> {
            assert comment != null;
            commentTitleEditText.setText(comment.getTitle());
            commentDetailEditText.setText(comment.getDescription());
            //todo: more mvvm, dagger, less gets, provide string arrays and such (injection)
            //todo: less string building in view, move logic to view model and expose String
            String title = editCommentViewModel.isNewComment() ? "Adding new Matchup " : "Editing Matchup ";
            String categoryString = getResources().getStringArray(R.array.comment_categories)[comment.getCategory()];
            getSupportActionBar().setTitle(title + categoryString);
        });
        editCommentViewModel.getMatchup().observe(this, matchup -> {
            String laneString = getResources().getStringArray(R.array.lanes_array)[matchup.getLane()];
            getSupportActionBar().setSubtitle(String.format("%s vs. %s %s", matchup.getPlayerChampion(), matchup.getEnemyChampion(), laneString));
        });
    }

    private void setupFAB() {
        FloatingActionButton fab = findViewById(R.id.fab_confirm_comment_edit);
        EditText title = findViewById(R.id.edit_text_comment_title);
        EditText description = findViewById(R.id.edit_text_comment_detail);

        fab.setOnClickListener(v -> {
            editCommentViewModel.onConfirm(title.getText().toString(), description.getText().toString());

        });
    }

    private void onConfirm() {
        setResult(RESULT_OK);
        finish();
    }

}
