/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import renderUtil.Texture;

/**
 *
 * @author avile
 */
public class Quad {

    private Vect3D a, b, c, d;
    private Texture texture;
    private boolean isVisible = true;

    public Quad() {
        this.a = new Vect3D(0, 0, 0);
        this.b = new Vect3D(0, 0, 0);
        this.c = new Vect3D(0, 0, 0);
        this.d = new Vect3D(0, 0, 0);
        texture = new Texture();
    }

    public Quad(Vect3D a, Vect3D b, Vect3D c, Vect3D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        texture = new Texture();
    }

    public Vect3D getA() {
        return a;
    }

    public void setA(Vect3D a) {
        this.a = a;
    }

    public Vect3D getB() {
        return b;
    }

    public void setB(Vect3D b) {
        this.b = b;
    }

    public Vect3D getC() {
        return c;
    }

    public void setC(Vect3D c) {
        this.c = c;
    }

    public Vect3D getD() {
        return d;
    }

    public void setD(Vect3D d) {
        this.d = d;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public String toString() {
        return "------Quad------\n{Point A: " + a.toString() + "\nPoint B: " + b.toString() + "\nPoint C: " + c.toString() + "\nPoint D: " + d.toString() + "\n}";
    }

}
