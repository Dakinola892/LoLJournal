package com.danielakinola.loljournal.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;


//TODO: finalise ids, put them constructor for database but choose another one for actual project use
@Entity(tableName = "Comments", indices = @Index("matchup_id"), foreignKeys = @ForeignKey(entity = Matchup.class,
        parentColumns = "matchup_id",
        childColumns = "matchup_id",
        onDelete = CASCADE))
public class Comment {
    @ColumnInfo(name = "matchup_id")
    private final String matchupId;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "comment_id")
    private int id;
    private String title;
    private String description;
    private int category;
    private boolean starred;

    public Comment(int id, String matchupId, String title, String description, int category, boolean starred) {
        this.id = id;
        this.matchupId = matchupId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.starred = starred;
    }

    @Ignore
    public Comment(String matchupId, int category) {
        this.matchupId = matchupId;
        this.category = category;
        this.starred = false;
    }

    @Ignore
    public Comment(String matchupId, String title, String description, int category) {
        this.matchupId = matchupId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.starred = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMatchupId() {
        return matchupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }
}
