package util;

import renderUtil.Mesh;

public final class Triangle {

    public final Vertex[] verts;
    public boolean isVisible = true;

    public Triangle(Vertex a, Vertex b, Vertex c) {
        verts = new Vertex[3];
        verts[0] = a;
        verts[1] = b;
        verts[2] = c;
    }

    public Triangle() {
        verts = new Vertex[3];
        verts[0] = new Vertex();
        verts[1] = new Vertex();
        verts[2] = new Vertex();
    }

    public static Triangle[] CreateIndexedTriangleStream(Mesh mesh) {
        Triangle[] triangles = new Triangle[mesh.triangleCount() / 3];
        Vector3[] vertices = mesh.getVertices();
        Vector2[] uv = mesh.getUV();
        Vector3[] colors = mesh.getColors();
        Vector3[] normals = mesh.getNormals();
        int[] tris = mesh.getTriangles();

        for (int i = 0; i < triangles.length; i++) {
            Triangle t = new Triangle();
            t.verts[0].position = vertices[tris[0 + i * 3]];
            t.verts[1].position = vertices[tris[1 + i * 3]];
            t.verts[2].position = vertices[tris[2 + i * 3]];

            if (uv != null) {
                t.verts[0].texcoord = uv[tris[0 + i * 3]];
                t.verts[1].texcoord = uv[tris[1 + i * 3]];
                t.verts[2].texcoord = uv[tris[2 + i * 3]];
            }
            if (colors != null) {
                t.verts[0].color = colors[tris[0 + i * 3]];
                t.verts[1].color = colors[tris[1 + i * 3]];
                t.verts[2].color = colors[tris[2 + i * 3]];
            }
            if (normals != null) {
                t.verts[0].normal = normals[tris[0 + i * 3]];
                t.verts[1].normal = normals[tris[1 + i * 3]];
                t.verts[2].normal = normals[tris[2 + i * 3]];
            }

            triangles[i] = t;
        }

        return triangles;
    }
}
