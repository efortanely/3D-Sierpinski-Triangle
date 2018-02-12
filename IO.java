
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public interface IO extends KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
	@Override
	public void keyPressed(KeyEvent e);

	@Override
	public void keyReleased(KeyEvent e);

	@Override
	public void keyTyped(KeyEvent e);

	@Override
	public void mouseClicked(MouseEvent me);

	@Override
	public void mouseEntered(MouseEvent me);

	@Override
	public void mouseExited(MouseEvent me);

	@Override
	public void mousePressed(MouseEvent me);

	@Override
	public void mouseReleased(MouseEvent me);

	@Override
	public void mouseDragged(MouseEvent me);

	@Override
	public void mouseMoved(MouseEvent me);

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe);
}
