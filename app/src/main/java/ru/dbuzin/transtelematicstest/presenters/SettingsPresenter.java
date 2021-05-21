package ru.dbuzin.transtelematicstest.presenters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vk.api.sdk.VK;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import ru.dbuzin.transtelematicstest.api.SettingsApi;
import ru.dbuzin.transtelematicstest.app.App;
import ru.dbuzin.transtelematicstest.fragments.SettingsFragment;
import ru.dbuzin.transtelematicstest.models.UploadPhotoResponse;
import ru.dbuzin.transtelematicstest.models.UploadUrlResponse;
import ru.dbuzin.transtelematicstest.models.UserInfoResponse;
import ru.dbuzin.transtelematicstest.views.SettingsView;

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    @Inject
    SettingsApi settingsApi;

        DisposableMaybeObserver<Response<UserInfoResponse>> observer = new DisposableMaybeObserver<Response<UserInfoResponse>>() {
        @Override
        public void onSuccess(@NonNull Response<UserInfoResponse> response) {
            if(response.isSuccessful())
                getViewState().onSuccess(response.body().getUserInfo().get(0));
            else
                getViewState().onError(response.message());
        }

        @Override
        public void onError(@NonNull Throwable e) {
            e.printStackTrace();
            getViewState().onError(e.getMessage());
        }

        @Override
        public void onComplete() {

        }
    };

    public SettingsPresenter() {
        App.getAppComponent().inject(this);
    }

    public void loadUserData(String token){
        settingsApi.getCurrentUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void changeAvatar(String token, Uri path){
        StringBuilder stringBuilder = new StringBuilder();
        settingsApi.getUploadUrl(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<Response<UploadUrlResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Response<UploadUrlResponse> uploadUrlResponseResponse) {
                        if (uploadUrlResponseResponse.isSuccessful())
                            stringBuilder.append(uploadUrlResponseResponse.body().getUploadUrl().getUploadUrl());
                        else {
                            try {
                                getViewState().onError(uploadUrlResponseResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        getViewState().onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        settingsApi.uploadPhoto(stringBuilder.toString(), pickFile(path))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<UploadPhotoResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull UploadPhotoResponse uploadPhotoResponse) {
                        settingsApi.saveChanges(uploadPhotoResponse.getServer(), uploadPhotoResponse.getPhoto(), uploadPhotoResponse.getHash())
                                .subscribeOn(Schedulers.io())
                                .subscribe();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getViewState().onError(e.getMessage());
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private MultipartBody.Part pickFile(Uri path){
        File photo = new File(path.getEncodedPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photo);
        return MultipartBody.Part.createFormData("photo", photo.getName(), requestFile);
    }

    public void logout(SharedPreferences preferences, FragmentTransaction transaction, SettingsFragment fragment){
        VK.logout();
        preferences.edit().clear().apply();
        transaction.remove(fragment);
        transaction.commit();
    }
}
