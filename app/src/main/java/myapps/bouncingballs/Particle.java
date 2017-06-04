package myapps.bouncingballs;

/**
 * Created by Chris on 07.05.2017.
 */

class Particle
{
    Vector3D m_Position;
    Vector3D m_Velocity;

    double m_Mass;
    float m_Size;

    Particle(Vector3D Pos, double Mass, float Size)
    {
        m_Position = new Vector3D(Pos);
        m_Velocity = new Vector3D();
        m_Mass = Mass;
        m_Size = Size;
    }


}
