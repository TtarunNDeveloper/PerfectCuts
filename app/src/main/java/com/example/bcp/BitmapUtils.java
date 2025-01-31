package com.example.bcp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BitmapUtils {

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromBytes(byte[] image) {
        ByteArrayInputStream stream = new ByteArrayInputStream(image);
        return BitmapFactory.decodeStream(stream);
    }
}
