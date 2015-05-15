package de.sander.til;

import java.awt.Color;
import java.util.Observable;
import java.util.TreeMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class stores the state of a PrimeView.
 */
public class PrimeModel extends Observable {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PrimeModel.class.getSimpleName());
	
	/**
	 * A enumeration of control modes (key: +/-).
	 */
	public enum PMMode {
		VERTICAL_STEP,
		HORIZONTAL_STEP,
		VERTICAL_OFFSET,
		HORIZONTAL_OFFSET,
		ZOOM,
		POLY_SIZE,
		POLY_FACTOR,
		POLY_DELTA,
		DIVISOR_EXP
	}
	
	/**
	 * A enumeration of view modes (keys: up/down/left/right).
	 */
	public enum PMView {
		GOLDBACH,
		FACTOR
	}

	public class InfoEntry {
		
		private String key;
		private String val;
		
		public InfoEntry(String key, String val) {
			this.key = key;
			this.val = val;
		}
		
		public String getKey() {
			return this.key;
		}
		
		public String getValue() {
			return this.val;
		}
	}

	private String title=null;
	private int blockSize = 12, xPos = 0, yPos = 0, mouseX = 0, mouseY = 0,
			verticalStep=1, horizontalStep=1, verticalOffset=0, horizontalOffset=0,
			window_width=1200, window_height=800, polySize=5, polyFactor=1, polyDelta=1,
			factorX=30, factorY=1, factorZ=1, divisorSumExponent=1;
	private boolean	drawRect = true, helper = true,
			rays = false, factors = true, chart = true, chartProp = true, chartPrimeCountCalc=false, chartMatchCountCalc=false,
			chartExp = false, chartPrimes = true, chartMatchCount = true, chartFirstMatch=false, chartFirstVoid=false, chartVoidCount=false,
			chartExpSum = true, stats=false, primes=true, _changed=false, polynomials=false, checkedPattern=false, primeMirror=false,
			polarFactors=false, factorOnlyOuter=false, factorOnlyNeeded=false, polarBalance=false, rotateView=false,
			chartDivisorSum=false, chartEulerTotient=false, polarOverlap=false, showHCN=false;
	private PMMode pmmode=PMMode.ZOOM;
	private PMView pmview=PMView.GOLDBACH;
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
			POLY_COLOR=null,
			CHART_PRIME=null,
			CHART_EXP=null,
			CHART_MATCH_COUNT=null,
			CHART_FIRST_MATCH=null,
			CHART_FIRST_VOID=null,
			CHART_VOID_COUNT=null,
			CHART_PRIME_COUNT_CALC=null,
			CHART_MATCH_COUNT_CALC=null,
			CHART_DIVISOR_SUM=null,
			CHART_EULER_TOTIENT=null,
			PRIME_MIRROR=null,
			POLAR_FACTOR_LEFT=null,
			POLAR_FACTOR_RIGHT=null,
			ALPHA=null;
	
	public PrimeModel() {
		this.BACKGROUND = new Color(0,0,0);
		this.TEXT_COLOR = new Color(255,255,255);
		this.HIGHLIGHT_TEXT_COLOR = new Color(255,190,55);
		this.LIGHT_WHITE = new Color(255,255,255,255);
		this.PRIME_BOX = new Color(150,210,255,50);
		this.PRIME_BORDER = new Color(140,190,235,60);
		this.PRIME_TEXT = new Color(100,150,225);
		this.EXP_BOX = new Color(185,45,90);
		this.EXP_BORDER = new Color(125,65,10);
		this.EXP_TEXT = new Color(255,255,255);
		this.MATCH_BORDER = new Color(102,150,255,150);
		this.MATCH_BOX = new Color(110,155,245,120);
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
		this.POLY_COLOR = new Color(155,235,255,250);
		this.CHART_PRIME = new Color(160,225,140);
		this.CHART_MATCH_COUNT = new Color(255,190,55);
		this.CHART_EXP = new Color(185,45,90);
		this.CHART_FIRST_MATCH = new Color(195,45,180);
		this.CHART_FIRST_VOID = new Color(75,135,250);
		this.CHART_VOID_COUNT = new Color(205,95,130);
		this.CHART_PRIME_COUNT_CALC = new Color(225,145,90);
		this.CHART_MATCH_COUNT_CALC = new Color(85,185,190);
		this.CHART_DIVISOR_SUM = new Color(140,205,190);
		this.CHART_EULER_TOTIENT = new Color(255,120,100);
		this.PRIME_MIRROR = new Color(255,235,235);
		this.POLAR_FACTOR_LEFT = Color.RED;
		this.POLAR_FACTOR_RIGHT = Color.GREEN;
		this.ALPHA = new Color(0,0,0,0);
		this.title = "PrimeExplorer";
	}
	
	/**
	 * 
	 * @return the title of the model
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * set the models title
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
		this.changed();
	}
	
	/**
	 * 
	 * @param cname a color name
	 * @return the color specified by cname
	 */
	public Color getColor(String cname) {
		if (cname == null) return this.ALPHA;
		if (cname.equals("BACKGROUND")) return this.BACKGROUND;
		if (cname.equals("TEXT_COLOR")) return this.TEXT_COLOR;
		if (cname.equals("HIGHLIGHT_TEXT_COLOR")) return this.HIGHLIGHT_TEXT_COLOR;
		if (cname.equals("LIGHT_WHITE")) return this.LIGHT_WHITE;
		if (cname.equals("PRIME_BOX")) return this.PRIME_BOX;
		if (cname.equals("PRIME_BORDER")) return this.PRIME_BORDER;
		if (cname.equals("PRIME_TEXT")) return this.PRIME_TEXT;
		if (cname.equals("EXP_BOX")) return this.EXP_BOX;
		if (cname.equals("EXP_BORDER")) return this.EXP_BORDER;
		if (cname.equals("EXP_TEXT")) return this.EXP_TEXT;
		if (cname.equals("MATCH_BORDER")) return this.MATCH_BORDER;
		if (cname.equals("MATCH_BOX")) return this.MATCH_BOX;
		if (cname.equals("MATCH_TEXT")) return this.MATCH_TEXT;
		if (cname.equals("ODD_BOX")) return this.ODD_BOX;
		if (cname.equals("ODD_BORDER")) return this.ODD_BORDER;
		if (cname.equals("ODD_TEXT")) return this.ODD_TEXT;
		if (cname.equals("EVEN_BOX")) return this.EVEN_BOX;
		if (cname.equals("EVEN_BORDER")) return this.EVEN_BORDER;
		if (cname.equals("EVEN_TEXT")) return this.EVEN_TEXT;
		if (cname.equals("AX_BOX")) return this.AX_BOX;
		if (cname.equals("AX_BORDER")) return this.AX_BORDER;
		if (cname.equals("AX_TEXT")) return this.AX_TEXT;
		if (cname.equals("HELPER_BOX")) return this.HELPER_BOX;
		if (cname.equals("HELPER_BOX_LIGHT")) return this.HELPER_BOX_LIGHT;
		if (cname.equals("HELPER_BORDER")) return this.HELPER_BORDER;
		if (cname.equals("HELPER_TEXT")) return this.HELPER_TEXT;
		if (cname.equals("RAY_BORDER")) return this.RAY_BORDER;
		if (cname.equals("POLY_COLOR")) return this.POLY_COLOR;
		if (cname.equals("CHART_PRIME")) return this.CHART_PRIME;
		if (cname.equals("CHART_MATCH_COUNT")) return this.CHART_MATCH_COUNT;
		if (cname.equals("CHART_EXP")) return this.CHART_EXP;
		if (cname.equals("CHART_FIRST_MATCH")) return this.CHART_FIRST_MATCH;
		if (cname.equals("CHART_FIRST_VOID")) return this.CHART_FIRST_VOID;
		if (cname.equals("CHART_VOID_COUNT")) return this.CHART_VOID_COUNT;
		if (cname.equals("CHART_PRIME_COUNT_CALC")) return this.CHART_PRIME_COUNT_CALC;
		if (cname.equals("CHART_MATCH_COUNT_CALC")) return this.CHART_MATCH_COUNT_CALC;
		if (cname.equals("CHART_DIVISOR_SUM")) return this.CHART_DIVISOR_SUM;
		if (cname.equals("CHART_EULER_TOTIENT")) return this.CHART_EULER_TOTIENT;
		if (cname.equals("PRIME_MIRROR")) return this.PRIME_MIRROR;
		if (cname.equals("POLAR_FACTOR_LEFT")) return this.POLAR_FACTOR_LEFT;
		if (cname.equals("POLAR_FACTOR_RIGHT")) return this.POLAR_FACTOR_RIGHT;
		return this.ALPHA;
	}
	
	/**
	 * set the color with the name cnam
	 * @param cname
	 * @param color
	 */
	public void setColor(String cname, Color color) {
		if (color == null) return;
		if (cname.equals("BACKGROUND")) this.BACKGROUND = color;
		if (cname.equals("TEXT_COLOR")) this.TEXT_COLOR = color;
		if (cname.equals("HIGHLIGHT_TEXT_COLOR")) this.HIGHLIGHT_TEXT_COLOR = color;
		if (cname.equals("LIGHT_WHITE")) this.LIGHT_WHITE = color;
		if (cname.equals("PRIME_BOX")) this.PRIME_BOX = color;
		if (cname.equals("PRIME_BORDER")) this.PRIME_BORDER = color;
		if (cname.equals("PRIME_TEXT")) this.PRIME_TEXT = color;
		if (cname.equals("EXP_BOX")) this.EXP_BOX = color;
		if (cname.equals("EXP_BORDER")) this.EXP_BORDER = color;
		if (cname.equals("EXP_TEXT")) this.EXP_TEXT = color;
		if (cname.equals("MATCH_BORDER")) this.MATCH_BORDER = color;
		if (cname.equals("MATCH_BOX")) this.MATCH_BOX = color;
		if (cname.equals("MATCH_TEXT")) this.MATCH_TEXT = color;
		if (cname.equals("ODD_BOX")) this.ODD_BOX = color;
		if (cname.equals("ODD_BORDER")) this.ODD_BORDER = color;
		if (cname.equals("ODD_TEXT")) this.ODD_TEXT = color;
		if (cname.equals("EVEN_BOX")) this.EVEN_BOX = color;
		if (cname.equals("EVEN_BORDER")) this.EVEN_BORDER = color;
		if (cname.equals("EVEN_TEXT")) this.EVEN_TEXT = color;
		if (cname.equals("AX_BOX")) this.AX_BOX = color;
		if (cname.equals("AX_BORDER")) this.AX_BORDER = color;
		if (cname.equals("AX_TEXT")) this.AX_TEXT = color;
		if (cname.equals("HELPER_BOX")) this.HELPER_BOX = color;
		if (cname.equals("HELPER_BOX_LIGHT")) this.HELPER_BOX_LIGHT = color;
		if (cname.equals("HELPER_BORDER")) this.HELPER_BORDER = color;
		if (cname.equals("HELPER_TEXT")) this.HELPER_TEXT = color;
		if (cname.equals("RAY_BORDER")) this.RAY_BORDER = color;
		if (cname.equals("POLY_COLOR")) this.POLY_COLOR = color;
		if (cname.equals("CHART_PRIME")) this.CHART_PRIME = color;
		if (cname.equals("CHART_MATCH_COUNT")) this.CHART_MATCH_COUNT = color;
		if (cname.equals("CHART_EXP")) this.CHART_EXP = color;
		if (cname.equals("CHART_FIRST_MATCH")) this.CHART_FIRST_MATCH = color;
		if (cname.equals("CHART_FIRST_VOID")) this.CHART_FIRST_VOID = color;
		if (cname.equals("CHART_VOID_COUNT")) this.CHART_VOID_COUNT = color;
		if (cname.equals("CHART_PRIME_COUNT_CALC")) this.CHART_PRIME_COUNT_CALC = color;
		if (cname.equals("CHART_MATCH_COUNT_CALC")) this.CHART_MATCH_COUNT_CALC = color;
		if (cname.equals("CHART_DIVISOR_SUM")) this.CHART_DIVISOR_SUM = color;
		if (cname.equals("CHART_EULER_TOTIENT")) this.CHART_EULER_TOTIENT = color;
		if (cname.equals("PRIME_MIRROR")) this.PRIME_MIRROR = color;
		if (cname.equals("POLAR_FACTOR_LEFT")) this.POLAR_FACTOR_LEFT = color;
		if (cname.equals("POLAR_FACTOR_RIGHT")) this.POLAR_FACTOR_RIGHT = color;
		this.changed();
	}
	
	/**
	 * 
	 * @return all known color names
	 */
	public String[] getColors() {
		return new String[] {"BACKGROUND",
		        "TEXT_COLOR",
		        "HIGHLIGHT_TEXT_COLOR",
		        "LIGHT_WHITE",
		        "PRIME_BOX",
		        "PRIME_BORDER",
		        "PRIME_TEXT",
		        "EXP_BOX",
		        "EXP_BORDER",
		        "EXP_TEXT",
		        "MATCH_BORDER",
		        "MATCH_BOX",
		        "MATCH_TEXT",
		        "ODD_BOX",
		        "ODD_BORDER",
		        "ODD_TEXT",
		        "EVEN_BOX",
		        "EVEN_BORDER",
		        "EVEN_TEXT",
		        "AX_BOX",
		        "AX_BORDER",
		        "AX_TEXT",
		        "HELPER_BOX",
		        "HELPER_BOX_LIGHT",
		        "HELPER_BORDER",
		        "HELPER_TEXT",
		        "RAY_BORDER",
		        "POLY_COLOR",
		        "CHART_PRIME",
		        "CHART_MATCH_COUNT",
		        "CHART_EXP",
		        "CHART_FIRST_MATCH",
		        "CHART_FIRST_VOID",
		        "CHART_VOID_COUNT",
		        "CHART_PRIME_COUNT_CALC",
		        "CHART_MATCH_COUNT_CALC",
		        "CHART_DIVISOR_SUM",
		        "CHART_EULER_TOTIENT",
		        "PRIME_MIRROR",
		        "POLAR_FACTOR_LEFT",
		        "POLAR_FACTOR_RIGHT"};
	}
	
	/**
	 * 
	 * @return true if the model has changed
	 */
	public boolean isChanged() {
		if (this._changed) {
			this._changed = false;
			return true;
		}
		return false;
	}
	
	/**
	 * sets the changed flag
	 */
	private void changed() {
		this._changed = true;
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * 
	 * @return a delta, inversely proportional to the block size
	 */
	public int getDelta() {
		int d = (int) (20.0F / (double) this.blockSize);
		if (d < 1)
			d = 1;
		return d;
	}

	/**
	 * 
	 * @return the size of the blocks
	 */
	public int getBlockSize() {
		return blockSize;
	}

	/**
	 * set the size of the blocks
	 * @param blockSize
	 */
	public void setBlockSize(int blockSize) {
		if (blockSize > 0) this.blockSize = blockSize;
		this.changed();
	}

	/**
	 * 
	 * @return the current x position
	 */
	public int getXPos() {
		return xPos;
	}

	/**
	 * set the x position
	 * @param xPos
	 */
	public void setXPos(int xPos) {
		if (xPos < 0)
			xPos = 0;
		this.xPos = xPos;
		this.changed();
	}

	/**
	 * 
	 * @return the current y position
	 */
	public int getYPos() {
		return yPos;
	}

	/**
	 * set the y position
	 * @param yPos
	 */
	public void setYPos(int yPos) {
		if (yPos < 0)
			yPos = 0;
		this.yPos = yPos;
		this.changed();
	}

	/**
	 * 
	 * @return the x position of the mouse pointer
	 */
	public int getMouseX() {
		return mouseX;
	}

	/**
	 * set the x position of the mouse pointer
	 * @param mouseX
	 */
	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
		this.changed();
	}

	/**
	 * 
	 * @return the y position of the mouse pointer
	 */
	public int getMouseY() {
		return mouseY;
	}

	/**
	 * set the y position of the mouse pointer
	 * @param mouseY
	 */
	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
		this.changed();
	}

	/**
	 * 
	 * @return true if the blocks should be drawed as rectangles rather than circles
	 */
	public boolean isDrawRect() {
		return drawRect;
	}

	/**
	 * set drawRect
	 * @param drawRect
	 */
	public void setDrawRect(boolean drawRect) {
		this.drawRect = drawRect;
		this.changed();
	}

	/**
	 * 
	 * @return true if helper is enabled
	 */
	public boolean isHelper() {
		return helper;
	}

	/**
	 * set helper
	 * @param helper
	 */
	public void setHelper(boolean helper) {
		this.helper = helper;
		this.changed();
	}

	/**
	 * 
	 * @return true if the rays are enabled
	 */
	public boolean isRays() {
		return rays;
	}

	/**
	 * set rays
	 * @param rays
	 */
	public void setRays(boolean rays) {
		this.rays = rays;
		this.changed();
	}

	/**
	 * 
	 * @return true if the factors should be showed
	 */
	public boolean isFactors() {
		return factors;
	}

	/**
	 * set factors
	 * @param factors
	 */
	public void setFactors(boolean factors) {
		this.factors = factors;
		this.changed();
	}

	/**
	 * 
	 * @return true if the charts should be displayed
	 */
	public boolean isChart() {
		return chart;
	}

	/**
	 * set chart
	 * @param chart
	 */
	public void setChart(boolean chart) {
		this.chart = chart;
		this.changed();
	}

	/**
	 * 
	 * @return true if the charts should be proportional
	 */
	public boolean isChartProp() {
		return chartProp;
	}

	/**
	 * set chartProp
	 * @param chartProp
	 */
	public void setChartProp(boolean chartProp) {
		this.chartProp = chartProp;
		this.changed();
	}

	/**
	 * 
	 * @return true if the Exponent Chart should be displayed
	 */
	public boolean isChartExp() {
		return chartExp;
	}

	/**
	 * 
	 * @param chartExp
	 */
	public void setChartExp(boolean chartExp) {
		this.chartExp = chartExp;
		this.changed();
	}

	/**
	 * 
	 * @return true if the Prime Count Chart should be displayed
	 */
	public boolean isChartPrimes() {
		return chartPrimes;
	}

	/**
	 * 
	 * @param chartPrimes
	 */
	public void setChartPrimes(boolean chartPrimes) {
		this.chartPrimes = chartPrimes;
		this.changed();
	}

	/**
	 * 
	 * @return true if the Goldbach Number Count Chart should be displayed
	 */
	public boolean isChartMatchCount() {
		return chartMatchCount;
	}

	/**
	 * 
	 * @param chartMatch
	 */
	public void setChartMatchCount(boolean chartMatch) {
		this.chartMatchCount = chartMatch;
		this.changed();
	}

	/**
	 * 
	 * @return true if the First Match Chart should be displayed
	 */
	public boolean isChartFirstMatch() {
		return chartFirstMatch;
	}

	/**
	 * 
	 * @param chartFirstMatch
	 */
	public void setChartFirstMatch(boolean chartFirstMatch) {
		this.chartFirstMatch = chartFirstMatch;
		this.changed();
	}

	/**
	 * 
	 * @return true if the First Void Chart should be displayed
	 */
	public boolean isChartFirstVoid() {
		return chartFirstVoid;
	}

	/**
	 * 
	 * @param chartFirstVoid
	 */
	public void setChartFirstVoid(boolean chartFirstVoid) {
		this.chartFirstVoid = chartFirstVoid;
		this.changed();
	}

	/**
	 * 
	 * @return true if the Void Count Chart should be displayed
	 */
	public boolean isChartVoidCount() {
		return chartVoidCount;
	}

	/**
	 * 
	 * @param chartVoidCount
	 */
	public void setChartVoidCount(boolean chartVoidCount) {
		this.chartVoidCount = chartVoidCount;
		this.changed();
	}

	/**
	 * 
	 * @return true if the Exponent Chart should show the sum of the exponents
	 */
	public boolean isChartExpSum() {
		return chartExpSum;
	}

	/**
	 * 
	 * @param chartExpSum
	 */
	public void setChartExpSum(boolean chartExpSum) {
		this.chartExpSum = chartExpSum;
		this.changed();
	}

	/**
	 * 
	 * @return the vertical step of numbers
	 */
	public int getVerticalStep() {
		return verticalStep;
	}

	/**
	 * set the vertical step
	 * @param verticalStep
	 */
	public void setVerticalStep(int verticalStep) {
		if (verticalStep > 0) {
			this.verticalStep = verticalStep;
			this.setVerticalOffset(0);
			this.changed();
		}
	}

	/**
	 * 
	 * @return the horizontal step of numbers
	 */
	public int getHorizontalStep() {
		return horizontalStep;
	}

	/**
	 * set the horizontal step
	 * @param horizontalStep
	 */
	public void setHorizontalStep(int horizontalStep) {
		if (horizontalStep > 0) {
			this.horizontalStep = horizontalStep;
			this.setHorizontalOffset(0);
			this.changed();
		}
	}

	/**
	 * 
	 * @return the mode for zooming with +/-
	 */
	public PMMode getPmmode() {
		return pmmode;
	}

	/**
	 * 
	 * @param pmmode
	 */
	public void setPmmode(PMMode pmmode) {
		this.pmmode = pmmode;
		this.changed();
	}

	/**
	 * 
	 * @return the vertical offset of numbers within a step
	 */
	public int getVerticalOffset() {
		return verticalOffset;
	}

	/**
	 * 
	 * @param verticalOffset
	 */
	public void setVerticalOffset(int verticalOffset) {
		if (verticalOffset >= 0) {
			this.verticalOffset = verticalOffset%this.getVerticalStep();
			this.changed();
		}
	}

	/**
	 * 
	 * @return the horizontal offset of numbers within a step
	 */
	public int getHorizontalOffset() {
		return horizontalOffset;
	}

	/**
	 * 
	 * @param horizontalOffset
	 */
	public void setHorizontalOffset(int horizontalOffset) {
		if (horizontalOffset >= 0) {
			this.horizontalOffset = horizontalOffset%this.getHorizontalStep();
			this.changed();
		}
	}
	
	/**
	 * 
	 * @return true if the stats should be displayed below the prime view
	 */
	public boolean isStats() {
		return stats;
	}

	/**
	 * 
	 * @param stats
	 */
	public void setStats(boolean stats) {
		this.stats = stats;
		this.changed();
	}

	/**
	 * @return true if the prime numbers should be displayed
	 */
	public boolean isPrimes() {
		return primes;
	}

	/**
	 * @param primes
	 */
	public void setPrimes(boolean primes) {
		this.primes = primes;
		this.changed();
	}

	/**
	 * 
	 * @return the width of the prime view's window
	 */
	public int getWindowWidth() {
		return window_width;
	}

	/**
	 * set the width of the window
	 * @param window_width
	 */
	public void setWindowWidth(int window_width) {
		this.window_width = window_width;
	}

	/**
	 * 
	 * @return the height of the prime view's window
	 */
	public int getWindowHeight() {
		return window_height;
	}

	/**
	 * set the height of the window
	 * @param window_height
	 */
	public void setWindowHeight(int window_height) {
		this.window_height = window_height;
	}

	/**
	 * 
	 * @return the size of diplayed polynomials
	 */
	public int getPolySize() {
		return polySize;
	}

	/**
	 * set the size of displayed polynomials
	 * @param polySize
	 */
	public void setPolySize(int polySize) {
		if (polySize > 0) {
			this.polySize = polySize;
			this.changed();
		}
	}

	/**
	 * 
	 * @return true if polynomials should be displayed
	 */
	public boolean isPolynomials() {
		return polynomials;
	}

	/**
	 * 
	 * @param polynomials
	 */
	public void setPolynomials(boolean polynomials) {
		this.polynomials = polynomials;
	}

	/**
	 * 
	 * @return the factor of displayed polynomials
	 */
	public int getPolyFactor() {
		return polyFactor;
	}

	/**
	 * set the factor of displayed polynomials
	 * @param polyFactor
	 */
	public void setPolyFactor(int polyFactor) {
		if (polyFactor > 0) {
			this.polyFactor = polyFactor;
			this.changed();
		}
	}

	/**
	 * 
	 * @return the horizontal step between the points of displayed polynomials
	 */
	public int getPolyDelta() {
		return polyDelta;
	}

	/**
	 * the horizontal step between the points of displayed polynomials
	 * @param polyDelta
	 */
	public void setPolyDelta(int polyDelta) {
		if (polyDelta > 0) {
			this.polyDelta = polyDelta;
			this.changed();
		}
	}

	/**
	 * 
	 * @return true if the view should be transformed so that a checked pattern is shown
	 */
	public boolean isCheckedPattern() {
		return checkedPattern;
	}

	/**
	 * 
	 * @param checkedPattern
	 */
	public void setCheckedPattern(boolean checkedPattern) {
		this.checkedPattern = checkedPattern;
		this.changed();
	}

	/**
	 * 
	 * @return true if a mirror of primes should be displayed
	 */
	public boolean isPrimeMirror() {
		return primeMirror;
	}

	/**
	 * 
	 * @param primeMirror
	 */
	public void setPrimeMirror(boolean primeMirror) {
		this.primeMirror = primeMirror;
		this.changed();
	}

	/**
	 * 
	 * @return the view mode (key: up/down/left/right)
	 */
	public PMView getPmview() {
		return pmview;
	}

	/**
	 * 
	 * @param pmview
	 */
	public void setPmview(PMView pmview) {
		this.pmview = pmview;
		this.changed();
	}

	/**
	 * 
	 * @return the x factor of the polar polynomials
	 */
	public int getFactorX() {
		return factorX;
	}

	/**
	 * set the x factor of the polar polynomials
	 * @param factorX
	 */
	public void setFactorX(int factorX) {
		if (factorX >= 0) this.factorX = factorX;
		else this.factorX = 0;
		this.changed();
	}

	/**
	 * 
	 * @return the y factor of the polar polynomials
	 */
	public int getFactorY() {
		return factorY;
	}

	/**
	 * set the y factor of the polar polynomials
	 * @param factorY
	 */
	public void setFactorY(int factorY) {
		if (factorY > 0) this.factorY = factorY;
		else this.factorY = 1;
		this.changed();
	}

	/**
	 * 
	 * @return the z factor of the polar polynomials
	 */
	public int getFactorZ() {
		return factorZ;
	}

	/**
	 * set the z factor of the polar polynomials
	 * @param factorZ
	 */
	public void setFactorZ(int factorZ) {
		if (factorZ > 0) this.factorZ = factorZ;
		else this.factorZ = 1;
		this.changed();
	}

	/**
	 * 
	 * @return true if the calculated prime count chart should be displayed
	 */
	public boolean isChartPrimeCountCalc() {
		return chartPrimeCountCalc;
	}

	/**
	 * 
	 * @param chartPrimeCountCalc
	 */
	public void setChartPrimeCountCalc(boolean chartPrimeCountCalc) {
		this.chartPrimeCountCalc = chartPrimeCountCalc;
		this.changed();
	}

	/**
	 * 
	 * @return true if the calculated goldbach number count chart should be displayed
	 */
	public boolean isChartMatchCountCalc() {
		return chartMatchCountCalc;
	}

	/**
	 * 
	 * @param chartMatchCountCalc
	 */
	public void setChartMatchCountCalc(boolean chartMatchCountCalc) {
		this.chartMatchCountCalc = chartMatchCountCalc;
		this.changed();
	}

	/**
	 * 
	 * @return true if the divisor function chart should be displayed
	 */
	public boolean isChartDivisorSum() {
		return chartDivisorSum;
	}

	/**
	 * 
	 * @param chartDivisorSum
	 */
	public void setChartDivisorSum(boolean chartDivisorSum) {
		this.chartDivisorSum = chartDivisorSum;
		this.changed();
	}

	/**
	 * true if the euler's totient chart should be displayed
	 * @return
	 */
	public boolean isChartEulerTotient() {
		return chartEulerTotient;
	}

	/**
	 * 
	 * @param chartEulerTotient
	 */
	public void setChartEulerTotient(boolean chartEulerTotient) {
		this.chartEulerTotient = chartEulerTotient;
		this.changed();
	}

	/**
	 * 
	 * @return true if only the outer factors in the polar polynomials should be displayed
	 */
	public boolean isFactorOnlyOuter() {
		return factorOnlyOuter;
	}

	/**
	 * 
	 * @param factorOnlyOuter
	 */
	public void setFactorOnlyOuter(boolean factorOnlyOuter) {
		this.factorOnlyOuter = factorOnlyOuter;
		this.changed();
	}

	/**
	 * Factors are needed if they are the first to prevent a number from being prime
	 * @return true if only the outer factors in the polar polynomials should be displayed
	 */
	public boolean isFactorOnlyNeeded() {
		return factorOnlyNeeded;
	}

	/**
	 * 
	 * @param factorOnlyNeeded
	 */
	public void setFactorOnlyNeeded(boolean factorOnlyNeeded) {
		this.factorOnlyNeeded = factorOnlyNeeded;
		this.changed();
	}

	/**
	 * 
	 * @return true if the polar factors should be displayed
	 */
	public boolean isPolarFactors() {
		return polarFactors;
	}

	/**
	 * 
	 * @param polarFactors
	 */
	public void setPolarFactors(boolean polarFactors) {
		this.polarFactors = polarFactors;
		this.changed();
	}

	/**
	 * 
	 * @return true if the polar factors should be displayed balanced
	 */
	public boolean isPolarBalance() {
		return polarBalance;
	}

	/**
	 * 
	 * @param polarBalance
	 */
	public void setPolarBalance(boolean polarBalance) {
		this.polarBalance = polarBalance;
	}

	/**
	 * 
	 * @return true if the view should be rotated by 90 degree
	 */
	public boolean isRotateView() {
		return rotateView;
	}

	/**
	 * 
	 * @param rotateView
	 */
	public void setRotateView(boolean rotateView) {
		this.rotateView = rotateView;
		this.changed();
	}

	/**
	 * 
	 * @return the exponent of the divisor function
	 */
	public int getDivisorSumExponent() {
		return divisorSumExponent;
	}

	/**
	 * 
	 * @param divisorSumExponent
	 */
	public void setDivisorSumExponent(int divisorSumExponent) {
		if (divisorSumExponent < 0) divisorSumExponent = 0;
		this.divisorSumExponent = divisorSumExponent;
		this.changed();
	}

	/**
	 * @return true if the polar factors should overlap
	 */
	public boolean isPolarOverlap() {
		return polarOverlap;
	}

	/**
	 * @param polarOverlap
	 */
	public void setPolarOverlap(boolean polarOverlap) {
		this.polarOverlap = polarOverlap;
		this.changed();
	}

	/**
	 * @return true if high composite numbers should be displayed
	 */
	public boolean isShowHCN() {
		return showHCN;
	}

	/**
	 * @param showHCN
	 */
	public void setShowHCN(boolean showHCN) {
		this.showHCN = showHCN;
		this.changed();
	}

	/**
	 * 
	 * @return the stats, stored by occurrence
	 */
	public Map<Integer,InfoEntry> getInfo() {
		Map<Integer,InfoEntry> ret = new TreeMap<Integer,InfoEntry>();
		String str = "";
		switch (this.getPmmode()) {
		case VERTICAL_STEP:
			str = "VERTICAL_STEP";
			break;
		case HORIZONTAL_STEP:
			str = "HORIZONTAL_STEP";
			break;
		case VERTICAL_OFFSET:
			str = "VERTICAL_OFFSET";
			break;
		case HORIZONTAL_OFFSET:
			str = "HORIZONTAL_OFFSET";
			break;
		case ZOOM:
			str = "ZOOM";
			break;
		case POLY_SIZE:
			str = "POLY_SIZE";
			break;
		case POLY_FACTOR:
			str = "POLY_FACTOR";
			break;
		case POLY_DELTA:
			str = "POLY_DELTA";
			break;
		case DIVISOR_EXP:
			str = "DIVISOR_EXP";
			break;
		}
		ret.put(1,new InfoEntry("POS_X",""+this.getXPos()));
		ret.put(2,new InfoEntry("POS_Y",""+this.getYPos()));
		ret.put(3,new InfoEntry("STEP_X",""+this.getHorizontalStep()));
		ret.put(4,new InfoEntry("STEP_Y",""+this.getVerticalStep()));
		ret.put(5,new InfoEntry("OFFSET_X",""+this.getHorizontalOffset()));
		ret.put(6,new InfoEntry("OFFSET_Y",""+this.getVerticalOffset()));
		ret.put(7,new InfoEntry("MOUSE_X",""+this.getMouseX()));
		ret.put(8,new InfoEntry("MOUSE_Y",""+this.getMouseY()));
		ret.put(9,new InfoEntry("POLAR_X",""+this.getFactorX()));
		ret.put(10,new InfoEntry("POLAR_Y",""+this.getFactorY()));
		ret.put(11,new InfoEntry("POLAR_Z",""+this.getFactorZ()));
		ret.put(12,new InfoEntry("MODE",str));
		ret.put(13,new InfoEntry("BLOCK_SIZE",""+this.getBlockSize()));
		ret.put(14,new InfoEntry("EXP_SUM",""+this.isChartExpSum()));
		ret.put(15,new InfoEntry("DIVISOR_EXP",""+this.getDivisorSumExponent()));
		ret.put(16,new InfoEntry("CHART_PROP",""+this.isChartProp()));
		ret.put(17,new InfoEntry("SHOW_PRIMES",""+this.isPrimes()));
		ret.put(18,new InfoEntry("SHOW_FACTORS",""+this.isFactors()));
		ret.put(19,new InfoEntry("RECTANGLES",""+this.isDrawRect()));
		ret.put(20,new InfoEntry("SHOW_HELPER",""+this.isHelper()));
		ret.put(21,new InfoEntry("SHOW_RAYS",""+this.isRays()));
		ret.put(22,new InfoEntry("SHOW_POLY",""+this.isPolynomials()));
		ret.put(23,new InfoEntry("SHOW_MIRROR",""+this.isPrimeMirror()));
		ret.put(24,new InfoEntry("SHOW_CHECKED_PAT",""+this.isCheckedPattern()));
		ret.put(25,new InfoEntry("SHOW_POLAR_FACTOR",""+this.isPolarFactors()));
		//ret.put(26,new InfoEntry("POLY_SIZE",""+this.getPolySize()));
		ret.put(27,new InfoEntry("POLY_FACTOR",""+this.getPolyFactor()));
		//ret.put(28,new InfoEntry("POLY_DELTA",""+this.getPolyDelta()));
		ret.put(29,new InfoEntry("CHART_PRIMES",""+this.isChartPrimes()));
		ret.put(30,new InfoEntry("CHART_EXP",""+this.isChartExp()));
		ret.put(31,new InfoEntry("CHART_MATCH_COUNT",""+this.isChartMatchCount()));
		ret.put(32,new InfoEntry("CHART_FIRST_MATCH",""+this.isChartFirstMatch()));
		ret.put(33,new InfoEntry("CHART_VOID_COUNT",""+this.isChartVoidCount()));
		ret.put(34,new InfoEntry("CHART_FIRST_VOID",""+this.isChartFirstVoid()));
		ret.put(35,new InfoEntry("CHART_PRIME_COUNT_CALC",""+this.isChartPrimeCountCalc()));
		ret.put(36,new InfoEntry("CHART_MATCH_COUNT_CALC",""+this.isChartMatchCountCalc()));
		ret.put(37,new InfoEntry("CHART_DIVISOR_SUM",""+this.isChartDivisorSum()));
		ret.put(38,new InfoEntry("CHART_EULER_TOTIENT",""+this.isChartEulerTotient()));
		return ret;
	}

}
