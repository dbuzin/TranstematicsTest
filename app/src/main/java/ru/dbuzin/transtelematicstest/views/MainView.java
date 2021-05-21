package ru.dbuzin.transtelematicstest.views;

import com.arellomobile.mvp.MvpView;

import java.util.ArrayList;
import ru.dbuzin.transtelematicstest.models.FriendsItem;

public interface MainView extends MvpView {
    void onSuccess(ArrayList<FriendsItem> friendsList);
    void onError(String error);
    void showLoading();
}
