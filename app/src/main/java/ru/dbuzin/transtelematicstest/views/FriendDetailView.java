package ru.dbuzin.transtelematicstest.views;

import com.arellomobile.mvp.MvpView;

import ru.dbuzin.transtelematicstest.models.UserInfo;

public interface FriendDetailView extends MvpView {
    void onSuccess(UserInfo userInfo);
    void onError(String error);
    void showLoading();
}
