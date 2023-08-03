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
import java.util.Arrays;
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

    private Block[][][] blocks = new Block[5][5][1];
    private boolean requiresFaceUpdate = true;
    private Camera camera;
    double[] pDepthBuffer;
    double[] pDepthBufferZero;

    private int screenHeight;
    private int screenWidth;
    private Texture texture;

    public Chunk(Camera cam) {
        this.camera = cam;
        pDepthBuffer = cam.pDepthBuffer;
        pDepthBufferZero = cam.pDepthBuffer;
        for (int i = 0; i < pDepthBuffer.length; i++) {
            pDepthBufferZero[i] = 0.0;
        }

        this.screenWidth = cam.screenWidth;
        this.screenHeight = cam.screenHeight;
        String texturePath = "\\test.png";
        texture = new Texture(texturePath);
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
                triTransformed.setTa(tri.getTa());
                triTransformed.setTb(tri.getTb());
                triTransformed.setTc(tri.getTc());

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

                rasterTriangle(triProjected, texture, g);
                g.drawPolygon(xPoints, yPoints, 3);

                //pDepthBuffer = new double[pDepthBuffer.length];
                //Arrays.fill(pDepthBuffer, 0.0);
                //rasterTriangle(triProjected, texture, g);
                //TexturedTriangle(triProjected, texture, g);
                return;
            }
        });
    }

    private void rasterTriangle(Triangle tri, Texture texture, Graphics g) {
        int x1 = (int) tri.getA().x;
        int y1 = (int) tri.getA().y;
        double u1 = (int) tri.getTa().u;
        double v1 = (int) tri.getTa().v;

        int x2 = (int) tri.getB().x;
        int y2 = (int) tri.getB().y;
        double u2 = tri.getTb().u;
        double v2 = tri.getTb().v;

        int x3 = (int) tri.getC().x;
        int y3 = (int) tri.getC().y;
        double u3 = tri.getTc().u;
        double v3 = tri.getTc().v;

        int minX = Math.min(x1, Math.min(x2, x3));
        int minY = Math.min(y1, Math.min(y2, y3));
        int maxX = Math.max(x1, Math.max(x2, x3));
        int maxY = Math.max(y1, Math.max(y2, y3));

        int delta_w1_col;
        int delta_w2_col;
        int delta_w3_col;

        int delta_w1_row;
        int delta_w2_row;
        int delta_w3_row;

        int w1_row;
        int w2_row;
        int w3_row;

        float area = edgeCross(x1, y1, x2, y2, x3, y3);

        if (edgeCross(x1, y1, x2, y2, x3, y3) >= 0) {
            delta_w1_col = y2 - y3;
            delta_w2_col = y3 - y1;
            delta_w3_col = y1 - y2;
            
            delta_w1_row = x3 - x2;
            delta_w2_row = x1 - x3;
            delta_w3_row = x2 - x1;
            
            w1_row = edgeCross(x2, y2, x3, y3, minX, minY);
            w2_row = edgeCross(x3, y3, x1, y1, minX, minY);
            w3_row = edgeCross(x1, y1, x2, y2, minX, minY);
            
        } else {
            delta_w1_col = y2 - y1;
            delta_w2_col = y1 - y3;
            delta_w3_col = y3 - y2;
            
            delta_w1_row = x1 - x2;
            delta_w2_row = x3 - x1;
            delta_w3_row = x2 - x3;
            
            w1_row = edgeCross(x2, y2, x1, y1, minX, minY);
            w2_row = edgeCross(x1, y1, x3, y3, minX, minY);
            w3_row = edgeCross(x3, y3, x2, y2, minX, minY);
        }

        for (int py = minY; py < maxY; py++) {
            double w1 = w1_row;
            double w2 = w2_row;
            double w3 = w3_row;
            for (int px = minX; px < maxX; px++) {
                boolean isInside = w1 >= 0 && w2 >= 0 && w3 >= 0;

                if (isInside) {
                    g.setColor(Color.GREEN);
                    g.fillRect(px, py, 1, 1);
                }
                w1 += delta_w1_col;
                w2 += delta_w2_col;
                w3 += delta_w3_col;
            }
            w1_row += delta_w1_row;
            w2_row += delta_w2_row;
            w3_row += delta_w3_row;
        }

    }

    private boolean isInsideTriangle(Triangle tri, int px, int py) {
        int x1 = (int) tri.getA().x;
        int y1 = (int) tri.getA().y;
        int x2 = (int) tri.getB().x;
        int y2 = (int) tri.getB().y;
        int x3 = (int) tri.getC().x;
        int y3 = (int) tri.getC().y;

        int w0;
        int w1;
        int w2;

        double alpha;
        double beta;
        double gama;

        double area = edgeCross(x1, y1, x2, y2, x3, y3);

        if (edgeCross(x1, y1, x2, y2, x3, y3) < 0) {
            w0 = edgeCross(x1, y1, x3, y3, px, py);
            w1 = edgeCross(x3, y3, x2, y2, px, py);
            w2 = edgeCross(x2, y2, x1, y1, px, py);
        } else {
            w0 = edgeCross(x1, y1, x2, y2, px, py);
            w1 = edgeCross(x2, y2, x3, y3, px, py);
            w2 = edgeCross(x3, y3, x1, y1, px, py);
        }

        alpha = w0 / area;
        beta = w1 / area;
        gama = w2 / area;

        if (w0 >= 0 && w1 >= 0 && w2 >= 0) {
            return true;
        }

        return false;
    }

    int edgeCross(int originX, int originY, int destinyX, int destinyY, int px, int py) {
        int vectX = destinyX - originX;
        int vectY = destinyY - originY;
        int pointX = px - originX;
        int pointY = py - originY;

        return vectX * pointY - vectY * pointX; // if is positive, point is to the right of vector
    }

    void TexturedTriangle(Triangle tri, Texture texture, Graphics g) {
        int x1 = (int) tri.getA().x;
        int y1 = (int) tri.getA().y;
        double w1 = (int) tri.getA().w;
        double u1 = (int) tri.getTa().u;
        double v1 = (int) tri.getTa().v;

        int x2 = (int) tri.getB().x;
        int y2 = (int) tri.getB().y;
        double w2 = tri.getB().w;
        double u2 = tri.getTb().u;
        double v2 = tri.getTb().v;

        int x3 = (int) tri.getC().x;
        int y3 = (int) tri.getC().y;
        double w3 = tri.getC().w;
        double u3 = tri.getTc().u;
        double v3 = tri.getTc().v;

        if (y2 < y1) {
            int auxi;
            double auxd;
            //swap(y1, y2);
            auxi = y1;
            y1 = y2;
            y2 = auxi;
            //swap(x1, x2);
            auxi = x1;
            x1 = x2;
            x2 = auxi;
            //swap(u1, u2);
            auxd = u1;
            u1 = u2;
            u2 = auxd;
            //swap(v1, v2);
            auxd = v1;
            v1 = v2;
            v2 = auxd;
            //swap(w1, w2);
            auxd = w1;
            w1 = w2;
            w2 = auxd;
        }

        if (y3 < y1) {
            int auxi;
            double auxd;
            //swap(y1, y3);
            auxi = y1;
            y1 = y3;
            y3 = auxi;
            //swap(x1, x3);
            auxi = x1;
            x1 = x3;
            x3 = auxi;
            //swap(u1, u3);
            auxd = u1;
            u1 = u3;
            u3 = auxd;
            //swap(v1, v3);
            auxd = v1;
            v1 = v3;
            v3 = auxd;
            //swap(w1, w3);
            auxd = w1;
            w1 = w3;
            w3 = auxd;
        }

        if (y3 < y2) {
            int auxi;
            double auxd;
            //swap(y2, y3);
            auxi = y2;
            y2 = y3;
            y3 = auxi;
            //swap(x2, x3);
            auxi = x2;
            x2 = x3;
            x3 = auxi;
            //swap(u2, u3);
            auxd = u2;
            u2 = u3;
            u3 = auxd;
            //swap(v2, v3);
            auxd = v2;
            v2 = v3;
            v3 = auxd;
            //swap(w2, w3);
            auxd = w2;
            w2 = w3;
            w3 = auxd;
        }

        int dy1 = y2 - y1;
        int dx1 = x2 - x1;
        double dv1 = v2 - v1;
        double du1 = u2 - u1;
        double dw1 = w2 - w1;

        int dy2 = y3 - y1;
        int dx2 = x3 - x1;
        double dv2 = v3 - v1;
        double du2 = u3 - u1;
        double dw2 = w3 - w1;

        double tex_u, tex_v, tex_w;

        double dax_step = 0, dbx_step = 0,
                du1_step = 0, dv1_step = 0,
                du2_step = 0, dv2_step = 0,
                dw1_step = 0, dw2_step = 0;

        if (dy1 != 0) {
            dax_step = dx1 / (float) Math.abs(dy1);
        }
        if (dy2 != 0) {
            dbx_step = dx2 / (float) Math.abs(dy2);
        }

        if (dy1 != 0) {
            du1_step = du1 / (float) Math.abs(dy1);
        }
        if (dy1 != 0) {
            dv1_step = dv1 / (float) Math.abs(dy1);
        }
        if (dy1 != 0) {
            dw1_step = dw1 / (float) Math.abs(dy1);
        }

        if (dy2 != 0) {
            du2_step = du2 / (float) Math.abs(dy2);
        }
        if (dy2 != 0) {
            dv2_step = dv2 / (float) Math.abs(dy2);
        }
        if (dy2 != 0) {
            dw2_step = dw2 / (float) Math.abs(dy2);
        }

        if (dy1 != 0) {
            for (int i = y1; i <= y2; i++) {
                int ax = (int) (x1 + (double) (i - y1) * dax_step);
                int bx = (int) (x1 + (double) (i - y1) * dbx_step);

                double tex_su = u1 + (double) (i - y1) * du1_step;
                double tex_sv = v1 + (double) (i - y1) * dv1_step;
                double tex_sw = w1 + (double) (i - y1) * dw1_step;

                double tex_eu = u1 + (double) (i - y1) * du2_step;
                double tex_ev = v1 + (double) (i - y1) * dv2_step;
                double tex_ew = w1 + (double) (i - y1) * dw2_step;

                if (ax > bx) {
                    int auxi;
                    double auxd;
                    //swap(ax, bx);
                    auxi = ax;
                    ax = bx;
                    bx = auxi;
                    //swap(tex_su, tex_eu);
                    auxd = tex_su;
                    tex_su = tex_eu;
                    tex_eu = auxd;
                    //swap(tex_sv, tex_ev);
                    auxd = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = auxd;
                    //swap(tex_sw, tex_ew);
                    auxd = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = auxd;
                }

                tex_u = tex_su;
                tex_v = tex_sv;
                tex_w = tex_sw;

                double tstep = 1.0f / ((float) (bx - ax));
                double t = 0.0f;

                for (int j = ax; j < bx; j++) {
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;
                    if (tex_w > pDepthBuffer[i * screenWidth + j]) {
                        if (u1 != 1) {
                            System.out.println(tex_u);
                        }
                        g.setColor(new Color(texture.getRGBPercent((int) (tex_u / tex_w), (int) (tex_v / tex_w))));
                        //Draw(j, i, tex -> SampleGlyph(tex_u / tex_w, tex_v / tex_w), tex -> SampleColour(tex_u / tex_w, tex_v / tex_w));
                        g.fillRect(j, i, 1, 1);
                        //pDepthBuffer[i * screenWidth + j] = tex_w;
                    }
                    t += tstep;
                }

            }
        }

        dy1 = y3 - y2;
        dx1 = x3 - x2;
        dv1 = v3 - v2;
        du1 = u3 - u2;
        dw1 = w3 - w2;

        if (dy1 != 0) {
            dax_step = dx1 / (float) Math.abs(dy1);
        }
        if (dy2 != 0) {
            dbx_step = dx2 / (float) Math.abs(dy2);
        }

        du1_step = 0;
        dv1_step = 0;
        if (dy1 != 0) {
            du1_step = du1 / (float) Math.abs(dy1);
        }
        if (dy1 != 0) {
            dv1_step = dv1 / (float) Math.abs(dy1);
        }
        if (dy1 != 0) {
            dw1_step = dw1 / (float) Math.abs(dy1);
        }

        if (dy1 != 0) {
            for (int i = y2; i <= y3; i++) {
                int ax = (int) (x2 + (double) (i - y2) * dax_step);
                int bx = (int) (x1 + (double) (i - y1) * dbx_step);

                double tex_su = u2 + (double) (i - y2) * du1_step;
                double tex_sv = v2 + (double) (i - y2) * dv1_step;
                double tex_sw = w2 + (double) (i - y2) * dw1_step;

                double tex_eu = u1 + (double) (i - y1) * du2_step;
                double tex_ev = v1 + (double) (i - y1) * dv2_step;
                double tex_ew = w1 + (double) (i - y1) * dw2_step;

                if (ax > bx) {
                    int auxi;
                    double auxd;
                    //swap(ax, bx);
                    auxi = ax;
                    ax = bx;
                    bx = auxi;
                    //swap(tex_su, tex_eu);
                    auxd = tex_su;
                    tex_su = tex_eu;
                    tex_eu = auxd;
                    //swap(tex_sv, tex_ev);
                    auxd = tex_sv;
                    tex_sv = tex_ev;
                    tex_ev = auxd;
                    //swap(tex_sw, tex_ew);
                    auxd = tex_sw;
                    tex_sw = tex_ew;
                    tex_ew = auxd;
                }

                tex_u = tex_su;
                tex_v = tex_sv;
                tex_w = tex_sw;

                float tstep = 1.0f / ((float) (bx - ax));
                float t = 0.0f;

                for (int j = ax; j < bx; j++) {
                    tex_u = (1.0f - t) * tex_su + t * tex_eu;
                    tex_v = (1.0f - t) * tex_sv + t * tex_ev;
                    tex_w = (1.0f - t) * tex_sw + t * tex_ew;

                    if (tex_w > pDepthBuffer[i * screenWidth + j]) {
                        g.setColor(new Color(texture.getRGBPercent((int) (tex_u / tex_w), (int) (tex_v / tex_w))));
                        g.fillRect(j, i, 1, 1);

                        //Draw(j, i, tex -> SampleGlyph(tex_u / tex_w, tex_v / tex_w), tex -> SampleColour(tex_u / tex_w, tex_v / tex_w));
                        pDepthBuffer[i * screenWidth + j] = tex_w;
                    }
                    t += tstep;
                }
            }
        }
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
