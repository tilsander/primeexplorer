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
	private ColorController colorer;
	private boolean REFRESH=true, EXIT=false;
	
	public PrimeController(PrimeModel mod, PrimeView view) {
		this.model = mod;
		this.view = view;
		this.view.addMouseMotionListener(this);
		this.view.addMouseListener(this);
		this.view.addKeyListener(this);
		this.colorer = new ColorController(this.model,new ColorView(this.model));
	}
	
	public void start() {
		while (true) {
			if (EXIT) return;
			if (this.REFRESH || this.model.isChanged()) {
				this.REFRESH = false;
				this.view.draw();
			}
			this.sleep(60);
		}
	}
	
	public void stop() {
		this.EXIT = true;
	}
	
	public void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch(Exception e) {}
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
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char kc = e.getKeyChar();
		int keyCode = e.getKeyCode();
		if (kc != 0) {
		    switch( keyCode ) { 
		        case KeyEvent.VK_UP:
		        	int y = this.model.getYPos();
		            if (y > 0) this.model.setYPos(y-this.model.getDelta());
		            break;
		        case KeyEvent.VK_DOWN:
		        	this.model.setYPos(this.model.getYPos()+this.model.getDelta());
		            break;
		        case KeyEvent.VK_LEFT:
		        	int x = this.model.getXPos();
		            if (x > 0) this.model.setXPos(x-this.model.getDelta());
		            break;
		        case KeyEvent.VK_RIGHT :
		        	this.model.setXPos(this.model.getXPos()+this.model.getDelta());
		            break;
		        default:
		        	switch (kc) {
					case '+':
						switch (this.model.getPmmode()) {
						case VERTICAL_STEP:
							this.model.setVerticalStep(this.model.getVerticalStep()+1);
							break;
						case HORIZONTAL_STEP:
							this.model.setHorizontalStep(this.model.getHorizontalStep()+1);
							break;
						case VERTICAL_OFFSET:
							this.model.setVerticalOffset(this.model.getVerticalOffset()+1);
							break;
						case HORIZONTAL_OFFSET:
							this.model.setHorizontalOffset(this.model.getHorizontalOffset()+1);
							break;
						case NORMAL:
							this.model.setBlockSize(this.model.getBlockSize()+1);
							break;
						}
						break;
					case '-':
						switch (this.model.getPmmode()) {
						case VERTICAL_STEP:
							this.model.setVerticalStep(this.model.getVerticalStep()-1);
							break;
						case HORIZONTAL_STEP:
							this.model.setHorizontalStep(this.model.getHorizontalStep()-1);
							break;
						case VERTICAL_OFFSET:
							this.model.setVerticalOffset(this.model.getVerticalOffset()-1);
							break;
						case HORIZONTAL_OFFSET:
							this.model.setHorizontalOffset(this.model.getHorizontalOffset()-1);
							break;
						case NORMAL:
							this.model.setBlockSize(this.model.getBlockSize()-1);
							break;
						}
						break;
					case 'r':
						this.model.setDrawRect(!this.model.isDrawRect());
						break;
		        	case 'h':
						this.model.setHelper(!this.model.isHelper());
						break;
		        	case 'l':
						this.model.setRays(!this.model.isRays());
						break;
		        	case 'b':
						this.model.setRayBox(!this.model.isRayBox());
						break;
		        	case 'p':
						this.model.setChartProp(!this.model.isChartProp());
						break;
		        	case 'c':
						this.model.setChart(!this.model.isChart());
						break;
		        	case 'e':
						this.model.setChartExpSum(!this.model.isChartExpSum());
						break;
		        	case 'v':
						this.model.setPmmode(PrimeModel.PMMode.VERTICAL_STEP);
						break;
		        	case 'o':
						this.model.setPmmode(PrimeModel.PMMode.HORIZONTAL_STEP);
						break;
		        	case 'n':
						this.model.setPmmode(PrimeModel.PMMode.NORMAL);
						break;
		        	case 'a':
						this.model.setPmmode(PrimeModel.PMMode.VERTICAL_OFFSET);
						break;
		        	case 'w':
						this.model.setPmmode(PrimeModel.PMMode.HORIZONTAL_OFFSET);
						break;
		        	case 's':
						this.model.setStats(!this.model.isStats());
						break;
		        	case 't':
						this.model.setPrimes(!this.model.isPrimes());
						break;
					}
		        	break;
		     }
		    this.refreshMouse();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
