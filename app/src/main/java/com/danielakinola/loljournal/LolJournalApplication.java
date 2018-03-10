package com.danielakinola.loljournal;

import com.danielakinola.loljournal.dependencyinjection.DaggerApplicationComponent;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * Created by dakin on 13/01/2018.
 */

//TODO: ADD TO MANIFEST
public class LolJournalApplication extends DaggerApplication {

    //Android Injector that injects Application with ApplicationComponent to get Application-level dependencies
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().application(this).build();
    }

}
