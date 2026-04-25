package com.example.waiterapp;

import static androidx.core.content.ContentProviderCompat.requireContext;

import static java.security.AccessController.getContext;

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

    /**
     * Checks if the selected image file is within the allowed size limit.
     *
     * @param context The context of the activity/fragment
     * @param uri The URI of the selected image
     * @param maxMb The maximum allowed size in Megabytes
     * @return true if the file is small enough, false otherwise
     */
    public static boolean isImageSizeValid(Context context, Uri uri, int maxMb) {
        try {
            // Use the passed context to get the ContentResolver
            android.content.res.AssetFileDescriptor fd = context.getContentResolver().openAssetFileDescriptor(uri, "r");
            if (fd == null) return false;

            long fileSize = fd.getLength();
            fd.close();

            // Convert bytes to Megabytes
            long maxBytes = (long) maxMb * 1024 * 1024;
            return fileSize <= maxBytes;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
