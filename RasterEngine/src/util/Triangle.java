package util;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * A class representing a 3D triangle.
 */
public class Triangle {

    private Vect3D a, b, c;
    private Vect2D tA, tB, tC;
    private Color edgeColor, fillColor;
    private BufferedImage texture;
    public boolean isVisible = true;

    public Triangle() {
        a = new Vect3D();
        b = new Vect3D();
        c = new Vect3D();
        tA = new Vect2D(0, 0);
        tB = new Vect2D(0, 0);
        tC = new Vect2D(0, 0);
        edgeColor = Color.BLACK;
        fillColor = Color.WHITE;
    }

    /**
     * Constructs a Triangle3D object with the specified vertices.
     *
     * @param vertex1 the first vertex of the triangle
     * @param vertex2 the second vertex of the triangle
     * @param vertex3 the third vertex of the triangle
     */
    public Triangle(Vect3D vertex1, Vect3D vertex2, Vect3D vertex3) {
        a = vertex1;
        b = vertex2;
        c = vertex3;
        fillColor = Color.WHITE;
        edgeColor = Color.BLACK;
    }

    public Triangle(Vect3D vertex1, Vect3D vertex2, Vect3D vertex3, Vect2D ta, Vect2D tb, Vect2D tc) {
        a = vertex1;
        b = vertex2;
        c = vertex3;
        tA = ta;
        tB = tb;
        tC = tc;
        fillColor = Color.WHITE;
        edgeColor = Color.BLACK;
    }

    /**
     * Returns the first vertex of the triangle.
     *
     * @return the first vertex
     */
    public Vect3D getA() {
        return a;
    }

    /**
     * Sets the first vertex of the triangle.
     *
     * @param a the first vertex to set
     */
    public void setA(Vect3D a) {
        this.a = a;
    }

    /**
     * Returns the second vertex of the triangle.
     *
     * @return the second vertex
     */
    public Vect3D getB() {
        return b;
    }

    /**
     * Sets the second vertex of the triangle.
     *
     * @param b the second vertex to set
     */
    public void setB(Vect3D b) {
        this.b = b;
    }

    /**
     * Returns the third vertex of the triangle.
     *
     * @return the third vertex
     */
    public Vect3D getC() {
        return c;
    }

    /**
     * Sets the third vertex of the triangle.
     *
     * @param c the third vertex to set
     */
    public void setC(Vect3D c) {
        this.c = c;
    }

    public Vect2D gettA() {
        return tA;
    }

    public void settA(Vect2D tA) {
        this.tA = tA;
    }

    public Vect2D gettB() {
        return tB;
    }

    public void settB(Vect2D tB) {
        this.tB = tB;
    }

    public Vect2D gettC() {
        return tC;
    }

    public void settC(Vect2D tC) {
        this.tC = tC;
    }

    public void set(Triangle v) {
        this.a = v.a;
        this.b = v.b;
        this.c = v.c;
        this.fillColor = v.fillColor;
        this.edgeColor = v.edgeColor;
    }

    public Color getEdgeColor() {
        return edgeColor;
    }

    public void setEdgeColor(Color edgeColor) {
        this.edgeColor = edgeColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public String toString() {
        return "Point A: " + a.toString() + "\nPoint B: " + b.toString() + "\nPoint C: " + c.toString() + "\nColor: " + fillColor;
    }

}
