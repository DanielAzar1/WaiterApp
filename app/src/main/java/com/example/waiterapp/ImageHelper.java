package com.example.waiterapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Class is a helper class for Handling Images.
 *
 * @author Daniel Azar
 * */
public class ImageHelper {

    /**
     * Function gets a bitmap from a uri
     *
     * @param context the context of the activity
     * @param uri the uri of the image
     *
     * @return bitmap of the given image
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri)
    {
        try
        {
            java.io.InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream);
            return selectedBitmap;
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Function uploads an image to firebase storage
     *
     * @param uri the image to upload
     * @param path the path to upload to
     */
    public static void uploadImageToFirebase(Uri uri, String path)
    {
        StorageReference ref = FBref.storageRef.child(path);
        UploadTask uploadTask = ref.putFile(uri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
        {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Log.d("uploadImage", "Upload successful");
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Log.d("uploadImage", "Upload Failed");
            }

        });
    }
}
