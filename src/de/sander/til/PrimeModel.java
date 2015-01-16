package de.sander.til;

import java.awt.Color;
import java.util.TreeMap;
import java.util.Map;

public class PrimeModel {
	
	public enum PMMode {
		VERTICAL_STEP,
		HORIZONTAL_STEP,
		VERTICAL_OFFSET,
		HORIZONTAL_OFFSET,
		NORMAL,
		POLY_SIZE,
		POLY_FACTOR,
		POLY_DELTA
	}

	private int blockSize = 12, xPos = 0, yPos = 0, mouseX = 0, mouseY = 0, verticalStep=1, horizontalStep=1, verticalOffset=0, horizontalOffset=0, window_width=1200, window_height=800, polySize=5, polyFactor=1, polyDelta=1;
	private boolean xyTransform = false,
			exponents = true, drawRect = true, helper = true,
			rays = true, rayBox = true, chart = true, chartProp = true,
			chartExp = true, chartPrimes = true, chartMatchCount = true, chartFirstMatch=true, chartFirstVoid=true, chartVoidCount=true,
			chartExpSum = true, stats=true, primes=true, _changed=false, polynomials=true;
	private PMMode pmmode=PMMode.NORMAL;
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
		this.CHART_FIRST_MATCH = new Color(185,45,190);
		this.CHART_FIRST_VOID = new Color(85,145,190);
		this.CHART_VOID_COUNT = new Color(225,145,90);
		this.ALPHA = new Color(0,0,0,0);
	}
	
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
		return this.ALPHA;
	}
	
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
		this.changed();
	}
	
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
		        "CHART_VOID_COUNT"};
	}
	
	public boolean isChanged() {
		if (this._changed) {
			this._changed = false;
			return true;
		}
		return false;
	}
	
	private void changed() {
		this._changed = true;
	}

	public int getDelta() {
		int d = (int) (20.0F / (double) this.blockSize);
		if (d < 1)
			d = 1;
		return d;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		if (blockSize > 0) this.blockSize = blockSize;
		this.changed();
	}

	public int getXPos() {
		return xPos;
	}

	public void setXPos(int xPos) {
		if (xPos < 0)
			xPos = 0;
		this.xPos = xPos;
		this.changed();
	}

	public int getYPos() {
		return yPos;
	}

	public void setYPos(int yPos) {
		if (yPos < 0)
			yPos = 0;
		this.yPos = yPos;
		this.changed();
	}

	public int getMouseX() {
		return mouseX;
	}

	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
		this.changed();
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
		this.changed();
	}

	public boolean isXYTransform() {
		return xyTransform;
	}

	public void setXyTransform(boolean xyTransform) {
		this.xyTransform = xyTransform;
		this.changed();
	}

	public boolean isExponents() {
		return exponents;
	}

	public void setExponents(boolean exponents) {
		this.exponents = exponents;
		this.changed();
	}

	public boolean isDrawRect() {
		return drawRect;
	}

	public void setDrawRect(boolean drawRect) {
		this.drawRect = drawRect;
		this.changed();
	}

	public boolean isHelper() {
		return helper;
	}

	public void setHelper(boolean helper) {
		this.helper = helper;
		this.changed();
	}

	public boolean isRays() {
		return rays;
	}

	public void setRays(boolean rays) {
		this.rays = rays;
		this.changed();
	}

	public boolean isRayBox() {
		return rayBox;
	}

	public void setRayBox(boolean rayBox) {
		this.rayBox = rayBox;
		this.changed();
	}

	public boolean isChart() {
		return chart;
	}

	public void setChart(boolean chart) {
		this.chart = chart;
		this.changed();
	}

	public boolean isChartProp() {
		return chartProp;
	}

	public void setChartProp(boolean chartProp) {
		this.chartProp = chartProp;
		this.changed();
	}

	public boolean isChartExp() {
		return chartExp;
	}

	public void setChartExp(boolean chartExp) {
		this.chartExp = chartExp;
		this.changed();
	}

	public boolean isChartPrimes() {
		return chartPrimes;
	}

	public void setChartPrimes(boolean chartPrimes) {
		this.chartPrimes = chartPrimes;
		this.changed();
	}

	public boolean isChartMatchCount() {
		return chartMatchCount;
	}

	public void setChartMatchCount(boolean chartMatch) {
		this.chartMatchCount = chartMatch;
		this.changed();
	}

	public boolean isChartFirstMatch() {
		return chartFirstMatch;
	}

	public void setChartFirstMatch(boolean chartFirstMatch) {
		this.chartFirstMatch = chartFirstMatch;
	}

	public boolean isChartFirstVoid() {
		return chartFirstVoid;
	}

	public void setChartFirstVoid(boolean chartFirstVoid) {
		this.chartFirstVoid = chartFirstVoid;
	}

	public boolean isChartVoidCount() {
		return chartVoidCount;
	}

	public void setChartVoidCount(boolean chartVoidCount) {
		this.chartVoidCount = chartVoidCount;
	}

	public boolean isChartExpSum() {
		return chartExpSum;
	}

	public void setChartExpSum(boolean chartExpSum) {
		this.chartExpSum = chartExpSum;
		this.changed();
	}

	public int getVerticalStep() {
		return verticalStep;
	}

	public void setVerticalStep(int verticalStep) {
		if (verticalStep > 0) {
			this.verticalStep = verticalStep;
			this.setVerticalOffset(0);
			this.changed();
		}
	}

	public int getHorizontalStep() {
		return horizontalStep;
	}

	public void setHorizontalStep(int horizontalStep) {
		if (horizontalStep > 0) {
			this.horizontalStep = horizontalStep;
			this.setHorizontalOffset(0);
			this.changed();
		}
	}

	public PMMode getPmmode() {
		return pmmode;
	}

	public void setPmmode(PMMode pmmode) {
		this.pmmode = pmmode;
		this.changed();
	}

	public int getVerticalOffset() {
		return verticalOffset;
	}

	public void setVerticalOffset(int verticalOffset) {
		if (verticalOffset >= 0) {
			this.verticalOffset = verticalOffset%this.getVerticalStep();
			this.changed();
		}
	}

	public int getHorizontalOffset() {
		return horizontalOffset;
	}

	public void setHorizontalOffset(int horizontalOffset) {
		if (horizontalOffset >= 0) {
			this.horizontalOffset = horizontalOffset%this.getHorizontalStep();
			this.changed();
		}
	}
	
	public boolean isStats() {
		return stats;
	}

	public void setStats(boolean stats) {
		this.stats = stats;
		this.changed();
	}

	/**
	 * @return the primes
	 */
	public boolean isPrimes() {
		return primes;
	}

	/**
	 * @param primes the primes to set
	 */
	public void setPrimes(boolean primes) {
		this.primes = primes;
		this.changed();
	}

	public int getWindow_width() {
		return window_width;
	}

	public void setWindow_width(int window_width) {
		this.window_width = window_width;
	}

	public int getWindow_height() {
		return window_height;
	}

	public void setWindow_height(int window_height) {
		this.window_height = window_height;
	}

	public int getPolySize() {
		return polySize;
	}

	public void setPolySize(int polySize) {
		if (polySize > 0) {
			this.polySize = polySize;
			this.changed();
		}
	}

	public boolean isPolynomials() {
		return polynomials;
	}

	public void setPolynomials(boolean polynomials) {
		this.polynomials = polynomials;
	}

	public int getPolyFactor() {
		return polyFactor;
	}

	public void setPolyFactor(int polyFactor) {
		if (polyFactor > 0) {
			this.polyFactor = polyFactor;
			this.changed();
		}
	}

	public int getPolyDelta() {
		return polyDelta;
	}

	public void setPolyDelta(int polyDelta) {
		if (polyDelta > 0) {
			this.polyDelta = polyDelta;
			this.changed();
		}
	}

	public Map<String,String> getInfo() {
		Map<String,String> ret = new TreeMap<String,String>();
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
		case NORMAL:
			str = "NORMAL";
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
		}
		ret.put("_MODE",str);
		ret.put("_BLOCK_SIZE",""+this.getBlockSize());
		ret.put("_EXP_SUM [E]",""+this.isChartExpSum());
		ret.put("_CHART_PROP [P]",""+this.isChartProp());
		ret.put("_SHOW_PRIMES [T]",""+this.isPrimes());
		ret.put("_SHOW_FACTORS [B]",""+this.isRayBox());
		ret.put("_RECTANGLES [R]",""+this.isDrawRect());
		ret.put("_SHOW_HELPER [H]",""+this.isHelper());
		ret.put("_SHOW_RAYS [L]",""+this.isRays());
		ret.put("_SHOW_POLY [U]",""+this.isPolynomials());
		ret.put("_POLY_SIZE",""+this.getPolySize());
		ret.put("_POLY_FACTOR",""+this.getPolyFactor());
		ret.put("_POLY_DELTA",""+this.getPolyDelta());
		ret.put("__CHART_PRIMES [1]",""+this.isChartPrimes());
		ret.put("__CHART_EXP [2]",""+this.isChartExp());
		ret.put("__CHART_MATCH_COUNT [3]",""+this.isChartMatchCount());
		ret.put("__CHART_FIRST_MATCH [4]",""+this.isChartFirstMatch());
		ret.put("__CHART_VOID_COUNT [5]",""+this.isChartVoidCount());
		ret.put("__CHART_FIRST_VOID [6]",""+this.isChartFirstVoid());
		ret.put("POS_X",""+this.getXPos());
		ret.put("POS_Y",""+this.getYPos());
		ret.put("STEP_X",""+this.getHorizontalStep());
		ret.put("STEP_Y",""+this.getVerticalStep());
		ret.put("OFFSET_X",""+this.getHorizontalOffset());
		ret.put("OFFSET_Y",""+this.getVerticalOffset());
		ret.put("MOUSE_X",""+this.getMouseX());
		ret.put("MOUSE_Y",""+this.getMouseY());
		return ret;
	}

}
