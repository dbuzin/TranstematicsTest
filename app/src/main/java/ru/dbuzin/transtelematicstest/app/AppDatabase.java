package ru.dbuzin.transtelematicstest.app;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import ru.dbuzin.transtelematicstest.converters.Converters;
import ru.dbuzin.transtelematicstest.dao.FriendsItemDao;
import ru.dbuzin.transtelematicstest.dao.UserInfoDao;
import ru.dbuzin.transtelematicstest.models.FriendsItem;
import ru.dbuzin.transtelematicstest.models.UserInfo;

@Database(entities = {FriendsItem.class, UserInfo.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract FriendsItemDao getFriendsItemDao();
    public abstract UserInfoDao getUserInfoDao();
}
