/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package World;

import static rasterengine.Raster.SCREEN_HEIGHT;
import static rasterengine.Raster.SCREEN_WIDTH;
import util.Camera;
import util.Vect3D;

/**
 *
 * @author avile
 */
public class Player {

    public static final Camera mainCamera = new Camera(75, SCREEN_WIDTH, SCREEN_HEIGHT, 0.1, 1000);

    public void moveZ(double distance) {
        mainCamera.position = Vect3D.add(mainCamera.position, Vect3D.multiply(mainCamera.direction, distance));
        mainCamera.updateViewMatrix();
    }

    public void moveX(double distance) {
        Vect3D right = Vect3D.normalize(Vect3D.crossProduct(mainCamera.direction, new Vect3D(0, 1, 0)));
        mainCamera.position = Vect3D.add(mainCamera.position, Vect3D.multiply(right, distance));
        mainCamera.updateViewMatrix();
    }

    public void moveY(double distance) {
        Vect3D up = new Vect3D(0, 1, 0);
        mainCamera.position = Vect3D.add(mainCamera.position, Vect3D.multiply(up, distance));
        mainCamera.updateViewMatrix();
    }

    public void rotateY(double angleDegrees) {
        mainCamera.fYaw += angleDegrees;
        // Asegurarse de mantener el ángulo de rotación entre 0 y 360 grados.
        mainCamera.fYaw %= 360;

        // Actualizar la dirección de la cámara según el nuevo ángulo.
        mainCamera.direction.x = Math.sin(Math.toRadians(mainCamera.fYaw));
        mainCamera.direction.z = Math.cos(Math.toRadians(mainCamera.fYaw));
        mainCamera.updateViewMatrix();
    }

    public void rotateX(double angleDegrees) {
        mainCamera.fTheta += angleDegrees;
        // Limitar el ángulo de rotación entre -90 y 90 grados para evitar volteos excesivos.
        mainCamera.fTheta = Math.max(-90, Math.min(90, mainCamera.fTheta));

        // Calcular la nueva dirección vertical de la cámara.
        double thetaRad = Math.toRadians(mainCamera.fTheta);
        mainCamera.direction.y = Math.tan(thetaRad);
        mainCamera.direction = Vect3D.normalize(mainCamera.direction);
        mainCamera.updateViewMatrix();
    }

}
