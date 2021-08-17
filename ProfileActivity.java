package elteam.pureblood.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;
import elteam.pureblood.R;

import static elteam.pureblood.Constants.KEY_Address;
import static elteam.pureblood.Constants.KEY_Birthday;
import static elteam.pureblood.Constants.KEY_BloodType;
import static elteam.pureblood.Constants.KEY_City;
import static elteam.pureblood.Constants.KEY_Email;
import static elteam.pureblood.Constants.KEY_Image;
import static elteam.pureblood.Constants.KEY_Name;
import static elteam.pureblood.Constants.KEY_Phone;
import static elteam.pureblood.Constants.KEY_ProfileImages;
import static elteam.pureblood.Constants.KEY_USERS;
import static elteam.pureblood.activities.HomeActivity.loadLocale;

public class ProfileActivity extends AppCompatActivity {

    private static final int GALLERY_PICK = 100;

    private TextView profile_name, profile_email, profile_blood_type,
            profile_city, profile_address, profile_phone;
    private CircleImageView profile_image;

    private ProgressDialog progressDialog;
    private ProgressDialog imageProgressDialog;

    private FirebaseUser current_user;
    private DatabaseReference reference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        setContentView(R.layout.activity_profile);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.my_profile));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait_while_we_loading_data));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        imageProgressDialog = new ProgressDialog(this);
        imageProgressDialog.setMessage(getString(R.string.please_wait_while_we_uploading_your_photo));
        imageProgressDialog.setCanceledOnTouchOutside(false);
        imageProgressDialog.setCancelable(false);

        profile_name = findViewById(R.id.profile_name);
        profile_email = findViewById(R.id.profile_email);
        profile_blood_type = findViewById(R.id.profile_blood_type);
        profile_city = findViewById(R.id.profile_city);
        profile_address = findViewById(R.id.profile_address);
        profile_phone = findViewById(R.id.profile_phone);
        profile_image = findViewById(R.id.profile_image);

        current_user = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = current_user.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child(KEY_USERS).child(current_uid);
        storageReference = FirebaseStorage.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String mName = dataSnapshot.child(KEY_Name).getValue().toString();
                String mEmail = dataSnapshot.child(KEY_Email).getValue().toString();
                String mBirthday = dataSnapshot.child(KEY_Birthday).getValue().toString();
                String mBloodType = dataSnapshot.child(KEY_BloodType).getValue().toString();
                String mCity = dataSnapshot.child(KEY_City).getValue().toString();
                String mAddress = dataSnapshot.child(KEY_Address).getValue().toString();
                String mPhone = dataSnapshot.child(KEY_Phone).getValue().toString();
                String mImage = dataSnapshot.child(KEY_Image).getValue().toString();

                profile_name.setText(mName);
                profile_email.setText(mEmail);
                profile_blood_type.setText(mBloodType);
                profile_city.setText(mCity);
                profile_address.setText(mAddress);
                profile_phone.setText(mPhone);

                if (mImage.equals(getString(R.string.default_word))) {
                    profile_image.setImageResource(R.drawable.ic_profile);
                    progressDialog.dismiss();
                } else {
                    Glide.with(ProfileActivity.this)
                            .asBitmap()
                            .load(mImage)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.ic_profile)
                                    .error(R.drawable.ic_profile))
                            .into(profile_image);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGallery = new Intent();
                intentGallery.setType("image/*");
                intentGallery.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intentGallery,
                        getString(R.string.select_image_intent)), GALLERY_PICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == GALLERY_PICK) {

            imageProgressDialog.show();

            Uri imageUri = data.getData();
            StorageReference filePath = storageReference.child(KEY_ProfileImages)
                    .child(current_user.getUid() + ".jpg");
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = String.valueOf(uri);
                            reference.child(KEY_Image).setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        imageProgressDialog.dismiss();
                                        Toast.makeText(ProfileActivity.this, getString(R.string.SuccessUploading), Toast.LENGTH_SHORT).show();
                                    } else {
                                        imageProgressDialog.hide();
                                        Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }
            });
        }

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
