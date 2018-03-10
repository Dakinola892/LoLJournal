package com.danielakinola.loljournal.data;

import android.arch.lifecycle.LiveData;

import com.danielakinola.loljournal.data.daos.ChampionDao;
import com.danielakinola.loljournal.data.daos.CommentDao;
import com.danielakinola.loljournal.data.daos.MatchupDao;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.data.models.Matchup;

import java.util.List;

public class MatchupRepository implements MatchupDataSource {

    private final ChampionDao championDao;
    private final MatchupDao matchupDao;
    private final CommentDao commentDao;

    //TODO: Conquer Dagger 2
    public MatchupRepository(MatchupDatabase matchupDatabase) {
        this.championDao = matchupDatabase.championDao();
        this.matchupDao = matchupDatabase.matchupDao();
        this.commentDao = matchupDatabase.commentDao();
    }

    public LiveData<Champion> getChampion(String id) {
        return championDao.getChampion(id);
    }

    public LiveData<Matchup> getMatchup(String matchupId) {
        return matchupDao.getMatchup(matchupId);
    }

    public LiveData<Comment> getComment(int commentId) {
        return commentDao.getComment(commentId);
    }

    public LiveData<List<Champion>> getChampPool(int lane) {
        return championDao.getChampPool(lane);
    }

    public LiveData<List<Matchup>> getMatchups(int lane, String champion) {
        return matchupDao.getMatchups(lane, champion);
    }

    public LiveData<List<Comment>> getStrengths(String matchupId) {
        int STRENGTHS = 0;
        return commentDao.getMatchupCommentsByCategory(matchupId, STRENGTHS);
    }

    public LiveData<List<Comment>> getGeneralComments(String matchupId) {
        int GENERAL = 1;
        return commentDao.getMatchupCommentsByCategory(matchupId, GENERAL);
    }

    public LiveData<List<Comment>> getWeaknesses(String matchupId) {
        int WEAKNESSES = 2;
        return commentDao.getMatchupCommentsByCategory(matchupId, WEAKNESSES);
    }

    @Override
    public List<String> getChampNames(int lane) {
        return championDao.getChampionNames(lane);
    }

    @Override
    public List<String> getMatchupNames(int lane, String playerChampion) {
        return matchupDao.getMatchupNames(lane, playerChampion);
    }

    public void addChampion(Champion... champion) {
        championDao.addChampion(champion);
    }

    public void addMatchup(Matchup... matchup) {
        matchupDao.addMatchup(matchup);
    }

    public void addComment(Comment... comment) {
        commentDao.addComment(comment);
    }

    public void saveComment(Comment comment) {
        commentDao.updateComment(comment);
    }

    public void setChampionStarred(String id) {
        championDao.updateChampionStarred(id);
    }

    public void changeMatchupStarred(String matchupId) {
        matchupDao.updateMatchupStarred(matchupId);
    }

    public void setCommentStarred(int commentId) {
        commentDao.updateStarred(commentId);
    }

    public void deleteChampion(Champion champion) {
        championDao.deleteChampion(champion);
    }

    public void deleteMatchup(Matchup matchup) {
        matchupDao.deleteMatchup(matchup);
    }

    public void deleteComment(Comment comment) {
        commentDao.deleteComment(comment);
    }

    @Override
    public void updateComment(Comment comment) {
        commentDao.updateComment(comment);
    }

}
