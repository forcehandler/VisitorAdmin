package com.example.sonupc.visitpreferencemanager;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class ImagePicker extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = ImagePicker.class.getSimpleName();

    private Button btn_picker, btn_upload;
    private ImageView imageView;

    private StorageReference mLogoStorageRef;

    private String instituteId;
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        btn_picker = findViewById(R.id.btn_pick_img);
        btn_upload = findViewById(R.id.btn_upload);
        imageView = findViewById(R.id.imageView);

        btn_picker.setOnClickListener(this);
        btn_upload.setOnClickListener(this);

        Intent intent = getIntent();
        instituteId = intent.getStringExtra(getString(R.string.intent_key_instituteId));
        mLogoStorageRef = FirebaseStorage.getInstance().getReference()
                .child(instituteId + "/" + getString(R.string.storage_logo_ref ) + "/" + getString(R.string.brand_logo_filename) + ".png");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_pick_img:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 0);//one can be replaced with any action code
                break;
            case R.id.btn_upload:
                uploadImage(selectedImage);
                Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    Log.d(TAG, "Got uri: " + selectedImage);
                    imageView.setImageURI(selectedImage);
                }
                else{
                    Log.e(TAG, " Error picking image");
                }
                break;
        }
    }

    private void uploadImage(Uri file){
        UploadTask uploadTask = mLogoStorageRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e(TAG, "Logo upload unsuccessful");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG, "Logo uploaded: " + downloadUrl);
                finish();
            }
        });

    }
}
