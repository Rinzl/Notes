package com.thd.notes.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Tran Hai Dang on 11/4/2017.
 * Email : tranhaidang2320@gmail.com
 */

public class DbBitmapUtility {
    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public static void saveFile(Context context, Bitmap b, String picName){
        try (FileOutputStream fos = context.openFileOutput(picName, Context.MODE_PRIVATE)) {
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            Log.d("ERROR", "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("ERROR", "io exception");
            e.printStackTrace();
        }
    }
    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        try (FileInputStream fis = context.openFileInput(picName)) {
            b = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            Log.d("ERROR", "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("ERROR", "io exception");
            e.printStackTrace();
        }
        return b;
    }
}
