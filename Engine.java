
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public abstract class Engine extends Canvas implements Runnable, IO {
	private static final long serialVersionUID = 1L;
	private boolean running;
	private static boolean smooth;
	private static double fps;

	public Engine(int width, int height, String title) {
		//// all your generic canvas initialization instructions (someone tell java to
		//// make this prettier)
		Dimension dimension = new Dimension(width, height);
		this.setPreferredSize(dimension);
		this.setMaximumSize(dimension);
		this.setMinimumSize(dimension);

		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		this.requestFocusInWindow();
		////

		this.running = false;
		Engine.smooth = false;
		// SPEED PRECISION ENDURANCE
		Engine.fps = 60;
	}

	public synchronized void start() {
		this.running = true;
		new Thread(this).start();
	}

	public abstract void first();

	public abstract void tick();

	public abstract void render(Graphics2D g);

	// ticks update logic, rendering updates graphics.
	// uncomment code in method to see number of frames and ticks per second
	@Override
	public void run() {
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);

		this.first();

		double lastTimeSec = System.nanoTime() / 1E9;
		double changeInTimeSec = 0;
		double secondsPerFrame = 1 / Engine.fps;
		boolean renderToFrame = false;
		double timerMillisec = System.currentTimeMillis();
		// int ticksSec = 0;
		// int framesSec = 0;

		while (this.running) {
			double currentTimeSec = System.nanoTime() / 1E9;
			changeInTimeSec += currentTimeSec - lastTimeSec;
			lastTimeSec = currentTimeSec;

			while (changeInTimeSec >= secondsPerFrame) {
				// ticksSec++;
				this.tick();
				changeInTimeSec -= secondsPerFrame;
				renderToFrame = true;
			}

			if (renderToFrame) {
				// framesSec++;
				BufferStrategy bufferStrategy = this.getBufferStrategy();
				if (bufferStrategy == null) {
					this.createBufferStrategy(3);
				} else {
					Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
					if (Engine.smooth)
						g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					this.render(g);
					g.dispose();
					bufferStrategy.show();
				}

				renderToFrame = false;
			} else {
				// sleeping before attempting to update the screen again helps
				// efficiency
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// int secondInMillisec = 1000;
			// if (System.currentTimeMillis() - timerMillisec > secondInMillisec) {
			// timerMillisec += secondInMillisec;
			// System.out.println("Fps: " + framesSec + " Ticks: " + ticksSec);
			// framesSec = ticksSec = 0;
			// }

		}
	}

	//// IO ////
	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent me) {
	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mousePressed(MouseEvent me) {
	}

	@Override
	public void mouseReleased(MouseEvent me) {
	}

	@Override
	public void mouseDragged(MouseEvent me) {
	}

	@Override
	public void mouseMoved(MouseEvent me) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) {
	}

	//// GETTERS AND SETTERS ////

	public static double getSecondsPerFrame() {
		return 1 / Engine.fps;
	}

	public static void setSmooth(boolean smooth) {
		Engine.smooth = smooth;
	}

	public static void setFps(int fps) {
		Engine.fps = fps;
	}
}