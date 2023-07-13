package util;

import java.util.Arrays;

/**
 *
 * @author avile
 */
public class Matrix4x4 {

    public double[][] m;

    public Matrix4x4() {
        m = new double[4][4];
        for (double[] row : m) {
            Arrays.fill(row, 0.0);
        }
    }

    public static Vect3D multiplyVector(Matrix4x4 m, Vect3D i) {
        Vect3D v = new Vect3D();

        v.setX(i.getX() * m.m[0][0] + i.getY() * m.m[1][0] + i.getZ() * m.m[2][0] + i.getW() * m.m[3][0]);
        v.setY(i.getX() * m.m[0][1] + i.getY() * m.m[1][1] + i.getZ() * m.m[2][1] + i.getW() * m.m[3][1]);
        v.setZ(i.getX() * m.m[0][2] + i.getY() * m.m[1][2] + i.getZ() * m.m[2][2] + i.getW() * m.m[3][2]);
        v.setW(i.getX() * m.m[0][3] + i.getY() * m.m[1][3] + i.getZ() * m.m[2][3] + i.getW() * m.m[3][3]);

        return v;
    }

    public static Matrix4x4 matrixIdentity() {
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = 1;
        matrix.m[1][1] = 1;
        matrix.m[2][2] = 1;
        matrix.m[3][3] = 1;
        return matrix;
    }

    public static Matrix4x4 rotationX(double fAngleRad) {
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = 1;
        matrix.m[1][1] = Math.cos(fAngleRad);
        matrix.m[1][2] = Math.sin(fAngleRad);
        matrix.m[2][1] = -Math.sin(fAngleRad);
        matrix.m[2][2] = Math.cos(fAngleRad);
        matrix.m[3][3] = 1;
        return matrix;
    }

    public static Matrix4x4 rotationY(double fAngleRad) {
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = Math.cos(fAngleRad);
        matrix.m[0][2] = Math.sin(fAngleRad);
        matrix.m[2][0] = -Math.sin(fAngleRad);
        matrix.m[1][1] = 1;
        matrix.m[2][2] = Math.cos(fAngleRad);
        matrix.m[3][3] = 1;
        return matrix;
    }

    public static Matrix4x4 rotationZ(double fAngleRad) {
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = Math.cos(fAngleRad);
        matrix.m[0][1] = Math.sin(fAngleRad);
        matrix.m[1][0] = -Math.sin(fAngleRad);
        matrix.m[1][1] = Math.cos(fAngleRad);
        matrix.m[2][2] = 1;
        matrix.m[3][3] = 1;
        return matrix;
    }

    public static Matrix4x4 translate(double x, double y, double z) {
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = 1;
        matrix.m[1][1] = 1;
        matrix.m[2][2] = 1;
        matrix.m[3][3] = 1;
        matrix.m[3][0] = x;
        matrix.m[3][1] = y;
        matrix.m[3][2] = z;
        return matrix;
    }

    public static Matrix4x4 matrixProjection(double fFovDegrees, double fAspectRatio, double fNear, double fFar) {
        double fFovRad = 1 / Math.tan(fFovDegrees * 0.5 * Math.PI / 180);
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = fAspectRatio * fFovRad;
        matrix.m[1][1] = fFovRad;
        matrix.m[2][2] = fFar / (fFar - fNear);
        matrix.m[3][2] = (-fFar * fNear) / (fFar - fNear);
        matrix.m[2][3] = 1;
        matrix.m[3][3] = 0;
        return matrix;
    }

//    public static Matrix4x4 orthographic(float width, float height, float nearPlane, float farPlane) {
//      Matrix4x4 matrix4f = new Matrix4x4();
//      matrix4f.set(0, 0, 2.0F / width);
//      matrix4f.set(1, 1, 2.0F / height);
//      float f = farPlane - nearPlane;
//      matrix4f.set(2, 2, -2.0F / f);
//      matrix4f.set(3, 3, 1.0F);
//      matrix4f.set(0, 3, -1.0F);
//      matrix4f.set(1, 3, -1.0F);
//      matrix4f.set(2, 3, -(farPlane + nearPlane) / f);
//      return matrix4f;
//   }
    
    public static Matrix4x4 multiplyMatrix(Matrix4x4 m1, Matrix4x4 m2) {
        Matrix4x4 matrix = new Matrix4x4();
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 4; r++) {
                matrix.m[r][c] = m1.m[r][0] * m2.m[0][c] + m1.m[r][1] * m2.m[1][c] + m1.m[r][2] * m2.m[2][c] + m1.m[r][3] * m2.m[3][c];
            }
        }
        return matrix;
    }

    public static Matrix4x4 pointAt(Vect3D pos, Vect3D target, Vect3D up) {
        Vect3D newForward = Vect3D.sub(target, pos);
        newForward = Vect3D.normalize(newForward);

        Vect3D a = Vect3D.multiply(newForward, Vect3D.dotProduct(up, newForward));
        Vect3D newUp = Vect3D.sub(up, a);
        newUp = Vect3D.normalize(newUp);

        Vect3D newRight = Vect3D.crossProduct(newUp, newForward);

        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = newRight.getX();
        matrix.m[0][1] = newRight.getY();
        matrix.m[0][2] = newRight.getZ();
        matrix.m[0][3] = 0.0f;
        matrix.m[1][0] = newUp.getX();
        matrix.m[1][1] = newUp.getY();
        matrix.m[1][2] = newUp.getZ();
        matrix.m[1][3] = 0.0f;
        matrix.m[2][0] = newForward.getX();
        matrix.m[2][1] = newForward.getY();
        matrix.m[2][2] = newForward.getZ();
        matrix.m[2][3] = 0.0f;
        matrix.m[3][0] = pos.getX();
        matrix.m[3][1] = pos.getY();
        matrix.m[3][2] = pos.getZ();
        matrix.m[3][3] = 1.0f;
        return matrix;

    }

    public static Matrix4x4 quickInverse(Matrix4x4 m) {
        Matrix4x4 matrix = new Matrix4x4();
        matrix.m[0][0] = m.m[0][0];
        matrix.m[0][1] = m.m[1][0];
        matrix.m[0][2] = m.m[2][0];
        matrix.m[0][3] = 0;
        matrix.m[1][0] = m.m[0][1];
        matrix.m[1][1] = m.m[1][1];
        matrix.m[1][2] = m.m[2][1];
        matrix.m[1][3] = 0;
        matrix.m[2][0] = m.m[0][2];
        matrix.m[2][1] = m.m[1][2];
        matrix.m[2][2] = m.m[2][2];
        matrix.m[2][3] = 0;
        matrix.m[3][0] = -(m.m[3][0] * matrix.m[0][0] + m.m[3][1] * matrix.m[1][0] + m.m[3][2] * matrix.m[2][0]);
        matrix.m[3][1] = -(m.m[3][0] * matrix.m[0][1] + m.m[3][1] * matrix.m[1][1] + m.m[3][2] * matrix.m[2][1]);
        matrix.m[3][2] = -(m.m[3][0] * matrix.m[0][2] + m.m[3][1] * matrix.m[1][2] + m.m[3][2] * matrix.m[2][2]);
        matrix.m[3][3] = 1;
        return matrix;
    }

}
