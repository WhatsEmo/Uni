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

        try{
            destination.createNewFile();
            fileOutputStream = new FileOutputStream(destination);
            fileOutputStream.write(bytes.toByteArray());
            fileOutputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return rotateProperly(cameraPicture, 90);
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

        return rotateProperly(bitmap, 90);
    }

    private Bitmap rotateProperly(Bitmap source, float angle){
        Bitmap rotatedImage;

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        rotatedImage = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return rotatedImage;
    }
}
