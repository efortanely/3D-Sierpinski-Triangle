import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class ThreeDee extends Engine {
	double θ, φ;
	ArrayList<Point3D> graph;
	private Vector mouseVector;
	private boolean[] keyInput;

	public static void main(String[] s) {
		new ThreeDee(500, 500, "Three Dimensional Sierpinski Triangle").start();
	}

	public ThreeDee(int width, int height, String title) {
		super(width, height, title);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.mouseVector = new Vector(e.getPoint());
	}

	@Override
	// creates a vector in the direction of mouse motion
	public void mouseReleased(MouseEvent e) {
		Vector newMouseVector = new Vector(e.getPoint());
		this.mouseVector = newMouseVector.minus(this.mouseVector);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode < this.keyInput.length)
			this.keyInput[keyCode] = true;
	}

	@Override
	public void first() {
		int size = 150;
		this.graph = new ArrayList<>();
		this.mouseVector = new Vector(0, 0);
		this.keyInput = new boolean[41];
		Point3D[] pt = { new Point3D(-size, size, 0), new Point3D(-size, -size, 0), new Point3D(size, size, 0),
				new Point3D(size, -size, 0), new Point3D(0, 0, size / Math.tan(Math.PI / 6)) };

		Random rand = new Random();
		Point3D cur = pt[0].midpoint(pt[1]);
		for (int i = 0; i < 25000; i++) {
			cur = cur.midpoint(pt[rand.nextInt(5)]);
			this.graph.add(new Point3D(cur.x, cur.y, cur.z, Color.getHSBColor((float) (cur.z / size), 0.8f, 0.7f)));
		}
	}

	@Override
	public void tick() {
		// limit magnitude of shift to small value
		this.mouseVector = this.mouseVector.unit();
		double stepSize = Math.PI / 20;
		double updateθ = 0;
		double updateφ = 0;

		// sum up mouse/arrow key motion in theta/phi directions
		updateθ += this.mouseVector.x();
		if (this.keyInput[KeyEvent.VK_LEFT])
			updateθ -= stepSize;
		if (this.keyInput[KeyEvent.VK_RIGHT])
			updateθ += stepSize;

		updateφ -= this.mouseVector.y();
		if (this.keyInput[KeyEvent.VK_UP])
			updateφ -= stepSize;
		if (this.keyInput[KeyEvent.VK_DOWN])
			updateφ += stepSize;

		this.θ += updateθ;
		this.φ += updateφ;

		// reset arrow keys (37-40) and mouse
		for (int i = 37; i < this.keyInput.length; i++)
			this.keyInput[i] = false;
		this.mouseVector = new Vector(0, 0);
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(new Color(30, 30, 30));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.translate(this.getWidth() / 2, this.getHeight() / 2);

		for (Point3D point : this.graph) {
			point.draw(g, this.θ, this.φ);
		}
	}

}

class Point3D {
	double x, y, z;
	Color c;

	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Point3D(double x, double y, double z, Color c) {
		this(x, y, z);
		this.c = c;
	}

	public Point getRender(double θ, double φ) {
		return new Point((int) (this.x * Math.cos(θ) - this.y * Math.sin(θ)),
				(int) ((this.x * Math.sin(θ) + this.y * Math.cos(θ)) * Math.sin(φ) - this.z * Math.cos(φ)));
	}

	public Point3D midpoint(Point3D b) {
		return new Point3D((this.x + b.x) / 2.0, (this.y + b.y) / 2.0, (this.z + b.z) / 2.0);
	}

	public void draw(Graphics2D g, double θ, double φ) {
		Point p = this.getRender(θ, φ);
		g.setColor(this.c);
		g.fillOval(p.x, p.y, 2, 2);
	}
}