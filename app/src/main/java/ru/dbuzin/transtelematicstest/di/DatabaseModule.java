package ru.dbuzin.transtelematicstest.di;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.dbuzin.transtelematicstest.app.AppDatabase;
import ru.dbuzin.transtelematicstest.dao.FriendsItemDao;
import ru.dbuzin.transtelematicstest.dao.UserInfoDao;

@Module
public class DatabaseModule {
    private AppDatabase appDatabase;

    public DatabaseModule(Application mApplication) {
        appDatabase = Room.databaseBuilder(mApplication, AppDatabase.class,"dbTranstelematics")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                .build();
    }

    @Singleton
    @Provides
    public AppDatabase provideDatabase(){
        return appDatabase;
    }

    @Singleton
    @Provides
    public FriendsItemDao provideFriendsItemDao(AppDatabase appDatabase){
        return appDatabase.getFriendsItemDao();
    }

    @Singleton
    @Provides
    public UserInfoDao provideUserInfoDao(AppDatabase appDatabase){
        return appDatabase.getUserInfoDao();
    }
}
