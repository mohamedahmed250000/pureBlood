package elteam.pureblood.activities;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import elteam.pureblood.R;
import elteam.pureblood.models.UserModel;

import static elteam.pureblood.Constants.KEY_Address;
import static elteam.pureblood.Constants.KEY_Birthday;
import static elteam.pureblood.Constants.KEY_BloodType;
import static elteam.pureblood.Constants.KEY_City;
import static elteam.pureblood.Constants.KEY_Email;
import static elteam.pureblood.Constants.KEY_Image;
import static elteam.pureblood.Constants.KEY_Name;
import static elteam.pureblood.Constants.KEY_Phone;
import static elteam.pureblood.Constants.KEY_USERS;
import static elteam.pureblood.Constants.KEY_Uid;
import static elteam.pureblood.activities.HomeActivity.loadLocale;

public class SignUpActivity extends AppCompatActivity {

    private EditText name, email, password, birthday, bloodType, city, address, phone;
    private ImageView ok;

    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.activity_sign_up);

        name = findViewById(R.id.name_sign_up);
        email = findViewById(R.id.email_sign_up);
        password = findViewById(R.id.password_sign_up);
        birthday = findViewById(R.id.birthday_sign_up);
        bloodType = findViewById(R.id.blood_type_sign_up);
        city = findViewById(R.id.city_sign_up);
        address = findViewById(R.id.address_sign_up);
        phone = findViewById(R.id.phone_sign_up);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        ok = findViewById(R.id.ic_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name_value = name.getText().toString();
                String email_value = email.getText().toString();
                String password_value = password.getText().toString();
                String birthday_value = birthday.getText().toString();
                String bloodType_value = bloodType.getText().toString();
                String city_value = city.getText().toString();
                String address_value = address.getText().toString();
                String phone_value = phone.getText().toString();

                if (!name_value.isEmpty() && !email_value.isEmpty() &&
                        !password_value.isEmpty() && !birthday_value.isEmpty() &&
                        !bloodType_value.isEmpty() && !city_value.isEmpty() &&
                        !address_value.isEmpty() && !phone_value.isEmpty()) {

                    progressDialog.setMessage(getString(R.string.redister_message));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    registerUser(name_value, email_value, password_value, birthday_value,
                            bloodType_value, city_value, address_value, phone_value);

                } else {
                    Toast.makeText(SignUpActivity.this, getString(R.string.please_enter_all_data), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerUser(final String mName, final String mEmail,
                              final String mPassword, final String mBirthday, final String mBloodType,
                              final String mCity, final String mAddress, final String mPhone) {

        auth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String mUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            reference = firebaseDatabase.getReference()
                                    .child(KEY_USERS).child(mUid);

                            HashMap<String, String> mUser = new HashMap<>();
                            mUser.put(KEY_Uid, mUid);
                            mUser.put(KEY_Name, mName);
                            mUser.put(KEY_Email, mEmail);
                            mUser.put(KEY_Birthday, mBirthday);
                            mUser.put(KEY_BloodType, mBloodType);
                            mUser.put(KEY_City, mCity);
                            mUser.put(KEY_Address, mAddress);
                            mUser.put(KEY_Phone, mPhone);
                            mUser.put(KEY_Image, getString(R.string.default_word));

                            reference.setValue(mUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        progressDialog.dismiss();
                                        sendVerification();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            progressDialog.hide();
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    private void sendVerification() {
        FirebaseUser currentUser = auth.getCurrentUser();
        currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    auth.signOut();
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
