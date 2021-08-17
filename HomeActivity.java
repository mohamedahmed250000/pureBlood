package elteam.pureblood.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

import elteam.pureblood.R;
import elteam.pureblood.models.UserModel;

import static elteam.pureblood.Constants.KEY_Address;
import static elteam.pureblood.Constants.KEY_Birthday;
import static elteam.pureblood.Constants.KEY_BloodType;
import static elteam.pureblood.Constants.KEY_City;
import static elteam.pureblood.Constants.KEY_Donors;
import static elteam.pureblood.Constants.KEY_Email;
import static elteam.pureblood.Constants.KEY_Image;
import static elteam.pureblood.Constants.KEY_Name;
import static elteam.pureblood.Constants.KEY_Phone;
import static elteam.pureblood.Constants.KEY_Requests;
import static elteam.pureblood.Constants.KEY_USERS;
import static elteam.pureblood.Constants.KEY_Uid;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    private Button blood_donors, blood_requests;

    private ProgressDialog progressDialog;
    private ViewFlipper flipper;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.home_page_toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.ID_drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        blood_donors = findViewById(R.id.blood_donors_btn);
        blood_requests = findViewById(R.id.blood_requests_btn);

        progressDialog = new ProgressDialog(this);

        blood_donors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BloodDonorsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        blood_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BloodRequestsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        flipper = findViewById(R.id.v_flipper);
        flipper.setFlipInterval(2000);
        flipper.startFlipping();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.Profile:
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                startActivity(profileIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.addDonor:
                addDonor();
                break;

            case R.id.addBloodRequest:
                addRequest();
                break;

            case R.id.Chat:
                Intent chatIntent = new Intent(this, ChatActivity.class);
                startActivity(chatIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.Banks:
                Intent banksIntent = new Intent(this, BloodBanksActivity.class);
                startActivity(banksIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.tips:
                Intent tipsIntent = new Intent(this, TipsActivity.class);
                startActivity(tipsIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.Setting:

                showChangeLanguageDialog();
                break;

            case R.id.About:
                Intent AboutIntent = new Intent(this, AboutActivity.class);
                startActivity(AboutIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.SingnOut:
                auth.signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showChangeLanguageDialog() {

        final String[] languages = {"English", "العربية"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle(R.string.choose_language);
        mBuilder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(i == 0){

                    Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    setLocale("EN", getApplicationContext());
                    recreate();

                } else if (i == 1){

                    Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    setLocale("AR", getApplicationContext());
                    recreate();

                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }

    public static void setLocale(String lang, Context context) {

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        context.getResources().updateConfiguration(configuration, context
                .getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = context.getSharedPreferences("SettingsOfLang", MODE_PRIVATE).edit();
        editor.putString("my_lang", lang);
        editor.apply();
    }

    public static void loadLocale(Context context){

        SharedPreferences preferences = context.getSharedPreferences("SettingsOfLang", Activity.MODE_PRIVATE);
        String language = preferences.getString("my_lang", "");
        setLocale(language, context);
    }

    private void addDonor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_donor))
                .setMessage(getString(R.string.are_you_sure_to_add_yourself_as_a_donor))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        uplaod_donor();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void uplaod_donor() {

        progressDialog.setMessage(getString(R.string.please_wait_while_we_uploading_your_data));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(KEY_USERS).child(auth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    UserModel me = new UserModel();
                    me.setmUid(dataSnapshot.child(KEY_Uid).getValue().toString());
                    me.setName(dataSnapshot.child(KEY_Name).getValue().toString());
                    me.setEmail(dataSnapshot.child(KEY_Email).getValue().toString());
                    me.setBirthday(dataSnapshot.child(KEY_Birthday).getValue().toString());
                    me.setBloodType(dataSnapshot.child(KEY_BloodType).getValue().toString());
                    me.setCity(dataSnapshot.child(KEY_City).getValue().toString());
                    me.setAddress(dataSnapshot.child(KEY_Address).getValue().toString());
                    me.setPhone(dataSnapshot.child(KEY_Phone).getValue().toString());
                    me.setImage(dataSnapshot.child(KEY_Image).getValue().toString());

                    DatabaseReference donoersReference = FirebaseDatabase.getInstance()
                            .getReference().child(KEY_Donors).child(auth.getCurrentUser().getUid());

                    donoersReference.setValue(me).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                progressDialog.dismiss();
                                Toast.makeText(HomeActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            } else {

                                progressDialog.hide();
                                Toast.makeText(HomeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_request))
                .setMessage(getString(R.string.are_you_sure_to_add_blood_request))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        uplaod_request();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void uplaod_request() {

        progressDialog.setMessage(getString(R.string.please_wait_while_we_uploading_your_data));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(KEY_USERS).child(auth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {

                    UserModel me = new UserModel();
                    me.setmUid(dataSnapshot.child(KEY_Uid).getValue().toString());
                    me.setName(dataSnapshot.child(KEY_Name).getValue().toString());
                    me.setEmail(dataSnapshot.child(KEY_Email).getValue().toString());
                    me.setBirthday(dataSnapshot.child(KEY_Birthday).getValue().toString());
                    me.setBloodType(dataSnapshot.child(KEY_BloodType).getValue().toString());
                    me.setCity(dataSnapshot.child(KEY_City).getValue().toString());
                    me.setAddress(dataSnapshot.child(KEY_Address).getValue().toString());
                    me.setPhone(dataSnapshot.child(KEY_Phone).getValue().toString());
                    me.setImage(dataSnapshot.child(KEY_Image).getValue().toString());

                    DatabaseReference requestsReference = FirebaseDatabase.getInstance()
                            .getReference().child(KEY_Requests).child(auth.getCurrentUser().getUid());

                    requestsReference.setValue(me).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                progressDialog.dismiss();
                                Toast.makeText(HomeActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            } else {

                                progressDialog.hide();
                                Toast.makeText(HomeActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
