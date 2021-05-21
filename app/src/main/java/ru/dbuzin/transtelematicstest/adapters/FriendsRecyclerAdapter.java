package ru.dbuzin.transtelematicstest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import ru.dbuzin.transtelematicstest.R;
import ru.dbuzin.transtelematicstest.interfaces.FriendCardClickListener;
import ru.dbuzin.transtelematicstest.models.FriendsItem;

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.FriendsViewHolder>{
    ArrayList<FriendsItem> friendsList;
    FriendCardClickListener mListener;

    public FriendsRecyclerAdapter(ArrayList<FriendsItem> friendsList, FriendCardClickListener mListener) {
        this.friendsList = friendsList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_card_view, parent,false);
        return new FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {
        holder.nameTextView.setText(holder.itemView.getContext().getString(R.string.full_name,
                friendsList.get(position).getFirstName(), friendsList.get(position).getLastName()));
        Glide.with(holder.itemView.getContext())
                .load(friendsList.get(position).getPhotoUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.circleCropTransform().circleCrop())
                .into(holder.miniAvatarImageView);
        switch (friendsList.get(position).getOnline()){
            case 0:
                holder.isOnlineTextView.setText(holder.itemView.getContext().getString(R.string.offline));
                holder.isOnlineTextView.setTextColor(holder.itemView.getContext().getColor(R.color.offline));
                break;
            case 1:
                holder.isOnlineTextView.setText(holder.itemView.getContext().getString(R.string.online));
                holder.isOnlineTextView.setTextColor(holder.itemView.getContext().getColor(R.color.online));
                break;
        }
        holder.moreInfoButton.setOnClickListener(v -> mListener.onMoreInfoClick(friendsList.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        ImageView miniAvatarImageView;
        MaterialTextView nameTextView;
        MaterialTextView isOnlineTextView;
        MaterialButton moreInfoButton;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            miniAvatarImageView = itemView.findViewById(R.id.mini_avatar_image_view);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            isOnlineTextView = itemView.findViewById(R.id.is_online_text_view);
            moreInfoButton = itemView.findViewById(R.id.more_info_button);
        }
    }
}
