package com.example.battleship;

public class GridPoint {
    private int x;
    private int y;
    public GridPoint(){}
    public GridPoint(int x,int y)
    {
        this.x=x;
        this.y=y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        GridPoint otherPoint=(GridPoint)obj;
        if((otherPoint.x==this.x)&&(otherPoint.y==this.y))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //returns true if 2 point are adjacent
    public boolean adjacent(GridPoint otherPoint)
    {
        if (((Math.abs(this.x-otherPoint.x)==1)&&(this.y==otherPoint.y))||
                ((Math.abs(this.y-otherPoint.y)==1)&&(this.x==otherPoint.x)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //convert from position (without offset) to (x,y) coordinates (1,1)->(10,10)
    public static GridPoint ConvertPositionToPoint(int position)
    {
        int x=position%10+1;
        int y=position/10+1;
        GridPoint g=new GridPoint(x,y);
        return g;
    }
}
