package de.sander.til;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.sander.til.PrimeModel.InfoEntry;

/**
 * A PrimeView renders the content of a prime model.
 */
public class PrimeView extends JPanel {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PrimeView.class.getSimpleName());
	
	private enum ColorType {
		BORDER,
		BOX,
		TEXT
	}
	
	private enum ChartType {
		PRIME_COUNT,
		MATCH_COUNT,
		EXPONENT,
		FIRST_MATCH,
		VOID_COUNT,
		FIRST_VOID,
		PRIME_COUNT_CALC,
		MATCH_COUNT_CALC,
		DIVISOR_SUM,
		EULER_TOTIENT
	}
	
	private class Point2D {
		
		public int x=0,y=0;
		public boolean b=false;
		
		public Point2D(int px, int py, boolean pb) {
			this.x = px;
			this.y = py;
			this.b = pb;
		}
	}
	
	private class PairMetric {
		
		private String key, value;
		private int key_width, value_width;
		
		public PairMetric(String k, String v, int kw, int vw) {
			this.key = k;
			this.value = v;
			this.key_width = kw;
			this.value_width = vw;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

		public int getKeyWidth() {
			return key_width;
		}

		public int getValueWidth() {
			return value_width;
		}

	}
	
	private static final long serialVersionUID = 1L;
	private PrimeModel model;
	private int X_LEFT=40,
			Y_TOP=50,
			Y_BOTTOM=150,
			X_RIGHT=300,
			BLOCK=0,
			Y_STEP=0,
			X_STEP=0,
			Y_OFF=0,
			X_OFF=0,
			MOUSE_X=0,
			MOUSE_Y=0,
			X_POS=0,
			Y_POS=0,
			X_COUNT=0,
			Y_COUNT=0,
			WIDTH=0,
			HEIGHT=0,
			Y_AXIS_DELTA=0,
			POLAR_FACTOR_X=0,
			POLAR_FACTOR_Y=0,
			POLAR_FACTOR_Z=0,
			POLY_DELTA=0,
			POLY_FACTOR=0,
			POLY_N=3,
			POLY_I=3,
			LAST_X_POS=0,
			LAST_Y_POS=0,
			LAST_WIDTH=0,
			LAST_HEIGHT=0,
			LAST_POLY_FACTOR;
	private boolean calc_poly=true;
	private double STRING_HEIGHT=0.0;
	private StringMetrics metric;
	private Font FONT, SMALL_FONT;
	private Graphics2D g2d=null;
	private JTextField typingArea;
	private JFrame viewFrame;
	
	public PrimeView(PrimeModel mod) {
		this.setModel(mod);
		this.viewFrame = new JFrame();
		this.viewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.typingArea = new JTextField(1);
		this.typingArea.setPreferredSize(new Dimension(3, 2));
		this.typingArea.setOpaque(true);
		this.typingArea.setBorder(new EmptyBorder(0,0,0,0));
		this.typingArea.setBackground(this.getBackgroundColor());
		this.typingArea.setForeground(this.getBackgroundColor());
		this.add(this.typingArea, BorderLayout.PAGE_END);
		this.viewFrame.getContentPane().add(this);
		this.viewFrame.setSize(this.model.getWindowWidth(), this.model.getWindowHeight());
		this.viewFrame.setTitle(this.model.getTitle());
		this.viewFrame.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				model.setWindowWidth(viewFrame.getWidth());
				model.setWindowHeight(viewFrame.getHeight());
			}
			public void componentMoved(ComponentEvent e) {}
			public void componentShown(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
        });
	}
	
	/**
	 * Set the focus to the view frame.
	 */
	public void focus() {
		this.viewFrame.setVisible(true);
	}
	
	/**
	 * Close the window.
	 */
	public void close() {
		this.viewFrame.setVisible(false);
		this.viewFrame = null;
	}
	
	/**
	 * Set the menu bar of this view.
	 * @param menu
	 */
	public void setMenuBar(JMenuBar menu) {
		this.viewFrame.setJMenuBar(menu);
	}
	
	/**
	 * @return The prime model.
	 */
	public PrimeModel getModel() {
		return model;
	}

	/**
	 * Set the model of this view.
	 * @param model
	 */
	public void setModel(PrimeModel model) {
		this.model = model;
	}

	/**
	 * Draw the view.
	 */
	public void draw() {
		this.setModel(model);
		this.repaint();
	}
	
	/**
	 * Initialize the view metric.
	 * @param g2d
	 */
	private void initMetric(Graphics2D g2d) {
		if (this.metric == null) {
			this.FONT = g2d.getFont();
			this.SMALL_FONT = FONT.deriveFont(9.0f);
			this.metric = new StringMetrics(g2d);
		}
	}

	/**
	 * Paint the view.
	 */
	public void paintComponent(Graphics g) {
		this.g2d = (Graphics2D) g;
		this.initMetric(g2d);
		this.drawView();
	}
	
	/**
	 * Draw the primes, exonents, rays, axis etc.
	 */
	private void drawView() {
		this.setupEnv();
		// draw view
		this.drawBackground();
		this.drawField();
		Map<Integer,Point2D> rays = this.drawRays();
		this.drawText();
		if (this.model.isShowHCN()) this.drawHCN();
		this.drawPolys();
		if (this.model.isPolarFactors()) this.drawPolarFactors();
		this.drawAxis();
		this.drawBottomRight();
		this.drawRayOrder(rays);
		if (this.model.isChart()) {
			if (this.model.isChartPrimes()) this.drawChart(ChartType.PRIME_COUNT);
			if (this.model.isChartExp()) this.drawChart(ChartType.EXPONENT);
			if (this.model.isChartMatchCount()) this.drawChart(ChartType.MATCH_COUNT);
			if (this.model.isChartFirstMatch()) this.drawChart(ChartType.FIRST_MATCH);
			if (this.model.isChartVoidCount()) this.drawChart(ChartType.VOID_COUNT);
			if (this.model.isChartFirstVoid()) this.drawChart(ChartType.FIRST_VOID);
			if (this.model.isChartPrimeCountCalc()) this.drawChart(ChartType.PRIME_COUNT_CALC);
			if (this.model.isChartMatchCountCalc()) this.drawChart(ChartType.MATCH_COUNT_CALC);
			if (this.model.isChartDivisorSum()) this.drawChart(ChartType.DIVISOR_SUM);
			if (this.model.isChartEulerTotient()) this.drawChart(ChartType.EULER_TOTIENT);
			this.drawChartDesc();
		}
		if (this.model.isStats()) this.drawStats();
	}
	
	/**
	 * Draw the polar factors at the top.
	 */
	private void drawPolarFactors() {
		this.drawPolarPolys();
		this.drawFactorField();
	}
	
	/**
	 * Initialize all environment variables.
	 */
	private void setupEnv() {
		if (this.model.isRotateView()) {
			this.HEIGHT = this.getWidth();
			this.WIDTH = this.getHeight();
			g2d.rotate(Math.PI/2);
			g2d.translate(0, -HEIGHT);
		} else {
			this.WIDTH = this.getWidth();
			this.HEIGHT = this.getHeight();
		}
		this.BLOCK = this.model.getBlockSize();
		this.Y_STEP = this.model.getVerticalStep();
		this.X_STEP = this.model.getHorizontalStep();
		this.Y_OFF = this.model.getVerticalOffset();
		this.X_OFF = this.model.getHorizontalOffset();
		this.MOUSE_X = this.model.getMouseX();
		this.MOUSE_Y = this.model.getMouseY();
		this.X_POS = this.model.getXPos();
		this.Y_POS = this.model.getYPos();
		if (this.model.isChart()) X_RIGHT=300;
		else X_RIGHT=30;
		if (this.model.isStats()) Y_BOTTOM=200;
		else Y_BOTTOM=30;
		if (X_RIGHT > WIDTH - X_LEFT) X_RIGHT = WIDTH - X_LEFT;
		STRING_HEIGHT = this.metric.getHeight("1");
		this.Y_AXIS_DELTA = (int)(STRING_HEIGHT/(double)BLOCK);
		if (Y_AXIS_DELTA <= 0) Y_AXIS_DELTA = 1;
		
		this.X_COUNT = (int)Math.ceil(((double)WIDTH-this.X_LEFT)/((double)BLOCK));
		this.Y_COUNT = (int)Math.ceil(((double)HEIGHT-this.Y_TOP)/((double)BLOCK));
		this.X_LEFT = (int)this.metric.getWidth(""+(this.transformY(Y_COUNT))) + 9;
		this.POLAR_FACTOR_X = this.model.getFactorX();
		this.POLAR_FACTOR_Y = this.model.getFactorY();
		this.POLAR_FACTOR_Z = this.model.getFactorZ();
		this.POLY_DELTA = this.model.getPolyDelta();
		this.POLY_FACTOR = this.model.getPolyFactor();
		this.calc_poly = this.X_POS != this.LAST_X_POS || this.Y_POS != this.LAST_Y_POS || this.WIDTH != this.LAST_WIDTH || this.HEIGHT != this.LAST_HEIGHT || this.POLY_FACTOR != this.LAST_POLY_FACTOR;
		this.LAST_X_POS = this.X_POS;
		this.LAST_Y_POS = this.Y_POS;
		this.LAST_HEIGHT = this.HEIGHT;
		this.LAST_WIDTH = this.WIDTH;
		this.LAST_POLY_FACTOR = this.POLY_FACTOR;
	}
	
	/**
	 * Draw the views background.
	 */
	private void drawBackground() {
		// clear background
		g2d.setBackground(this.getBackgroundColor());
		g2d.clearRect(0, 0, WIDTH, HEIGHT);
	}
	
	/**
	 * Draw lines at the bottom right.
	 */
	private void drawBottomRight() {
		// right-bottom-border
		g2d.setColor(this.getBackgroundColor());
		g2d.fillRect(this.X_LEFT, HEIGHT-this.Y_BOTTOM, WIDTH-this.X_LEFT, this.Y_BOTTOM);
		g2d.fillRect(WIDTH-this.X_RIGHT,this.Y_TOP,this.X_RIGHT,HEIGHT-this.Y_BOTTOM);
		g2d.setColor(this.model.getColor("LIGHT_WHITE"));
		g2d.drawLine(this.X_LEFT, HEIGHT-this.Y_BOTTOM, WIDTH-this.X_RIGHT, HEIGHT-this.Y_BOTTOM);
		g2d.drawLine(WIDTH-this.X_RIGHT,this.Y_TOP,WIDTH-this.X_RIGHT,HEIGHT-this.Y_BOTTOM);
	}
	
	/**
	 * Draw the x and y axis.
	 */
	private void drawAxis() {
		int x, y, y_str;
		g2d.setColor(this.getBackgroundColor());
		g2d.fillRect(0, 0, X_LEFT-1, HEIGHT);
		g2d.fillRect(0, 0, WIDTH, Y_TOP-1);
		// draw view rect
		g2d.setColor(this.model.getColor("LIGHT_WHITE"));
		g2d.drawLine(X_LEFT-1, Y_TOP-1, X_LEFT-1, HEIGHT-1);
		g2d.drawLine(X_LEFT-1, Y_TOP-1, WIDTH-1, Y_TOP-1);
		g2d.setColor(this.getTextColor());
		// y-achsis
		for (int yp = 1; yp <= Y_COUNT; ++yp) {
			y = this.transformY(yp);
			// delta logic
			if ((yp%Y_AXIS_DELTA!=0 || (y > MOUSE_Y-Y_AXIS_DELTA && y < MOUSE_Y+Y_AXIS_DELTA) || (y > MOUSE_X-Y_AXIS_DELTA && y < MOUSE_X+Y_AXIS_DELTA)) && MOUSE_Y!=y && (MOUSE_X!=y)) continue;
			if (MOUSE_Y==y || MOUSE_X==y) g2d.setColor(this.getHightlightTextColor());
			y_str = this.model.isCheckedPattern() ? y*2 - 1 : y;
			g2d.drawString(""+y_str, 2, yp*BLOCK+Y_TOP);
			if (MOUSE_Y==y || MOUSE_X==y) g2d.setColor(this.getTextColor());
		}
		// x-achsis
		int last_x_offset = 0;
		for (int xp = 1; xp <= X_COUNT; ++xp) {
			x = this.transformX(xp);
			if (Primes._().isPrime(x) || MOUSE_X==x || MOUSE_Y==x) {
				int cx_p = (xp-1)*BLOCK+X_LEFT;
				if (MOUSE_X==x || MOUSE_Y==x) {
					g2d.setColor(this.getHightlightTextColor());
					String dstr = ""+x+"="; 
					int dw = (int)this.metric.getWidth(dstr);
					int cy_p = Y_TOP-(int)STRING_HEIGHT-2;
					if (MOUSE_X==x) cy_p -= 15;
					g2d.drawString(dstr, cx_p, cy_p);
					this.drawPrimeProduct(Primes._().getExponents(x), cx_p+dw+1, cy_p);
					g2d.setColor(this.getTextColor());
				} else {
					if (cx_p > last_x_offset) {
						g2d.drawString(""+x, cx_p, Y_TOP-3);
						last_x_offset = cx_p + (int)this.metric.getWidth(""+x) + 2;
					}
				}
			}
		}
		// prime factors
		if (MOUSE_Y <= X_POS || MOUSE_Y > X_POS+X_COUNT) {
			g2d.setColor(this.getHightlightTextColor());
			String dstr = ""+MOUSE_Y+"="; 
			int dw = (int)this.metric.getWidth(dstr);
			int cy_p = Y_TOP-(int)STRING_HEIGHT-2;
			int cx_p = (MOUSE_Y <= X_POS ? this.X_LEFT : WIDTH - 130);
			g2d.drawString(dstr, cx_p, cy_p);
			this.drawPrimeProduct(Primes._().getExponents(MOUSE_Y), cx_p+dw+1, cy_p);
			g2d.setColor(this.getTextColor());
		}
	}
	
	/**
	 * Draw the number field.
	 */
	private void drawField() {
		int x, y, cp=0;
		// blocks
		for (int yp = 1; yp <= Y_COUNT; ++yp) {
			y = this.transformY(yp);
			for (int xp = 1; xp <= X_COUNT; ++xp) {
				if ((xp-1)*BLOCK+X_LEFT > WIDTH-X_RIGHT) continue;
				x = this.transformX(xp);
				cp = this.model.isCheckedPattern() ? x/2 : 0;
				Color col = this.getColor(x,y+cp,ColorType.BOX);
				if (col != null) {
					g2d.setColor(col);
					this.drawBox((xp-1)*BLOCK+X_LEFT, (yp-1)*BLOCK+Y_TOP, BLOCK, BLOCK);
				}
			}
		}
		// borders
		for (int yp = 1; yp <= Y_COUNT; ++yp) {
			y = this.transformY(yp);
			for (int xp = 1; xp <= X_COUNT; ++xp) {
				if ((xp-1)*BLOCK+X_LEFT > WIDTH-X_RIGHT) continue;
				x = this.transformX(xp);
				cp = this.model.isCheckedPattern() ? x/2 : 0;
				Color bor = this.getColor(x,y+cp,ColorType.BORDER);
				if (bor != null) {
					g2d.setColor(bor);
					if (BLOCK > 1) {
						if (this.model.isDrawRect()) {
							g2d.drawLine((xp-1)*BLOCK+X_LEFT, yp *BLOCK+Y_TOP, xp*BLOCK+X_LEFT, yp*BLOCK+Y_TOP); // bottom
							if ((y+cp)%x==0 && this.model.isFactors()) {
								g2d.drawLine((xp-1)*BLOCK+X_LEFT, (yp-1)  *BLOCK+Y_TOP, xp*BLOCK+X_LEFT, (yp-1)*BLOCK+Y_TOP); // top
								g2d.drawLine((xp-1)*BLOCK+X_LEFT, (yp-1)  *BLOCK+Y_TOP, (xp-1)*BLOCK+X_LEFT, yp*BLOCK+Y_TOP); // left
								g2d.drawLine(xp*BLOCK+X_LEFT, (yp-1)  *BLOCK+Y_TOP, xp*BLOCK+X_LEFT, yp*BLOCK+Y_TOP); // right
							}
						} else {
							if ((y+cp)%x==0 && this.model.isFactors()) {
								g2d.drawOval((xp-1)*BLOCK+X_LEFT, (yp-1)*BLOCK+Y_TOP, BLOCK, BLOCK);
							}
						}
					} else {
						g2d.fillRect((xp-1)*BLOCK+X_LEFT, yp *BLOCK+Y_TOP, 1, 1);
					}
				}
			}
		}
	}
	
	/**
	 * Draw the factor field.
	 */
	private void drawFactorField() {
		int x, y;
		if (!this.model.isPolarBalance()) {
			g2d.setColor(this.model.getColor("POLAR_FACTOR_LEFT"));
			for (int yp = 1; yp <= Y_COUNT; ++yp) {
				y = yp;
				for (int xp = 1; xp <= X_COUNT; ++xp) {
					x = xp + X_POS;
					if (this.model.isFactorOnlyOuter() && x > y*y) continue;
					if (x > 0 && y > 1 && x/y != 1 && x%y==0) {
						boolean match = false;
						if (this.model.isFactorOnlyNeeded()) for (int i = y+1; i <= x/2; ++i) {
							if (x%i==0) {
								match = true;
								break;
							}
						}
						if (match==false) g2d.drawOval((xp-1)*BLOCK+X_LEFT, (int)(yp-1)*POLAR_FACTOR_Y*BLOCK+Y_TOP, BLOCK, BLOCK);
					}
				}
			}
			g2d.setColor(this.model.getColor("POLAR_FACTOR_RIGHT"));
			for (int yp = 1; yp <= Y_COUNT; ++yp) {
				y = yp;
				for (int xp = 1; xp <= X_COUNT; ++xp) {
					x = POLAR_FACTOR_X - (xp + X_POS);
					if (this.model.isFactorOnlyOuter() && x > y*y) continue;
					if (x > 0 && y > 1 && x/y != 1 && x%y==0) {
						boolean match = false;
						if (this.model.isFactorOnlyNeeded()) for (int i = y+1; i <= x/2; ++i) {
							if (x%i==0) {
								match = true;
								break;
							}
						}
						if (match==false) g2d.drawOval((xp-1)*BLOCK+BLOCK/4+X_LEFT, (yp-1)*POLAR_FACTOR_Y*BLOCK+BLOCK/4+Y_TOP, BLOCK/2, BLOCK/2);
					}
				}
			}
		}
		g2d.setColor(new Color(255,255,255,100));
		for (int xp = 1; xp <= X_COUNT; ++xp) {
			x = xp + X_POS;
			if (Primes._().isPrime(x) && Primes._().isPrime(POLAR_FACTOR_X - (xp + X_POS))) {
				g2d.drawLine((xp-1)*BLOCK+BLOCK/2+X_LEFT, Y_TOP, (xp-1)*BLOCK+BLOCK/2+X_LEFT, HEIGHT);
			}
		}
	}
	
	/**
	 * Draw text.
	 */
	private void drawText() {
		int x, y, cp=0;
		// text
		if (STRING_HEIGHT <= BLOCK + 5) {
			for (int yp = 1; yp <= Y_COUNT; ++yp) {
				y = this.transformY(yp);
				for (int xp = 1; xp <= X_COUNT; ++xp) {
					if ((xp-1)*BLOCK+X_LEFT > WIDTH-X_RIGHT) continue;
					x = this.transformX(xp);
					cp = this.model.isCheckedPattern() ? x/2 : 0;
					Color tex = this.getColor(x,y+cp,ColorType.TEXT);
					if (tex != null) {
						String lbl = this.getText(x,y+cp);
						if (lbl != null && lbl.equals("") == false) {
							g2d.setColor(tex);
							g2d.drawString(lbl, (xp-1)*BLOCK+X_LEFT+(BLOCK/5), yp*BLOCK+Y_TOP-(BLOCK/7));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Draw the chart with the specified type.
	 * @param type
	 */
	private void drawChart(ChartType type) {
		
		switch (type) {
		case PRIME_COUNT:
			this.g2d.setColor(this.model.getColor("CHART_PRIME"));
			break;
		case MATCH_COUNT:
			this.g2d.setColor(this.model.getColor("CHART_MATCH_COUNT"));
			break;
		case EXPONENT:
			this.g2d.setColor(this.model.getColor("CHART_EXP"));
			break;
		case FIRST_MATCH:
			this.g2d.setColor(this.model.getColor("CHART_FIRST_MATCH"));
			break;
		case FIRST_VOID:
			this.g2d.setColor(this.model.getColor("CHART_FIRST_VOID"));
			break;
		case VOID_COUNT:
			this.g2d.setColor(this.model.getColor("CHART_VOID_COUNT"));
			break;
		case PRIME_COUNT_CALC:
			this.g2d.setColor(this.model.getColor("CHART_PRIME_COUNT_CALC"));
			break;
		case MATCH_COUNT_CALC:
			this.g2d.setColor(this.model.getColor("CHART_MATCH_COUNT_CALC"));
			break;
		case DIVISOR_SUM:
			this.g2d.setColor(this.model.getColor("CHART_DIVISOR_SUM"));
			break;
		case EULER_TOTIENT:
			this.g2d.setColor(this.model.getColor("CHART_EULER_TOTIENT"));
			break;
		default:
			return;
		}
		
		int Y_MAX = this.transformY(Y_COUNT),
			X_CHART_LEFT = WIDTH - X_RIGHT + 1,
			xc_val=0, y=0;
		double chart_px=0.0, cpx_dev=1.0;
		if (this.model.isChartProp()) {
			List<Double> devs = new ArrayList<Double>();
			if (this.model.isChartPrimes()) devs.add((double)(Primes._().primesUntil(Y_MAX)));
			if (this.model.isChartMatchCount()) devs.add((double)(Primes._().getMaxMatchCount(Y_MAX)));
			if (this.model.isChartExp()) {
				if (this.model.isChartExpSum()) devs.add((double)(Primes._().getMaxExpSum(Y_MAX)));
				else devs.add((double)(Primes._().getMaxExpCount(Y_MAX)));
			}
			if (this.model.isChartFirstMatch()) devs.add((double)(Primes._().getMaxFirstMatch(Y_MAX)));
			if (this.model.isChartFirstVoid()) devs.add((double)(Primes._().getMaxFirstVoid(Y_MAX)));
			if (this.model.isChartVoidCount()) devs.add((double)(Primes._().getMaxVoidCount(Y_MAX)));
			if (this.model.isChartPrimeCountCalc()) devs.add((double)(Primes._().getMaxPrimeCountCalc(Y_MAX)));
			if (this.model.isChartMatchCountCalc()) devs.add((double)(Primes._().getMaxMatchCountCalc(Y_MAX)));
			if (this.model.isChartDivisorSum()) devs.add((double)(Primes._().getMaxDivisorSum(Y_MAX, this.model.getDivisorSumExponent())));
			if (this.model.isChartEulerTotient()) devs.add((double)(Primes._().getMaxEulerTotient(Y_MAX)));
			cpx_dev = Collections.max(devs);
		} else {
			switch (type) {
			case PRIME_COUNT:
				cpx_dev = (double)(Primes._().primesUntil(Y_MAX));
				break;
			case MATCH_COUNT:
				cpx_dev = (double)(Primes._().getMaxMatchCount(Y_MAX));
				break;
			case EXPONENT:
				if (this.model.isChartExpSum()) cpx_dev = (double)(Primes._().getMaxExpSum(Y_MAX)); 
				else cpx_dev = (double)(Primes._().getMaxExpCount(Y_MAX));
				break;
			case FIRST_MATCH:
				cpx_dev = (double)(Primes._().getMaxFirstMatch(Y_MAX));
				break;
			case FIRST_VOID:
				cpx_dev = (double)(Primes._().getMaxFirstVoid(Y_MAX));
				break;
			case VOID_COUNT:
				cpx_dev = (double)(Primes._().getMaxVoidCount(Y_MAX));
				break;
			case PRIME_COUNT_CALC:
				cpx_dev = (double)(Primes._().getMaxPrimeCountCalc(Y_MAX));
				break;
			case MATCH_COUNT_CALC:
				cpx_dev = (double)(Primes._().getMaxMatchCountCalc(Y_MAX));
				break;
			case DIVISOR_SUM:
				cpx_dev = (double)(Primes._().getMaxDivisorSum(Y_MAX, this.model.getDivisorSumExponent()));
				break;
			case EULER_TOTIENT:
				cpx_dev = (double)(Primes._().getMaxEulerTotient(Y_MAX));
				break;
			default:
				return;
			}
		}
		chart_px = ((double)this.X_RIGHT-1.0)/(cpx_dev+1.0);
		
		for (int yp = 1; yp <= Y_COUNT; ++yp) {
			y = this.transformY(yp);
			switch (type) {
			case PRIME_COUNT:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().primesUntil(y)*chart_px);
				break;
			case MATCH_COUNT:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().getMatchCount(y)*chart_px);
				break;
			case EXPONENT:
				if (this.model.isChartExpSum()) xc_val = X_CHART_LEFT + (int)((double)Primes._().getExponentSum(y)*chart_px);
				else xc_val = X_CHART_LEFT + (int)((double)Primes._().getExponentCount(y)*chart_px);
				break;
			case FIRST_MATCH:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().getFirstMatch(y)*chart_px);
				break;
			case FIRST_VOID:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().getFirstVoid(y)*chart_px);
				break;
			case VOID_COUNT:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().getVoidCount(y)*chart_px);
				break;
			case PRIME_COUNT_CALC:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().calculatePrimeCount(y)*chart_px);
				break;
			case MATCH_COUNT_CALC:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().calculateMatchCount(y)*chart_px);
				break;
			case DIVISOR_SUM:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().getDivisorSum(y, this.model.getDivisorSumExponent())*chart_px);
				break;
			case EULER_TOTIENT:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().getEulerTotient(y)*chart_px);
				break;
			}
			g2d.drawLine(xc_val, (yp-1)*BLOCK+Y_TOP, xc_val, yp*BLOCK+Y_TOP);
		}
	}
	
	/**
	 * Draw the chart description.
	 */
	private void drawChartDesc() {
		if (MOUSE_Y > Y_POS) {
			int y = MOUSE_Y;
			int yp = y;
			yp -= this.model.getVerticalOffset();
			yp /= this.model.getVerticalStep();
			yp -= this.model.getYPos();
			String str = "-";
			int chartCount = 0;
			if (this.model.isChartPrimes()) {
				this.g2d.setColor(this.model.getColor("CHART_PRIME"));
				str = "" + Primes._().primesUntil(y);
				g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*chartCount);
				chartCount++;
			}
			if (this.model.isChartMatchCount()) {
				this.g2d.setColor(this.model.getColor("CHART_MATCH_COUNT"));
				str = "" + Primes._().getMatchCount(y);
				g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*chartCount);
				chartCount++;
			}
			if (this.model.isChartExp()) {
				this.g2d.setColor(this.model.getColor("CHART_EXP"));
				if (this.model.isChartExpSum()) str = "" + Primes._().getExponentSum(y);
				else str = "" + Primes._().getExponentCount(y);
				g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*chartCount);
				chartCount++;
			}
			if (this.model.isChartFirstMatch()) {
				this.g2d.setColor(this.model.getColor("CHART_FIRST_MATCH"));
				str = "" + Primes._().getFirstMatch(y);
				g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*chartCount);
				chartCount++;
			}
			if (this.model.isChartVoidCount()) {
				this.g2d.setColor(this.model.getColor("CHART_VOID_COUNT"));
				str = "" + Primes._().getVoidCount(y);
				g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*chartCount);
				chartCount++;
			}
			if (this.model.isChartFirstVoid()) {
				this.g2d.setColor(this.model.getColor("CHART_FIRST_VOID"));
				str = "" + Primes._().getFirstVoid(y);
				g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*chartCount);
				chartCount++;
			}
			if (this.model.isChartPrimeCountCalc()) {
				this.g2d.setColor(this.model.getColor("CHART_PRIME_COUNT_CALC"));
				str = "" + Primes._().calculatePrimeCount(y);
				g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*chartCount);
				chartCount++;
			}
			if (this.model.isChartMatchCountCalc()) {
				this.g2d.setColor(this.model.getColor("CHART_MATCH_COUNT_CALC"));
				str = "" + Primes._().calculateMatchCount(y);
				g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*chartCount);
				chartCount++;
			}
			if (this.model.isChartDivisorSum()) {
				this.g2d.setColor(this.model.getColor("CHART_DIVISOR_SUM"));
				str = "" + Primes._().getDivisorSum(y, this.model.getDivisorSumExponent());
				g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*chartCount);
				chartCount++;
			}
			if (this.model.isChartEulerTotient()) {
				this.g2d.setColor(this.model.getColor("CHART_EULER_TOTIENT"));
				str = "" + Primes._().getEulerTotient(y);
				g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*chartCount);
				chartCount++;
			}
		}
	}
	
	/**
	 * Draw the rays, i.e. the lines along the factors of order n.
	 * @return A map of points where the rays end, stored by the ray order.
	 */
	private Map<Integer,Point2D> drawRays() {
		int x, y;
		TreeMap<Integer,Point2D> rays = new TreeMap<Integer,Point2D>();
		if (this.model.isRays()) {
			double start_x,start_y,
				from_x,from_y,
				to_x,to_y,
				x_left,x_right,
				y_up,y_down,
				cut_top,cut_bottom;
			for (int yp = 1; yp <= Y_COUNT+Y_POS; ++yp) {
				y = this.transformY(yp);
				for (int xp = 1; xp <= X_COUNT; ++xp) {
					x = this.transformX(xp);
					if (y%x==0) {
						int order = y/x;
						double ratio = ((double)order)*((double)X_STEP/(double)Y_STEP);
						if (rays.get(order) == null || y == MOUSE_Y) {
							if (Primes._().isPrime(x) && y == MOUSE_Y) g2d.setColor(this.getHightlightTextColor());
							else g2d.setColor(this.model.getColor("RAY_BORDER")); 
							x_left = ((double)xp - 0.5)*(double)BLOCK;
							y_up = (x_left*ratio);
							cut_top = y_up - (((double)yp - 0.5)*(double)BLOCK);
							if (cut_top > 0) {
								y_up -= cut_top;
								x_left -= cut_top/ratio;
							}
							x_right = ((double)(X_COUNT - xp) + 0.5)*(double)BLOCK-(double)this.X_RIGHT;
							y_down = (x_right*ratio);
							cut_bottom = y_down - (((double)(Y_COUNT - yp) + 0.5)*(double)BLOCK - this.Y_BOTTOM);
							if (cut_bottom > 0) {
								y_down -= cut_bottom;
								x_right -= cut_bottom/ratio;
							}
							start_x = this.X_LEFT + ((double)xp - 0.5)*(double)BLOCK;
							start_y = this.Y_TOP + ((double)yp - 0.5)*(double)BLOCK;
							from_x = start_x - x_left;
							from_y = start_y - y_up;
							to_x = start_x + x_right;
							to_y = start_y + y_down;
							g2d.drawLine((int)to_x, (int)to_y, (int)from_x, (int)from_y);
							rays.put(order,new Point2D((int)to_x, (int)to_y, Primes._().isPrime(x) && y == MOUSE_Y));
						}
					}
				}
			}
		}
		return rays;
	}
	
	/**
	 * Draw the order of the selected rays.
	 * @param rays
	 */
	private void drawRayOrder(Map<Integer,Point2D> rays) {
		g2d.setColor(this.getHightlightTextColor());
		Iterator<Entry<Integer, Point2D>> iter = rays.entrySet().iterator();
		int last_x=WIDTH+300, px;
		while (iter.hasNext()) {
			Map.Entry<Integer, Point2D> entry = (Map.Entry<Integer, Point2D>)iter.next();
			int ray = (Integer)entry.getKey();
			Point2D point = (Point2D)entry.getValue();
			if (point.b) {
				int addy = 0;
				int strw = (int)this.metric.getWidth(""+ray);
				px = point.x-2;
				if (px+strw >= last_x && point.x < WIDTH-this.X_RIGHT) {
					addy = (int)STRING_HEIGHT;
					g2d.drawLine(point.x, HEIGHT-this.Y_BOTTOM, point.x, HEIGHT-this.Y_BOTTOM+addy-4);
				}
				int py = HEIGHT-this.Y_BOTTOM;
				if (point.y < py) py = point.y-3;
				else py += 13;
				g2d.drawString(""+ray, px, py+addy);
				last_x = px;
			}
		}
	}
	
	/**
	 * Draw the stats at the bottom.
	 */
	private void drawStats() {
		int start_y = HEIGHT - Y_BOTTOM + 40,
			start_x = X_LEFT + 10;
		int row_count = (int)(((double)HEIGHT - (double)start_y)/(STRING_HEIGHT+3.0))+1;
		row_count -= row_count%2;
		Map<Integer,PrimeModel.InfoEntry> infos = this.model.getInfo();
		List<PairMetric> pairs = new ArrayList<PairMetric>();
		Iterator<Entry<Integer, InfoEntry>> iter = infos.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, InfoEntry> entry = (Map.Entry<Integer, InfoEntry>)iter.next();
			PrimeModel.InfoEntry info = (PrimeModel.InfoEntry)entry.getValue(); 
			String key = info.getKey();
			String value = info.getValue();
			pairs.add(new PairMetric(key,value,(int)this.metric.getWidth(key),(int)this.metric.getWidth(value)));
		}
		int column_width = 0, max = 0, temp = 0;
		PairMetric pair;
		g2d.setColor(this.getTextColor());
		for (int i = 0; i < pairs.size(); ++i) {
			if (i%row_count==0) {
				start_x += column_width + 20;
				for (int r = 0; r < row_count; ++r) {
					if (i+r >= pairs.size()) break;
					pair = pairs.get(i+r);
					temp = (int)this.metric.getWidth(pair.getKey()) + 10 + (int)this.metric.getWidth(pair.getValue());
					if (temp > max) max = temp;
				}
				column_width = max;
			}
			pair = pairs.get(i);
			g2d.setColor(this.getTextColor());
			g2d.drawString(pair.getKey(), start_x, start_y + ((int)STRING_HEIGHT+3)*(i%row_count));
			g2d.setColor(this.getHightlightTextColor());
			g2d.drawString(pair.getValue(), start_x+column_width-pair.value_width, start_y + ((int)STRING_HEIGHT+3)*(i%row_count));
		}
	}
	
	/**
	 * Draw a composite as as product of primes, e.g. 12 = 2^2 * 3^1.
	 * @param product
	 * @param x
	 * @param y
	 * @return The new position.
	 */
	private int drawPrimeProduct(Map<Integer,Integer> product, int x, int y) {
		if (product == null) return 0;
		int cur_x = x,
		prime, exp;
		Iterator<Entry<Integer, Integer>> iter = product.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>)iter.next();
			prime = (Integer)entry.getKey();
			exp = (Integer)entry.getValue();
			this.g2d.drawString(""+prime, cur_x, y);
			cur_x += this.metric.getWidth(""+prime);
			this.changeFont(SMALL_FONT);
			this.g2d.drawString(""+exp, cur_x, y-8);
			cur_x += this.metric.getWidth(""+exp);
			this.changeFont(FONT);
		}
		return cur_x-x;
	}
	
	/**
	 * Draw a box at the specified xy-position with the given width and height.
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	private void drawBox(int x, int y, int width, int height) {
		if (this.model.isDrawRect() || (width == 1 && height == 1)) this.g2d.fillRect(x, y, width, height);
		else this.g2d.fillOval(x, y, width, height);
	}
	
	/**
	 * Draw all recognized polynomials within the view rect.
	 */
	private void drawPolys() {
		if (!this.model.isPolynomials() || this.model.isCheckedPattern() || this.model.getVerticalStep() > 1 || this.model.getHorizontalStep() > 1) return;
		if (this.calc_poly) this.calcPoly();
		g2d.setColor(this.model.getColor("POLY_COLOR"));
		int c = this.POLY_FACTOR;
		int start_k = 2;
		if (this.X_POS > 2) start_k = this.X_POS;
		for (int j = 2 - this.POLY_FACTOR; j <= 1; ++j) {
			for (int i = this.POLY_I; i <= this.POLY_N; ++i) {
				int end_k = this.X_POS + this.X_COUNT;
				if (end_k > i - 1) end_k = i - 1;
				int last_x = -1, last_y = -1, cur_x = -1, cur_y = -1;
				for (int k = start_k; k <= end_k; ++k) {
					cur_x = k;
					cur_y = k * (c * (i - k) + j);
					if (last_x < 0) last_x = cur_x;
					if (last_y < 0) last_y = cur_y;
					g2d.drawLine((int)((last_x-this.X_POS-0.5)*BLOCK+X_LEFT), (int)((last_y-this.Y_POS-0.5)*BLOCK+Y_TOP), (int)((cur_x-this.X_POS-0.5)*BLOCK+X_LEFT), (int)((cur_y-this.Y_POS-0.5)*BLOCK+Y_TOP));
					last_x = cur_x;
					last_y = cur_y;
				}
			}
		}
	}
	
	/**
	 * Calculate the parameter for the polynomial function.
	 */
	private void calcPoly() {
		if (this.testPoly() == false) this.searchPoly();
		this.expandPoly();
	}
	
	private boolean testPoly() {
		int c = this.POLY_FACTOR;
		int start_k = 2;
		if (this.X_POS > 2) start_k = this.X_POS;
		for (int j = 2 - this.POLY_FACTOR; j <= 1; ++j) {
			for (int i = this.POLY_I; i <= this.POLY_N; ++i) {
				int end_k = this.X_POS + this.X_COUNT;
				if (end_k > i - 1) end_k = i - 1;
				int cur_x = -1, cur_y = -1;
				for (int k = start_k; k <= end_k; ++k) {
					cur_x = k;
					cur_y = k * (c * (i - k) + j);
					if (cur_x >= this.X_POS && cur_x < this.X_POS + this.X_COUNT && cur_y >= this.Y_POS && cur_y < this.Y_POS + this.Y_COUNT) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean searchPoly() {
		this.POLY_I = this.POLY_N = 3;
		int left_bottom = this.Y_POS + this.Y_COUNT;
		if (left_bottom <= this.X_POS*2) return false;
		while (true) {
			if (this.testPoly()) return true;
			this.POLY_I = this.POLY_N = this.POLY_N + 1;
		}
	}
	
	private void expandPoly() {
		boolean i_found = false;
		int start_i = this.POLY_I;
		do {
			int c = this.POLY_FACTOR;
			int start_k = 2;
			if (this.X_POS > 2) start_k = this.X_POS;
			boolean j_found = false;
			j_loop: for (int j = 2 - this.POLY_FACTOR; j <= 1; ++j) {
				int i = start_i;
				int end_k = this.X_POS + this.X_COUNT;
				if (end_k > i - 1) end_k = i - 1;
				int cur_x = -1, cur_y = -1;
				for (int k = start_k; k <= end_k; ++k) {
					cur_x = k;
					cur_y = k * (c * (i - k) + j);
					if (cur_x >= this.X_POS && cur_x < this.X_POS + this.X_COUNT && cur_y >= this.Y_POS && cur_y < this.Y_POS + this.Y_COUNT) {
						j_found = true;
						break j_loop;
					}
				}
			}
			if (j_found) {
				i_found = true;
				this.POLY_I = start_i;
			} else break;
			--start_i;
		} while (true);
		if (i_found == false) {
			do {
				int c = this.POLY_FACTOR;
				int start_k = 2;
				if (this.X_POS > 2) start_k = this.X_POS;
				boolean j_found = false;
				j_loop: for (int j = 2 - this.POLY_FACTOR; j <= 1; ++j) {
					int i = start_i;
					int end_k = this.X_POS + this.X_COUNT;
					if (end_k > i - 1) end_k = i - 1;
					int cur_x = -1, cur_y = -1;
					for (int k = start_k; k <= end_k; ++k) {
						cur_x = k;
						cur_y = k * (c * (i - k) + j);
						if (cur_x >= this.X_POS && cur_x < this.X_POS + this.X_COUNT && cur_y >= this.Y_POS && cur_y < this.Y_POS + this.Y_COUNT) {
							j_found = true;
							break j_loop;
						}
					}
				}
				if (j_found) {
					i_found = true;
					this.POLY_I = start_i;
					break;
				}
				++start_i;
			} while (true);
		}
		this.POLY_N = this.POLY_I;
		int start_n = this.POLY_N;
		do {
			int c = this.POLY_FACTOR;
			int start_k = 2;
			if (this.X_POS > 2) start_k = this.X_POS;
			boolean j_found = false;
			j_loop: for (int j = 2 - this.POLY_FACTOR; j <= 1; ++j) {
				int i = start_n;
				int end_k = this.X_POS + this.X_COUNT;
				if (end_k > i - 1) end_k = i - 1;
				int cur_x = -1, cur_y = -1;
				for (int k = start_k; k <= end_k; ++k) {
					cur_x = k;
					cur_y = k * (c * (i - k) + j);
					if (cur_x >= this.X_POS && cur_x < this.X_POS + this.X_COUNT && cur_y >= this.Y_POS && cur_y < this.Y_POS + this.Y_COUNT) {
						j_found = true;
						break j_loop;
					}
				}
			}
			if (j_found) this.POLY_N = start_n;
			else break;
			++start_n;
		} while (true);
	}
	
	/**
	 * if (!this.model.isPolynomials() || this.model.isCheckedPattern() || this.model.getVerticalStep() > 1 || this.model.getHorizontalStep() > 1) return;
		int x, y, y_sub=0, step=0, base=0, cur_x=0, cur_y=0, last_x=0, last_y=0, polySize=this.model.getPolySize();
		g2d.setColor(this.model.getColor("POLY_COLOR"));
		int max_y = (polySize*polySize)*this.model.getPolyDelta()*this.model.getPolyFactor();
		column: for (int yp = 1; yp <= Y_COUNT+max_y; ++yp) {
			y = this.transformY(yp);
			row: for (int xp = -polySize*this.model.getPolyDelta(); xp <= X_COUNT+polySize*this.model.getPolyDelta(); ++xp) {
				if ((xp-1)*BLOCK+X_LEFT > WIDTH-X_RIGHT) continue row;
				x = this.transformX(xp);
				Primes.Polynomial poly = Primes._().getPoly(x, y, this.model.getPolyDelta(),this.model.getPolyFactor());
				if (poly != null && poly.getStep() == this.model.getPolyDelta() && poly.getFactor() == this.model.getPolyFactor()) {
					if (x==this.MOUSE_X && y==this.MOUSE_Y) g2d.setColor(this.model.getColor("RAY_BORDER"));
					else g2d.setColor(this.model.getColor("POLY_COLOR"));
					base = step = y_sub = last_x = last_y = 0;
					do_poly: do {
						++base;
						step += poly.getStep();
						y_sub = base*base*poly.getStep()*poly.getFactor();
						if (y_sub==0) break do_poly;
						cur_x = step;
						cur_y = -y_sub;
						//if ((double)(y+cur_y)/(double)(x+cur_x) < 2.0) break do_poly;
						if (base <= polySize) {
							g2d.drawLine((int)((xp+last_x-0.5)*BLOCK+X_LEFT), (int)((yp+last_y-0.5)*BLOCK+Y_TOP), (int)((xp+cur_x-0.5)*BLOCK+X_LEFT), (int)((yp+cur_y-0.5)*BLOCK+Y_TOP));
							g2d.drawLine((int)((xp-last_x-0.5)*BLOCK+X_LEFT), (int)((yp+last_y-0.5)*BLOCK+Y_TOP), (int)((xp-cur_x-0.5)*BLOCK+X_LEFT), (int)((yp+cur_y-0.5)*BLOCK+Y_TOP));
						} else break do_poly;
						last_x=cur_x;
						last_y=cur_y;
					} while (y_sub <= yp);
				}
			}
		}
	 */
	
	/**
	 * Draw the polar polynomials at the top of the view.
	 */
	private void drawPolarPolys() {
		double mid, low, half, last_x, last_y, cur_x, cur_y, bal, shift, nolap, onlyOuter;
		shift = (double)X_POS;
		for (int i = 2; i <= this.POLAR_FACTOR_X/2; ++i) {
			mid = ((double)i+2.0)/2.0;
			low =  mid*mid;
			half = mid % 1.0;
			last_x = last_y = cur_x = cur_y = 0.0;
			bal = this.model.isPolarBalance() ? (double)POLAR_FACTOR_X/4.0 - ((double)i*0.5) : 0.0;
			nolap = !this.model.isPolarOverlap() && this.model.isPolarBalance() ? ((double)POLAR_FACTOR_X/2.0-4.0)/2.0*POLAR_FACTOR_Y : 0.0;
			onlyOuter = this.model.isFactorOnlyOuter() && this.model.isPolarBalance() ? -(((double)POLAR_FACTOR_X-4.0)*POLAR_FACTOR_Y/4.0) : 0.0;
			for (int b = 0; b < (i-2)/2+1; ++b) {
				cur_x = ((double)b+half)*((double)b+half);
				cur_y = (b+half);
				g2d.setColor(this.model.getColor("POLAR_FACTOR_LEFT"));
				g2d.drawLine((int)((low-cur_x-0.5-shift)*BLOCK)+X_LEFT,
							 (int)((mid+cur_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK+(onlyOuter)*BLOCK)+Y_TOP,
							 (int)((low-last_x-0.5-shift)*BLOCK)+X_LEFT,
							 (int)((mid+last_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK+(onlyOuter)*BLOCK)+Y_TOP);
				if (this.model.isFactorOnlyOuter() == false || this.model.isPolarBalance() == false) g2d.drawLine((int)((low-cur_x-0.5-shift)*BLOCK)+X_LEFT,
						 (int)((mid-cur_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK+(onlyOuter)*BLOCK)+Y_TOP,
						 (int)((low-last_x-0.5-shift)*BLOCK)+X_LEFT,
						 (int)((mid-last_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK+(onlyOuter)*BLOCK)+Y_TOP);
				if (this.model.isPolarBalance()) {
					g2d.drawOval((int)((low-cur_x-0.5-shift)*BLOCK-0.5*BLOCK)+X_LEFT,
							 	 (int)((mid+cur_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK-0.5*BLOCK+(onlyOuter)*BLOCK)+Y_TOP, BLOCK, BLOCK);
					if (this.model.isFactorOnlyOuter() == false) g2d.drawOval((int)((low-cur_x-0.5-shift)*BLOCK-0.5*BLOCK)+X_LEFT,
							 	 (int)((mid-cur_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK-0.5*BLOCK+(onlyOuter)*BLOCK)+Y_TOP, BLOCK, BLOCK);
				}

				g2d.setColor(this.model.getColor("POLAR_FACTOR_RIGHT"));
				if (this.model.isFactorOnlyOuter() == false || this.model.isPolarBalance() == false) g2d.drawLine((int)(((POLAR_FACTOR_X-1.0-shift)-(low-cur_x-0.5))*BLOCK)+X_LEFT,
							 (int)((mid+cur_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK+(nolap+onlyOuter)*BLOCK)+Y_TOP,
							 (int)(((POLAR_FACTOR_X-1.0-shift)-(low-last_x-0.5))*BLOCK)+X_LEFT,
							 (int)((mid+last_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK+(nolap+onlyOuter)*BLOCK)+Y_TOP);
				g2d.drawLine((int)(((POLAR_FACTOR_X-1.0-shift)-(low-cur_x-0.5))*BLOCK)+X_LEFT,
						 	 (int)((mid-cur_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK+(nolap+onlyOuter)*BLOCK)+Y_TOP,
						 	 (int)(((POLAR_FACTOR_X-1.0-shift)-(low-last_x-0.5))*BLOCK)+X_LEFT,
						 	 (int)((mid-last_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK+(nolap+onlyOuter)*BLOCK)+Y_TOP);
				if (this.model.isPolarBalance()) {
					if (this.model.isFactorOnlyOuter() == false) g2d.drawOval((int)(((POLAR_FACTOR_X-1.0-shift)-(low-cur_x-0.5))*BLOCK-0.5*BLOCK)+X_LEFT,
							 	 (int)((mid+cur_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK-0.5*BLOCK+(nolap+onlyOuter)*BLOCK)+Y_TOP, BLOCK, BLOCK);
					g2d.drawOval((int)(((POLAR_FACTOR_X-1.0-shift)-(low-cur_x-0.5))*BLOCK-0.5*BLOCK)+X_LEFT,
						 	 	 (int)((mid-cur_y-0.5+bal)*POLAR_FACTOR_Y*BLOCK-0.5*(POLAR_FACTOR_Y-1.0)*BLOCK-0.5*BLOCK+(nolap+onlyOuter)*BLOCK)+Y_TOP, BLOCK, BLOCK);
				}
				last_x = cur_x;
				last_y = cur_y;
			}
		}
	}
	
	/**
	 * Draw highly composite numbers.
	 */
	private void drawHCN() {
		int y;
		for (int yp = 1; yp <= Y_COUNT; ++yp) {
			y = this.transformY(yp);
			if (Primes._().isHCN(y)) {
				g2d.setColor(this.getHightlightTextColor());
				g2d.drawLine(X_LEFT, (int)(((double)yp-0.5)*BLOCK)+Y_TOP, WIDTH-X_RIGHT, (int)(((double)yp-0.5)*BLOCK)+Y_TOP);
			}
		}
	}
	
	/**
	 * Change the font.
	 * @param font
	 */
	private void changeFont(Font font) {
		this.g2d.setFont(font);
	}
	
	/**
	 * @param x
	 * @param y
	 * @return The text at the position (x|y).
	 */
	private String getText(int x, int y) {
		if (Primes._().isPrime(x)) {
			int exp = Primes._().getExponent(x, y);
			if (exp > 0) return ""+exp;
		}
		return null;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param ct
	 * @return The color at the position (x|y).
	 */
	private Color getColor(int x, int y, ColorType ct) {
		if (this.model.isPrimeMirror() && MOUSE_Y == y) {
			if (Primes._().isPrime(MOUSE_Y*2-x) && ct == ColorType.BOX) {
				return this.model.getColor("PRIME_MIRROR");
			}
		}
		if (this.model.isHelper() && (x == this.model.getMouseX() || y == this.model.getMouseY() || y == this.model.getMouseX() || x == this.model.getMouseY()
			|| this.isOnLine(x,y,this.model.getMouseX()) || this.isOnLine(x,y,this.model.getMouseY()))
			|| (this.model.isPolarFactors() && y == POLAR_FACTOR_X/2)) {
			switch (ct) {
			case BORDER:
				if (y%x==0 && this.model.isFactors()) return this.model.getColor("RAY_BORDER");
				else return this.model.getColor("HELPER_BORDER");
			case BOX:
				if (Primes._().isMatch(x, y)) return this.model.getColor("HELPER_BOX_LIGHT");
				else if (Primes._().getExponent(x, y)>0) return this.model.getColor("EXP_BOX");
				else return this.model.getColor("HELPER_BOX");
			case TEXT: return this.model.getColor("HELPER_TEXT");
			}
		}
		if (Primes._().isPrime(x)) {
			if (this.model.isPrimes() && Primes._().isMatch(x, y)) {
				switch (ct) {
				case BORDER: return this.model.getColor("MATCH_BORDER");
				case BOX: return this.model.getColor("MATCH_BOX");
				case TEXT: return this.model.getColor("MATCH_TEXT");
				}
			} else {
				if (this.model.isChartExpSum() && Primes._().getExponent(x, y)>0) {
					switch (ct) {
					case BORDER: return this.model.getColor("EXP_BORDER");
					case BOX: return this.model.getColor("EXP_BOX");
					case TEXT: return this.model.getColor("EXP_TEXT");
					}
				}
				if (!(x == 2 && y == 1)) {
					if (this.model.isPrimes()) {
						switch (ct) {
						case BORDER: return this.model.getColor("PRIME_BORDER");
						case BOX: return this.model.getColor("PRIME_BOX");
						case TEXT: return this.model.getColor("PRIME_TEXT");
						}
					} else {
						if (x%2==0) {
							switch (ct) {
							case BORDER:
								if (y%x==0 && this.model.isFactors()) return this.model.getColor("RAY_BORDER");
								else return this.model.getColor("EVEN_BORDER");
							case BOX: return this.model.getColor("EVEN_BOX");
							case TEXT: return this.model.getColor("EVEN_TEXT");
							}
						} else {
							switch (ct) {
							case BORDER:
								if (y%x==0 && this.model.isFactors()) return this.model.getColor("RAY_BORDER");
								else return this.model.getColor("ODD_BORDER");
							case BOX: return this.model.getColor("ODD_BOX");
							case TEXT: return this.model.getColor("ODD_TEXT");
							}
						}
					}
				}
			}
		}
		if (x==y || x == 2*y) {
			switch (ct) {
			case BORDER: return this.model.getColor("AX_BORDER");
			case BOX: return this.model.getColor("AX_BOX");
			case TEXT: return this.model.getColor("AX_TEXT");
			}
		}
		if (x%2==0) {
			switch (ct) {
			case BORDER:
				if (y%x==0 && this.model.isFactors()) return this.model.getColor("RAY_BORDER");
				else return this.model.getColor("EVEN_BORDER");
			case BOX: return this.model.getColor("EVEN_BOX");
			case TEXT: return this.model.getColor("EVEN_TEXT");
			}
		} else {
			switch (ct) {
			case BORDER:
				if (y%x==0 && this.model.isFactors()) return this.model.getColor("RAY_BORDER");
				else return this.model.getColor("ODD_BORDER");
			case BOX: return this.model.getColor("ODD_BOX");
			case TEXT: return this.model.getColor("ODD_TEXT");
			}
		}
		return null;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param fix
	 * @return <code>true</code> if the point is on a helper line.
	 */
	private boolean isOnLine(int x, int y, int fix) {
		if (x%2==0 && fix%2==1 || x%2==1 && fix%2==0) return false;
		int x_d = x-fix;
		int y_d = y-fix;
		if (x_d/2==y_d) return true;
		return false;
	}
	
	/**
	 * @return The background color of the view.
	 */
	private Color getBackgroundColor() {
		return this.model.getColor("BACKGROUND");
	}
	
	/**
	 * @return The text color of the view.
	 */
	private Color getTextColor() {
		return this.model.getColor("TEXT_COLOR");
	}
	
	/**
	 * @return The highlight text color of the view.
	 */
	private Color getHightlightTextColor() {
		return this.model.getColor("HIGHLIGHT_TEXT_COLOR");
	}
	
	/**
	 * @param x
	 * @return The transformed x postion of the mouse pointer.
	 */
	public int transformMouseX(int x) {
		return this.transform(x, 0);
	}

	/**
	 * @param y
	 * @return The transformed y postion of the mouse pointer.
	 */
	public int transformMouseY(int y) {
		return this.transform(y, 1);
	}
	
	/**
	 * @param c
	 * @param i 
	 * @return The transformed coordinate.
	 */
	private int transform(int c, int i) {
		double temp = 0.0;
		if (i == 0) {
			temp = (double)c - (double)this.X_LEFT;
			temp /= this.model.getBlockSize();
			return this.transformX((int)temp+1);
		} else {
			temp = (double)c - (double)this.Y_TOP;
			temp /= this.model.getBlockSize();
			return this.transformY((int)temp+1);
		}
	}
	
	/**
	 * @param x
	 * @return The transformed x coordinate.
	 */
	private int transformX(int x) {
		x += this.model.getXPos();
		x *= this.model.getHorizontalStep();
		x += this.model.getHorizontalOffset();
		x -= this.model.getHorizontalStep()-1;
		return x;
	}
	
	/**
	 * @param y
	 * @return The transformed y coordinate.
	 */
	private int transformY(int y) {
		y += this.model.getYPos();
		y *= this.model.getVerticalStep();
		y += this.model.getVerticalOffset();
		y -= this.model.getVerticalStep()-1;
		return y;
	}
	
	/**
	 * Add a listener for key events in the typing area.
	 */
	public void addKeyListener(KeyListener keyl) {
		this.typingArea.addKeyListener(keyl);
	}
	
	/**
	 * Add a listener for window events of the views frame.
	 * @param wl
	 */
	public void addWindowListener(WindowListener wl) {
		this.viewFrame.addWindowListener(wl);
	}

}
