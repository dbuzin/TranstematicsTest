package ru.dbuzin.transtelematicstest.api;

import io.reactivex.Maybe;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import ru.dbuzin.transtelematicstest.models.UploadPhotoResponse;
import ru.dbuzin.transtelematicstest.models.UploadUrlResponse;
import ru.dbuzin.transtelematicstest.models.UserInfoResponse;

public interface SettingsApi {
    @GET("users.get?v=5.52&fields=photo_200_orig")
    Maybe<Response<UserInfoResponse>> getCurrentUserInfo(@Query("access_token") String token);

    @GET("photos.getOwnerPhotoUploadServer?v=5.51")
    Maybe<Response<UploadUrlResponse>> getUploadUrl(@Query("access_token") String token);

    @Multipart
    @POST
    Maybe<UploadPhotoResponse> uploadPhoto(@Url String uploadUrl, @Part() MultipartBody.Part body);

    @GET("photos.saveOwnerPhoto?v=5.51")
    Maybe<ResponseBody> saveChanges(@Query("server") int server, @Query("photo") String photo, @Query("hash") String hash);
}
