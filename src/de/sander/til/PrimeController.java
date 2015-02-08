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
		this.view.focus();
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
		        case KeyEvent.VK_RIGHT :
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
						case POLY_SIZE:
							this.model.setPolySize(this.model.getPolySize()+1);
							break;
						case POLY_FACTOR:
							this.model.setPolyFactor(this.model.getPolyFactor()+1);
							break;
						case POLY_DELTA:
							this.model.setPolyDelta(this.model.getPolyDelta()+1);
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
						case POLY_SIZE:
							this.model.setPolySize(this.model.getPolySize()-1);
							break;
						case POLY_FACTOR:
							this.model.setPolyFactor(this.model.getPolyFactor()-1);
							break;
						case POLY_DELTA:
							this.model.setPolyDelta(this.model.getPolyDelta()-1);
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
		        	case 'd':
						this.model.setPolarFactors(!this.model.isPolarFactors());
						break;
		        	case 'i':
						this.model.setPolarBalance(!this.model.isPolarBalance());
						break;
		        	case 'j':
						this.model.setFactorOnlyOuter(!this.model.isFactorOnlyOuter());
						break;
		        	case 'k':
						this.model.setFactorOnlyNeeded(!this.model.isFactorOnlyNeeded());
						break;
		        	case 'b':
						this.model.setRayBox(!this.model.isRayBox());
						break;
		        	case 'f':
						this.model.setCheckedPattern(!this.model.isCheckedPattern());
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
		        	case 'm':
						this.model.setPrimeMirror(!this.model.isPrimeMirror());
						break;
		        	case 'v':
						this.model.setPmmode(PrimeModel.PMMode.VERTICAL_STEP);
						break;
		        	case 'g':
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
		        	case 'q':
						if (this.model.getPmmode()==PrimeModel.PMMode.POLY_SIZE) this.model.setPmmode(PrimeModel.PMMode.POLY_FACTOR);
						else if (this.model.getPmmode()==PrimeModel.PMMode.POLY_FACTOR) this.model.setPmmode(PrimeModel.PMMode.POLY_DELTA);
						else if (this.model.getPmmode()==PrimeModel.PMMode.POLY_DELTA) this.model.setPmmode(PrimeModel.PMMode.POLY_SIZE);
						else this.model.setPmmode(PrimeModel.PMMode.POLY_SIZE);
						break;
		        	case 's':
						this.model.setStats(!this.model.isStats());
						break;
		        	case 't':
						this.model.setPrimes(!this.model.isPrimes());
						break;
		        	case 'u':
						this.model.setPolynomials(!this.model.isPolynomials());
						break;
		        	case 'o':
		        		switch (this.model.getPmview()) {
			        	case GOLDBACH:
			        		this.model.setXPos(0);
							this.model.setYPos(0);
			        		break;
			        	case FACTOR:
			        		this.model.setFactorX(0);
							this.model.setFactorY(0);
			        		break;
			        	}
						break;
		        	case 'y':
		        		if (this.model.getPmview()==PrimeModel.PMView.GOLDBACH) this.model.setPmview(PrimeModel.PMView.FACTOR);
		        		else if (this.model.getPmview()==PrimeModel.PMView.FACTOR) this.model.setPmview(PrimeModel.PMView.GOLDBACH);
		        		break;
		        	case 'x':
						if (this.model.getXPos() < this.model.getYPos()) this.model.setXPos(this.model.getYPos()/this.model.getHorizontalStep());
						else this.model.setYPos(this.model.getXPos()/this.model.getVerticalStep());
						break;
		        	case '1':
		        		this.model.setChartPrimes(!this.model.isChartPrimes());
		        		break;
		        	case '2':
		        		this.model.setChartExp(!this.model.isChartExp());
		        		break;
		        	case '3':
		        		this.model.setChartMatchCount(!this.model.isChartMatchCount());
		        		break;
		        	case '4':
		        		this.model.setChartFirstMatch(!this.model.isChartFirstMatch());
		        		break;
		        	case '5':
		        		this.model.setChartVoidCount(!this.model.isChartVoidCount());
		        		break;
		        	case '6':
		        		this.model.setChartFirstVoid(!this.model.isChartFirstVoid());
		        		break;
		        	case '7':
		        		this.model.setChartPrimeCountCalc(!this.model.isChartPrimeCountCalc());
		        		break;
		        	case '8':
		        		this.model.setChartMatchCountCalc(!this.model.isChartMatchCountCalc());
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
