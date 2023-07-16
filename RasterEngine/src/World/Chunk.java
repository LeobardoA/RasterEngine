/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import java.awt.Color;
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
import renderUtil.Shape;
import renderUtil.Texture;
import util.Camera;
import util.Matrix4x4;
import util.Triangle;
import util.Vect3D;

/**
 *
 * @author avile
 */
public class Chunk {

    private Block[][][] blocks = new Block[10][10][10];
    private boolean requiresFaceUpdate = true;
    private Camera camera;

    public Chunk(Camera cam) {
        this.camera = cam;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                for (int k = 0; k < blocks[0][0].length; k++) {
                    blocks[i][j][k] = new Block(Block.Properties.create(new Texture()));
                    blocks[i][j][k].createShape();
                    blocks[i][j][k].scale(2);
                    blocks[i][j][k].rotateX(0);
                    blocks[i][j][k].rotateY(0);
                    blocks[i][j][k].rotateZ(0);
                    blocks[i][j][k].translate(i * 4, j * 4 - 4, k * 4 + 50);
                    blocks[i][j][k].applyTransforms();
                }
            }
        }
    }

    public void update(int tick) {
        if (Raster.keyHandler.rightPressed) {
            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks[0].length; j++) {
                    for (int k = 0; k < blocks[0][0].length; k++) {
                        blocks[i][j][k].translate(blocks[i][j][k].position.m[3][0] - 0.5, blocks[i][j][k].position.m[3][1], blocks[i][j][k].position.m[3][2]);
                        blocks[i][j][k].applyTransforms();
                    }
                }
            }
        }
        if (Raster.keyHandler.leftPressed) {
            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks[0].length; j++) {
                    for (int k = 0; k < blocks[0][0].length; k++) {
                        blocks[i][j][k].translate(blocks[i][j][k].position.m[3][0] + 0.5, blocks[i][j][k].position.m[3][1], blocks[i][j][k].position.m[3][2]);
                        blocks[i][j][k].applyTransforms();
                    }
                }
            }
        }
        if (Raster.keyHandler.upPressed) {
            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks[0].length; j++) {
                    for (int k = 0; k < blocks[0][0].length; k++) {
                        blocks[i][j][k].translate(blocks[i][j][k].position.m[3][0], blocks[i][j][k].position.m[3][1] + 0.5, blocks[i][j][k].position.m[3][2]);
                        blocks[i][j][k].applyTransforms();
                    }
                }
            }
        }
        if (Raster.keyHandler.downPressed) {
            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks[0].length; j++) {
                    for (int k = 0; k < blocks[0][0].length; k++) {
                        blocks[i][j][k].translate(blocks[i][j][k].position.m[3][0], blocks[i][j][k].position.m[3][1] - 0.5, blocks[i][j][k].position.m[3][2]);
                        blocks[i][j][k].applyTransforms();
                    }
                }
            }
        }
    }

    public void draw(Graphics g) {
        if (requiresFaceUpdate) {
            checkFaces();
        } else {
            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks[0].length; j++) {
                    for (int k = 0; k < blocks[0][0].length; k++) {
                        renderObj(g, blocks[i][j][k]);
                    }
                }
            }
        }
    }

    private void renderObj(Graphics g, Shape shape) {
        shape.tris.forEach((tri) -> {
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

                triTransformed.setA(Matrix4x4.multiplyVector(shape.transformations, tri.getA()));
                triTransformed.setB(Matrix4x4.multiplyVector(shape.transformations, tri.getB()));
                triTransformed.setC(Matrix4x4.multiplyVector(shape.transformations, tri.getC()));

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

                g.setColor(Color.WHITE);
                g.drawPolygon(xPoints, yPoints, 3);
            }
        });
    }

    private void checkFaces() {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                for (int k = 0; k < blocks[0][0].length; k++) {
                    Block currentBlock = blocks[i][j][k];

                    // Verificar la existencia de bloques adyacentes
                    boolean upExists = (j < blocks[0].length - 1) && (blocks[i][j + 1][k] != null);
                    boolean downExists = (j > 0) && (blocks[i][j - 1][k] != null);
                    boolean northExists = (k > 0) && (blocks[i][j][k - 1] != null);
                    boolean southExists = (k < blocks[0][0].length - 1) && (blocks[i][j][k + 1] != null);
                    boolean leftExists = (i > 0) && (blocks[i - 1][j][k] != null);
                    boolean rightExists = (i < blocks.length - 1) && (blocks[i + 1][j][k] != null);

                    // Eliminar las caras no visibles
                    if (upExists) {
                        currentBlock.removeFaces("UP");
                    }
                    if (downExists) {
                        currentBlock.removeFaces("DOWN");
                    }
                    if (northExists) {
                        currentBlock.removeFaces("SOUTH");
                    }
                    if (southExists) {
                        currentBlock.removeFaces("NORTH");
                    }
                    if (leftExists) {
                        currentBlock.removeFaces("RIGHT");
                    }
                    if (rightExists) {
                        currentBlock.removeFaces("LEFT");
                    }
                }
            }
        }

        requiresFaceUpdate = false;
    }

}
