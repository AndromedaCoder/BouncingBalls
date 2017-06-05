package myapps.bouncingballs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.view.SurfaceHolder;


class GraphicsHandler implements Runnable
{
    private int counter;
    private SurfaceHolder mSurfaceHolder;
    private volatile Boolean mCancel;

    private Vector3D m_curGDirection;
    private ParticleSimulator m_sim;

    GraphicsHandler(SurfaceHolder Holder)
    {
        mSurfaceHolder = Holder;
        mCancel = false;
        counter = 0;

        m_curGDirection = new Vector3D(0.0, 1.0, 0.0);
    }

    synchronized void setNewGDirection(float DirX, float DirY)
    {
        m_curGDirection.setX(DirX);
        m_curGDirection.setY(DirY);
        m_curGDirection.normalize();
    }

    @Override
    public void run()
    {
        m_sim = new ParticleSimulator(30, 30, 40, 1,
                mSurfaceHolder.getSurfaceFrame().width(),
                mSurfaceHolder.getSurfaceFrame().height());
        Circle circ = new Circle();
        CornerPathEffect ceff = new CornerPathEffect(1);
        Paint p = new Paint();
        p.setPathEffect(ceff);
        while (!mCancel)
        {
            //threadsafe update of gravity direction in the simulator
            synchronized (this){
                m_sim.setGravity(m_curGDirection.getX() * 198100,
                        m_curGDirection.getY() * 198100);
            }
            m_sim.doNextSimulationStep();

            Canvas c = mSurfaceHolder.lockCanvas();

            if (c == null)
            {
                continue;
            }
            int width = c.getWidth();
            int height = c.getHeight();
            //do drawings and such:
            c.drawColor(Color.rgb(0, 0, 0));

            int count = m_sim.particleCount();

            for (int i = 0; i < count; ++i)
            {
                m_sim.getParticleAt(i).applyToCircle(circ);
                p.setColor(Color.rgb(10, 10 + (245 * i) / count, 255));
                c.drawCircle(circ.Cx + width/2, circ.Cy + height/2, circ.Radius, p);
            }

            p.setTextSize(20);

            String text = String.format("Relaxation fails=%1d", m_sim.getRelaxFailCount());
            c.drawText(text, 0, text.length(), 1, 15, p);

            mSurfaceHolder.unlockCanvasAndPost(c);

            ++counter;
        }
    }

    void stop()
    {
        mCancel = true;
    }
}