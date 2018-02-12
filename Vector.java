
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Arrays;

public class Vector {
	private double[] v;
	private int dimension;

	public enum CoordinateSystem {
		Cartesian, Polar, Spherical
	};

	//// CONSTRUCTORS ////

	public Vector(double x, double y) {
		this.dimension = 2;
		this.v = new double[] { x, y };
	}

	public Vector(double x, double y, double z) {
		this.dimension = 3;
		this.v = new double[] { x, y, z };
	}

	// allows for 2d cartesian/polar coordinates
	public Vector(CoordinateSystem type, double v1, double v2) {
		this.dimension = 2;
		double x, y;
		switch (type) {
		case Cartesian:
			x = v1;
			y = v2;
			break;
		case Polar:
			x = v1 * Math.cos(v2);
			y = v1 * Math.sin(v2);
			break;
		default:
			throw new IllegalArgumentException("Invalid coordinate system for this vector.");
		}
		this.v = new double[] { x, y };
	}

	// allows for 3d cartesian/spherical coordinates
	public Vector(CoordinateSystem type, double v1, double v2, double v3) {
		this.dimension = 3;
		double x, y, z;
		switch (type) {
		case Cartesian:
			x = v1;
			y = v2;
			z = v3;
			break;
		case Spherical:
			x = v1 * Math.cos(v2) * Math.sin(v3);
			y = v1 * Math.sin(v2) * Math.sin(v3);
			z = v1 * Math.cos(v3);
			break;
		default:
			throw new IllegalArgumentException("Invalid coordinate system for this vector.");
		}
		this.v = new double[] { x, y, z };
	}

	public Vector(double[] arr) {
		this.v = arr;
		this.dimension = this.v.length == 2 ? 2 : 3;
	}

	public Vector(int[] arr) {
		this.dimension = arr.length == 2 ? 2 : 3;
		for (int i = 0; i < this.dimension; i++)
			this.v[i] = arr[i];
	}

	public Vector(Point p) {
		this.v = new double[] { p.getX(), p.getY() };
		this.dimension = 2;
	}

	public Vector(Vector vector) {
		if (vector.dimension == 2) {
			this.v = new double[] { vector.x(), vector.y() };
			this.dimension = 2;
		} else {
			this.v = new double[] { vector.x(), vector.y(), vector.z() };
			this.dimension = 3;
		}
	}

	//// VECTOR OPERATIONS ////

	public Vector plus(Vector b) {
		double[] p = new double[this.dimension];
		for (int i = 0; i < this.dimension; i++)
			p[i] = this.v[i] + b.v[i];
		return new Vector(p);
	}

	public Vector plusScaledVector(Vector b, double scalar) {
		return this.plus(b.times(scalar));
	}

	public Vector minus(Vector b) {
		double[] m = new double[this.dimension];
		for (int i = 0; i < this.dimension; i++)
			m[i] = this.v[i] - b.v[i];
		return new Vector(m);
	}

	public Vector times(double a) {
		double[] t = new double[this.dimension];
		for (int i = 0; i < this.dimension; i++)
			t[i] = this.v[i] * a;
		return new Vector(t);
	}

	public Vector componentProduct(Vector v2) {
		double[] cp = new double[this.dimension];
		for (int i = 0; i < this.dimension; i++)
			cp[i] = this.v[i] * v2.v[i];
		return new Vector(cp);
	}

	public Vector divide(double a) {
		double[] d = new double[this.dimension];
		for (int i = 0; i < this.dimension; i++)
			d[i] = this.v[i] / a;
		return new Vector(d);
	}

	public Vector unit() {
		if (this.x() == 0 && this.y() == 0 && (this.dimension == 3 ? this.z() == 0 : true))
			return this;

		double mag = this.magnitude();
		return this.dimension == 2 ? new Vector(this.x() / mag, this.y() / mag)
				: new Vector(this.x() / mag, this.y() / mag, this.z() / mag);
	}

	public Vector setMagnitude(double magnitude) {
		return this.unit().times(magnitude);
	}

	// set the maximum value a vector can have to magnitude
	public Vector limit(double magnitude) {
		Vector limitedMagnitude = new Vector(this.v);
		if (this.magnitudeOptimized() > magnitude * magnitude)
			limitedMagnitude = limitedMagnitude.setMagnitude(magnitude);
		return limitedMagnitude;
	}

	//// SCALAR OPERATIONS ////

	public double dot(Vector b) {
		double sum = 0;
		for (int i = 0; i < this.dimension; i++)
			sum += this.v[i] * b.v[i];
		return sum;
	}

	public double magnitude() {
		return Math.sqrt(this.magnitudeOptimized());
	}

	public double magnitudeOptimized() {
		return this.dot(this);
	}

	public double distanceOptimized(Vector v2) {
		return this.minus(v2).magnitudeOptimized();
	}

	public double distance(Vector v2) {
		return this.minus(v2).magnitude();
	}


	//// 2D METHODS ////

	public double scalarProjectionOn2D(Vector on) {
		return this.dot(on.unit());
	}

	public Vector vectorProjectionOn(Vector on) {
		return on.times(this.dot(on) / on.dot(on));
	}

	// definition for dot product |A||B|cos(θ)=A.B -> θ = cos^-1(A.B/|A||B|)
	public double angleBetween2D(Vector v2) {
		return Math.acos(this.dot(v2) / (this.magnitude() * v2.magnitude()));
	}

	public double getAngle() {
		return Math.atan2(this.y(), this.x());
	}

	public void drawVector2D(Graphics g, Vector magnitude) {
		g.drawLine((int) this.x(), (int) this.y(), (int) (this.x() + magnitude.x()), (int) (this.y() + magnitude.y()));
		g.drawOval((int) (this.x() + magnitude.x()), (int) (this.y() + magnitude.y()), 2, 2);
	}

	public void drawPoint(Graphics g) {
		g.drawOval((int) this.x(), (int) this.y(), 1, 1);
	}

	public void drawPoint(Graphics g, Color col) {
		g.setColor(col);
		this.drawPoint(g);
	}

	//// 3D METHODS ////

	public Vector crossProduct(Vector v2) {
		double a = this.x(), b = this.y(), c = this.z(), d = v2.x(), e = v2.y(), f = v2.z();
		return new Vector(b * f - c * e, c * d - a * f, a * e - b * d);
	}

	public Vector angleProjection(double θ, double φ) {
		double x = this.x() * Math.cos(θ) - this.y() * Math.sin(θ);
		double y = (this.x() * Math.sin(θ) + this.y() * Math.cos(θ)) * Math.sin(φ) - this.z() * Math.cos(φ);
		return new Vector(x, y);
	}

	public void drawVectorAngle(Graphics2D g, Vector magnitude, double θ, double φ) {
		Vector tail = this.angleProjection(θ, φ);
		Vector tip = this.plus(magnitude).angleProjection(θ, φ);
		g.drawLine((int) tip.x(), (int) tip.y(), (int) tail.x(), (int) tail.y());
	}

	public void drawPointAngle(Graphics2D g, Vector magnitude, double θ, double φ, int width) {
		Vector tip = this.plus(magnitude).angleProjection(θ, φ);
		g.fillOval((int) tip.x(), (int) tip.y(), width, width);
	}

	//// GETTERS AND SETTERS ////

	public void clear() {
		Arrays.fill(this.v, 0);
	}

	public double x() {
		return this.v[0];
	}

	public double y() {
		return this.v[1];
	}

	public double z() {
		return this.v[2];
	}

	public void setX(double x) {
		this.v[0] = x;
	}

	public void setY(double y) {
		this.v[1] = y;
	}

	public void setZ(double z) {
		this.v[2] = z;
	}

	public void setVector(double x, double y) {
		this.setX(x);
		this.setY(y);
	}

	public void setVector(double x, double y, double z) {
		this.setVector(x, y);
		this.setZ(z);
	}

	public double[] getArray() {
		return this.dimension == 2 ? new double[] { this.x(), this.y() }
				: new double[] { this.x(), this.y(), this.z() };
	}

	public int getDimension() {
		return this.dimension;
	}

	@Override
	public String toString() {
		return Arrays.toString(this.v);
	}

	@Override
	public boolean equals(Object obj) {
		boolean equalsOther = true;
		Vector vectorb = (Vector) obj;
		double[] comp = vectorb.getArray();

		for (int i = 0; i < comp.length; i++) {
			if (this.v[i] != comp[i])
				equalsOther = false;
		}

		return equalsOther;
	}

	@Override
	public int hashCode() {
		int hash = ((Double) this.x()).hashCode() * 13 ^ ((Double) this.y()).hashCode() * 17;

		if (this.dimension == 3)
			hash ^= ((Double) this.z()).hashCode() * 19;

		return hash;
	}

	@Override
	public Vector clone() {
		return this.dimension == 2 ? new Vector(this.x(), this.y()) : new Vector(this.x(), this.y(), this.z());
	}
}