package renderutil;

import java.awt.Color;
import java.util.Arrays;
import util.MathHelper;
import util.Vector2;

/**
 * The DisplayRenderer class is designed for low-level pixel manipulation on the
 * screen. It provides static methods to manipulate pixels and draw shapes,
 * offering precise control at the pixel level. With this class, you can utilize
 * various algorithms to create custom graphics and implement your own effects.
 * This class serves as the foundational layer for graphics rendering.
 */
public final class DisplayRenderer {

    private static int[] pixelBuffer;
    private static int bufferWidth;
    private static int bufferHeight;

    private DisplayRenderer() {
    }

    public static void Initialize(int width, int height, int[] buffer) {
        bufferWidth = width;
        bufferHeight = height;
        pixelBuffer = buffer;
    }

    /**
     * Fills the entire pixel buffer with the specified color, effectively
     * clearing the screen.
     *
     * @param color The color to fill the pixel buffer with.
     */
    public static void clear(Color color) {
        Arrays.fill(pixelBuffer, color.getRGB());
    }

    public static int getBufferWidth() {
        return bufferWidth;
    }

    public static int getBufferHeight() {
        return bufferHeight;
    }

    /**
     * Sets the color of a pixel at the specified coordinates.
     *
     * @param x The x-coordinate of the pixel.
     * @param y The y-coordinate of the pixel.
     * @param color The color to set for the pixel.
     */
    public static void setPixel(int x, int y, Color color) {
        if (y >= bufferHeight || y < 0 || x >= bufferWidth || x < 0) {
            return;
        }
        pixelBuffer[x + y * bufferWidth] = color.getRGB();
    }

    /**
     * Sets the color of a pixel at the specified coordinates using an RGB
     * value.
     *
     * @param x The x-coordinate of the pixel.
     * @param y The y-coordinate of the pixel.
     * @param rgb The RGB value representing the color to set for the pixel.
     */
    public static void setPixel(int x, int y, int rgb) {
        if (y >= bufferHeight || y < 0 || x >= bufferWidth || x < 0) {
            return;
        }
        rgb = (int) MathHelper.clamp(rgb, 0, 0xffffff);
        pixelBuffer[x + y * bufferWidth] = rgb;
    }

    /**
     * Retrieves the color of the pixel at the specified coordinates.
     *
     * @param x The x-coordinate of the pixel.
     * @param y The y-coordinate of the pixel.
     * @return The color of the pixel at the specified coordinates.
     */
    public static Color GetPixel(int x, int y) {
        if (y >= bufferHeight || y < 0 || x >= bufferWidth || x < 0) {
            return Color.black;
        }
        return new Color(pixelBuffer[x + y * bufferWidth]);
    }

    /**
     * Fills a rectangle with the specified position, size, and color.
     *
     * @param pos The top-left position of the rectangle.
     * @param size The size of the rectangle.
     * @param color The color to fill the rectangle with.
     */
    public static void FillRect(Vector2 pos, Vector2 size, Color color) {
        for (int y = 0; y < size.y; y++) {
            for (int x = 0; x < size.x; x++) {

                setPixel((int) pos.x + x, (int) pos.y + y, color);
            }
        }
    }

    private static void DrawLineLow(Vector2 from, Vector2 to, Color color) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        int yi = 1;
        if (dy < 0) {
            yi = -1;
            dy = -dy;
        }
        double D = (2 * dy) - dx;
        double y = from.y;

        for (double x = from.x; x < to.x; x++) {
            setPixel((int) x, (int) y, color);
            if (D > 0) {
                y += yi;
                D += 2 * (dy - dx);
            } else {
                D += 2 * dy;
            }
        }

    }

    private static void DrawLineHigh(Vector2 from, Vector2 to, Color color) {
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        int xi = 1;
        if (dx < 0) {
            xi = -1;
            dx = -dx;
        }
        double D = (2 * dx) - dy;
        double x = from.x;

        for (double y = from.y; y < to.y; y++) {
            setPixel((int) x, (int) y, color);
            if (D > 0) {
                x += xi;
                D += 2 * (dx - dy);
            } else {
                D += 2 * dx;
            }
        }
    }

    /**
     * Draws a line between two points with the specified color.
     *
     * @param from The starting point of the line.
     * @param to The ending point of the line.
     * @param color The color of the line.
     */
    public static void DrawLine(Vector2 from, Vector2 to, Color color) {
        if (Math.abs(to.y - from.y) < Math.abs(to.x - from.x)) {
            if (from.x > to.x) {
                DrawLineLow(to, from, color);
            } else {
                DrawLineLow(from, to, color);
            }
        } else {
            if (from.y > to.y) {
                DrawLineHigh(to, from, color);
            } else {
                DrawLineHigh(from, to, color);
            }
        }
    }

    /**
     * Draws a triangle with the specified vertices and color.
     *
     * @param p1 The first vertex of the triangle.
     * @param p2 The second vertex of the triangle.
     * @param p3 The third vertex of the triangle.
     * @param color The color of the triangle.
     */
    public static void DrawTriangle(Vector2 p1, Vector2 p2, Vector2 p3, Color color) {
        DrawLine(p1, p2, color);
        DrawLine(p2, p3, color);
        DrawLine(p3, p1, color);
    }
}
