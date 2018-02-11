package com.danielakinola.loljournal.dependencyinjection;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.danielakinola.loljournal.data.MatchupDatabase;
import com.danielakinola.loljournal.data.daos.ChampionDao;
import com.danielakinola.loljournal.data.daos.CommentDao;
import com.danielakinola.loljournal.data.daos.MatchupDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    private MatchupDatabase matchupDatabase;

    public DataModule(Application application) {
        this.matchupDatabase = Room.databaseBuilder(application, MatchupDatabase.class, "Matchups.db").build();
    }

    @Singleton
    @Provides
    MatchupDatabase providesMatchupDatabase() {
        return matchupDatabase;
    }

    @Singleton
    @Provides
    ChampionDao providesChampionDao(MatchupDatabase matchupDatabase) {
        return matchupDatabase.championDao();
    }

    @Singleton
    @Provides
    MatchupDao providesMatchupDao(MatchupDatabase matchupDatabase) {
        return matchupDatabase.matchupDao();
    }

    @Singleton
    @Provides
    CommentDao providesCommentDao(MatchupDatabase matchupDatabase) {
        return matchupDatabase.commentDao();
    }

}
