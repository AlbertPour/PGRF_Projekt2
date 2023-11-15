package model;

import java.util.ArrayList;

public class Polygon {
    private ArrayList<Point> points;

    public Polygon() {
        this.points = new ArrayList<>();
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

    public void clearPolygon() {
        this.points.clear();
    }

    public ArrayList<Edge> getEdges() {
        ArrayList<Edge> edges = new ArrayList<>();

        for (int i = 0; i < size(); i++) {
            int indexA = i;
            int indexB = i + 1;

            if (i == size() - 1) {
                indexB = 0;
            }

            Point pA = getPoint(indexA);
            Point pB = getPoint(indexB);

            int x1 = pA.x;
            int y1 = pA.y;
            int x2 = pB.x;
            int y2 = pB.y;

            Edge edge = new Edge(x1, y1, x2, y2);
            if(!edge.isHorizontal())
                edges.add(edge);
        }
        return edges;
    }
}
