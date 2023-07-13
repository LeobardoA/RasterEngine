/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import objects.Block;
import objects.Blocks;
import util.Camera;
import util.Matrix4x4;
import util.Triangle;

/**
 *
 * @author avile
 */
public class World {

    private Block test;
    private Camera camera;

    public World() {
    }

    public World(Camera camera) {
        this.camera = camera;
        this.test = Blocks.AIR;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);

        test.getVoxelRenderer().getTris().forEach((tri) -> {
            Triangle triTransformed = new Triangle();

            triTransformed.setA(Matrix4x4.multiplyVector(test.getVoxelRenderer().transformations, tri.getA()));
            triTransformed.setB(Matrix4x4.multiplyVector(test.getVoxelRenderer().transformations, tri.getB()));
            triTransformed.setC(Matrix4x4.multiplyVector(test.getVoxelRenderer().transformations, tri.getC()));

            g.drawLine((int) triTransformed.getA().getX(), (int) triTransformed.getA().getY(), (int) triTransformed.getB().getX(), (int) triTransformed.getB().getY());
            g.drawLine((int) triTransformed.getB().getX(), (int) triTransformed.getB().getY(), (int) triTransformed.getC().getX(), (int) triTransformed.getC().getY());
            g.drawLine((int) triTransformed.getA().getX(), (int) triTransformed.getA().getY(), (int) triTransformed.getC().getX(), (int) triTransformed.getC().getY());
        });
    }

    public void tick(int tick) {
        test.getVoxelRenderer().scale(50);
        test.getVoxelRenderer().rotateX(tick);
        test.getVoxelRenderer().rotateY(tick);
        test.getVoxelRenderer().rotateZ(tick);
        test.getVoxelRenderer().translate(600, 300, 0);
        test.getVoxelRenderer().applyTransforms();
    }

}
