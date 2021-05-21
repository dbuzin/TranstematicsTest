package ru.dbuzin.transtelematicstest.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import io.reactivex.Maybe;
import ru.dbuzin.transtelematicstest.models.UserInfo;

@Dao
public interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Maybe<Long> insert(UserInfo userInfo);

    @Query("SELECT * FROm UserInfo WHERE id = :id")
    Maybe<UserInfo> getById(Long id);
}
