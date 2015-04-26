package de.sander.til;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The PrimeController controls a PrimeView.
 * It listens for Mouse-, Key-, Focus- and Windowevents.
 */
public class PrimeController implements MouseMotionListener, MouseListener, KeyListener, WindowListener {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PrimeController.class.getSimpleName());
	
	private PrimeModel model;
	private PrimeView view;
	private MenuController menu;
	private Settings settings;
	private boolean REFRESH=true;
	
	/**
	 * 
	 * @param mod
	 * @param settings
	 */
	public PrimeController(PrimeModel mod, Settings settings) {
		this.model = mod;
		this.settings = settings;
		this.view = new PrimeView(this.model);
		this.menu = new MenuController(this.model, this.settings);
		this.view.setMenuBar(this.menu.getMenuBar());
		this.view.addMouseMotionListener(this);
		this.view.addMouseListener(this);
		this.view.addKeyListener(this);
		this.view.addWindowListener(this);
		this.focusView();
	}
	
	/**
	 * focus the prime view
	 */
	public void focusView() {
		this.view.focus();		
	}
	
	/**
	 * close the prime view
	 */
	public void closeView() {
		this.view.close();
	}
	
	/**
	 * update the prime view
	 */
	public void updateView() {
		if (this.REFRESH || this.model.isChanged()) {
			this.REFRESH = false;
			this.view.draw();
		}
	}
	
	public MenuController getMenu() {
		return this.menu;
	}
	
	/**
	 * refresh the the mouse pointer position
	 */
	public void refreshMouse() {
		Point b = null;
		try {
			b = MouseInfo.getPointerInfo().getLocation();
		} catch (NullPointerException npe) {
			return;
		}
		SwingUtilities.convertPointFromScreen(b, view);
		int x = (int) b.getX();
		int y = (int) b.getY();
		this.setXY(x, y);
	}
	
	/**
	 * set the refresh flag
	 */
	public void refreshScreen() {
		this.REFRESH = true;
	}
	
	/**
	 * set the current mouse pointer position
	 * @param x
	 * @param y
	 */
	public void setXY(int x, int y) {
		x = this.view.transformMouseX(x);
		y = this.view.transformMouseY(y);
		this.model.setMouseX(x);
		this.model.setMouseY(y);
		this.refreshScreen();
	}
	
	// MouseMotionListener

	@Override
	public void mouseDragged(MouseEvent e) {
		System.out.println("drag");
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		this.setXY(x,y);
	}
	
	// MouseListener

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	// KeyListener
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		char kc = e.getKeyChar();
		int keyCode = e.getKeyCode();
		if (kc != 0) {
			if (this.model.isRotateView()) {
				switch(keyCode) { 
		        case KeyEvent.VK_UP:
		        	keyCode = KeyEvent.VK_LEFT;
		        	break;
		        case KeyEvent.VK_DOWN:
		        	keyCode = KeyEvent.VK_RIGHT;
		        	break;
		        case KeyEvent.VK_LEFT:
		        	keyCode = KeyEvent.VK_DOWN;
		        	break;
		        case KeyEvent.VK_RIGHT:
		        	keyCode = KeyEvent.VK_UP;
		        	break;
				}
			}
		    switch(keyCode) { 
		        case KeyEvent.VK_UP:
		        	switch (this.model.getPmview()) {
		        	case GOLDBACH:
		        		int y = this.model.getYPos();
			            if (y > 0) this.model.setYPos(y-this.model.getDelta());
		        		break;
		        	case FACTOR:
		        		int fy = this.model.getFactorY();
		        		if (fy > 0) this.model.setFactorY(fy-1);
		        		break;
		        	}
		            break;
		        case KeyEvent.VK_DOWN:
		        	switch (this.model.getPmview()) {
		        	case GOLDBACH:
		        		this.model.setYPos(this.model.getYPos()+this.model.getDelta());
		        		break;
		        	case FACTOR:
		        		int fy = this.model.getFactorY();
		        		this.model.setFactorY(fy+1);
		        		break;
		        	}
		            break;
		        case KeyEvent.VK_LEFT:
		        	switch (this.model.getPmview()) {
		        	case GOLDBACH:
		        		int x = this.model.getXPos();
			            if (x > 0) this.model.setXPos(x-this.model.getDelta());
		        		break;
		        	case FACTOR:
		        		int fx = this.model.getFactorX();
		        		this.model.setFactorX(fx-2);
		        		break;
		        	}
		            break;
		        case KeyEvent.VK_RIGHT:
		        	switch (this.model.getPmview()) {
		        	case GOLDBACH:
		        		this.model.setXPos(this.model.getXPos()+this.model.getDelta());
		        		break;
		        	case FACTOR:
		        		int fx = this.model.getFactorX();
		        		this.model.setFactorX(fx+2);
		        		break;
		        	}
		            break;
		        default:
		        	break;
		     }
		    this.refreshMouse();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	// WindowListener
	
	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		this.settings.closeModel(this.model);
	}

	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowActivated(WindowEvent e) {
		this.settings.setCurrentModel(this.model);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {}

}
