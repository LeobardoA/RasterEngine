package util;


public class MathHelper {

    public static final double Deg2Rad = 0.01745329251994329576923690768489;

    public static double inverseLerp(double a, double b, double t) {
        return (t - a) / (b - a);
    }

    public static double lerp(double a, double b, double t) {
        t = MathHelper.clamp01(t);
        return a + t * (b - a);
    }

    public static double clamp(double value, double min, double max) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        } else {
            return value;
        }
    }

    public static double clamp01(double value) {
        return clamp(value, 0, 1);
    }

}
