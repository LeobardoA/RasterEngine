/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.awt.Color;
import java.awt.Polygon;

/**
 * A class representing a 3D triangle.
 */
public class Triangle {

    Polygon p = new Polygon();

    private Vect3D a, b, c;
    private Color edgeColor, fillColor;

    public Triangle() {
        a = new Vect3D();
        b = new Vect3D();
        c = new Vect3D();
        edgeColor = Color.BLACK;
        fillColor = Color.WHITE;
    }

    /**
     * Constructs a Triangle3D object with the specified vertices.
     *
     * @param vertex1 the first vertex of the triangle
     * @param vertex2 the second vertex of the triangle
     * @param vertex3 the third vertex of the triangle
     * @param fill Color fill of each triangle
     */
    public Triangle(Vect3D vertex1, Vect3D vertex2, Vect3D vertex3, Color fill) {
        a = vertex1;
        b = vertex2;
        c = vertex3;
        fillColor = fill;
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
