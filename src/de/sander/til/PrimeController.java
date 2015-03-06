package de.sander.til;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

public class PrimeController implements MouseMotionListener, MouseListener, KeyListener {
	
	private PrimeModel model;
	private PrimeView view;
	private MenuController menu;
	private boolean REFRESH=true;
	
	public PrimeController(PrimeModel mod) {
		this.model = mod;
		this.view = new PrimeView(this.model);
		this.view.addMouseMotionListener(this);
		this.view.addMouseListener(this);
		this.view.addKeyListener(this);
		this.menu = new MenuController(this.model, new MenuView(this.model));
		this.view.setMenuBar(this.menu.getMenuBar());
		this.focusView();
	}
	
	public void focusView() {
		this.view.focus();		
	}
	
	public void closeView() {
		
	}
	
	public void updateView() {
		if (this.REFRESH || this.model.isChanged()) {
			this.REFRESH = false;
			this.view.draw();
		}
	}
	
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
	
	public void refreshScreen() {
		this.REFRESH = true;
	}
	
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
	public void mouseClicked(MouseEvent e) {
		System.out.println("click");
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println("press");
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("release");
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

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
		        		if (fy > 0) this.model.setFactorY(fy-this.model.getDelta()*1);
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
		        		this.model.setFactorY(fy+this.model.getDelta()*1);
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
		        		this.model.setFactorX(fx-this.model.getDelta()*2);
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
		        		this.model.setFactorX(fx+this.model.getDelta()*2);
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

}
