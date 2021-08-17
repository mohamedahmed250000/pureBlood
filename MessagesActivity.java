package elteam.pureblood.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import elteam.pureblood.R;
import elteam.pureblood.adapters.MessageAdapter;
import elteam.pureblood.models.Message;

import static elteam.pureblood.Constants.KEY_Chats;
import static elteam.pureblood.Constants.KEY_FriendUid;
import static elteam.pureblood.Constants.KEY_Image;
import static elteam.pureblood.Constants.KEY_Name;
import static elteam.pureblood.Constants.KEY_USERS;
import static elteam.pureblood.activities.HomeActivity.loadLocale;

public class MessagesActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;

    private String friendUid = null;
    private String friendName = null;
    private String friendImage = null;

    private Toolbar toolbar = null;
    private CircleImageView otherImage = null;
    private TextView otherName = null;

    private EditText type_message;
    private ImageView ic_add_photo, ic_send_message;

    private RecyclerView messagesRecyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.activity_messages);

        setToolbar();

        messagesRecyclerView = findViewById(R.id.message_recycler_view);
        messagesRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messagesRecyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(KEY_FriendUid)) {

            friendUid = intent.getStringExtra(KEY_FriendUid);
        }

        type_message = findViewById(R.id.type_message);
        ic_add_photo = findViewById(R.id.ic_add_photo);
        ic_send_message = findViewById(R.id.ic_send_message);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_USERS);
        if (friendUid != null) {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {

                        friendName = dataSnapshot.child(friendUid).child(KEY_Name).getValue().toString();
                        friendImage = dataSnapshot.child(friendUid).child(KEY_Image).getValue().toString();

                        otherName.setText(friendName);
                        Glide.with(MessagesActivity.this)
                                .asBitmap()
                                .load(friendImage)
                                .apply(new RequestOptions()
                                        .placeholder(R.drawable.ic_profile_white)
                                        .error(R.drawable.ic_profile_white))
                                .into(otherImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            type_message.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().trim().length() > 0) {
                        ic_send_message.setEnabled(true);
                    } else {
                        ic_send_message.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            ic_send_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mMessage = type_message.getText().toString();
                    if (!mMessage.equals("")) {
                        send_message(currentUser.getUid(), friendUid, mMessage);
                        type_message.setText("");
                    }
                }
            });

            messageList = new ArrayList<>();
            DatabaseReference chatReference = FirebaseDatabase.getInstance()
                    .getReference().child(KEY_Chats);
            chatReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    messageList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Message message = snapshot.getValue(Message.class);

                        if ((message.getReceiverId().equals(currentUser.getUid()) &&
                                message.getSenderId().equals(friendUid))
                                || (message.getReceiverId().equals(friendUid) &&
                                message.getSenderId().equals(currentUser.getUid()))) {

                            messageList.add(message);
                            messageAdapter = new MessageAdapter(MessagesActivity.this, messageList, "default");
                            messagesRecyclerView.setAdapter(messageAdapter);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.chat_toolbar);

        otherImage = toolbar.findViewById(R.id.custom_bar_image);
        otherName = toolbar.findViewById(R.id.custom_bar_title);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(null);
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    private void send_message(String senderId, String receiverId, String textMessage) {

        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child(KEY_Chats);

        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setTextMessage(textMessage);

        chatReference.push().setValue(message);
    }

    private void read_message(final String senderId, final String receiverId, final String sender_image) {
        messageList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_Chats);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if ((message.getReceiverId().equals(senderId) && message.getSenderId().equals(receiverId))
                            || (message.getReceiverId().equals(receiverId) && message.getSenderId().equals(senderId))) {
                        messageList.add(message);
                    }

                    messageAdapter = new MessageAdapter(MessagesActivity.this, messageList, sender_image);
                    messagesRecyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
