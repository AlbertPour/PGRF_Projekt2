package fill;

import model.Edge;
import model.Line;
import model.Point;
import model.Polygon;
import rasterize.LineRasterizer;
import rasterize.PolygonRasterizer;

import java.util.ArrayList;
import java.util.Comparator;

public class ScanLineFiller implements Filler{
    private LineRasterizer lineRasterizer;
    private PolygonRasterizer polygonRasterizer;
    private Polygon polygon;
    private int xMin, xMax, yMin, yMax;
    public boolean firstMin = true;
    public boolean firstMax = true;
    ArrayList<Edge> edges;
    ArrayList<Point> intersections;


    public ScanLineFiller(LineRasterizer lineRasterizer, PolygonRasterizer polygonRasterizer, Polygon polygon){
        this.lineRasterizer = lineRasterizer;
        this.polygonRasterizer = polygonRasterizer;
        this.polygon = polygon;
        this.edges = polygon.getEdges();
        this.intersections = new ArrayList<>();
    }
    @Override
    public void fill() {
        for (int i = 0; i < polygon.size(); i++){
            Point p1 = polygon.getPoint(i);

            if (yMin > p1.y || firstMin){
                yMin= p1.y;
                firstMin = false;
            }
            if (yMax < p1.y || firstMax){
                yMax= p1.y;
                firstMax = false;
            }

        }

        for (int y = yMin+1; y <= yMax-1; y++) {
            // for each cyklus = pro každej prvek edge z listu edges
            for(Edge edge : edges) {
                if(edge.hasIntersection(y)) {
                    calculate(edge,y);}
            }
        }

       intersections.sort(new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                // Porovnání podle souřadnice y
                int compareY = Integer.compare(p1.y, p2.y);

                // Pokud jsou souřadnice y stejné, porovnej podle souřadnice x
                if (compareY == 0) {
                    return Integer.compare(p1.x, p2.x);
                }

                return compareY;
            }
        });

        for(int i = 0; i < intersections.size(); i += 2) {
            Line line = new Line(intersections.get(i),intersections.get(i+1),0xffffff);
            lineRasterizer.rasterize(line);
        }

    }

    private void calculate(Edge e, int y) {
        int x1 = e.getX1();
        int x2 = e.getX2();
        int y1 = e.getY1();
        int y2 = e.getY2();

        float k = (x1-x2)/(y1-y2);
        float q = x1-k*y1;
        int x =  (int)(k*y + q);
        Point intersection = new Point(x,y);
        intersections.add(intersection);
    }
}
