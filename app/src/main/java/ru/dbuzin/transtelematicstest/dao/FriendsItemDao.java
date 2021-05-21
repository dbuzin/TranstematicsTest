package ru.dbuzin.transtelematicstest.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import ru.dbuzin.transtelematicstest.models.FriendsItem;

@Dao
public interface FriendsItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Maybe<Long> insert(FriendsItem item);

    @Query("SELECT * FROM FriendsItem")
    Observable<List<FriendsItem>> getAll();
}
