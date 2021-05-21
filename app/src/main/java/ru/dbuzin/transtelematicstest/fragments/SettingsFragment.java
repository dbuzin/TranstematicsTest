package ru.dbuzin.transtelematicstest.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import ru.dbuzin.transtelematicstest.R;
import ru.dbuzin.transtelematicstest.databinding.FragmentSettingsBinding;
import ru.dbuzin.transtelematicstest.models.UserInfo;
import ru.dbuzin.transtelematicstest.presenters.SettingsPresenter;
import ru.dbuzin.transtelematicstest.views.SettingsView;

public class SettingsFragment extends MvpAppCompatFragment implements SettingsView {

    private static final String ARG_PARAM_TOKEN = "Token";
    private static final String ARG_MODE = "Mode";

    private String token;
    private int mode;

    FragmentSettingsBinding mBinding;
    ImageView currentAvatar;
    MaterialTextView fullName;
    MaterialButton changeAvatarButton;
    MaterialButton logoutButton;
    SharedPreferences preferences;
    final int REQUEST_CODE_STORAGE = 161;

    @InjectPresenter
    SettingsPresenter settingsPresenter;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance(String token, int mode) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_TOKEN, token);
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString(ARG_PARAM_TOKEN);
            mode = getArguments().getInt(ARG_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSettingsBinding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentAvatar = mBinding.currentAvatarImageView;
        fullName = mBinding.fullNameTextView;
        changeAvatarButton = mBinding.changeAvatarButton;
        logoutButton = mBinding.logoutButton;

        if(mode == 0) {
            settingsPresenter.loadUserData(token); //загружаем актуальную Аву юзера и имя

            preferences = getActivity().getSharedPreferences("TokenPrefs", Context.MODE_PRIVATE);
            logoutButton.setOnClickListener(v -> settingsPresenter.logout(preferences,
                    getActivity().getSupportFragmentManager().beginTransaction(), this)); // Разлогиниваемся и удаляем этот фрагмент

            changeAvatarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            });
        }
        else {
            changeAvatarButton.setEnabled(false);
            logoutButton.setEnabled(false);
            Glide.with(this.getContext())
                    .load(R.drawable.offline_avatar)
                    .into(currentAvatar);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            getActivity().startActivityForResult(intent, REQUEST_CODE_STORAGE);
        }
        else {
            Toast.makeText(this.getContext(), getString(R.string.permission_gallery_denied), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSuccess(UserInfo userInfo) {
        Glide.with(this.getContext())
                .load(userInfo.getAvatarOrigUrl())
                .into(currentAvatar);
        fullName.setText(getString(R.string.full_name, userInfo.getFirstName(), userInfo.getLastName()));
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this.getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        settingsPresenter.changeAvatar(token, data.getData());
    }

}