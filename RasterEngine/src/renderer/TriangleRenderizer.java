package renderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import rasterengine.Raster;
import static renderutil.DisplayRenderer.clear;
import static renderutil.DisplayRenderer.setPixel;
import renderutil.Shader;
import util.MathHelper;
import util.Triangle;
import util.Vector3;
import util.Vertex;

/**
 * The <code>TriangleRasterizer</code> class provides functionality for
 * rasterizing triangles and interpolating attributes across the triangle
 * surface. It is used for rendering triangles on a 2D screen or surface.
 */
public final class TriangleRenderizer {

    private static double[] depthBuffer;
    private int width, height;

    public TriangleRenderizer() {
        width = Raster.SCREEN_WIDTH;
        height = Raster.SCREEN_HEIGHT;
    }

    /**
     * Binds a depth buffer to the renderer, specifying the buffer to use for
     * storing depth information. The depth buffer should have the same size as
     * the screen.
     *
     * @param buffer The depth buffer to bind.
     * @throws IllegalArgumentException if the provided depth buffer size does
     * not match the screen size.
     */
    public void bindDepth(double[] buffer) {
        if (buffer.length != width * height) {
            throw new IllegalArgumentException("Depth buffer should be same size as screen");
        }
        depthBuffer = buffer;
    }

    /**
     * Clears all the buffers
     */
    public void clearAll() {
        clear(Color.black);
        Arrays.fill(depthBuffer, Float.MAX_VALUE);
    }

    private void writeDepth(int x, int y, Double value) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Invalid write index. Cannot write at " + x + ", " + y);
        }
        depthBuffer[x + y * width] = value;
    }

    private double readDepth(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IndexOutOfBoundsException("Invalid read index. Cannot read at " + x + ", " + y);
        }
        return depthBuffer[x + y * width];
    }

    /**
     * Draws a triangle using the specified vertices and shader.
     *
     * @param vertices The array of vertices that make up the triangle.
     * @param shader The shader to use for rendering the triangle.
     */
    public void DrawTriangle(Vertex[] vertices, Shader shader) {

        Plane[] clippingPlanes = new Plane[]{new Plane(Vector3.zero(), Vector3.up()), // top
            new Plane(Vector3.zero(), Vector3.right()), // left
            new Plane(new Vector3(width, 0, 0), Vector3.left()), // right
            new Plane(new Vector3(0, height, 0), Vector3.down()), // bottom
    };

        Triangle in = new Triangle(vertices[0], vertices[1], vertices[2]);
        List<Triangle> finalResult = new ArrayList<>();
        finalResult.add(in);

        for (Plane p : clippingPlanes) {
            int initialSize = finalResult.size();
            for (int i = 0; i < initialSize; i++) {
                Triangle[] clippedTris = Plane.triangleClipAgainstPlane(p.point, p.normal, finalResult.get(0));
                finalResult.remove(0);

                for (Triangle clipped : clippedTris) {

                    if (clipped == null) {
                        continue;
                    }
                    finalResult.add(clipped);
                }
            }
        }

        for (Triangle clipped : finalResult) {
            DrawTriangleClipped(clipped.verts, shader);
        }
    }

    private void DrawTriangleClipped(Vertex[] vertices, Shader shader) {
        Arrays.sort(vertices, Comparator.comparingDouble((v) -> v.position.y));
        Vertex v1 = vertices[0], v2 = vertices[1], v3 = vertices[2];

        if (v2.position.y == v3.position.y) {
            if (v2.position.x > v3.position.x) {
                flatBottom(v1, v3, v2, shader);
                return;
            }
            flatBottom(v1, v2, v3, shader);
        } else if (v1.position.y == v2.position.y) {
            if (v1.position.x > v2.position.x) {
                flatTop(v2, v1, v3, shader);
                return;
            }
            flatTop(v1, v2, v3, shader);
        } else {
            double t = MathHelper.inverseLerp(v1.position.y, v3.position.y, v2.position.y);
            Vertex v4 = Vertex.Lerp(v1, v3, t);

            if (v4.position.x > v2.position.x) {
                flatBottom(v1, v2, v4, shader);
                flatTop(v2, v4, v3, shader);
                return;
            }
            flatBottom(v1, v4, v2, shader);
            flatTop(v4, v2, v3, shader);
        }
    }

    private void flatBottom(Vertex v1, Vertex v2, Vertex v3, Shader shader) {
        flatTriangle(v1, v2, v1, v3, shader);
    }

    private void flatTop(Vertex v1, Vertex v2, Vertex v3, Shader shader) {
        flatTriangle(v1, v3, v2, v3, shader);
    }

    private void flatTriangle(Vertex leftSlope1, Vertex leftSlope2, Vertex rightSlope1, Vertex rightSlope2,
            Shader shader) {
        int yStart = (int) Math.ceil(leftSlope1.position.y - 0.5f);
        int yEnd = (int) Math.ceil(rightSlope2.position.y - 0.5f);

        double leftSlope = calculateSlope(leftSlope1.position, leftSlope2.position);
        double rightSlope = calculateSlope(rightSlope1.position, rightSlope2.position);

        for (int y = yStart; y < yEnd; y++) {
            double px1 = (leftSlope * (y + 0.5f - leftSlope1.position.y)) + leftSlope1.position.x;
            double px2 = (rightSlope * (y + 0.5f - rightSlope1.position.y)) + rightSlope1.position.x;

            int xStart = (int) Math.ceil(px1 - 0.5f);
            int xEnd = (int) Math.ceil(px2 - 0.5f);

            double si = InverseLerp(leftSlope1.position, leftSlope2.position, new Vector3(xStart, y, 0));
            double ei = InverseLerp(rightSlope1.position, rightSlope2.position, new Vector3(xEnd, y, 0));
            Vertex startVertex = Vertex.Lerp(leftSlope1, leftSlope2, si);
            Vertex endVertex = Vertex.Lerp(rightSlope1, rightSlope2, ei);

            float mag = xEnd - xStart;
            Vertex delta = Vertex.delta(startVertex, endVertex, mag);

            Vertex in = new Vertex();
            for (int x = xStart; x < xEnd; x++) {
                in.cloneVertex(startVertex);

                double w = in.position.w;
                w = 1.0f / w;

                boolean depthTestPassed = true;
                if (depthBuffer != null) {
                    depthTestPassed = w < readDepth(x, y);
                }

                if (depthTestPassed) {
                    in.texcoord.multiplyBy(w);
                    in.worldPos.multiplyBy(w);
                    int finalCol = toColor(shader.Fragment(in));
                    setPixel(x, y, finalCol);

                    if (depthBuffer != null) {
                        writeDepth(x, y, w);
                    }
                }

                Vertex.add(startVertex, delta, startVertex);
            }
        }
    }

    private int toColor(Vector3 v) {
        v.x = MathHelper.clamp01(v.x);
        v.y = MathHelper.clamp01(v.y);
        v.z = MathHelper.clamp01(v.z);

        int r = (int) (v.x * 255);
        int g = (int) (v.y * 255);
        int b = (int) (v.z * 255);
        int rgb = (r << 16) | (g << 8) | (b << 0);
        return rgb;
    }

    private static double InverseLerp(Vector3 a, Vector3 b, Vector3 value) {
        Vector3 AB = Vector3.subtract2Vecs(b, a);
        Vector3 AV = Vector3.subtract2Vecs(value, a);
        return Vector3.Dot(AV, AB) / Vector3.Dot(AB, AB);
    }

    private double calculateSlope(Vector3 v1, Vector3 v2) {
        return (v2.x - v1.x) / (v2.y - v1.y);
    }

}
