package elteam.pureblood.adapters;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import elteam.pureblood.R;
import elteam.pureblood.models.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MESSAGE_TYPE_OTHER = 0;
    public static final int MESSAGE_TYPE_ME = 1;

    private FirebaseUser user;

    private Context context;
    private List<Message> messages;
    private String senderImage;

    public MessageAdapter(Context context, List<Message> messages, String senderImage) {
        this.context = context;
        this.messages = messages;
        this.senderImage = senderImage;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == MESSAGE_TYPE_ME){
            return new MessageViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.message_me, viewGroup, false));
        } else {
            return new MessageViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.message_other, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int position) {

        Message message = messages.get(position);

        messageViewHolder.textView.setText(message.getTextMessage());
        if (senderImage.equals("default")){
            messageViewHolder.circleImageView.setImageResource(R.drawable.ic_profile);
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(senderImage)
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile))
                    .into(messageViewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (messages != null) {
            return messages.size();
        } else {
            return 0;
        }
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private ImageView imageView;
        private TextView textView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.sender_image);
            imageView = itemView.findViewById(R.id.image_message_me);
            textView = itemView.findViewById(R.id.text_message_me);
        }
    }

    @Override
    public int getItemViewType(int position) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (messages.get(position).getSenderId().equals(user.getUid())){
            return MESSAGE_TYPE_ME;
        } else {
            return MESSAGE_TYPE_OTHER;
        }
    }
}
