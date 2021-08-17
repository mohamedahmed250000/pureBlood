package elteam.pureblood.activities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import elteam.pureblood.R;
import elteam.pureblood.adapters.ChatUsersAdapter;
import elteam.pureblood.models.Message;
import elteam.pureblood.models.UserModel;

import static elteam.pureblood.Constants.KEY_Chats;
import static elteam.pureblood.Constants.KEY_USERS;
import static elteam.pureblood.activities.HomeActivity.loadLocale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatUsersAdapter adapter;
    private List<UserModel> models;

    private List<String> usersList;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.activity_chat);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.chat));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait_while_we_loading_chat_users));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = findViewById(R.id.chat_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        usersList = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_Chats);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usersList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Message message = snapshot.getValue(Message.class);

                    if (message.getSenderId().equals(firebaseUser.getUid())) {
                        usersList.add(message.getReceiverId());
                    }

                    if (message.getReceiverId().equals(firebaseUser.getUid())) {
                        usersList.add(message.getSenderId());
                    }

                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readChats() {
        models = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_USERS);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                models.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserModel value = snapshot.getValue(UserModel.class);

                    for (String id : usersList) {
                        if (value.getmUid().equals(id)) {
                            if (models.size() != 0) {
                                for (UserModel userModel : models) {
                                    if (!value.getmUid().equals(userModel.getmUid())) {
                                        models.add(value);
                                    }
                                }
                            } else {
                                models.add(value);
                            }
                        }
                    }
                }

                adapter = new ChatUsersAdapter(ChatActivity.this, models);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
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
