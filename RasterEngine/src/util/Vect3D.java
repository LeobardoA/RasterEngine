package util;

/**
 *
 * @author avile
 */
public class Vect3D {

    public double x, y, z, w;

    public Vect3D() {
        x = 0;
        y = 0;
        z = 0;
        w = 1;
    }

    public Vect3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public static Vect3D add(Vect3D v1, Vect3D v2) {
        return new Vect3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public static Vect3D sub(Vect3D v1, Vect3D v2) {
        return new Vect3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public static Vect3D multiply(Vect3D v1, double k) {
        return new Vect3D(v1.x * k, v1.y * k, v1.z * k);
    }

    public static Vect3D divide(Vect3D v1, double k) {
        return new Vect3D(v1.x / k, v1.y / k, v1.z / k);
    }

    public static Vect3D normalize(Vect3D v) {
        double l = lenght(v);
        return new Vect3D(v.x / l, v.y / l, v.z / l);
    }

    public static Vect3D crossProduct(Vect3D v1, Vect3D v2) {
        Vect3D v = new Vect3D();
        v.x = v1.y * v2.z - v1.z * v2.y;
        v.y = v1.z * v2.x - v1.x * v2.z;
        v.z = v1.x * v2.y - v1.y * v2.x;
        return v;
    }

    public static Vect3D intersectionPlane(Vect3D plane_p, Vect3D plane_n, Vect3D lineStart, Vect3D lineEnd) {
        plane_n = normalize(plane_n);
        double plane_d = -dotProduct(plane_n, plane_p);
        double ad = dotProduct(lineStart, plane_n);
        double bd = dotProduct(lineEnd, plane_n);
        double t = (-plane_d - ad) / (bd - ad);
        Vect3D lineStartToEnd = sub(lineEnd, lineStart);
        Vect3D lineToIntersect = multiply(lineStartToEnd, t);
        return add(lineStart, lineToIntersect);
    }

    public static double dotProduct(Vect3D v1, Vect3D v2) {
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static double lenght(Vect3D v) {
        return Math.sqrt(dotProduct(v, v));
    }

    public static double calculateDistance(Vect3D planeNormal, Vect3D pointInPlane, Vect3D point) {
        // Calcula el vector que conecta el punto en el plano al punto dado
        Vect3D vectorToPoint = Vect3D.sub(point, pointInPlane);

        // Calcula la distancia proyectando el vector sobre el vector normal del plano
        double distance = dotProduct(vectorToPoint, normalize(planeNormal));

        return distance;
    }

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y + " Z: " + z;
    }
}
