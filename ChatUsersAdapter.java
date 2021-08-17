package elteam.pureblood.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import elteam.pureblood.R;
import elteam.pureblood.activities.MessagesActivity;
import elteam.pureblood.models.UserModel;

import static elteam.pureblood.Constants.KEY_FriendUid;

public class ChatUsersAdapter extends RecyclerView.Adapter<ChatUsersAdapter.ChatUserViewHolder> {

    private Context context;
    private List<UserModel> allUsers;

    public ChatUsersAdapter(Context context, List<UserModel> allUsers) {
        this.context = context;
        this.allUsers = allUsers;
    }

    @NonNull
    @Override
    public ChatUserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ChatUserViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_chat_user, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserViewHolder chatUserViewHolder, int i) {

        UserModel user = allUsers.get(i);

        chatUserViewHolder.mName.setText(user.getName());
        chatUserViewHolder.mCity.setText(user.getCity());
        Glide.with(context)
                .asBitmap()
                .load(user.getImage())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile))
                .into(chatUserViewHolder.mCircleImageView);
    }

    @Override
    public int getItemCount() {
        if (allUsers != null) {
            return allUsers.size();
        } else {
            return 0;
        }
    }

    class ChatUserViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mCircleImageView;
        private TextView mName, mCity;

        public ChatUserViewHolder(@NonNull View itemView) {
            super(itemView);

            mCircleImageView = itemView.findViewById(R.id.user_single_image);
            mName = itemView.findViewById(R.id.user_single_name);
            mCity = itemView.findViewById(R.id.user_single_city);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MessagesActivity.class);
                    intent.putExtra(KEY_FriendUid, allUsers.get(getAdapterPosition()).getmUid());
                    context.startActivity(intent);
                }
            });
        }
    }
}
