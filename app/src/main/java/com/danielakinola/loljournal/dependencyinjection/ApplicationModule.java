package com.danielakinola.loljournal.dependencyinjection;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.danielakinola.loljournal.data.MatchupDatabase;
import com.danielakinola.loljournal.data.MatchupRepository;
import com.danielakinola.loljournal.data.daos.ChampionDao;
import com.danielakinola.loljournal.data.daos.CommentDao;
import com.danielakinola.loljournal.data.daos.MatchupDao;
import com.danielakinola.loljournal.utils.ChampionGallery;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;

/**
 * Module for defining application-level dependencies (Database DAOs and ViewModels)
 */

@Module(includes = ViewModelModule.class)
public class ApplicationModule {

    @Singleton
    @Provides
    MatchupDatabase providesMatchupDatabase(Application application) {
        return Room.databaseBuilder(application, MatchupDatabase.class, "LoLJournal.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    MatchupRepository matchupRepository(MatchupDatabase matchupDatabase) {
        return new MatchupRepository(matchupDatabase);
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

    @Reusable
    @Provides
    ChampionGallery providesChampionReference() {
        return new ChampionGallery();
    }

}
