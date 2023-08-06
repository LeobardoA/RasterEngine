package util;

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
    private int lastMouseX, lastMouseY;
    private double sensitivity = 0.001;

    public Camera(double fov, double screenWidth, double screenHeight, double nearPlane, double farPlane) {
        position = new Vect3D(0, 0, 0);
        direction = new Vect3D(0, 0, 1);
        matProj = Matrix4x4.matrixProjection(fov, (screenHeight / screenWidth), nearPlane, farPlane);
        this.screenWidth = (int) screenWidth;
        this.screenHeight = (int) screenHeight;
        length = (int) (screenHeight * screenWidth);
        pDepthBuffer = new double[length];
        updateViewMatrix();
    }

    public void updateViewMatrix() {
        Vect3D up = new Vect3D(0, 1, 0);
        Vect3D target = Vect3D.add(position, direction);
        matView = Matrix4x4.pointAt(position, target, up);
    }

    public void updateMouseDirection(int mouseX, int mouseY) {
        // Calcular el cambio en la posición del ratón desde la última actualización.
        int deltaX = mouseX - lastMouseX;
        int deltaY = mouseY - lastMouseY;

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
