package com.danielakinola.loljournal.dependencyinjection;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.danielakinola.loljournal.data.models.Champion;
import com.danielakinola.loljournal.data.models.Comment;
import com.danielakinola.loljournal.data.models.Matchup;
import com.danielakinola.loljournal.utils.SingleLiveEvent;
import com.danielakinola.loljournal.utils.SnackbarMessage;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class LiveDataModule {
    @Provides
    SnackbarMessage providesSnackbarMessage() {
        return new SnackbarMessage();
    }

    @Provides
    SingleLiveEvent<Void> providesVoidSingleLiveEvent() {
        return new SingleLiveEvent<>();
    }

    @Provides
    SingleLiveEvent<Integer> providesIntegerSingleLiveEvent() {
        return new SingleLiveEvent<>();
    }

    @Provides
    SingleLiveEvent<String> providesStringSingleLiveEvent() {
        return new SingleLiveEvent<>();
    }

    @Provides
    SingleLiveEvent<Champion> providesChampionSingleLiveEvent() {
        return new SingleLiveEvent<>();
    }

    @Provides
    SingleLiveEvent<Matchup> providesMatchupSingleLiveEvent() {
        return new SingleLiveEvent<>();
    }

    @Provides
    SingleLiveEvent<Comment> providesCommentSingleLiveEvent() {
        return new SingleLiveEvent<>();
    }

    @Provides
    MutableLiveData<String> providesStringMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    MutableLiveData<Integer> providesIntegerMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    MutableLiveData<List<String>> providesStringListMutableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    @Named("champPoolArray")
    LiveData[] providesChampPoolArray() {
        return new LiveData[5];
    }
}
