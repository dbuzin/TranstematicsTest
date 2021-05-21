package ru.dbuzin.transtelematicstest.converters;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import ru.dbuzin.transtelematicstest.models.FriendsItem;
import ru.dbuzin.transtelematicstest.models.Location;

public class Converters {

    @TypeConverter
    public static Observable<List<FriendsItem>> toListItems(String value){
        Type itemsType = new TypeToken<Observable<List<FriendsItem>>>(){}.getType();
        return new Gson().fromJson(value, itemsType);
    }

    @TypeConverter
    public static String fromListItems(Observable<List<FriendsItem>> items){
        return new Gson().toJson(items);
    }

    @TypeConverter
    public static Location toLocation(String value){
        Type locationType = new TypeToken<Location>(){}.getType();
        return new Gson().fromJson(value, locationType);
    }

    @TypeConverter
    public static String fromLocation(Location location){
        return new Gson().toJson(location);
    }
}
