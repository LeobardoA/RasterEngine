/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import java.awt.Graphics;
import objects.Block;
import rasterengine.Raster;
import renderUtil.Shape;
import renderUtil.Texture;
import util.Camera;
import util.Matrix4x4;
import util.Quad;
import util.Vect3D;

/**
 *
 * @author avile
 */
public class Chunk {

//    private Block[][][] blocks = new Block[1][1][1];
    private Block[][][] blocks = new Block[3][3][3];
//    private Block[][][] blocks = new Block[10][10][10];
    private boolean requiresFaceUpdate = true;
    private final Camera camera;

    public Chunk(Camera cam) {
        this.camera = cam;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[0].length; j++) {
                for (int k = 0; k < blocks[0][0].length; k++) {
                    blocks[i][j][k] = new Block(Block.Properties.create(new Texture()));
                    blocks[i][j][k].createShape();
                    blocks[i][j][k].scale(2);
                    blocks[i][j][k].translate(i * 4, j * 4 - 4, k * 4 + 20);
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
        shape.quads.forEach((quad) -> {
            if (quad.isVisible()) {
                Quad quadTransformed = new Quad();
                Quad quadProjected = new Quad();
                Quad quadViewed = new Quad();
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

                quadTransformed.setA(Matrix4x4.multiplyVector(shape.transformations, quad.getA()));
                quadTransformed.setB(Matrix4x4.multiplyVector(shape.transformations, quad.getB()));
                quadTransformed.setC(Matrix4x4.multiplyVector(shape.transformations, quad.getC()));
                quadTransformed.setD(Matrix4x4.multiplyVector(shape.transformations, quad.getD()));

                quadViewed.setA(Matrix4x4.multiplyVector(matView, quadTransformed.getA()));
                quadViewed.setB(Matrix4x4.multiplyVector(matView, quadTransformed.getB()));
                quadViewed.setC(Matrix4x4.multiplyVector(matView, quadTransformed.getC()));
                quadViewed.setD(Matrix4x4.multiplyVector(matView, quadTransformed.getD()));
//                quadViewed.setFillColor(quadTransformed.getTexture().getDefaultColor());

                quadProjected.setA(Matrix4x4.multiplyVector(camera.matProj, quadViewed.getA()));
                quadProjected.setB(Matrix4x4.multiplyVector(camera.matProj, quadViewed.getB()));
                quadProjected.setC(Matrix4x4.multiplyVector(camera.matProj, quadViewed.getC()));
                quadProjected.setD(Matrix4x4.multiplyVector(camera.matProj, quadViewed.getD()));

                quadProjected.setA(Vect3D.divide(quadProjected.getA(), quadProjected.getA().getW()));
                quadProjected.setB(Vect3D.divide(quadProjected.getB(), quadProjected.getB().getW()));
                quadProjected.setC(Vect3D.divide(quadProjected.getC(), quadProjected.getC().getW()));
                quadProjected.setD(Vect3D.divide(quadProjected.getD(), quadProjected.getD().getW()));

                quadProjected.getA().x *= -1;
                quadProjected.getB().x *= -1;
                quadProjected.getC().x *= -1;
                quadProjected.getD().x *= -1;

                quadProjected.getA().y *= -1;
                quadProjected.getB().y *= -1;
                quadProjected.getC().y *= -1;
                quadProjected.getD().y *= -1;

                Vect3D vOffsetView = new Vect3D(1, 1, 0);
                quadProjected.setA(Vect3D.add(quadProjected.getA(), vOffsetView));
                quadProjected.setB(Vect3D.add(quadProjected.getB(), vOffsetView));
                quadProjected.setC(Vect3D.add(quadProjected.getC(), vOffsetView));
                quadProjected.setD(Vect3D.add(quadProjected.getD(), vOffsetView));

                quadProjected.getA().x *= 0.5 * Raster.SCREEN_WIDTH;
                quadProjected.getA().y *= 0.5 * Raster.SCREEN_HEIGHT;
                quadProjected.getB().x *= 0.5 * Raster.SCREEN_WIDTH;
                quadProjected.getB().y *= 0.5 * Raster.SCREEN_HEIGHT;
                quadProjected.getC().x *= 0.5 * Raster.SCREEN_WIDTH;
                quadProjected.getC().y *= 0.5 * Raster.SCREEN_HEIGHT;
                quadProjected.getD().x *= 0.5 * Raster.SCREEN_WIDTH;
                quadProjected.getD().y *= 0.5 * Raster.SCREEN_HEIGHT;

                g.setColor(quadProjected.getTexture().getDefaultColor());
                
                g.drawLine((int) quadProjected.getA().x, (int) quadProjected.getA().y, (int) quadProjected.getB().x, (int) quadProjected.getB().y);
                g.drawLine((int) quadProjected.getA().x, (int) quadProjected.getA().y, (int) quadProjected.getC().x, (int) quadProjected.getC().y);
                g.drawLine((int) quadProjected.getD().x, (int) quadProjected.getD().y, (int) quadProjected.getB().x, (int) quadProjected.getB().y);
                g.drawLine((int) quadProjected.getD().x, (int) quadProjected.getD().y, (int) quadProjected.getC().x, (int) quadProjected.getC().y);
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
                        currentBlock.setFaces("UP", false);
                    }
                    if (downExists) {
                        currentBlock.setFaces("DOWN", false);
                    }
                    if (northExists) {
                        currentBlock.setFaces("SOUTH", false);
                    }
                    if (southExists) {
                        currentBlock.setFaces("NORTH", false);
                    }
                    if (leftExists) {
                        currentBlock.setFaces("RIGHT", false);
                    }
                    if (rightExists) {
                        currentBlock.setFaces("LEFT", false);
                    }
                }
            }
        }

        requiresFaceUpdate = false;
    }

}
