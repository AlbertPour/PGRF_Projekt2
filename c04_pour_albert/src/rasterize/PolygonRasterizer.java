package rasterize;

import model.Ellipse;
import model.Line;
import model.Point;
import model.Polygon;

public class PolygonRasterizer {
    private LineRasterizer lineRasterizer;

    public PolygonRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    public void rasterize(Polygon polygon) {
        if (polygon.size() < 3) {
            return;
        }
        for (int i = 0; i < polygon.size(); i++) {
            int indexA = i;
            int indexB = i + 1;

            if (i == polygon.size() - 1) {
                indexB = 0;
            }

            Point pA = polygon.getPoint(indexA);
            Point pB = polygon.getPoint(indexB);

            lineRasterizer.rasterize(new Line(pA, pB, 0xff0000));
        }
    }
    public void rasterize(Ellipse ellipse) {
        for (int i = 1; i < ellipse.size()-2; i++) {
            int indexA = i;
            int indexB = i + 1;

            Point pA = ellipse.getPoint(indexA);
            Point pB = ellipse.getPoint(indexB);

            lineRasterizer.rasterize(new Line(pA, pB, 0xff0000));
        }
    }
}
