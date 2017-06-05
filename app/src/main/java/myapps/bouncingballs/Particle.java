package myapps.bouncingballs;

/**
 * Particle class
 */

class Particle
{
    private Vector3D m_Position;
    private Vector3D m_Velocity;

    private double m_Mass;
    private float m_Size;

    Particle(Vector3D Pos, double Density, float Size)
    {
        m_Position = new Vector3D(Pos);
        m_Velocity = new Vector3D();
        m_Mass = 4.0/3.0 * Math.PI * Math.pow(Size, 3) * Density;
        m_Size = Size;
    }

    void applyForce(double Dt, Vector3D Force)
    {
        m_Position.add(Force, 0.5 * Dt * Dt);
        m_Position.add(m_Velocity, Dt);
        m_Velocity.add(Force, Dt);
    }

    void setPos(Vector3D NewPos)
    {
        m_Position = new Vector3D(NewPos);
    }

    boolean isInside(Container2D Rect)
    {
        return Rect.isInside(m_Position, m_Size) == 0;
    }

    boolean doContainerCollision(Container2D Rect, double DampingFactor)
    {
        int sides = Rect.isInside(m_Position, m_Size);
        if (sides > 0)
        {
            Rect.reflectOnSide(m_Velocity, sides);
            m_Velocity.scale(1 - DampingFactor);
            return true;
        }
        else { return false; }
    }

    boolean clipToContainer(Container2D Rect)
    {
        return Rect.clip(m_Position, m_Size + 0.1);
    }

    boolean collidesWith(Particle Other)
    {
        return m_Position.getSqrDistTo(Other.m_Position) < Math.pow(m_Size + Other.m_Size, 2);
    }

    void doElasticCollisionWith(Particle B)
    {
        Vector3D dist = new Vector3D(m_Position);
        dist.sub(B.m_Position);
        dist.normalize();

        double velInA = m_Velocity.multiply(dist);
        double velInB = B.m_Velocity.multiply(dist);

        double velAOut = (velInA * (m_Mass - B.m_Mass) + 2 * B.m_Mass * velInB) / (m_Mass + B.m_Mass);
        double velBOut = (velInB * (B.m_Mass - m_Mass) + 2 * m_Mass * velInA) / (m_Mass + B.m_Mass);

        m_Velocity.add(dist, -velInA); //remove component along distance vector, only orthogonal component remains
        m_Velocity.add(dist, velAOut); //add new post-collision velocity vector

        B.m_Velocity.add(dist, -velInB); //remove component along distance vector, only orthogonal component remains
        B.m_Velocity.add(dist, velBOut); //add new post-collision velocity vector
    }

    boolean relaxCollisionCondition(Particle B)
    {
        double dist = m_Position.getSqrDistTo(B.m_Position);
        if (dist < Math.pow(m_Size + B.m_Size, 2))
        {
            Vector3D distVec = new Vector3D(m_Position);
            distVec.sub(B.m_Position);
            distVec.normalize();

            double delta = 0.51 * (m_Size + B.m_Size - Math.sqrt(dist)) + 0.1;
            m_Position.add(distVec, delta);
            B.m_Position.add(distVec, -delta);

            return true;
        }
        else
        {
            return false;
        }
    }

    void applyToBoundaryBox(BoundaryBox BB)
    {
        BB.Bottom = (float)m_Position.getY() + m_Size;
        BB.Top = (float)m_Position.getY() - m_Size;
        BB.Right = (float)m_Position.getX() + m_Size;
        BB.Left = (float)m_Position.getX() - m_Size;
    }

    void applyToCircle(Circle Crc)
    {
        Crc.Cx = (float)m_Position.getX();
        Crc.Cy = (float)m_Position.getY();
        Crc.Radius = m_Size;
    }
}
