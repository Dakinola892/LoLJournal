package com.danielakinola.loljournal.dependencyinjection;

import android.app.Application;
import android.content.res.TypedArray;
import android.view.View;

import com.danielakinola.loljournal.R;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ResourcesModule {

    @Provides
    @Named("laneTitles")
    String[] provideLanesTitles(Application application) {
        return application.getResources().getStringArray(R.array.lanes_array);
    }

    @Provides
    @Named("laneIcons")
    TypedArray provideLaneIcons(Application application) {
        return application.getResources().obtainTypedArray(R.array.lane_icons);
    }

    @Provides
    @Named("visibilities")
    int[] provideVisibilities() {
        return new int[]{View.GONE, View.VISIBLE};
    }
}
