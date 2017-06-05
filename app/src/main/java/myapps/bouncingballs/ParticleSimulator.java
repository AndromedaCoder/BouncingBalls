package myapps.bouncingballs;

import java.util.Random;

class ParticleSimulator
{
    private Particle m_Particles[];

    private Container2D m_Cont;

    private Vector3D m_Gravity;

    private double m_Dt;
    private double m_WallDamping;
    private int m_RelaxIterations;
    private int m_MaxIterReachedCounter;


    ParticleSimulator(int N, double MinSize, double MaxSize,
                      double Density,
                      double ContainerSizeX, double ContainerSizeY)
    {
        m_Particles = new Particle[N];
        m_Gravity = new Vector3D(0.0, 981.0, 0.0);

        m_Cont = new Container2D(ContainerSizeX, ContainerSizeY);

        //randomly distribute particle in the given region
        //so that they don't overlap:
        Random r = new Random(123456);
        Vector3D pos = new Vector3D();
        for (int i = 0; i < N; ++i)
        {
            double val = r.nextDouble();
            float size = (float)((MaxSize - MinSize) * val + MinSize);
            val = r.nextDouble();
            pos.setX(ContainerSizeX * val - 0.5 * ContainerSizeX);
            val = r.nextDouble();
            pos.setY(ContainerSizeY * val - 0.5 * ContainerSizeY);
            pos.setZ(0.0);
            m_Particles[i] = new Particle(pos, Density, size);
            boolean collides = true;
            while (collides)
            {
                collides = false;
                for (int j = 0; j < i; ++j)
                {
                    if (m_Particles[j].collidesWith(m_Particles[i]))
                    {
                        if (m_Particles[i].relaxCollisionCondition(m_Particles[j]))
                        {
                            collides = true;
                        }
                    }
                }
            }
        }

        m_Dt = 0.001;
        m_WallDamping = 0.2;
        m_RelaxIterations = 10;
        m_MaxIterReachedCounter = 0;
    }

    void setGravity(double XGrav, double YGrav)
    {
        m_Gravity.setX(XGrav);
        m_Gravity.setY(YGrav);
    }

    void doNextSimulationStep()
    {
        //move all particles according to the set gravity force:
        for (Particle Part : m_Particles)
        {
            Part.applyForce(m_Dt, m_Gravity);
        }

        //check for boundary violations and perform
        //the relevant elastic collisions:
        boolean constraintsViolated = false;
        for(int i = 0; i < m_Particles.length; ++i)
        {
            if (m_Particles[i].doContainerCollision(m_Cont, m_WallDamping) &&
                    m_Particles[i].clipToContainer(m_Cont))
            {
                constraintsViolated = true;
            }
            for(int j = i + 1; j < m_Particles.length; ++j)
            {
                if (m_Particles[i].collidesWith(m_Particles[j]))
                {
                    m_Particles[i].doElasticCollisionWith(m_Particles[j]);
                    if (m_Particles[i].relaxCollisionCondition(m_Particles[j]))
                    {
                        constraintsViolated = true;
                    }
                }
            }
        }

        //do an iterative constraint relaxation until no boundaries are violated anymore:
        int rem = m_RelaxIterations;
        while(constraintsViolated && rem-- > 0)
        {
            constraintsViolated = false;
            for (int i = 0; i < m_Particles.length; ++i)
            {
                if (m_Particles[i].clipToContainer(m_Cont))
                {
                    constraintsViolated = true;
                }
                for(int j = i + 1; j < m_Particles.length; ++j)
                {
                    if (m_Particles[i].relaxCollisionCondition(m_Particles[j]))
                    {
                        constraintsViolated = true;
                    }
                }
            }
        }
        if (rem <= 0) { ++m_MaxIterReachedCounter; }
    }

    int particleCount()
    {
        return m_Particles.length;
    }

    Particle getParticleAt(int Index)
    {
        return m_Particles[Index];
    }

    int getRelaxFailCount()
    {
        return m_MaxIterReachedCounter;
    }
}
