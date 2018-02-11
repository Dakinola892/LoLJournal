package com.danielakinola.loljournal.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.danielakinola.loljournal.ChampionReference;

import java.util.Locale;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "Matchups",
        indices = @Index(value = {"player_champion", "lane"}),
        foreignKeys = @ForeignKey(entity = Champion.class,
                parentColumns = {"name", "lane"},
                childColumns = {"player_champion", "lane"},
                onDelete = CASCADE))

public final class Matchup {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "matchup_id")
    private final String id;

    @ColumnInfo(name = "player_champion")
    private final String playerChampion;

    @ColumnInfo(name = "enemy_champion")
    private final String enemyChampion;
    private final int lane;
    @Ignore
    private final int playerChampionImageResource;
    @Ignore
    private final int enemyChampionImageResource;
    private boolean starred;


    public Matchup(String id, String playerChampion, String enemyChampion, int lane, boolean starred) {
        this.id = String.format(Locale.ENGLISH, "%s_%s_%d", playerChampion, enemyChampion, lane);
        this.playerChampion = playerChampion;
        this.enemyChampion = enemyChampion;
        this.lane = lane;
        this.starred = starred;
        this.playerChampionImageResource = ChampionReference.champions.get(playerChampion);
        this.enemyChampionImageResource = ChampionReference.champions.get(enemyChampion);
    }

    @Ignore
    public Matchup(String playerChampion, String enemyChampion, int lane, boolean starred) {
        this.id = String.format(Locale.ENGLISH, "%s_%s_%d", playerChampion, enemyChampion, lane);
        this.playerChampion = playerChampion;
        this.enemyChampion = enemyChampion;
        this.lane = lane;
        this.starred = starred;
        this.playerChampionImageResource = ChampionReference.champions.get(playerChampion);
        this.enemyChampionImageResource = ChampionReference.champions.get(enemyChampion);
    }

    @Ignore
    public Matchup(String playerChampion, String enemyChampion, int lane) {
        this.id = String.format(Locale.ENGLISH, "%s_%s_%d", playerChampion, enemyChampion, lane);
        this.playerChampion = playerChampion;
        this.enemyChampion = enemyChampion;
        this.lane = lane;
        this.starred = false;
        this.playerChampionImageResource = ChampionReference.champions.get(playerChampion);
        this.enemyChampionImageResource = ChampionReference.champions.get(enemyChampion);
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getPlayerChampion() {
        return playerChampion;
    }

    public String getEnemyChampion() {
        return enemyChampion;
    }

    public int getLane() {
        return lane;
    }

    public int getPlayerChampionImageResource() {
        return playerChampionImageResource;
    }

    public int getEnemyChampionImageResource() {
        return enemyChampionImageResource;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}
