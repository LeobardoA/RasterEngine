package util;

import java.util.Random;
import java.util.function.Consumer;

public class MathHelper {

    public static final double SQRT_2 = Math.sqrt(2);
    private static final Random RANDOM = new Random();
    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
    private static final float[] SIN_TABLE = make(new float[65536], (e) -> {
        for (int i = 0; i < e.length; ++i) {
            e[i] = (float)Math.sin(i * Math.PI * 2.0D / 65536.0D);
        }

    });

    public static <T> T make(T object, Consumer<T> consumer) {
        consumer.accept(object);
        return object;
    }

    public static int smallestEncompassingPowerOfTwo(int value) {
        int i = value - 1;
        i = i | i >> 1;
        i = i | i >> 2;
        i = i | i >> 4;
        i = i | i >> 8;
        i = i | i >> 16;
        return i + 1;
    }

    private static boolean isPowerOfTwo(int value) {
        return value != 0 && (value & value - 1) == 0;
    }

    public static int log2DeBruijn(int value) {
        value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int) ((long) value * 125613361L >> 27) & 31];
    }

    public static int log2(int value) {
        return log2DeBruijn(value) - (isPowerOfTwo(value) ? 0 : 1);
    }

    public static float sin(float value) {
        return SIN_TABLE[(int) (value * 10430.378F) & '\uffff'];
    }

    public static double cos(float value) {
        return SIN_TABLE[(int) (value * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static double lerp(double pct, double start, double end) {
      return start + pct * (end - start);
   }
    
}
