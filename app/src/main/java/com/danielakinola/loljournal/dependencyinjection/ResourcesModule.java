package com.danielakinola.loljournal.dependencyinjection;

import android.app.Application;

import com.danielakinola.loljournal.R;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ResourcesModule {
    //Application application;

    @Provides
    @Named("lanesTitles")
    String[] provideLanesTitles(Application application) {
        return application.getResources().getStringArray(R.array.lanes_array);
    }

    @Provides
    @Named("laneIcons")
    int[] provideLaneIcons(Application application) {
        return application.getResources().getIntArray(R.array.lane_icons);
    }
}
