package ru.dbuzin.transtelematicstest.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import ru.dbuzin.transtelematicstest.R;
import ru.dbuzin.transtelematicstest.adapters.FriendsRecyclerAdapter;
import ru.dbuzin.transtelematicstest.databinding.ActivityMainBinding;
import ru.dbuzin.transtelematicstest.fragments.FriendDetailFragment;
import ru.dbuzin.transtelematicstest.fragments.SettingsFragment;
import ru.dbuzin.transtelematicstest.interfaces.FriendCardClickListener;
import ru.dbuzin.transtelematicstest.models.FriendsItem;
import ru.dbuzin.transtelematicstest.presenters.MainPresenter;
import ru.dbuzin.transtelematicstest.views.MainView;

public class MainActivity extends MvpAppCompatActivity implements MainView, FriendCardClickListener {

    ActivityMainBinding mBinding;
    RecyclerView friendsRecyclerView;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;
    MaterialTextView errorTextView;
    SharedPreferences preferences;
    FriendsRecyclerAdapter friendsRecyclerAdapter;
    FriendDetailFragment friendDetailFragment;
    SettingsFragment settingsFragment;
    Snackbar snackbar;
    int mode;

    @InjectPresenter
    MainPresenter mainPresenter;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Boolean isConnected = isConnected(); //Проверка наличия интернета
        this.setTheme(R.style.Theme_TranstelematicsTest);//меняем тему на дефолтную после загрузки (при старте приложения тема лаунчера)

        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        friendsRecyclerView = mBinding.friendsRecyclerView;
        progressBar = mBinding.authProgressBar;
        refreshLayout = mBinding.mainRefreshLayout;
        errorTextView = mBinding.noConnectionNoAuthText;

        preferences = getSharedPreferences("TokenPrefs", Context.MODE_PRIVATE);

        settingsFragment = SettingsFragment.newInstance(preferences.getString("Token",""),mode);

        refreshLayout.setOnRefreshListener(() -> {
            if(isConnected()) {
                mainPresenter.getFriends(preferences);
                refreshLayout.setRefreshing(false);
                snackbar.dismiss();
            }
            else {
                onError(getString(R.string.offline));
                refreshLayout.setRefreshing(false);
                snackbar.show();
            }
        });

        snackbar = Snackbar.make(view, getString(R.string.offline), Snackbar.LENGTH_INDEFINITE);

        if(!VK.isLoggedIn() && isConnected){ // Если не залогинен и есть интернет
            mainPresenter.authorize(this);
        }
        else if (VK.isLoggedIn() && isConnected){ // Если залогинен и есть интернет
            mainPresenter.getFriends(preferences);
            mode = 1;
        }
        else if (VK.isLoggedIn() && !isConnected){ //Если залогинен и нет интернета
            mainPresenter.getFriendsFromDb();
            mode = 0;
            snackbar.show();
            Toast.makeText(this, getString(R.string.offline_notification), Toast.LENGTH_LONG).show();
        }
        else // Если не залогинен и нет интернета
            errorTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()){
            case R.id.settings_fragment:
                if(transaction.isEmpty())
                    transaction.replace(mBinding.getRoot().getId(), settingsFragment);
                    transaction.addToBackStack("SettingsFragment");
                    friendsRecyclerView.setVisibility(View.GONE);
                    transaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Метод работает только на физических девайсах,
    на эмуляторе всегда возвращается false из-за особенностей эмулятора */
    private boolean isConnected() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mProcess = runtime.exec("/system/bin/ping -c 1 vk.com"); //пингуем ВК, проверяет сразу и наличие интернета, и доступность сервиса
            return mProcess.waitFor() == 0; //true если процесс завершился со значением 0
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onSuccess(ArrayList<FriendsItem> friendsList) {
        progressBar.setActivated(false);
        progressBar.setVisibility(View.GONE);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        friendsRecyclerAdapter = new FriendsRecyclerAdapter(friendsList, this);
        friendsRecyclerView.setLayoutManager(llm);
        friendsRecyclerView.setAdapter(friendsRecyclerAdapter);

    }

    @Override
    public void onError(String error) {
        progressBar.setActivated(false);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        progressBar.setActivated(true);
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 161 && resultCode == RESULT_OK){
            settingsFragment.onActivityResult(requestCode, resultCode, data);
        }
        else {
            /* Сделано следуя оф документации Vk Android SDK,
     авторизация через Retrofit будет сложнее и с тем же результатом*/
            VKAuthCallback callback = new VKAuthCallback() {
                @Override
                public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                    mainPresenter.cacheToken(preferences, vkAccessToken.getAccessToken()); // Сохранение в SharedPref
                    mainPresenter.getFriends(preferences);
                }

                @Override
                public void onLoginFailed(int i) {
                    Toast.makeText(MainActivity.this, getString(R.string.auth_error), Toast.LENGTH_LONG).show();
                }
            };
            if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    //Передаем id юезра из выбраного item в recyclerView
    @Override
    public void onMoreInfoClick(Long userId) {
        friendDetailFragment = FriendDetailFragment.newInstance(userId, preferences.getString("Token",""), mode);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(this.mBinding.getRoot().getId(), friendDetailFragment, "FriendDetailFragment");
        transaction.addToBackStack("FriendDetailFragment");
        friendsRecyclerView.setVisibility(View.GONE);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            friendsRecyclerView.setVisibility(View.VISIBLE);
            getSupportFragmentManager().popBackStackImmediate();
        }
        else super.onBackPressed();
    }
}