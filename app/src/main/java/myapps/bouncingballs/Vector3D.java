package myapps.bouncingballs;


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
    public double getZ()
    {
        return m_pos[2];
    }

    void setX(double X)
    {
        m_pos[0] = X;
    }
    void setY(double Y)
    {
        m_pos[1] = Y;
    }
    void setZ(double Z)
    {
        m_pos[2] = Z;
    }

    Vector3D add(Vector3D A)
    {
        for(int i = 0; i < 3; ++i)
        {
            m_pos[i] += A.m_pos[i];
        }
        return this;
    }

    Vector3D add(Vector3D A, double Scale)
    {
        for(int i = 0; i < 3; ++i)
        {
            m_pos[i] += Scale * A.m_pos[i];
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

    private double getNorm()
    {
        return Math.sqrt(getNormSqr());
    }

    void normalize()
    {
        double norm = getNorm();
        for(int i = 0; i < 3; ++i)
        {
            m_pos[i] /= norm;
        }
    }

    private double getNormSqr()
    {
        return m_pos[0] * m_pos[0] + m_pos[1] * m_pos[1] + m_pos[2] * m_pos[2];
    }

    double getSqrDistTo(Vector3D Other)
    {
        double val = 0.0;
        for(int i = 0; i < 3; ++i)
        {
            val +=  Math.pow(m_pos[i] - Other.m_pos[i], 2);
        }
        return val;
    }

    void scale(double F)
    {
        for(int i = 0; i < 3; ++i)
        {
            m_pos[i] *= F;
        }
    }

    double multiply(Vector3D Other)
    {
        return m_pos[0] * Other.m_pos[0] + m_pos[1] * Other.m_pos[1] + m_pos[2] * Other.m_pos[2];
    }
}
