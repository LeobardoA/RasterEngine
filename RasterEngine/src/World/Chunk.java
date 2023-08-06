package World;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import objects.Block;
import rasterengine.Raster;
import renderUtil.Texture;
import renderUtil.TextureLoader;
import util.Camera;
import util.Matrix4x4;
import util.Triangle;
import util.Vect3D;

/**
 *
 * @author avile
 */
public class Chunk {

    private Block[][][] blocks = new Block[5][5][5];
    private boolean requiresFaceUpdate = true;
    private Camera camera;

    private ArrayList<Block> zBuffer;

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
        
    }

    public void draw(Graphics g) {
        zBuffer = new ArrayList<>();
        Graphics2D g2 = (Graphics2D) g;
        if (requiresFaceUpdate) {
            checkFaces();
        } else {
            //RENDER BLOCKS
            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks[0].length; j++) {
                    for (int k = 0; k < blocks[0][0].length; k++) {
                        zBuffer.add(blocks[i][j][k]);
                    }
                }
            }
            renderAll(g2);
        }
    }

    private void renderAll(Graphics2D g2) {
        //ORDEN DE Z
        Collections.sort(zBuffer, (Block t1, Block t2) -> {
            double z1, z2;
            z1 = centerCube(t1);
            z2 = centerCube(t2);
            return Double.compare(z1, z2);
        });

        //RENDERIZADO
        zBuffer.forEach((t) -> {
            t.tris.forEach((tri) -> {
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

                    triTransformed.setA(Matrix4x4.multiplyVector(t.transformations, tri.getA()));
                    triTransformed.setB(Matrix4x4.multiplyVector(t.transformations, tri.getB()));
                    triTransformed.setC(Matrix4x4.multiplyVector(t.transformations, tri.getC()));

                    Vect3D normal, line1, line2;

                    line1 = Vect3D.sub(triTransformed.getB(), triTransformed.getA());
                    line2 = Vect3D.sub(triTransformed.getC(), triTransformed.getA());

                    normal = Vect3D.crossProduct(line1, line2);

                    normal = Vect3D.normalize(normal);

                    Vect3D vCameraRay = Vect3D.sub(triTransformed.getA(), camera.position);

                    if (Vect3D.dotProduct(normal, vCameraRay) < 0) {
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
                        TexturePaint tp = crearTexturePaint(TextureLoader.individualTextures[8], xPoints, yPoints);
                        Polygon triangulo = new Polygon(xPoints, yPoints, 3);
                        g2.setPaint(tp);
                        g2.fill(triangulo);
                    }
                }
            });
        });
    }

    TexturePaint crearTexturePaint(BufferedImage imagen, int[] xPoints, int[] yPoints) {
        // Calcular el tamaño del rectángulo que se ajuste al triángulo
        int minX = Math.min(xPoints[0], Math.min(xPoints[1], xPoints[2]));
        int minY = Math.min(yPoints[0], Math.min(yPoints[1], yPoints[2]));
        int maxX = Math.max(xPoints[0], Math.max(xPoints[1], xPoints[2]));
        int maxY = Math.max(yPoints[0], Math.max(yPoints[1], yPoints[2]));

        int rectWidth = maxX - minX;
        int rectHeight = maxY - minY;

        // Crear el Rectangle con las dimensiones calculadas
        Rectangle rect = new Rectangle(minX, minY, rectWidth, rectHeight);

        // Crear y devolver el TexturePaint con la imagen y el Rectangle
        return new TexturePaint(imagen, rect);
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

    private Double centerCube(Block block) {
        return camera.position.z - block.position.m[3][2];
    }

}
