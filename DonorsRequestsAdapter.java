package elteam.pureblood.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import elteam.pureblood.R;
import elteam.pureblood.activities.MessagesActivity;
import elteam.pureblood.models.UserModel;

import static elteam.pureblood.Constants.KEY_FriendUid;

public class DonorsRequestsAdapter extends RecyclerView.Adapter<DonorsRequestsAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> userModels;

    public DonorsRequestsAdapter(Context context, List<UserModel> userModels) {
        this.context = context;
        this.userModels = userModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_donors_requests, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final UserModel currentModel = userModels.get(i);
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        viewHolder.nameItem.setText(currentModel.getName());
        viewHolder.bloodTypeItem.setText(currentModel.getBloodType());
        viewHolder.addressItem.setText(currentModel.getAddress());
        viewHolder.cityItem.setText(currentModel.getCity());

        if (currentModel.getImage().equals(context.getString(R.string.default_word))) {
            viewHolder.imageItem.setImageResource(R.drawable.ic_profile);
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(currentModel.getImage())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile))
                    .into(viewHolder.imageItem);
        }

        if (currentUid.equals(currentModel.getmUid())) {
            viewHolder.callItem.setVisibility(View.GONE);
            viewHolder.chatItem.setVisibility(View.GONE);
        } else {
            viewHolder.callItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + currentModel.getPhone()));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }

                }
            });

            viewHolder.chatItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MessagesActivity.class);
                    intent.putExtra(KEY_FriendUid, currentModel.getmUid());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (userModels != null) {
            return userModels.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imageItem;
        private TextView nameItem, bloodTypeItem, addressItem, cityItem;
        private ImageView callItem, chatItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageItem = itemView.findViewById(R.id.item_image);
            nameItem = itemView.findViewById(R.id.item_name);
            bloodTypeItem = itemView.findViewById(R.id.itme_blood_type);
            addressItem = itemView.findViewById(R.id.item_address);
            cityItem = itemView.findViewById(R.id.item_city);
            callItem = itemView.findViewById(R.id.item_call);
            chatItem = itemView.findViewById(R.id.item_chat);
        }
    }
}
