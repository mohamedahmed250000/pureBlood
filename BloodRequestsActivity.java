package elteam.pureblood.activities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import elteam.pureblood.R;
import elteam.pureblood.adapters.DonorsRequestsAdapter;
import elteam.pureblood.models.UserModel;

import static elteam.pureblood.Constants.KEY_Donors;
import static elteam.pureblood.Constants.KEY_Requests;
import static elteam.pureblood.activities.HomeActivity.loadLocale;

public class BloodRequestsActivity extends AppCompatActivity {

    private RecyclerView mRequestsList;
    private DonorsRequestsAdapter adapter;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.activity_blood_requests);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.blood_requests));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait_while_we_loading_data));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(KEY_Requests);

        mRequestsList = findViewById(R.id.recycler_requests);
        mRequestsList.setHasFixedSize(true);
        mRequestsList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {

                    HashMap<String, UserModel> results = dataSnapshot.getValue(
                            new GenericTypeIndicator<HashMap<String, UserModel>>() {
                            });
                    List<UserModel> users = new ArrayList<>(results.values());

                    adapter = new DonorsRequestsAdapter(BloodRequestsActivity.this, users);
                    mRequestsList.setAdapter(adapter);

                    progressDialog.dismiss();
                } else {
                    Toast.makeText(BloodRequestsActivity.this, getString(R.string.no_blood_requests), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
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
