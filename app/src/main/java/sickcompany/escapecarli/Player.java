package sickcompany.escapecarli;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DUMORA on 10/27/2017.
 */

public class Player {

    boolean isMoving = true;
    boolean isJumping = false;
    boolean isPaused = false;

    int width;
    int height;
    int coefx = 15;
    int coefy = 80;
    int x;
    int y;
    int currentRunningFrame = 0;
    int currentJumpingFrame = 0;

    ArrayList<Bitmap> playerBitMapRunning;
    ArrayList<Bitmap> playerBitMapJumping;
    private long lastFrameChange;
    private int FrameLength = 100;
    int nbrFrame = 10;


    public Player(Context context, int screenWidth, int screenHeight) {
        playerBitMapRunning = new ArrayList<>();
        for (int i = 1; i <= nbrFrame; i++) {
            Bitmap toAdd;
            int resID = context.getResources().getIdentifier("run" + i, "drawable", context.getPackageName());
            toAdd = (BitmapFactory.decodeResource(context.getResources(), resID));
            x = coefx * screenWidth / 100;
            y = coefy * screenHeight / 100;
            toAdd = Bitmap.createScaledBitmap(toAdd, 136, 130, true);
            playerBitMapRunning.add(toAdd);
            width = toAdd.getWidth();
            height = toAdd.getHeight();
        }
    }

    public void pause() {
        isPaused = true;
    }

    public void update() {
        long time = System.currentTimeMillis();
        if (!isPaused) {
            if (isMoving) {
                if (time > lastFrameChange + FrameLength) {
                    lastFrameChange = time;
                    currentRunningFrame++;
                    if (currentRunningFrame >= nbrFrame)
                        currentRunningFrame = 0;
                }
            } else if (isJumping) {
                currentRunningFrame = 0;
            }
        }
    }
        public void draw(Canvas canvas, Paint paint) {
            if (isMoving)
                canvas.drawBitmap(playerBitMapRunning.get(currentRunningFrame), x, y, paint);
            else if (isJumping)
                canvas.drawBitmap(playerBitMapJumping.get(currentJumpingFrame), x, y, paint);
        }
}
