package com.example.whatsemo.classmates;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Base64;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by WhatsEmo on 5/8/2016.
 */
public class ImageHandler {

    private Context context;

    public ImageHandler(Context context) {
        this.context = context;
    }

    public Bitmap fromCamera(Intent data){
        Bitmap cameraPicture = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        cameraPicture.compress(Bitmap.CompressFormat.PNG, 50, bytes);

        //Saves the newly taken picture into their files
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".png");

        FileOutputStream fileOutputStream;
        Uri selectedImageUri = data.getData();
        int orientation = getOrientation(selectedImageUri);

        try{
            destination.createNewFile();
            fileOutputStream = new FileOutputStream(destination);
            fileOutputStream.write(bytes.toByteArray());
            fileOutputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        if(orientation > 0){
            return rotateProperly(cameraPicture, orientation);
        }else {
            return cameraPicture;
        }
    }

    public Bitmap fromLibrary(Intent data){
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        CursorLoader cursorLoader = new CursorLoader(context, selectedImageUri, projection, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(columnIndex);

        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);

        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while(options.outWidth / scale / 2 >= REQUIRED_SIZE &&
                options.outHeight / scale / 2 >= REQUIRED_SIZE) {
            scale *= 2;
        }
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(selectedImagePath, options);

        int orientation = getOrientation(selectedImageUri);
        if( orientation > 0){
            return rotateProperly(bitmap, orientation);
        }else {
            return bitmap;
        }
    }

    private Bitmap rotateProperly(Bitmap source, float angle){
        Bitmap rotatedImage;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        rotatedImage = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return rotatedImage;
    }

    private int getOrientation(Uri photoUri){
        Cursor cursor = context.getContentResolver().query(photoUri, new String[]{ MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
        if(cursor.getCount() != 1){
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public void uploadToFirebase(Firebase ref, Bitmap bm, String label){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);

        ref.child("users").child(ref.getAuth().getUid()).child(label).setValue(imageFile);
    }

    public Bitmap convertByteArrayToBitmap(String stringOfByteArray){
        byte[] imageAsByte = Base64.decode(stringOfByteArray, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
    }
}
