package myapps.bouncingballs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.SurfaceHolder;

import static java.lang.Math.random;

/**
 * Created by Chris on 07.05.2017.
 */

class GraphicsHandler implements Runnable
{
    int counter;
    private SurfaceHolder mSurfaceHolder;
    private volatile Boolean mCancel;

    GraphicsHandler(SurfaceHolder Holder)
    {
        mSurfaceHolder = Holder;
        mCancel = false;
        counter = 0;
    }

    @Override
    public void run()
    {
        int ballGrid = 25;
        float[] radius = new float[ballGrid * ballGrid];
        PointF[] pos = new PointF[ballGrid * ballGrid];
        PointF[] vel = new PointF[ballGrid * ballGrid];
        float internalW = 50.0f;
        float internalH = 50.0f;
        for (int i = 0; i < ballGrid; ++i)
        {
            for (int j = 0; j < ballGrid; ++j)
            {
                int index = i * ballGrid + j;
                double speed = random() + 0.1f;
                double dir = random() * Math.PI * 2f;
                vel[index] = new PointF((float)(speed * Math.cos(dir)),
                        (float)(speed * Math.sin(dir)));
                pos[index] = new PointF(internalW / ballGrid * i + 2,
                        internalH / ballGrid * j + 2);
            }
        }

        while (!mCancel)
        {
            for (int i = 0; i < ballGrid; ++i)
            {
                for (int j = 0; j < ballGrid; ++j)
                {
                    int index = i * ballGrid + j;
                    radius[index] = (float) (Math.cos((counter + i + j) / 50.0 * 2 * Math.PI) * 0.5 + 1);
                    pos[index].offset(vel[index].x, vel[index].y);

                    if (pos[index].x + radius[index] >= internalW ||
                            pos[index].x - radius[index] <= 0.0f)
                    {
                        vel[index].x *= -1.0f;
                        pos[index].x = Math.max(0.5f + radius[index],
                                Math.min(pos[index].x, internalW - 0.5f - radius[index]));
                    }
                    if (pos[index].y + radius[index] >= internalH ||
                            pos[index].y - radius[index] <= 0.0f)
                    {
                        vel[index].y *= -1.0f;
                        pos[index].y = Math.max(0.5f + radius[index],
                                Math.min(pos[index].y, internalH - 0.5f - radius[index]));
                    }
                }
            }
            Canvas c = mSurfaceHolder.lockCanvas();

            int width = c.getWidth();
            int height = c.getHeight();
            //do drawings and such:
            Paint p = new Paint();

            p.setPathEffect(new CornerPathEffect(1));

            c.drawColor(Color.rgb(0, 0, 0));
            float baseR = width / (2 * ballGrid);
            float wRange = width - 4 * baseR;
            float hRange = height - 4 * baseR;

            for (int i = 0; i < ballGrid; ++i)
            {
                for (int j = 0; j < ballGrid; ++j)
                {
                    p.setColor(Color.rgb(j * 10, i * 10, 255 - i * 5 - 5 * j ));
                    int index = ballGrid * i + j;
                    c.drawCircle(pos[index].x * wRange / internalW + 2 * baseR,
                            pos[index].y * hRange / internalH + 2 * baseR,
                            baseR * radius[ballGrid * i + j], p);
                }
            }

            c.drawText("hello world!", 0, 11, 1, height / 4, p);

            mSurfaceHolder.unlockCanvasAndPost(c);

            ++counter;
        }
    }

    void stop()
    {
        mCancel = true;
    }
}