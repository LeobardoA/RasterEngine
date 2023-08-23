package util;

import java.util.Objects;

/**
 * Represents a three-dimensional vector.
 */
public final class Vector3 {
	public double x = 0;
	public double y = 0;
	public double z = 0;
	public double w = 1;

	public static Vector3 zero() {
		return new Vector3(0, 0, 0);
	}

	public static Vector3 one() {
		return new Vector3(1, 1, 1);
	}

	public static Vector3 up() {
		return new Vector3(0, 1, 0);
	}

	public static Vector3 down() {
		return new Vector3(0, -1, 0);
	}

	public static Vector3 forward() {
		return new Vector3(0, 0, 1);
	}

	public static Vector3 back() {
		return new Vector3(0, 0, -1);
	}

	public static Vector3 right() {
		return new Vector3(1, 0, 0);
	}

	public static Vector3 left() {
		return new Vector3(-1, 0, 0);
	}

	@Override
	public boolean equals(Object obj) {

		if ((obj == null) || (obj.getClass() != this.getClass())) {
			return false;
		}

		final Vector3 other = (Vector3) obj;
		if (this.x == other.x && this.y == other.y && this.z == other.z) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3(Vector3 other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.w = other.w;
	}

	public Vector3(Vector2 other) {
		this.x = other.x;
		this.y = other.y;
	}

	public void addTo(Vector3 vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
	}

	public void addTo(double a) {
		x += a;
		y += a;
		z += a;
	}

	public void subtractFrom(Vector3 vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
	}

	public void subtractFrom(double a) {
		x -= a;
		y -= a;
		z -= a;
	}

	public void multiplyBy(double a) {
		x *= a;
		y *= a;
		z *= a;
	}

	public void divideBy(double a) {
		x /= a;
		y /= a;
		z /= a;
	}

	public static Vector3 mulVecFloat(Vector3 vec, double num) {
		return new Vector3(vec.x * num, vec.y * num, vec.z * num);
	}

	public static Vector3 divide2Vecs(Vector3 vec1, Vector3 vec2) {
		return new Vector3(vec1.x / vec2.x, vec1.y / vec2.y, vec1.z / vec2.z);
	}

	public static Vector3 subtract2Vecs(Vector3 vec1, Vector3 vec2) {
		return new Vector3(vec1.x - vec2.x, vec1.y - vec2.y, vec1.z - vec2.z);
	}

	public static Vector3 add2Vecs(Vector3 vec1, Vector3 vec2) {
		return new Vector3(vec1.x + vec2.x, vec1.y + vec2.y, vec1.z + vec2.z);
	}

	public static Vector3 mul2Vecs(Vector3 vec1, Vector3 vec2) {
		return new Vector3(vec1.x * vec2.x, vec1.y * vec2.y, vec1.z * vec2.z);
	}

	/**
	 * Calculates the magnitude (length) of the vector.
	 *
	 * @return The magnitude of the vector.
	 */
	public double magnitude() {
		return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
	}

	/**
	 * Calculates the cross product of two vectors.
	 *
	 * @param a The first vector.
	 * @param b The second vector.
	 * @return The cross product of the two vectors.
	 */
	public static Vector3 Cross(Vector3 a, Vector3 b) {
		return new Vector3(a.y * b.z - a.z * b.y, a.z * b.x - a.x * b.z, a.x * b.y - a.y * b.x);
	}

	/**
	 * Calculates the dot product of two vectors.
	 *
	 * @param a The first vector.
	 * @param b The second vector.
	 * @return The dot product of the two vectors.
	 */
	public static double Dot(Vector3 a, Vector3 b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	/**
	 * Normalizes this vector making it a unit vector.
	 */
	public void Normalize() {
		x /= magnitude();
		y /= magnitude();
		z /= magnitude();
	}

	/**
	 * Returns a normalized version of the vector. If the vector's magnitude is
	 * zero, returns a zero vector.
	 *
	 * @return The normalized vector.
	 */
	public Vector3 normalized() {
		double mag = magnitude();
		return mag == 0 ? Vector3.zero() : new Vector3(x / mag, y / mag, z / mag);
	}

	/**
	 * Calculates the distance between two vectors.
	 *
	 * @param from The starting vector.
	 * @param to   The target vector.
	 * @return The distance between the two vectors.
	 */
	public static double distance(Vector3 from, Vector3 to) {
		Vector3 v = Vector3.subtract2Vecs(to, from);
		return v.magnitude();
	}

	/**
	 * Performs a linear interpolation between two vectors.
	 *
	 * @param a The starting vector.
	 * @param b The target vector.
	 * @param t The interpolation parameter. Should be between 0 and 1.
	 * @return The interpolated vector.
	 */
	public static Vector3 Lerp(Vector3 a, Vector3 b, double t) {
		Vector3 v = Vector3.zero();
		v.x = MathHelper.lerp(a.x, b.x, t);
		v.y = MathHelper.lerp(a.y, b.y, t);
		v.z = MathHelper.lerp(a.z, b.z, t);
		v.w = MathHelper.lerp(a.w, b.w, t);
		return v;
	}

	/**
	 * Clamps each component of the vector to the range [0, 1].
	 *
	 * @param v The vector to clamp.
	 */
	public static void Clamp01(Vector3 v) {
		v.x = MathHelper.clamp01(v.x);
		v.y = MathHelper.clamp01(v.y);
		v.z = MathHelper.clamp01(v.z);
	}
}