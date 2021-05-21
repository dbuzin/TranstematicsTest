package ru.dbuzin.transtelematicstest.app;

import android.app.Application;

import ru.dbuzin.transtelematicstest.di.AppComponent;
import ru.dbuzin.transtelematicstest.di.AppModule;
import ru.dbuzin.transtelematicstest.di.DaggerAppComponent;
import ru.dbuzin.transtelematicstest.di.DatabaseModule;
import ru.dbuzin.transtelematicstest.di.NetworkModule;

public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
    }
    private void initDagger(){
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .databaseModule(new DatabaseModule(this))
                .networkModule(new NetworkModule("https://api.vk.com/method/"))
                .build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
