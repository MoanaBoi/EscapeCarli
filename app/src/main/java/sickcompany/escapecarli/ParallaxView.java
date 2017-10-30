package sickcompany.escapecarli;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class ParallaxView extends SurfaceView implements Runnable {

    ArrayList<Background> backgrounds;
    Player player;

    private volatile boolean running;
    private Thread gameThread = null;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;

    boolean isPaused = false;

    Context context;

    long fps = 60;

    int screenWidth;
    int screenHeight;

    public ParallaxView(Context context, int x, int y) {
        super(context);
        this.context = context;

        this.screenWidth = x;
        this.screenHeight = y;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();

        backgrounds = new ArrayList<>();

        // Load the player asset
        player = new Player(this.context,
                screenWidth,
                screenHeight);

        // load the background data into the Background objects and
        // place them in our GameObject arraylist

        backgrounds.add(new Background(
                this.context,
                screenWidth,
                screenHeight,
                "foreground", 0, 105, 200));

        backgrounds.add(new Background(
                this.context,
                screenWidth,
                screenHeight,
                "back_buildings", 0, 110, 150));
        backgrounds.add(new Background(
                this.context,
                screenWidth,
                screenHeight,
                "far_buildings", 0, 110, 100));
    }

    @Override
    public void run() {
        while (running) {
            long startFrameTime = System.currentTimeMillis();

            update();
            draw();

            // Calculate the fps this frame
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {
        if (running) {
            for (Background bg : backgrounds) {
                bg.update(fps);
            }
            player.update();
        }
    }

    private void drawBackground(int position) {

        // Make a copy of the relevant background
        Background bg = backgrounds.get(position);

        // define what portion of images to capture and
        // what coordinates of screen to draw them at

        // For the regular bitmap
        Rect fromRect1 = new Rect(0, 0, bg.width - bg.xClip, bg.height);
        Rect toRect1 = new Rect(bg.xClip, bg.startY, bg.width, bg.endY);

        // For the reversed background
        Rect fromRect2 = new Rect(bg.width - bg.xClip, 0, bg.width, bg.height);
        Rect toRect2 = new Rect(0, bg.startY, bg.xClip, bg.endY);

        //draw the two background bitmaps
        if (!bg.reversedFirst) {
            canvas.drawBitmap(bg.bitmap, fromRect1, toRect1, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect2, toRect2, paint);
        } else {
            canvas.drawBitmap(bg.bitmap, fromRect2, toRect2, paint);
            canvas.drawBitmap(bg.bitmapReversed, fromRect1, toRect1, paint);
        }

    }

    private void draw() {

        if (ourHolder.getSurface().isValid()) {
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            //draw a background color

            //Draw the background parallax
            drawBackground(2);
            drawBackground(1);
            drawBackground(0);

            // Draw the rest of the game
            player.draw(canvas, paint);
            paint.setTextSize(60);
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawText("I am a plane", 350, screenHeight / 100 * 5, paint);

            // Draw the foreground parallax

            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
            player.pause();
        } catch (InterruptedException e) {
            // Error
        }
    }

    // Execution moves to our run method
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}
