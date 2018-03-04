package com.danielakinola.loljournal.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.danielakinola.loljournal.data.models.Comment;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM Comments WHERE comment_id = :commentId")
    LiveData<Comment> getComment(int commentId);

    @Query("SELECT * FROM Comments WHERE matchup_id = :matchupId")
    LiveData<List<Comment>> getMatchupComments(int matchupId);

    @Query("SELECT * FROM Comments WHERE matchup_id = :matchupId AND category = :category")
    LiveData<List<Comment>> getMatchupCommentsbyCategory(int matchupId, int category);

    @Query("UPDATE Comments SET starred = (NOT starred) WHERE  comment_id = :commentId")
    void updateStarred(int commentId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateComment(Comment comment);

    @Insert
    void addComment(Comment... comment);

    @Delete
    void deleteComment(Comment comment);

}
