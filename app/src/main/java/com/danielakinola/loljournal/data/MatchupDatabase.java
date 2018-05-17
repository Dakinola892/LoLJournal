package com.danielakinola.loljournal.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.danielakinola.loljournal.data.daos.ChampionDao;
import com.danielakinola.loljournal.data.daos.CommentDao;
import com.danielakinola.loljournal.data.daos.MatchupDao;
import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.data.models.Matchup;

@Database(entities = {Champion.class, Matchup.class, Comment.class}, version = 2, exportSchema = false)
public abstract class MatchupDatabase extends RoomDatabase {
    private static MatchupDatabase ourInstance;

    public abstract ChampionDao championDao();

    public abstract MatchupDao matchupDao();

    public abstract CommentDao commentDao();

    /*private static final Object lock = new Object();


    public static MatchupDatabase getOurInstance(Context context) {
        synchronized (lock) {
            if (ourInstance == null) {
                ourInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MatchupDatabase.class, "LolJournal.db").build();
            }
            return ourInstance;
        }
    }*/
}
