package com.danielakinola.loljournal.data;

/**
 * Created by dakin on 12/01/2018.
 */

/*

public class AppRepository implements MatchupDataSource{
    private MatchupRepository matchupRepository;

    public AppRepository(MatchupRepository matchupRepository) {
        this.matchupRepository = matchupRepository;
    }

    @Override
    public LiveData<Champion> getChampion(int lane, String name) {
        return matchupRepository.getChampion(lane, name);
    }

    @Override
    public LiveData<Matchup> getMatchup(int matchupId) {
        return matchupRepository.getMatchup(matchupId);
    }

    @Override
    public LiveData<Comment> getComment(int commentId) {
        return matchupRepository.getComment(commentId);
    }

    @Override
    public LiveData<List<Champion>> getChampPool(int lane) {
        return matchupRepository.getChampPool(lane);
    }

    @Override
    public LiveData<List<Matchup>> getMatchups(int lane, String name) {
        return matchupRepository.getMatchups(lane, name);
    }

    @Override
    public LiveData<List<Comment>> getStrengths(int matchupId) {
        return matchupRepository.getStrengths(matchupId);
    }

    @Override
    public LiveData<List<Comment>> getGeneralComments(int matchupId) {
        return matchupRepository.getGeneralComments(matchupId);
    }

    @Override
    public LiveData<List<Comment>> getWeaknesses(int matchupId) {
        return matchupRepository.getWeaknesses(matchupId);
    }

    @Override
    public void addChampion(Champion playerChampion) {
        matchupRepository.addChampion(playerChampion);
    }

    @Override
    public void addMatchup(Matchup... matchup) {
        matchupRepository.addMatchup(matchup);
    }

    @Override
    public void addComment(Comment comment) {
        matchupRepository.addComment(comment);
    }

    @Override
    public void saveComment(Comment comment) {
        matchupRepository.saveComment(comment);
    }

    @Override
    public void setChampionStarred(int lane, String name, boolean starred) {
        matchupRepository.setChampionStarred(lane, name, starred);
    }

    @Override
    public void changeMatchupStarred(int matchupId, boolean starred) {
        matchupRepository.changeMatchupStarred(matchupId, starred);
    }

    @Override
    public void setCommentStarred(int commentId, boolean starred) {
        matchupRepository.setCommentStarred(commentId, starred);
    }

    @Override
    public void deleteChampion(int lane, String name) {
        matchupRepository.deleteChampion(lane, name);
    }

    @Override
    public void deleteMatchup(int matchupId) {
        matchupRepository.deleteMatchup(matchupId);
    }

    @Override
    public void deleteComment(int commentId) {
        matchupRepository.deleteComment(commentId);
    }
}
*/
