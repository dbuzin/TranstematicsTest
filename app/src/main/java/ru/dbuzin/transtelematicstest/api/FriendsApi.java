package ru.dbuzin.transtelematicstest.api;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.dbuzin.transtelematicstest.models.FriendsResponse;
import ru.dbuzin.transtelematicstest.models.UserInfoResponse;

public interface FriendsApi {
    @GET("friends.get?v=5.52&fields=photo_100,online")
    Observable<Response<FriendsResponse>> getFriends(@Query("access_token") String token);

    @GET("users.get?v=5.52&fields=sex,bdate,city,country,photo_200_orig,photo_200,contacts,status,followers_count")
    Observable<Response<UserInfoResponse>> getUserInfo(@Query("user_ids") Long userId, @Query("access_token") String token);
}
