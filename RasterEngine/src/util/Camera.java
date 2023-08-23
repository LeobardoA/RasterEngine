package util;

import renderutil.Transform;

/**
 * The <code>Camera</code> class enables the engine to "see" objects in the 3D
 * world. It provides functionality for positioning and orienting the camera,
 * allowing the rendering of scenes from different perspectives.
 */
public class Camera {

    public final Transform transform = new Transform();
    public double fov = 75;
    public double near = .1f;
    public double far = 1000;
    public Matrix4x4 projection;
    public Matrix4x4 view;
    public Vector2 frameSize;

    /**
     * Sets the field of view (FOV) of the camera.
     *
     * @param fov The desired field of view in degrees.
     */
    public void SetFov(double fov) {
        fov = MathHelper.clamp(fov, 0.01f, 179);
        this.fov = fov;
    }

    public double GetFov() {
        return fov;
    }

    /**
     * Sets the distance to the near clipping plane of the camera.
     *
     * @param near The distance to the near clipping plane.
     */
    public void SetNear(double near) {
        near = MathHelper.clamp(near, 0.01f, far);
        this.near = near;
    }

    public double GetNear() {
        return near;
    }

    /**
     * Sets the distance to the far clipping plane of the camera.
     *
     * @param far The distance to the far clipping plane.
     */
    public void SetFar(double far) {
        far = far < near ? near + 0.01f : far;
        this.far = far;
    }

    public double GetFar() {
        return far;
    }

    /**
     * Retrieves the view matrix of the camera.
     *
     * @return The view matrix representing the camera's orientation and
     * position in the scene.
     */
    public Matrix4x4 getViewMatrix() {
        transform.getMatrixTRS();

        Vector3 targetForward = Vector3.add2Vecs(transform.position, transform.forward);
        Matrix4x4 cameraMatrix = Matrix4x4.MatrixPointAt(transform.position, targetForward, transform.up);
        Matrix4x4 viewMatrix = Matrix4x4.inverseMatrix(cameraMatrix);

        return viewMatrix;
    }

    /**
     * Transforms a point from world space to screen space coordinates.
     *
     * @param point The point in world space to transform.
     * @return The point's coordinates in screen space.
     */
    public Vector3 worldToScreenPoint(Vector3 point) {
        return viewportToScreenPoint(worldToViewportPoint(point));
    }

    /**
     * Transforms a point from world coordinates to viewport coordinates.
     *
     * @param point The point in world coordinates to transform.
     * @return The point's coordinates in viewport space.
     */
    public Vector3 worldToViewportPoint(Vector3 point) {
        if (view == null || projection == null) {
            return Vector3.zero();
        }
        Vector3 newPoint = view.MultiplyByVector(point);
        newPoint = projection.MultiplyByVector(newPoint);
        newPoint.divideBy(newPoint.w);
        return newPoint;
    }

    /**
     * Transforms a point from viewport coordinates to screen coordinates.
     *
     * @param point The point in viewport coordinates to transform.
     * @return The point's coordinates in screen space.
     */
    public Vector3 viewportToScreenPoint(Vector3 point) {
        if (frameSize == null) {
            return Vector3.zero();
        }
        double x = (point.x + 1) / 2.0f * frameSize.x;
        double y = (-point.y + 1) / 2.0f * frameSize.y;

        Vector3 v = new Vector3(x, y, point.z);
        v.w = point.w;
        return v;
    }
}
