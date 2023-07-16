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

    public static int clipping(Vect3D plane_p, Vect3D plane_n, Triangle triangle, Triangle clipped1, Triangle clipped2) {

//        System.out.println(triangle);
        double d0 = calculateDistance(plane_n, plane_p, triangle.getA());
        double d1 = calculateDistance(plane_n, plane_p, triangle.getB());
        double d2 = calculateDistance(plane_n, plane_p, triangle.getC());

//        System.out.println("D0: " + d0 + "D1:" + d1 + " D2:" + d2);
        Vect3D[] insidePoints = new Vect3D[3];
        int nInsidePoints = 0;
        Vect3D[] outsidePoints = new Vect3D[3];
        int nOutsidePoints = 0;

        if (d0 >= 0) {
            insidePoints[nInsidePoints++] = triangle.getA();
        } else {
            outsidePoints[nOutsidePoints++] = triangle.getA();
        }
        if (d1 >= 0) {
            insidePoints[nInsidePoints++] = triangle.getB();
        } else {
            outsidePoints[nOutsidePoints++] = triangle.getB();
        }
        if (d2 >= 0) {
            insidePoints[nInsidePoints++] = triangle.getC();
        } else {
            outsidePoints[nOutsidePoints++] = triangle.getC();
        }

        if (nInsidePoints == 0) {
            return 0;
        }
        if (nInsidePoints == 3) {
            clipped1.set(triangle);
            return 1;
        }
        if (nInsidePoints == 1 && nOutsidePoints == 2) {

            clipped1.setFillColor(triangle.getFillColor());
            clipped1.setA(insidePoints[0]);
            clipped1.setB(intersectionPlane(plane_p, plane_n, insidePoints[0], outsidePoints[0]));
            clipped1.setC(intersectionPlane(plane_p, plane_n, insidePoints[0], outsidePoints[1]));

//            clipped1.setFillColor(Color.GREEN);
            return 1;
        }
        if (nInsidePoints == 2 && nOutsidePoints == 1) {
            clipped1.setFillColor(triangle.getFillColor());
            clipped2.setFillColor(triangle.getFillColor());

            clipped1.setA(insidePoints[0]);
            clipped1.setB(insidePoints[1]);
            clipped1.setC(intersectionPlane(plane_p, plane_n, insidePoints[0], outsidePoints[0]));

            clipped2.setA(insidePoints[1]);
            clipped2.setB(intersectionPlane(plane_p, plane_n, insidePoints[1], outsidePoints[0]));
            clipped2.setC(clipped1.getC());

//            clipped1.setFillColor(Color.RED);
//            clipped2.setFillColor(Color.BLUE);
            return 2;
        }
        return 6;
    }

    public static double calculateDistance(Vect3D planeNormal, Vect3D pointInPlane, Vect3D point) {
        // Calcula el vector que conecta el punto en el plano al punto dado
        Vect3D vectorToPoint = Vect3D.sub(point, pointInPlane);

        // Calcula la distancia proyectando el vector sobre el vector normal del plano
        double distance = dotProduct(vectorToPoint, normalize(planeNormal));

        return distance;
    }

    public static Vect3D interpolate(double tiempoInicial, double tiempoFinal, Vect3D valorInicial, Vect3D valorDestino, double tick) {
        double t = (tick - tiempoInicial) / (tiempoFinal - tiempoInicial);

        double interpolatedX = interpolateValue(valorInicial.getX(), valorDestino.getX(), t);
        double interpolatedY = interpolateValue(valorInicial.getY(), valorDestino.getY(), t);
        double interpolatedZ = interpolateValue(valorInicial.getZ(), valorDestino.getZ(), t);

        return new Vect3D(interpolatedX, interpolatedY, interpolatedZ);
    }

    private static double interpolateValue(double startValue, double endValue, double t) {
        return startValue + (endValue - startValue) * t;
    }

    @Override
    public String toString() {
        return "X: " + x + " Y: " + y + " Z: " + z;
    }
}
