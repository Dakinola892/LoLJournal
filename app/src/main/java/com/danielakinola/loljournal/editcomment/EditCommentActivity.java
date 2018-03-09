package com.danielakinola.loljournal.editcomment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import com.danielakinola.loljournal.R;

public class EditCommentActivity extends AppCompatActivity {
    private EditCommentViewModel editCommentViewModel;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_comment);
        requestCode = getIntent().getIntExtra(getString(R.string.request_code), -1);
        setupFAB();

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
