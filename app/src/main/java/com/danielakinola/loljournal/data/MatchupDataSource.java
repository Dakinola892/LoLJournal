package com.danielakinola.loljournal.data;

import android.arch.lifecycle.LiveData;

import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.data.models.Matchup;

import java.util.List;

public interface MatchupDataSource {
    LiveData<Champion> getChampion(String id);

    LiveData<Matchup> getMatchup(String matchupId);

    LiveData<Comment> getComment(int commentId);

    LiveData<List<Champion>> getChampPool(int lane);

    LiveData<List<Matchup>> getMatchups(int lane, String champion);

    LiveData<List<Comment>> getStrengths(int matchupId);

    LiveData<List<Comment>> getGeneralComments(int matchupId);

    LiveData<List<Comment>> getWeaknesses(int matchupId);

    void addChampion(Champion champion);

    void addMatchup(Matchup... matchup);

    void addComment(Comment comment);

    void saveComment(Comment comment);

    void setChampionStarred(String id, boolean starred);

    void changeMatchupStarred(String matchupId, boolean starred);

    void setCommentStarred(int commentId, boolean starred);

    void deleteChampion(Champion champion);

    void deleteMatchup(Matchup matchup);

    void deleteComment(Comment comment);

}
