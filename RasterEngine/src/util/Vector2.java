package util;

import java.util.Objects;

/**
 * Represents a two-dimensional vector.
 */
public final class Vector2 {
	public double x = 0;
	public double y = 0;
	public double z = 1;

	public static Vector2 zero() {
		return new Vector2(0, 0);
	}

	public static Vector2 one() {
		return new Vector2(1, 1);
	}

	public static Vector2 up() {
		return new Vector2(0, 1);
	}

	public static Vector2 down() {
		return new Vector2(0, -1);
	}

	public static Vector2 right() {
		return new Vector2(1, 0);
	}

	public static Vector2 left() {
		return new Vector2(-1, 0);
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj == null) || (obj.getClass() != this.getClass())) {
			return false;
		}

		final Vector2 other = (Vector2) obj;
		if (this.x == other.x && this.y == other.y) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "(" + x + ", " + y + ")";
	}

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 other) {
		this.x = other.x;
		this.y = other.y;
	}

	public Vector2(Vector3 other) {
		this.x = other.x;
		this.y = other.y;
	}

	public void addTo(double a) {
		x += a;
		y += a;
	}

	public void addTo(Vector2 v) {
		x += v.x;
		y += v.y;
	}

	public void subtractFrom(double a) {
		x -= a;
		y -= a;
	}

	public void subtractFrom(Vector2 v) {
		x -= v.x;
		y -= v.y;
	}

	public void multiplyBy(double a) {
		x *= a;
		y *= a;
	}

	public void divideBy(double a) {
		x /= a;
		y /= a;
	}

	public static Vector2 mulVecFloat(Vector2 vec, double num) {
		return new Vector2(vec.x * num, vec.y * num);
	}

	public static Vector2 divide2Vecs(Vector2 a, Vector2 b) {
		return new Vector2(a.x / b.x, a.y / b.y);
	}

	public static Vector2 subtract2Vecs(Vector2 a, Vector2 b) {
		return new Vector2(a.x - b.x, a.y - b.y);
	}

	public static Vector2 add2Vecs(Vector2 a, Vector2 b) {
		return new Vector2(a.x + b.x, a.y + b.y);
	}

	public static Vector2 mul2Vecs(Vector2 vec1, Vector2 vec2) {
		return new Vector2(vec1.x * vec2.x, vec1.y * vec2.y);
	}

	/**
	 * Calculates the magnitude (length) of the vector.
	 *
	 * @return The magnitude of the vector.
	 */
	public double magnitude() {
		return Math.sqrt((x * x + y * y));
	}

	/**
	 * Calculates the dot product of two vectors.
	 *
	 * @param a The first vector.
	 * @param b The second vector.
	 * @return The dot product of the two vectors.
	 */
	public static double Dot(Vector2 a, Vector2 b) {
		return a.x * b.x + a.y * b.y;
	}

	/**
	 * Normalizes this vector making it a unit vector.
	 */
	public void Normalize() {
		x /= magnitude();
		y /= magnitude();
	}

	/**
	 * Returns a normalized version of the vector. If the vector's magnitude is
	 * zero, returns a zero vector.
	 *
	 * @return The normalized vector.
	 */
	public Vector2 normalized() {
		return new Vector2(x / magnitude(), y / magnitude());
	}

	/**
	 * Calculates the distance between two vectors.
	 *
	 * @param from The starting vector.
	 * @param to   The target vector.
	 * @return The distance between the two vectors.
	 */
	public static double distance(Vector2 from, Vector2 to) {
		Vector2 v = Vector2.subtract2Vecs(to, from);
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
	public static Vector2 Lerp(Vector2 a, Vector2 b, double t) {
		Vector2 v = Vector2.zero();
		v.x = MathHelper.lerp(a.x, b.x, t);
		v.y = MathHelper.lerp(a.y, b.y, t);
		return v;
	}

	/**
	 * Clamps each component of the vector to the range [0, 1].
	 *
	 * @param v The vector to clamp.
	 */
	public static void Clamp01(Vector2 v) {
		v.x = MathHelper.clamp01(v.x);
		v.y = MathHelper.clamp01(v.y);
	}
}