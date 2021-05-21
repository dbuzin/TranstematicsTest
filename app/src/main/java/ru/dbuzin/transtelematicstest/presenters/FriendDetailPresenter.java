package ru.dbuzin.transtelematicstest.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import ru.dbuzin.transtelematicstest.api.FriendsApi;
import ru.dbuzin.transtelematicstest.app.App;
import ru.dbuzin.transtelematicstest.dao.UserInfoDao;
import ru.dbuzin.transtelematicstest.models.UserInfo;
import ru.dbuzin.transtelematicstest.models.UserInfoResponse;
import ru.dbuzin.transtelematicstest.views.FriendDetailView;

@InjectViewState
public class FriendDetailPresenter extends MvpPresenter<FriendDetailView> {

    @Inject
    FriendsApi friendsApi;

    @Inject
    UserInfoDao userInfoDao;

    DisposableMaybeObserver<UserInfo> offlineObserver = new DisposableMaybeObserver<UserInfo>() {
        @Override
        public void onSuccess(@NonNull UserInfo userInfo) {
            getViewState().onSuccess(userInfo);
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

    Observer<Response<UserInfoResponse>> observer = new Observer<Response<UserInfoResponse>>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            getViewState().showLoading();
        }

        @Override
        public void onNext(@NonNull Response<UserInfoResponse> userInfoResponseResponse) {
            if(userInfoResponseResponse.isSuccessful()){
                getViewState().onSuccess(userInfoResponseResponse.body().getUserInfo().get(0));
                userInfoDao.insert(userInfoResponseResponse.body().getUserInfo().get(0))
                        .subscribeOn(Schedulers.io())
                        .subscribe();
            }
            else
                getViewState().onError(userInfoResponseResponse.message());
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

    public FriendDetailPresenter() {
        App.getAppComponent().inject(this);
    }

    public void getUserInfo(Long userId, String token){
        friendsApi.getUserInfo(userId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    public void getUserInfoFromDb(Long userId){
        userInfoDao.getById(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(offlineObserver);
    }
}
