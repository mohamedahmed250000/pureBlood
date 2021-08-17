package elteam.pureblood.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import elteam.pureblood.R;

import static elteam.pureblood.activities.HomeActivity.loadLocale;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameEditText, passwordEditText;
    private TextView signUp, forgetPassword;
    private Button signIn;

    private ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.activity_login);

        userNameEditText = findViewById(R.id.username_login);
        passwordEditText = findViewById(R.id.password_login);
        signUp = findViewById(R.id.signup_login);
        forgetPassword = findViewById(R.id.forget_pass_login);
        signIn = findViewById(R.id.signin_login);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginToYourAccount();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void loginToYourAccount() {
        String textUserName = userNameEditText.getText().toString();
        String textPassword = passwordEditText.getText().toString();

        if (!textUserName.isEmpty() && !textPassword.isEmpty()) {

            progressDialog.setMessage(getString(R.string.logging_in_message));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

            auth.signInWithEmailAndPassword(textUserName, textPassword)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                progressDialog.dismiss();
                                checkVerification();
                            } else {
                                progressDialog.hide();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.please_enter_all_data), Toast.LENGTH_LONG).show();
        }
    }

    private void checkVerification() {
        FirebaseUser user = auth.getCurrentUser();
        if (user.isEmailVerified()) {

            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.please_verify_account), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
