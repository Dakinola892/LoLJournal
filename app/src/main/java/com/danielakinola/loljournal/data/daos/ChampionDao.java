package com.danielakinola.loljournal.data.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.danielakinola.loljournal.data.models.Champion;

import java.util.List;

@Dao
public interface ChampionDao {
    @Query("SELECT * FROM ChampPool WHERE id = :id")
    LiveData<Champion> getChampion(String id);

    @Query("SELECT * FROM ChampPool where lane = :lane")
    LiveData<List<Champion>> getChampPool(int lane);

    @Query("SELECT * FROM ChampPool where lane = :lane AND starred = 1")
    LiveData<List<Champion>> getStarredChampPool(int lane);

    @Insert
    void addChampion(Champion... champions);

    @Delete
    void deleteChampion(Champion champion);

    @Query("UPDATE ChampPool SET starred = :starred WHERE id = :id")
    void updateChampion(String id, boolean starred);
}
