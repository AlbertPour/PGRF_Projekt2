package model;

import java.util.ArrayList;

public class Rectangle extends Polygon{
    public Point p1, p3;
    private ArrayList<Rectangle> rectangles;


    public Rectangle(Point p1, Point p3)
    {
       addPoint(p1);
       addPoint(new Point(p1.x, p3.y));
       addPoint(p3);
       addPoint(new Point(p3.x, p1.y));
    }

}
