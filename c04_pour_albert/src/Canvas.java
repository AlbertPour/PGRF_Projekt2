import fill.ScanLineFiller;
import fill.SeedFiller;
import model.*;
import model.Point;
import model.Polygon;
import model.Rectangle;
import rasterize.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * trida pro kresleni na platno: vyuzita tridy RasterBufferedImage
 *
 * @author PGRF FIM UHK
 * @version 2020
 */

public class Canvas {

    private JPanel panel;
    private JTextField clickModeText;
    private JTextField switchModeText;
    private RasterBufferedImage raster;
    private LineRasterizerGraphics lineRasterizerGraphics;
    private Point p1, p2, p3, p4, lastP, lastP2;
    private Polygon polygon;
    private PolygonRasterizer polygonRasterizer;
    private int switchMode = 1;
    private int typeOfMode = 1;
    private boolean clickMode = true;
    private boolean firstPoint = true;
    private boolean firstPointRectangle = true;
    private boolean firstPointEllipse = true;


    public Canvas(int width, int height) {

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Inicializace textového pole
        clickModeText = new JTextField("Click mode Activated", 20);
        clickModeText.setEditable(false);
        clickModeText.setBackground(Color.black);
        clickModeText.setForeground(Color.white);

        switchModeText = new JTextField("Normal mode", 20);
        switchModeText.setEditable(false);
        switchModeText.setBackground(Color.black);
        switchModeText.setForeground(Color.white);

        raster = new RasterBufferedImage(width, height);

        lineRasterizerGraphics = new LineRasterizerGraphics(raster);
        polygonRasterizer = new PolygonRasterizer(lineRasterizerGraphics);

        polygon = new Polygon();

        panel = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        panel.add(clickModeText);
        panel.add(switchModeText);
        panel.setFocusable(true);

        //Zachytávání události pro myš
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (e.getButton() == MouseEvent.BUTTON1){
                    raster.clear();
                    if (typeOfMode == 1){
                        //Zjišujeme zda je aktivovanı klikací mód
                        if (clickMode) {
                            p1 = new Point(e.getX(), e.getY());
                            polygon.addPoint(p1);
                            polygonRasterizer.rasterize(polygon);
                            if (!firstPoint){
                                Line line = new Line(lastP, p1, 0xff0000);
                                lineRasterizerGraphics.rasterize(line);
                            }
                            firstPoint = false;
                            lastP = p1;
                        }
                        if (!clickMode) {
                            if (firstPoint) {
                                p1 = new Point(e.getX(), e.getY());
                                polygon.addPoint(p1);
                                firstPoint = !firstPoint;
                            }
                        }
                    }
                    if (typeOfMode == 2){
                        //Vykreslení obdelníku
                        p3 = new Point(e.getX(), e.getY());
                        if (!firstPointRectangle){
                            Rectangle rectangle = new Rectangle(lastP2,p3);
                            polygonRasterizer.rasterize(rectangle);
                        }
                        firstPointRectangle = false;
                        lastP2 = p3;
                    }
                    if (typeOfMode == 3){
                        //Vykreslení elipsy
                        p4 = new Point(e.getX(), e.getY());
                    }

                }
                if (e.getButton() == MouseEvent.BUTTON3){
                    SeedFiller seedFiller= new SeedFiller(raster, raster.getPixel(e.getX(), e.getY()), e.getX(), e.getY());
                    seedFiller.fill();
                }
                panel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                raster.clear();
                if (typeOfMode == 3){
                    //Vykreslení elipsy
                    int a = e.getX();
                    int b = e.getY();
                    Ellipse ellipse = new Ellipse(p4,a,b);
                    polygonRasterizer.rasterize(ellipse);
                }
                if (!clickMode) {
                    lastP = p2;
                    polygon.addPoint(lastP);
                    polygonRasterizer.rasterize(polygon);
                }
                panel.repaint();

            }
        });
        //Zachytávání události pro pohyb myši
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                raster.clear();
                if (typeOfMode == 3){
                    //Vykreslení elipsy
                    int a = e.getX();
                    int b = e.getY();
                    Line line = new Line(p4, a, p4.y,0xffffff);
                    lineRasterizerGraphics.rasterize(line);
                    Line line2 = new Line(p4, p4.x, b,0xffffff);
                    lineRasterizerGraphics.rasterize(line2);

                }
                if (!clickMode) {
                    p2 = new Point(e.getX(), e.getY());

                    if (lastP != null) {
                        Line line = new Line(lastP, p2, 0xffffff);
                        lineRasterizerGraphics.rasterize(line);
                    }
                    else {
                        Line line = new Line(p1, p2, 0xffffff);
                        lineRasterizerGraphics.rasterize(line);
                    }
                }
                panel.repaint();
            }
        });
        //Zachytávání události klávesnici
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    clearAllObjects();
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    clickMode = !clickMode;
                    if (!clickMode) {
                        clickModeText.setText("Drag mode Activated");
                    } else {
                        clickModeText.setText("Click mode Activated");
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    switchMode++;
                    switch (switchMode){
                        case 1:
                            switchModeText.setText("Normal mode");
                            typeOfMode = 1;
                            break;
                        case 2:
                            switchModeText.setText("Rectangle mode");
                            typeOfMode = 2;
                            break;
                        case 3:
                            switchModeText.setText("Ellipse mode");
                            typeOfMode = 3;
                            switchMode = 0;
                            break;
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {

                }
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    ScanLineFiller scanLineFiller= new ScanLineFiller(lineRasterizerGraphics, polygonRasterizer, polygon);
                    scanLineFiller.fill();
                    panel.repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {

                }
            }
        });
    }

    //Metoda pro vymazání všech objektù
    public void clearAllObjects() {
        clear(0x000000);
        polygon.clearPolygon();
        firstPoint = true;
        firstPointRectangle = true;
        lastP = null;
        lastP2 = null;
        p1 = null;
        p2 = null;
        p3 = null;
        p4 = null;
        panel.repaint();
    }

    public void clear(int color) {
        raster.setClearColor(color);
        raster.clear();
    }

    public void present(Graphics graphics) {
        raster.repaint(graphics);
    }

    public void start() {
        clear(0x000000);
        panel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Canvas(800, 600).start());
    }

}
