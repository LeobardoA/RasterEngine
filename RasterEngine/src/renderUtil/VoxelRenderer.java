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
public class VoxelRenderer extends Shape {

    public VoxelRenderer() {

        rotateX(0);
        rotateY(0);
        rotateZ(0);
        translate(0, 0, 0);
        transformations = new Matrix4x4();

        Vect3D[] verts = new Vect3D[8];
        verts[0] = new Vect3D(-1, 1, 1);
        verts[1] = new Vect3D(-1, -1, 1);
        verts[2] = new Vect3D(-1, 1, -1);
        verts[3] = new Vect3D(-1, -1, -1);
        verts[4] = new Vect3D(1, 1, 1);
        verts[5] = new Vect3D(1, -1, 1);
        verts[6] = new Vect3D(1, 1, -1);
        verts[7] = new Vect3D(1, -1, -1);

        this.tris.add(new Triangle(verts[4], verts[2], verts[0]));
        this.tris.add(new Triangle(verts[2], verts[7], verts[3]));
        this.tris.add(new Triangle(verts[6], verts[5], verts[7]));
        this.tris.add(new Triangle(verts[1], verts[7], verts[5]));
        this.tris.add(new Triangle(verts[0], verts[3], verts[1]));
        this.tris.add(new Triangle(verts[4], verts[1], verts[5]));
        this.tris.add(new Triangle(verts[4], verts[6], verts[2]));
        this.tris.add(new Triangle(verts[2], verts[6], verts[7]));
        this.tris.add(new Triangle(verts[6], verts[4], verts[5]));
        this.tris.add(new Triangle(verts[1], verts[3], verts[7]));
        this.tris.add(new Triangle(verts[0], verts[2], verts[3]));
        this.tris.add(new Triangle(verts[4], verts[0], verts[1]));
    }

    public ArrayList<Triangle> getTris() {
        return tris;
    }

}
