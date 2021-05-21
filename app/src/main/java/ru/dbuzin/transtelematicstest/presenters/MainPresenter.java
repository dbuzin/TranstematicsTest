package ru.dbuzin.transtelematicstest.presenters;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKScope;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.dbuzin.transtelematicstest.activities.MainActivity;
import ru.dbuzin.transtelematicstest.api.FriendsApi;
import ru.dbuzin.transtelematicstest.app.App;
import ru.dbuzin.transtelematicstest.dao.FriendsItemDao;
import ru.dbuzin.transtelematicstest.models.FriendsItem;
import ru.dbuzin.transtelematicstest.models.FriendsResponse;
import ru.dbuzin.transtelematicstest.views.MainView;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    @Inject
    FriendsApi friendsApi;

    @Inject
    FriendsItemDao friendsItemDao;

    Observer<Response<FriendsResponse>> observer = new Observer<Response<FriendsResponse>>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            getViewState().showLoading();
        }

        @Override
        public void onNext(@NonNull Response<FriendsResponse> response) {
            if(response.isSuccessful()){
                getViewState().onSuccess(response.body().getFriends().getFriendsList());
                for(FriendsItem item: response.body().getFriends().getFriendsList()){
                friendsItemDao.insert(item)
                        .subscribeOn(Schedulers.io())
                        .subscribe();
                }
            }
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
    Observer<List<FriendsItem>> offlineObserver = new Observer<List<FriendsItem>>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            getViewState().showLoading();
        }

        @Override
        public void onNext(@NonNull List<FriendsItem> friendsItems) {
            getViewState().onSuccess((ArrayList<FriendsItem>) friendsItems);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            getViewState().onError(e.getMessage());
        }

        @Override
        public void onComplete() {

        }
    };

    public MainPresenter() {
        App.getAppComponent().inject(this);
    } // инициализация для инжекта компонентов даггера

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void authorize(MainActivity mainActivity){
        List<VKScope> scopeList = List.of(VKScope.OFFLINE, VKScope.FRIENDS, VKScope.PHOTOS); //Запрос разрешений у юзера
        VK.login(mainActivity, scopeList); //в main activity откроется oauth авторизация вк
    }

    public void getFriends(SharedPreferences preferences){
        friendsApi.getFriends(preferences.getString("Token","")) //передаем токен из shared prefs
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getFriendsFromDb(){
        friendsItemDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(offlineObserver);
    }

    public void cacheToken(SharedPreferences preferences, String token){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Token", token); // сохраняем токен из activity result в shared prefs
        editor.apply();
    }
}
