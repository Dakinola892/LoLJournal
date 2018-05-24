package com.danielakinola.loljournal.dependencyinjection;

import android.app.Application;
import android.content.res.TypedArray;

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
    @Named("actionBarIcons")
    TypedArray provideActionBarLaneIcons(Application application) {
        return application.getResources().obtainTypedArray(R.array.ab_lane_icons);
    }

    @Provides
    @Named("commentCategories")
    String[] provideCommentCategories(Application application) {
        return application.getResources().getStringArray(R.array.comment_categories);
    }

    @Provides
    @Named("versus")
    String provideVersusString(Application application) {
        return application.getString(R.string.versus);
    }
}
