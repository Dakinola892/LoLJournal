package com.danielakinola.loljournal.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.danielakinola.loljournal.data.models.Matchup;

import java.util.List;

@Dao
public interface MatchupDao {
    @Query("SELECT * FROM Matchups WHERE matchup_id = :matchupId")
    LiveData<Matchup> getMatchup(String matchupId);

    @Query("SELECT * FROM Matchups WHERE lane = :lane AND player_champion = :playerChampion")
    LiveData<List<Matchup>> getMatchups(int lane, String playerChampion);

    @Query("SELECT * FROM Matchups WHERE lane = :lane AND player_champion = :playerChampion AND " +
            "starred = 1")
    LiveData<List<Matchup>> getStarredMatchups(int lane, String playerChampion);

    @Query("SELECT enemy_champion FROM Matchups WHERE lane = :lane AND player_champion = :playerChampion")
    List<String> getMatchupNames(int lane, String playerChampion);

    @Query("UPDATE Matchups SET starred = :starred WHERE matchup_id = :matchupId")
    void updateMatchupStarred(String matchupId, boolean starred);

    @Query("DELETE FROM Matchups WHERE player_champion = :playerChampion")
    void deleteChampionMatchups(String playerChampion);

    @Insert
    void addMatchup(Matchup... matchup);

    @Delete
    void deleteMatchup(Matchup matchup);


}
