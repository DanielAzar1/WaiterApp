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

public class ImageHelper {
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
     * Input: Uri uri, String path
     * Output: Void
     * Function uploads an image to firebase storage
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
