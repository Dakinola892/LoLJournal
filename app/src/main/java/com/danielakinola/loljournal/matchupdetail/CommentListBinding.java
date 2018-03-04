package com.danielakinola.loljournal.matchupdetail;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.danielakinola.loljournal.data.models.Comment;

import java.util.List;

public class CommentListBinding {
    @BindingAdapter("comments")
    public static void setComments(RecyclerView recyclerView, List<Comment> comments) {
        MatchupDetailFragment.CommentAdapter adapter = (MatchupDetailFragment.CommentAdapter) recyclerView.getAdapter();
        adapter.setComments(comments);
    }
}
