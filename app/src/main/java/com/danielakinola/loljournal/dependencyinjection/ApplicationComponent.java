package com.danielakinola.loljournal.dependencyinjection;

import android.app.Application;

import com.danielakinola.loljournal.LolJournalApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

@Singleton
@Component(modules = {ApplicationModule.class, AndroidInjectionModule.class, ActivityBuilder.class, ViewModelModule.class, ResourcesModule.class})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {
    void inject(LolJournalApplication lolJournalApplication);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application); //adds instance of application to ApplicationComponent and Children
        ApplicationComponent build();
    }

}
