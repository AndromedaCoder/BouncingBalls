package myapps.bouncingballs;

class Container2D
{
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int TOP = 4;
    private static final int BOTTOM = 8;

    private Vector3D mULCorner;
    private Vector3D mLRCorner;

    Container2D(double SizeX, double SizeY)
    {
        mULCorner = new Vector3D(-SizeX * 0.5, -SizeY * 0.5, 0.0);
        mLRCorner = new Vector3D(SizeX * 0.5, SizeY * 0.5, 0.0);
    }


    int isInside(Vector3D Pos, double Margin)
    {
        int res = 0;
        if (mULCorner.getX() > Pos.getX() - Margin) { res |= LEFT; }
        if (mLRCorner.getX() < Pos.getX() + Margin) { res |= RIGHT; }
        if (mULCorner.getY() > Pos.getY() - Margin) { res |= TOP; }
        if (mLRCorner.getY() < Pos.getY() + Margin) { res |= BOTTOM; }
        return res;
    }

    void reflectOnSide(Vector3D Vect, int Sides)
    {
        if ((Sides & (LEFT | RIGHT)) > 0)
        {
            Vect.negateAxis(0);
        }
        if ((Sides & (TOP | BOTTOM)) > 0)
        {
            Vect.negateAxis(1);
        }
    }

    boolean clip(Vector3D Pos, double Margin)
    {
        if (isInside(Pos, Margin) > 0)
        {
            Pos.setX(Math.max(Math.min(Pos.getX(), mLRCorner.getX() - Margin),
                    mULCorner.getX() + Margin));
            Pos.setY(Math.max(Math.min(Pos.getY(), mLRCorner.getY() - Margin),
                    mULCorner.getY() + Margin));
            return true;
        }
        else
        {
            return false;
        }

    }
}

class BoundaryBox
{
    float Right;
    float Left;
    float Top;
    float Bottom;
}

class Circle
{
    float Cx;
    float Cy;
    float Radius;
}
