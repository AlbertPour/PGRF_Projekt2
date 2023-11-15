package model;

public class Edge {
     private int  x1, x2, y1, y2;

    public Edge(int x1, int y1, int x2, int y2)
    {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        orientate();
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }
    public boolean isHorizontal(){
       return y1 == y2;
    }

    public void orientate(){
        if(y1 > y2) {
            int tmp;
            tmp = x1;
            this.x1 = x2;
            this.x2 = tmp;

            tmp = y1;
            this.y1 = y2;
            this.y2 = tmp;
        }
    }

    public boolean hasIntersection(int y) {
        return (y1 <= y && y < y2);
    }
}
