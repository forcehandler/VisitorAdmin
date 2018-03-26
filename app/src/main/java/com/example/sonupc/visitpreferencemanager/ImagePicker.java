package com.example.sonupc.visitpreferencemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


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

                    File file = new File(selectedImage.getPath());
                    long length = file.length();
                    length = length/1024;
                    System.out.println("File Path : " + file.getPath() + ", File size : " + length +" KB");

                    /*String absPath = file.getAbsolutePath();
                    Log.d(TAG, "absPath: " + absPath);
                    Log.d(TAG, "path: " + selectedImage.getPath());
                    //Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(selectedImage.getPath(), width, height);
                    BitmapFactory.Options mBitmapOptions = new BitmapFactory.Options();
                    mBitmapOptions.inSampleSize = 4;

                    Bitmap img = BitmapFactory.decodeFile(selectedImage.getPath(), mBitmapOptions);
                    imageView.setImageBitmap(img);
                    Log.d(TAG, "Got uri: " + selectedImage);*/


                    Picasso.get().load(selectedImage).into(imageView);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(selectedImage.getPath(), options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;
                    String imageType = options.outMimeType;
                    Log.d(TAG, "height: " + imageHeight + ", width: " + imageWidth + ", type: " + imageType);

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


    /*public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }*/
}
