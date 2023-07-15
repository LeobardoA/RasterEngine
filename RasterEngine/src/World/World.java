/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import objects.Block;
import objects.Blocks;
import rasterengine.Raster;
import util.Camera;
import util.Matrix4x4;
import util.Triangle;
import util.Vect3D;

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
        test.createShape();
        test.removeFaces("DOWN", "LEFT", "RIGHT");
    }

    public void draw(Graphics g) {

        test.tris.forEach((tri) -> {
            if (tri.isVisible) {
                Triangle triTransformed = new Triangle();
                Triangle triProjected = new Triangle();
                Triangle triViewed = new Triangle();
                Matrix4x4 matView;

                Vect3D vUp = new Vect3D(0, 1, 0);
                Vect3D vTarget = new Vect3D(0, 0, 1);

                Matrix4x4 matCameraRotY = Matrix4x4.rotationY(camera.fYaw);
                Matrix4x4 matCameraRotX = Matrix4x4.rotationX(camera.fTheta);
                Matrix4x4 matCameraRot = Matrix4x4.multiplyMatrix(matCameraRotY, matCameraRotX);
                camera.direction = Matrix4x4.multiplyVector(matCameraRot, vTarget);

                vTarget = Vect3D.add(camera.position, camera.direction);

                Matrix4x4 matCamera = Matrix4x4.pointAt(camera.position, vTarget, vUp);

                matView = Matrix4x4.quickInverse(matCamera);

                triTransformed.setA(Matrix4x4.multiplyVector(test.transformations, tri.getA()));
                triTransformed.setB(Matrix4x4.multiplyVector(test.transformations, tri.getB()));
                triTransformed.setC(Matrix4x4.multiplyVector(test.transformations, tri.getC()));

                triViewed.setA(Matrix4x4.multiplyVector(matView, triTransformed.getA()));
                triViewed.setB(Matrix4x4.multiplyVector(matView, triTransformed.getB()));
                triViewed.setC(Matrix4x4.multiplyVector(matView, triTransformed.getC()));
                triViewed.setFillColor(triTransformed.getFillColor());

                triProjected.setA(Matrix4x4.multiplyVector(camera.matProj, triViewed.getA()));
                triProjected.setB(Matrix4x4.multiplyVector(camera.matProj, triViewed.getB()));
                triProjected.setC(Matrix4x4.multiplyVector(camera.matProj, triViewed.getC()));

                triProjected.setA(Vect3D.divide(triProjected.getA(), triProjected.getA().getW()));
                triProjected.setB(Vect3D.divide(triProjected.getB(), triProjected.getB().getW()));
                triProjected.setC(Vect3D.divide(triProjected.getC(), triProjected.getC().getW()));

                triProjected.getA().x *= -1;
                triProjected.getB().x *= -1;
                triProjected.getC().x *= -1;
                triProjected.getA().y *= -1;
                triProjected.getB().y *= -1;
                triProjected.getC().y *= -1;

                Vect3D vOffsetView = new Vect3D(1, 1, 0);
                triProjected.setA(Vect3D.add(triProjected.getA(), vOffsetView));
                triProjected.setB(Vect3D.add(triProjected.getB(), vOffsetView));
                triProjected.setC(Vect3D.add(triProjected.getC(), vOffsetView));

                triProjected.getA().x *= 0.5 * Raster.SCREEN_WIDTH;
                triProjected.getA().y *= 0.5 * Raster.SCREEN_HEIGHT;
                triProjected.getB().x *= 0.5 * Raster.SCREEN_WIDTH;
                triProjected.getB().y *= 0.5 * Raster.SCREEN_HEIGHT;
                triProjected.getC().x *= 0.5 * Raster.SCREEN_WIDTH;
                triProjected.getC().y *= 0.5 * Raster.SCREEN_HEIGHT;

                Vect3D a = triProjected.getA();
                Vect3D b = triProjected.getB();
                Vect3D c = triProjected.getC();

                int[] xPoints = {(int) a.getX(), (int) b.getX(), (int) c.getX()};
                int[] yPoints = {(int) a.getY(), (int) b.getY(), (int) c.getY()};

//                g.setColor(Color.RED);
//                g.fillPolygon(xPoints, yPoints, 3);
                try {
                    File file = new File(getClass().getResource("test.png").getFile());
                    Image textura = ImageIO.read(file);
                    
                    TexturePaint texturaPaint = new TexturePaint((BufferedImage) textura, new Rectangle((int) a.getX(), (int) a.getY(), textura.getWidth(null), textura.getHeight(null)));

                    Graphics2D g2 = (Graphics2D) g;
                    g2.setPaint(texturaPaint);
                    g.fillPolygon(xPoints, yPoints, 3);
                } catch (IOException ex) {
                    Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
                }
//                g.setColor(Color.WHITE);
//                g.drawPolygon(xPoints, yPoints, 3);
//                g.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
//                g.drawLine((int) a.getX(), (int) a.getY(), (int) c.getX(), (int) c.getY());
//                g.drawLine((int) c.getX(), (int) c.getY(), (int) b.getX(), (int) b.getY());
            }
        });
    }

    public void tick(int tick) {

        test.scale(2);
        test.rotateX(tick);
        test.rotateY(0);
        test.rotateZ(0);
        test.translate(0, 0, 10);
        test.applyTransforms();
    }

}
