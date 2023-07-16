/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package renderUtil;

import java.util.ArrayList;
import util.Matrix4x4;
import util.Triangle;
import util.Vect3D;

/**
 *
 * @author avile
 */
public class Shape {

    public Matrix4x4 transformations = new Matrix4x4();
    private Matrix4x4 rotationX, rotationY, rotationZ;
    public Matrix4x4 position;
    public double originalScale;

    public ArrayList<Triangle> tris = new ArrayList<>();

    public Shape() {
        this.rotateX(0);
        this.rotateY(0);
        this.rotateZ(0);
        this.translate(0, 0, 0);
        transformations = new Matrix4x4();
    }

    public void rotateX(double angle) {
        double rad = Math.toRadians(angle);
        rotationX = Matrix4x4.rotationX(rad);
    }

    public void rotateY(double angle) {
        double rad = Math.toRadians(angle);
        rotationY = Matrix4x4.rotationY(rad);
    }

    public void rotateZ(double angle) {
        double rad = Math.toRadians(angle);
        rotationZ = Matrix4x4.rotationZ(rad);
    }

    public void translate(double x, double y, double z) {
        position = Matrix4x4.translate(x, y, z);
    }

    public void translate(Vect3D v) {
        position = Matrix4x4.translate(v.getX(), v.getY(), v.getZ());
    }

    public void scale(double scale) {
        if (scale != originalScale) {
            originalScale = scale;
            for (Triangle tri : tris) {
                tri.setA(Vect3D.multiply(tri.getA(), scale));
                tri.setB(Vect3D.multiply(tri.getB(), scale));
                tri.setC(Vect3D.multiply(tri.getC(), scale));
            }
        }
    }

    public void applyTransforms() {
        transformations = Matrix4x4.multiplyMatrix(rotationZ, rotationX);
        transformations = Matrix4x4.multiplyMatrix(rotationY, transformations);
        transformations = Matrix4x4.multiplyMatrix(transformations, position);
    }

}
