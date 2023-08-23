package renderutil;

import java.awt.Color;
import util.Vector3;
import util.Vertex;

/**
 * The Shader class represents a shader used for rendering objects in a 3D
 * scene. It provides an abstract base class that can be extended to implement
 * specific shader functionality. Shaders are responsible for determining the
 * appearance of objects by defining the color of each fragment or in other
 * words pixels that make up this object.
 */
public class Shader {

    public Transform objectTransform = null;
    public Transform cameraTransform = null;
    public boolean cull = true;
    public Texture texture = Texture.solidTexture(Color.white);
    private Vector3 tint = Vector3.one();
    public boolean fastSample = true;
    public Vector3 magenta = new Vector3(1, 0, 1);

    public Shader() {
    }

    public void setTint(Vector3 tint) {
        if (tint == null) {
            return;
        }
        this.tint = new Vector3(tint);
    }

    public Vector3 getTint() {
        return tint;
    }

    public void setTexture(Texture texture) {
        if (texture == null) {
            return;
        }
        this.texture = texture;
    }


    public Vector3 Fragment(Vertex in) {
        Vector3 col = null;
        if (fastSample) {
            col = texture.SampleFast(in.texcoord.x, in.texcoord.y);
        } else {
            col = texture.Sample(in.texcoord.x, in.texcoord.y);
        }
        col = Vector3.mul2Vecs(col, tint);
        return col;
    }

    /**
     * Passes object data of this Shader to another shader. Use this if you
     * wanna use ouput of another shader and modify it and if the other shader
     * uses lighting or ({@link #objectTransform}, {@link #cameraTransform}).
     * Call this function before getting the output of another shader. Example:
     *
     * <pre>
     * //inside Fragment function of your shader:
     * passObjectData(base);
     * Vector3 baseCol = base.Fragment(in);
     * </pre>
     *
     * @param other
     */
    protected void passObjectData(Shader other) {
        if (other == null) {
            return;
        }
        if (other.objectTransform == null) {
            other.objectTransform = this.objectTransform;
        }
        if (other.cameraTransform == null) {
            other.cameraTransform = this.cameraTransform;
        }
    }

}
