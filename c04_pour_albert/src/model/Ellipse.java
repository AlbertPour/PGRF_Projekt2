package model;

import java.awt.*;
import java.util.ArrayList;

public class Ellipse extends Polygon {
    private ArrayList<Point> points;

    public Point p1;
    //hlavní a vedlejší osa
    double a,b;

    public Ellipse(){this.points = new ArrayList<>();}
    public Ellipse(Point p1, int a, int b){
        this.points = new ArrayList<>();
        this.a = a;
        this.b = b;
        double px, py =0;
        points.add(p1);
        Point p0 = getPoint(0);
        if (a > p0.x){
            a = a-p0.x;
        }
        else {
            a = p0.x - a;
        }
        if (b > p0.y){
            b = b - p0.y;
        }
        else {
            b = p0.y - b;
        }
        //for cyklus pro 360°
        for (int i = 0; i <=360; i++) {
            double x = a * Math.sin(Math.toRadians(i));
            double y = b * Math.cos(Math.toRadians(i));
            Point p2 = new Point(x+p0.x, y+ p0.y);
            points.add(p2);
        }
    }
    public int size() {
        return this.points.size();
    }

    public Point getPoint(int index) {
        return this.points.get(index);
    }

    public void addPoint(Point p) {
        this.points.add(p);
    }
}
