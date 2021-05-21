package ru.dbuzin.transtelematicstest.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.textview.MaterialTextView;

import ru.dbuzin.transtelematicstest.R;
import ru.dbuzin.transtelematicstest.databinding.FragmentFriendDetailBinding;
import ru.dbuzin.transtelematicstest.models.UserInfo;
import ru.dbuzin.transtelematicstest.presenters.FriendDetailPresenter;
import ru.dbuzin.transtelematicstest.views.FriendDetailView;

public class FriendDetailFragment extends MvpAppCompatFragment implements FriendDetailView {

    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_TOKEN = "token";
    private static final String ARG_MODE = "mode";

    private Long userId;
    private String token;
    private int mode; // 1 - online, 0 - offline

    FragmentFriendDetailBinding mBinding;
    ImageView avatarView;
    MaterialTextView fullNameTextView;
    MaterialTextView statusTextView;
    MaterialTextView sexTextView;
    MaterialTextView bdateTextView;
    MaterialTextView locationTextView;
    MaterialTextView phoneTextView;
    MaterialTextView followersTextView;
    ProgressBar progressBar;
    String avatarOrigUrl;
    ImageView avatarOrigImageView;
    Button closeButton;

    @InjectPresenter
    FriendDetailPresenter friendDetailPresenter;

    public FriendDetailFragment() {
    }

    public static FriendDetailFragment newInstance(Long userId, String token, int mode) {
        FriendDetailFragment fragment = new FriendDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        args.putString(ARG_TOKEN, token);
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getLong(ARG_USER_ID);
            token = getArguments().getString(ARG_TOKEN);
            mode = getArguments().getInt(ARG_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentFriendDetailBinding.inflate(getLayoutInflater(), container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        avatarView = mBinding.avatarImageView;
        fullNameTextView = mBinding.nameTextView;
        statusTextView = mBinding.statusTextView;
        sexTextView = mBinding.sexTextView;
        bdateTextView = mBinding.bdateTextView;
        locationTextView = mBinding.locationTextView;
        phoneTextView = mBinding.phoneTextView;
        followersTextView = mBinding.followersTextView;
        progressBar = mBinding.userProgressBar;

        if(mode == 1) {
            friendDetailPresenter.getUserInfo(userId, token);
        }
        else {
            friendDetailPresenter.getUserInfoFromDb(userId);
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(FriendDetailFragment.super.getContext()); //В диалоговом окне будет ImageView для аватара

        avatarView.setOnClickListener(v -> {
            View dialogLayout = getLayoutInflater().inflate(R.layout.image_dialog_layout, null);
            builder.setView(dialogLayout);
            avatarOrigImageView = dialogLayout.findViewById(R.id.dialog_image_view);
            Glide.with(FriendDetailFragment.super.getContext())
                    .load(avatarOrigUrl)
                    .into(avatarOrigImageView);
            closeButton = dialogLayout.findViewById(R.id.close_button);
            final AlertDialog alertDialog = builder.create();

            closeButton.setOnClickListener(v1 -> alertDialog.cancel()); //кнопка закрытия диалога

            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setCancelable(true);
            alertDialog.show();
        });
    }

    @Override
    public void onSuccess(UserInfo userInfo) {
        progressBar.setActivated(false);
        progressBar.setVisibility(View.GONE);
        Glide.with(this.getContext())
                .load(userInfo.getAvatarUrl())
                .into(avatarView);
        fullNameTextView.setText(getString(R.string.full_name, userInfo.getFirstName(), userInfo.getLastName()));
        statusTextView.setText(userInfo.getStatus());
        sexTextView.setText(getString(R.string.sex, getString(userInfo.getSex())));
        bdateTextView.setText(getString(R.string.bdate, userInfo.getBdate()));
        locationTextView.setText(getString(R.string.location, userInfo.getCountry().getTitle(), userInfo.getCity().getTitle()));
        phoneTextView.setText(getString(R.string.phone, userInfo.getMobilePhone()));
        followersTextView.setText(getString(R.string.followers, userInfo.getFollowers()));
        avatarOrigUrl = userInfo.getAvatarOrigUrl();
    }

    @Override
    public void onError(String error) {
        progressBar.setActivated(false);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this.getContext(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        progressBar.setActivated(true);
        progressBar.setVisibility(View.VISIBLE);
    }

}