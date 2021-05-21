package ru.dbuzin.transtelematicstest.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import ru.dbuzin.transtelematicstest.api.FriendsApi;
import ru.dbuzin.transtelematicstest.api.SettingsApi;
import ru.dbuzin.transtelematicstest.app.AppDatabase;
import ru.dbuzin.transtelematicstest.presenters.MainPresenter;
import ru.dbuzin.transtelematicstest.presenters.FriendDetailPresenter;
import ru.dbuzin.transtelematicstest.presenters.SettingsPresenter;

@Singleton
@Component(dependencies = {}, modules = {AppModule.class, NetworkModule.class, DatabaseModule.class})
public interface AppComponent {
    void inject(MainPresenter mainPresenter);
    void inject(FriendDetailPresenter friendDetailPresenter);
    void inject(SettingsPresenter settingsPresenter);

    FriendsApi authApi();
    SettingsApi settingsApi();
    AppDatabase appDatabase();
    Application application();
}
