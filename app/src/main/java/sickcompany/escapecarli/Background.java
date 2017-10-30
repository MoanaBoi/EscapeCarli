package sickcompany.escapecarli;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by DUMORA on 10/27/2017.
 */

public class Background {
    Bitmap bitmap;
    Bitmap bitmapReversed;

    int width;
    int height;
    boolean reversedFirst;
    float speed;

    int xClip;
    int startY;
    int endY;

    Background(Context context, int screenWidth, int screenHeight, String bitmapName, int sY, int eY, float s){
        int resID = context.getResources().getIdentifier(bitmapName,"drawable", context.getPackageName());
        bitmap = BitmapFactory.decodeResource(context.getResources(), resID);
        reversedFirst = false;
        xClip = 0;
        startY = sY * (screenHeight / 100);
        endY = eY * (screenHeight / 100);
        speed = s;
        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth,
                (endY - startY)
                , true);
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        bitmapReversed = bitmap.createBitmap(bitmap);
    }

    public void update(long fps){
        xClip -= speed/fps;
        if (xClip >= width) {
            xClip = 0;
        } else if (xClip <= 0) {
            xClip = width;
            reversedFirst = !reversedFirst;
        }
    }
}
