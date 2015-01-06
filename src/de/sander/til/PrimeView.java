package de.sander.til;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

public class PrimeView extends JPanel {
	
	private enum ColorType {
		BORDER,
		BOX,
		TEXT
	}
	
	private enum ChartType {
		PRIME,
		MATCH,
		EXPONENT
	}
	
	class Point2D {
		
		public int x=0,y=0;
		public boolean b=false;
		
		public Point2D(int px, int py, boolean pb) {
			this.x = px;
			this.y = py;
			this.b = pb;
		}
	}
	
	class PairMetric {
		
		private String key, value;
		private int key_width, value_width;
		
		public PairMetric(String k, String v, int kw, int vw) {
			this.key = k;
			this.value = v;
			this.key_width = kw;
			this.value_width = vw;
			while (this.key.startsWith("_")) this.key = this.key.substring(1);
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
	private Color BACKGROUND=null,
			TEXT_COLOR=null,
			HIGHLIGHT_TEXT_COLOR=null,
			LIGHT_WHITE=null,
			PRIME_BOX=null,
			PRIME_BORDER=null,
			PRIME_TEXT=null,
			EXP_BOX=null,
			EXP_BORDER=null,
			EXP_TEXT=null,
			MATCH_BORDER=null,
			MATCH_BOX=null,
			MATCH_TEXT=null,
			EVEN_BOX=null,
			EVEN_BORDER=null,
			EVEN_TEXT=null,
			ODD_BOX=null,
			ODD_BORDER=null,
			ODD_TEXT=null,
			AX_BOX=null,
			AX_BORDER=null,
			AX_TEXT=null,
			HELPER_BOX=null,
			HELPER_BOX_LIGHT=null,
			HELPER_BORDER=null,
			HELPER_TEXT=null,
			RAY_BORDER=null,
			CHART_PRIME=null,
			CHART_EXP=null,
			CHART_MATCH=null;
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
			Y_AXIS_DELTA=0;
	double STRING_HEIGHT=0.0;
	private StringMetrics metric;
	private Font FONT, SMALL_FONT;
	private Graphics2D g2d=null;
	
	public PrimeView(PrimeModel mod) {
		this.setModel(mod);
		this.BACKGROUND = new Color(0,0,0);
		this.TEXT_COLOR = new Color(255,255,255);
		this.HIGHLIGHT_TEXT_COLOR = new Color(255,190,55);
		this.LIGHT_WHITE = new Color(255,255,255,150);
		this.PRIME_BOX = new Color(180,230,255);
		this.PRIME_BORDER = new Color(140,190,235);
		this.PRIME_TEXT = new Color(100,150,225);
		this.EXP_BOX = new Color(185,45,90);
		this.EXP_BORDER = new Color(125,65,10);
		this.EXP_TEXT = new Color(255,255,255);
		this.MATCH_BORDER = new Color(80,130,205);
		this.MATCH_BOX = new Color(60,75,215);
		this.MATCH_TEXT = new Color(180,230,255);
		this.ODD_BOX = new Color(25,25,25);
		this.ODD_BORDER = new Color(10,10,10);
		this.ODD_TEXT = null;
		this.EVEN_BOX = null;
		this.EVEN_BORDER = new Color(25,25,25);
		this.EVEN_TEXT = null;
		this.AX_BOX = new Color(160,225,140);
		this.AX_BORDER = new Color(140,235,190);
		this.AX_TEXT = new Color(100,225,150);
		this.HELPER_BOX = new Color(55,55,55);
		this.HELPER_BOX_LIGHT = new Color(255,205,100);
		this.HELPER_BORDER = new Color(35,35,35);
		this.HELPER_TEXT = new Color(255,255,255);
		this.RAY_BORDER = new Color(255,155,155,150);
		this.CHART_PRIME = new Color(160,225,140);
		this.CHART_MATCH = new Color(255,190,55);
		this.CHART_EXP = new Color(185,45,90);
	}
	
	public PrimeModel getModel() {
		return model;
	}

	public void setModel(PrimeModel model) {
		this.model = model;
	}

	public void draw() {
		this.setModel(model);
		this.repaint();
	}
	
	public void initMetric(Graphics2D g2d) {
		if (this.metric == null) {
			this.FONT = g2d.getFont();
			this.SMALL_FONT = FONT.deriveFont(9.0f);
			this.metric = new StringMetrics(g2d);
		}
	}

	public void paintComponent(Graphics g) {
		this.g2d = (Graphics2D) g;
		this.initMetric(g2d);
		// initialize all environment variables
		this.setupEnv();
		// draw view
		this.drawBackground();
		this.drawAxis();
		this.drawField();
		Map<Integer,Point2D> rays = this.drawRays();
		this.drawText();
		this.drawBottomRight();
		this.drawRayOrder(rays);
		if (this.model.isChart()) {
			this.drawChart(ChartType.PRIME);
			this.drawChart(ChartType.EXPONENT);
			this.drawChart(ChartType.MATCH);
			this.drawChartDesc();
		}
		if (this.model.isStats()) this.drawStats();
		
	}
	
	private void setupEnv() {
		this.WIDTH = this.getWidth();
		this.HEIGHT = this.getHeight();
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
		if (this.model.isStats()) Y_BOTTOM=150;
		else Y_BOTTOM=30;
		if (X_RIGHT > WIDTH - X_LEFT) X_RIGHT = WIDTH - X_LEFT;
		STRING_HEIGHT = this.metric.getHeight("1");
		this.Y_AXIS_DELTA = (int)(STRING_HEIGHT/(double)BLOCK);
		if (Y_AXIS_DELTA <= 0) Y_AXIS_DELTA = 1;
		
		{
			double xc = ((double)WIDTH-this.X_LEFT)/((double)BLOCK);
			this.X_COUNT = (int)Math.ceil(xc);
			double yc = ((double)HEIGHT-this.Y_TOP)/((double)BLOCK);
			this.Y_COUNT = (int)Math.ceil(yc);
		}
		
		this.X_LEFT = (int)this.metric.getWidth(""+(this.transformY(Y_COUNT))) + 9;
	}
	
	private void drawBackground() {
		// clear background
		g2d.setBackground(this.getBackgroundColor());
		g2d.clearRect(0, 0, WIDTH, HEIGHT);
		// draw view rect
		g2d.setColor(this.getTextColor());
		g2d.drawLine(X_LEFT-1, Y_TOP-1, X_LEFT-1, HEIGHT-1);
		g2d.drawLine(X_LEFT-1, Y_TOP-1, WIDTH-1, Y_TOP-1);
	}
	
	private void drawBottomRight() {
		// right-bottom-border
		g2d.setColor(this.getBackgroundColor());
		g2d.fillRect(this.X_LEFT, HEIGHT-this.Y_BOTTOM, WIDTH-this.X_LEFT, this.Y_BOTTOM);
		g2d.fillRect(WIDTH-this.X_RIGHT,this.Y_TOP,this.X_RIGHT,HEIGHT-this.Y_BOTTOM);
		g2d.setColor(this.getTextColor());
		g2d.drawLine(this.X_LEFT, HEIGHT-this.Y_BOTTOM, WIDTH-this.X_RIGHT, HEIGHT-this.Y_BOTTOM);
		g2d.drawLine(WIDTH-this.X_RIGHT,this.Y_TOP,WIDTH-this.X_RIGHT,HEIGHT-this.Y_BOTTOM);
	}
	
	private void drawAxis() {
		int x, y;
		g2d.setColor(this.getTextColor());
		// y-achsis
		for (int yp = 1; yp <= Y_COUNT; ++yp) {
			y = this.transformY(yp);
			// delta logic
			if ((y%Y_AXIS_DELTA!=0 || (y > MOUSE_Y-Y_AXIS_DELTA && y < MOUSE_Y+Y_AXIS_DELTA) || (y > MOUSE_X-Y_AXIS_DELTA && y < MOUSE_X+Y_AXIS_DELTA)) && MOUSE_Y!=y && (MOUSE_X!=y)) continue;
			if (MOUSE_Y==y || MOUSE_X==y) g2d.setColor(this.getHightlightTextColor());
			g2d.drawString(""+y, 2, yp*BLOCK+Y_TOP);
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
	
	private void drawField() {
		int x, y;
		// blocks
		for (int yp = 1; yp <= Y_COUNT; ++yp) {
			y = this.transformY(yp);
			for (int xp = 1; xp <= X_COUNT; ++xp) {
				if ((xp-1)*BLOCK+X_LEFT > WIDTH-X_RIGHT) continue;
				x = this.transformX(xp);
				Color col = this.getColor(x,y,ColorType.BOX);
				if (col != null) {
					g2d.setColor(col);
					this.drawBox((xp-1)*BLOCK+X_LEFT, (yp-1)*BLOCK+Y_TOP, BLOCK, BLOCK);
				}
			}
		}
		// borders
		if (BLOCK >= 3) {
			for (int yp = 1; yp <= Y_COUNT; ++yp) {
				y = this.transformY(yp);
				for (int xp = 1; xp <= X_COUNT; ++xp) {
					if ((xp-1)*BLOCK+X_LEFT > WIDTH-X_RIGHT) continue;
					x = this.transformX(xp);
					Color bor = this.getColor(x,y,ColorType.BORDER);
					if (bor != null) {
						g2d.setColor(bor);
						if (this.model.isDrawRect()) {
							g2d.drawLine((xp-1)*BLOCK+X_LEFT, yp *BLOCK+Y_TOP, xp*BLOCK+X_LEFT, yp*BLOCK+Y_TOP); // bottom
							if (y%x==0 && this.model.isRayBox()) {
								g2d.drawLine((xp-1)*BLOCK+X_LEFT, (yp-1)  *BLOCK+Y_TOP, xp*BLOCK+X_LEFT, (yp-1)*BLOCK+Y_TOP); // top
								g2d.drawLine((xp-1)*BLOCK+X_LEFT, (yp-1)  *BLOCK+Y_TOP, (xp-1)*BLOCK+X_LEFT, yp*BLOCK+Y_TOP); // left
								g2d.drawLine(xp*BLOCK+X_LEFT, (yp-1)  *BLOCK+Y_TOP, xp*BLOCK+X_LEFT, yp*BLOCK+Y_TOP); // right
							}
						} else {
							if (y%x==0 && this.model.isRayBox()) {
								g2d.drawOval((xp-1)*BLOCK+X_LEFT, (yp-1)*BLOCK+Y_TOP, BLOCK, BLOCK);
							}
						}
					}
				}
			}
		}
	}
	
	private void drawText() {
		int x, y;
		// text
		if (STRING_HEIGHT <= BLOCK + 5) {
			for (int yp = 1; yp <= Y_COUNT; ++yp) {
				y = this.transformY(yp);
				for (int xp = 1; xp <= X_COUNT; ++xp) {
					if ((xp-1)*BLOCK+X_LEFT > WIDTH-X_RIGHT) continue;
					x = this.transformX(xp);
					Color tex = this.getColor(x,y,ColorType.TEXT);
					if (tex != null) {
						String lbl = this.getText(x,y);
						if (lbl != null && lbl.equals("") == false) {
							g2d.setColor(tex);
							g2d.drawString(lbl, (xp-1)*BLOCK+X_LEFT+(BLOCK/5), yp*BLOCK+Y_TOP-(BLOCK/7));
						}
					}
				}
			}
		}
	}
	
	private void drawChart(ChartType type) {
		
		switch (type) {
		case PRIME:
			this.g2d.setColor(this.CHART_PRIME);
			break;
		case MATCH:
			this.g2d.setColor(this.CHART_MATCH);
			break;
		case EXPONENT:
			this.g2d.setColor(this.CHART_EXP);
			break;
		default:
			return;
		}
		
		int Y_MAX = this.transformY(Y_COUNT),
			X_CHART_LEFT = WIDTH - X_RIGHT,
			xc_val=0, y=0;
		double chart_px=0.0, cpx_dev=1.0;
		if (this.model.isChartProp()) {
			double pdev = (double)(Primes._().primesUntil(Y_MAX*2));
			double mdev = (double)(Primes._().getMaxMatchCount(Y_MAX*2));
			double edev;
			if (this.model.isChartExpSum()) edev = (double)(Primes._().getMaxExpSum(Y_MAX));
			else edev = (double)(Primes._().getMaxExpCount(Y_MAX));
			cpx_dev = Math.max(pdev, Math.max(mdev, edev));
		} else {
			switch (type) {
			case PRIME:
				cpx_dev = (double)(Primes._().primesUntil(Y_MAX));
				break;
			case MATCH:
				cpx_dev = (double)(Primes._().getMaxMatchCount(Y_MAX*2));
				break;
			case EXPONENT:
				if (this.model.isChartExpSum()) cpx_dev = (double)(Primes._().getMaxExpSum(Y_MAX)); 
				else cpx_dev = (double)(Primes._().getMaxExpCount(Y_MAX));
				break;
			default:
				return;
			}
		}
		chart_px = (double)this.X_RIGHT/(cpx_dev+1.0);
		
		for (int yp = 1; yp <= Y_COUNT; ++yp) {
			y = this.transformY(yp);
			switch (type) {
			case PRIME:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().primesUntil(y)*chart_px);
				break;
			case MATCH:
				xc_val = X_CHART_LEFT + (int)((double)Primes._().getMatchCount(y*2)*chart_px);
				break;
			case EXPONENT:
				if (this.model.isChartExpSum()) xc_val = X_CHART_LEFT + (int)((double)Primes._().getExponentSum(y)*chart_px);
				else xc_val = X_CHART_LEFT + (int)((double)Primes._().getExponentCount(y)*chart_px);
				break;
			}
			g2d.drawLine(xc_val, (yp-1)*BLOCK+Y_TOP, xc_val, yp*BLOCK+Y_TOP);
		}
	}
	
	public void drawChartDesc() {
		
		if (MOUSE_Y > Y_POS) {
			int y = MOUSE_Y;
			int yp = y;
			yp -= this.model.getVerticalOffset();
			yp /= this.model.getVerticalStep();
			yp -= this.model.getYPos();
			String str = "-";
			this.g2d.setColor(this.CHART_EXP);
			if (this.model.isChartExpSum()) str = "" + Primes._().getExponentSum(y);
			else str = "" + Primes._().getExponentCount(y);
			g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK);
			
			this.g2d.setColor(this.CHART_MATCH);
			str = "" + Primes._().getMatchCount(y*2);
			g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2));
			
			this.g2d.setColor(this.CHART_PRIME);
			str = "" + Primes._().primesUntil(y);
			g2d.drawString(str, WIDTH-X_RIGHT+10, yp*BLOCK+((int)STRING_HEIGHT+2)*2);
		}
	}
	
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
							else g2d.setColor(this.RAY_BORDER); 
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
	
	private void drawRayOrder(Map<Integer,Point2D> rays) {
		g2d.setColor(this.getHightlightTextColor());
		Iterator iter = rays.entrySet().iterator();
		int last_x=WIDTH+300, px;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
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
	
	private void drawStats() {
		int start_y = HEIGHT - Y_BOTTOM + 40,
			start_x = X_LEFT + 10;
		int row_count = (int)(((double)HEIGHT - (double)start_y)/(STRING_HEIGHT+3.0))+1;
		row_count -= row_count%2;
		Map<String,String> info = this.model.getInfo();
		List<PairMetric> pairs = new ArrayList<PairMetric>();
		Iterator iter = info.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
			String key = (String)entry.getKey();
			String value = (String)entry.getValue();
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
	
	protected int drawPrimeProduct(Map<Integer,Integer> product, int x, int y) {
		if (product == null) return 0;
		int cur_x = x,
		prime, exp;
		Iterator iter = product.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
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
	
	protected void drawBox(int x, int y, int width, int height) {
		if (this.model.isDrawRect()) this.g2d.fillRect(x, y, width, height);
		else this.g2d.fillOval(x, y, width, height);
	}
	
	protected void changeFont(Font font) {
		this.g2d.setFont(font);
	}
	
	protected String getText(int x, int y) {
		if (Primes._().isPrime(x)) {
			int exp = Primes._().getExponent(x, y);
			if (exp > 0) return ""+exp;
		}
		return null;
	}
	
	protected Color getColor(int x, int y, ColorType ct) {
		if (this.model.isHelper() && (x == this.model.getMouseX() || y == this.model.getMouseY() || y == this.model.getMouseX() || x == this.model.getMouseY()
			|| this.isOnLine(x,y,this.model.getMouseX()) || this.isOnLine(x,y,this.model.getMouseY()))) {
			switch (ct) {
			case BORDER:
				if (y%x==0 && this.model.isRayBox()) return RAY_BORDER;
				else return HELPER_BORDER;
			case BOX:
				if (Primes._().isMatch(x, y*2)) return HELPER_BOX_LIGHT;
				else if (Primes._().getExponent(x, y)>0) return EXP_BOX;
				else return HELPER_BOX;
			case TEXT: return HELPER_TEXT;
			}
		}
		if (Primes._().isPrime(x)) {
			if (this.model.isPrimes() && Primes._().isMatch(x, y*2)) {
				switch (ct) {
				case BORDER: return MATCH_BORDER;
				case BOX: return MATCH_BOX;
				case TEXT: return MATCH_TEXT;
				}
			} else {
				if (Primes._().getExponent(x, y)>0) {
					switch (ct) {
					case BORDER: return EXP_BORDER;
					case BOX: return EXP_BOX;
					case TEXT: return EXP_TEXT;
					}
				}
				if (this.model.isPrimes()) {
					switch (ct) {
					case BORDER: return PRIME_BORDER;
					case BOX: return PRIME_BOX;
					case TEXT: return PRIME_TEXT;
					}
				} else {
					if (x%2==0) {
						switch (ct) {
						case BORDER:
							if (y%x==0 && this.model.isRayBox()) return RAY_BORDER;
							else return EVEN_BORDER;
						case BOX: return EVEN_BOX;
						case TEXT: return EVEN_TEXT;
						}
					} else {
						switch (ct) {
						case BORDER:
							if (y%x==0 && this.model.isRayBox()) return RAY_BORDER;
							else return ODD_BORDER;
						case BOX: return ODD_BOX;
						case TEXT: return ODD_TEXT;
						}
					}
				}
			}
		} else if (x==y || x == 2*y) {
			switch (ct) {
			case BORDER: return AX_BORDER;
			case BOX: return AX_BOX;
			case TEXT: return AX_TEXT;
			}
		} else if (x%2==0) {
			switch (ct) {
			case BORDER:
				if (y%x==0 && this.model.isRayBox()) return RAY_BORDER;
				else return EVEN_BORDER;
			case BOX: return EVEN_BOX;
			case TEXT: return EVEN_TEXT;
			}
		} else {
			switch (ct) {
			case BORDER:
				if (y%x==0 && this.model.isRayBox()) return RAY_BORDER;
				else return ODD_BORDER;
			case BOX: return ODD_BOX;
			case TEXT: return ODD_TEXT;
			}
		}
		return null;
	}
	
	protected boolean isOnLine(int x, int y, int fix) {
		if (x%2==0 && fix%2==1 || x%2==1 && fix%2==0) return false;
		int x_d = x-fix;
		int y_d = y-fix;
		if (x_d/2==y_d) return true;
		return false;
	}
	
	protected Color getBackgroundColor() {
		return this.BACKGROUND;
	}
	
	protected Color getTextColor() {
		return this.TEXT_COLOR;
	}
	
	protected Color getHightlightTextColor() {
		return this.HIGHLIGHT_TEXT_COLOR;
	}
	
	public int transformMouseX(int x) {
		return this.transform(x, 0);
	}

	public int transformMouseY(int y) {
		return this.transform(y, 1);
	}
	
	protected int transform(int c, int i) {
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
	
	protected int transformX(int x) {
		x += this.model.getXPos();
		x *= this.model.getHorizontalStep();
		x += this.model.getHorizontalOffset();
		return x;
	}
	
	protected int transformY(int y) {
		y += this.model.getYPos();
		y *= this.model.getVerticalStep();
		y += this.model.getVerticalOffset();
		return y;
	}

}
