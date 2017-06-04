package myapps.bouncingballs;

/**
 * Created by Chris on 07.05.2017.
 */

class Vector3D
{
    private double[] m_pos;
    Vector3D()
    {
        m_pos = new double[]{0.0, 0.0, 0.0};
    }
    Vector3D(double X, double Y, double Z)
    {
       m_pos = new double[]{ X, Y, Z};
    }
    Vector3D(Vector3D Inst)
    {
        m_pos = new double[]{ Inst.m_pos[0], Inst.m_pos[1], Inst.m_pos[2]};
    }

    double getX()
    {
        return m_pos[0];
    }
    double getY()
    {
        return m_pos[1];
    }
    double getZ()
    {
        return m_pos[2];
    }

    Vector3D add(Vector3D A)
    {
        for(int i = 0; i < 3; ++i)
        {
            m_pos[i] += A.m_pos[i];
        }
        return this;
    }

    Vector3D sub(Vector3D A)
    {
        for(int i = 0; i < 3; ++i)
        {
            m_pos[i] -= A.m_pos[i];
        }
        return this;
    }

    void negateAxis(int Index)
    {
        m_pos[Index] = -m_pos[Index];
    }

    double getNorm()
    {
        return Math.sqrt(getNormSqr());
    }

    double getNormSqr()
    {
        return m_pos[0] * m_pos[0] + m_pos[1] * m_pos[1] + m_pos[2] * m_pos[2];
    }
}
