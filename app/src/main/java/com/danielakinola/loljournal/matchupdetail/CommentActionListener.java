package com.danielakinola.loljournal.matchupdetail;

import com.danielakinola.loljournal.data.models.Comment;

public interface CommentActionListener {
    void onCommentFavourited(Comment commentId);

    void onCommentPlusOned();

    void onCommentSelected(Comment commentId);

    void onDeleteClicked(Comment comment);
}
