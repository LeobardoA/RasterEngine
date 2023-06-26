package util;

/**
 *
 * @author avile
 */
public class Camera {

    public Matrix4x4 matProj;
    public Matrix4x4 matView;
    public Vect3D position;
    public Vect3D direction;
    public double fYaw = 0;
    public double fTheta = 0;

    public Camera(double fov, double screenWidth, double screenHeight, double nearPlane, double farPlane) {
        position = new Vect3D(0, 0, 0);
        direction = new Vect3D(0, 0, 1);
        matProj = Matrix4x4.matrixProjection(fov, (screenHeight / screenWidth), nearPlane, farPlane);
        this.updateViewMatrix();
    }

    public void updateViewMatrix() {
        Vect3D up = new Vect3D(0, 1, 0);
        Vect3D target = Vect3D.add(position, direction);

        matView = Matrix4x4.pointAt(position, target, up);
    }
}
