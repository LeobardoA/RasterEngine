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
    public double[] pDepthBuffer;
    public int length;
    public int screenHeight;
    public int screenWidth;

    public Camera(double fov, double screenWidth, double screenHeight, double nearPlane, double farPlane) {
        position = new Vect3D(0, 0, 0);
        direction = new Vect3D(0, 0, 1);
        matProj = Matrix4x4.matrixProjection(fov, (screenHeight / screenWidth), nearPlane, farPlane);
        this.screenWidth = (int) screenWidth;
        this.screenHeight = (int) screenHeight;
        length = (int) (screenHeight * screenWidth);
        pDepthBuffer = new double[length];
        this.updateViewMatrix();
    }

    public void updateViewMatrix() {
        Vect3D up = new Vect3D(0, 1, 0);
        Vect3D target = Vect3D.add(position, direction);
        matView = Matrix4x4.pointAt(position, target, up);
    }

    @Override
    public String toString() {
        return "Camera{" + "matProj=" + matProj + ", matView=" + matView + ", position=" + position + ", direction=" + direction + ", fYaw=" + fYaw + ", fTheta=" + fTheta + '}';
    }
    
    
}
