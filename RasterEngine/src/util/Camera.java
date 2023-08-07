package util;

import java.awt.AWTException; 
import java.awt.Robot;
import rasterengine.RasterEngine;

/**
 *
 * @author avile
 */
public final class Camera {

    public Matrix4x4 matProj;
    public Matrix4x4 matView;
    public Vect3D position;
    public Vect3D direction;
    public double fYaw = 0;
    public double fTheta = 0;
    public double[] pDepthBuffer;
    public int length;
    public int screenHeight;
    public int screenWidth;
    public int centerX;
    public int centerY;
    private Robot robot;
    private int lastMouseX, lastMouseY;
    private double sensitivity = 0.005;
    private boolean stopMove = false;

    public Camera(double fov, double screenWidth, double screenHeight, double nearPlane, double farPlane) {
        position = new Vect3D(0, 0, 0);
        direction = new Vect3D(0, 0, 1);
        matProj = Matrix4x4.matrixProjection(fov, (screenHeight / screenWidth), nearPlane, farPlane);
        this.screenWidth = (int) screenWidth;
        this.screenHeight = (int) screenHeight;
        length = (int) (screenHeight * screenWidth);
        pDepthBuffer = new double[length];
        updateViewMatrix();

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        centerX = this.screenWidth / 2;
        centerY = this.screenHeight / 2;
        robot.mouseMove(centerX, centerY);
    }

    public void updateViewMatrix() {
        Vect3D up = new Vect3D(0, 1, 0);
        Vect3D target = Vect3D.add(position, direction);
        matView = Matrix4x4.pointAt(position, target, up);
    }

    public void updateMouseDirection(int mouseX, int mouseY) {
        System.out.println();
        
        if(mouseX < 200 || mouseX >= this.screenWidth - 200 || mouseY < 200 || mouseY >= this.screenHeight - 200){
            int width = RasterEngine.ventana.getX() + RasterEngine.ventana.getWidth() / 2;
            int height = RasterEngine.ventana.getY() + RasterEngine.ventana.getHeight() / 2;
            robot.mouseMove( width, height);
            lastMouseX = screenWidth;
            lastMouseY = screenHeight;
            stopMove = true;
            return;
        }
        
        // Calcular el cambio en la posición del ratón desde la última actualización.
        int deltaX = mouseX - lastMouseX;
        int deltaY = mouseY - lastMouseY;
        
        if(stopMove){
            deltaX = 0;
            deltaY = 0;
            stopMove = false;
        }

        // Actualizar la dirección horizontal de la cámara en función del cambio en la posición horizontal del ratón.
        fYaw += deltaX * sensitivity;
        direction.x = Math.sin(Math.toRadians(fYaw));
        direction.z = Math.cos(Math.toRadians(fYaw));

        // Actualizar la dirección vertical de la cámara en función del cambio en la posición vertical del ratón.
        fTheta += deltaY * sensitivity;
        fTheta = Math.max(-90, Math.min(90, fTheta));
        double thetaRad = Math.toRadians(fTheta);
        direction.y = Math.tan(thetaRad);
        direction = Vect3D.normalize(direction);

        // Actualizar las últimas posiciones del ratón.
        lastMouseX = mouseX;
        lastMouseY = mouseY;

        updateViewMatrix();
    }

    @Override
    public String toString() {
        return "Camera{" + "matProj=" + matProj + ", matView=" + matView + ", position=" + position + ", direction=" + direction + ", fYaw=" + fYaw + ", fTheta=" + fTheta + '}';
    }

}
