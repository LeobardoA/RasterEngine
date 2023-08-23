package renderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import objects.Block;
import rasterengine.Raster;
import renderUtil.Mesh;
import static renderutil.DisplayRenderer.DrawTriangle;
import renderutil.Shader;
import util.Camera;
import util.Matrix4x4;
import util.Triangle;
import util.Vector2;
import util.Vector3;
import util.Vertex;

/**
 * Represents a pipeline that a mesh goes through to get rendered on the screen.
 *
 * This class provides a way to render a single Mesh on the screen. By making a
 * call to <code>DrawMesh</code> method you can draw a single mesh on the
 * screen. When a mesh gets rendered it goes through all the stages of pipeline
 * before it turns into triangles that can be drawn on the screen by the
 * {@link TriangleRasterizer}
 */
public final class Pipeline {

    /**
     * The RenderFlag enumeration represents different rendering modes for the
     * Pipeline It specifies how the objects should be rendered.
     */
    public enum RenderFlag {
        Shaded, Wireframe, ShadedWireframe
    }

    public RenderFlag renderFlag = RenderFlag.ShadedWireframe;
    private Camera camera;
    private Matrix4x4 projectionMatrix;

    private double[] depthBuffer;
    private Vector2 frameSize;

    private TriangleRenderizer tr;

    public Pipeline() {

        projectionMatrix = new Matrix4x4();

        frameSize = Vector2.zero();
        frameSize.x = Raster.SCREEN_WIDTH;
        frameSize.y = Raster.SCREEN_HEIGHT;
        depthBuffer = new double[(int) (frameSize.x * frameSize.y)];

        tr = new TriangleRenderizer();
        tr.bindDepth(depthBuffer);
    }

    /**
     * Sets the camera for the Pipeline The camera determines the viewpoint and
     * perspective for rendering.
     *
     * @param camera The camera object to set. Must not be null.
     */
    public void setCamera(Camera camera) {
        if (camera != null) {
            this.camera = camera;
        }
    }

    /**
     * Draws a mesh on the screen using the provided render object. This method
     * is part of the Pipeline and handles the complete rendering process for an
     * object, including culling, projecting, and drawing its triangles.
     *
     * @param renderObject The render object containing the mesh, shader, and
     * other relevant data. It must not be null and should have valid mesh and
     * shader references.
     */
    public void DrawMesh(Block renderObject) {
        if (renderObject == null) {
            System.err.println("RenderObject is null");
            return;
        }
        if (camera == null) {
            System.err.println("Camera has not been set");
            return;
        }
        camera.frameSize = frameSize;

        Vector2 windowSize = new Vector2(frameSize.x / 1,
                frameSize.y / 1);
        double aspectRatio = windowSize.y / windowSize.x;

        Matrix4x4.PerspectiveProjection(camera.fov, aspectRatio, camera.near, camera.far, projectionMatrix);
        camera.projection = projectionMatrix;
        Matrix4x4 transformMatrix = renderObject.transform.getMatrixTRS();
        Matrix4x4 viewMatrix = camera.getViewMatrix();
        camera.view = viewMatrix;
        Matrix4x4 cameraObjectCombined = Matrix4x4.matrixMultiplyMatrix(transformMatrix, viewMatrix);

        renderObject.shader.objectTransform = renderObject.transform;
        renderObject.shader.cameraTransform = camera.transform;
        for (Triangle t : renderObject.tris) {
            if (t.isVisible) {
                Triangle fullyTransformed = new Triangle();
                Triangle transformed = new Triangle();

                for (int i = 0; i < 3; i++) {
                    transformed.verts[i].position = transformMatrix.MultiplyByVector(t.verts[i].position);
                    fullyTransformed.verts[i] = t.verts[i].clone();
                    fullyTransformed.verts[i].position = cameraObjectCombined.MultiplyByVector(t.verts[i].position);
                    fullyTransformed.verts[i].worldPos = transformed.verts[i].position;
                }

                Vector3 faceNormal = Mesh.surfaceNormalFromVertices(transformed.verts[0].position,
                        transformed.verts[1].position, transformed.verts[2].position);
                Vector3 dirToCamera = Vector3.subtract2Vecs(camera.transform.position, transformed.verts[0].position)
                        .normalized();

                if (Vector3.Dot(faceNormal, dirToCamera) < 0.0f && renderObject.shader.cull) {
                    continue;
                }

                Plane[] clippingPlanes = new Plane[]{new Plane(new Vector3(0, 0, camera.near), Vector3.forward()),
                    new Plane(new Vector3(0, 0, camera.far), Vector3.back()),};

                List<Triangle> finalResult = new ArrayList<>();
                finalResult.add(fullyTransformed);

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
                    for (int i = 0; i < 3; i++) {
                        clipped.verts[i].position = projectionMatrix.MultiplyByVector(clipped.verts[i].position);
                        clipped.verts[i].position.w = 1.0f / clipped.verts[i].position.w;
                        clipped.verts[i].position.multiplyBy(clipped.verts[i].position.w);
                        clipped.verts[i].texcoord = Vector2.mulVecFloat(clipped.verts[i].texcoord,
                                clipped.verts[i].position.w);
                        clipped.verts[i].worldPos = Vector3.mulVecFloat(clipped.verts[i].worldPos,
                                clipped.verts[i].position.w);

                        clipped.verts[i].position = camera.viewportToScreenPoint(clipped.verts[i].position);
                    }

                    DrawProjectedTriangle(clipped, renderObject.shader);
                }
            }
        }
    }

    private void DrawProjectedTriangle(Triangle projected, Shader shader) {
        if (renderFlag != RenderFlag.Wireframe) {
            tr.DrawTriangle(projected.verts, shader);
        }
        if (renderFlag != RenderFlag.Shaded) {
            DrawTriangle(new Vector2(projected.verts[0].position), new Vector2(projected.verts[1].position),
                    new Vector2(projected.verts[2].position), Color.gray);
        }
    }

    /**
     * Clear all screen buffers
     */
    public void clear() {
        tr.clearAll();
    }
}
