package de.sander.til;

import java.util.TreeMap;
import java.util.Map;

public class PrimeModel {
	
	public enum PMMode {
		VERTICAL_STEP,
		HORIZONTAL_STEP,
		VERTICAL_OFFSET,
		HORIZONTAL_OFFSET,
		NORMAL
	}

	private int blockSize = 12, xPos = 0, yPos = 0, mouseX = 0, mouseY = 0, verticalStep=1, horizontalStep=1, verticalOffset=0, horizontalOffset=0;
	private boolean xyTransform = false,
			exponents = true, drawRect = true, helper = true,
			rays = true, rayBox = true, chart = true, chartProp = true,
			chartExp = true, chartPrimes = true, chartMatch = true,
			chartExpSum = true, stats=true;
	private PMMode pmmode=PMMode.NORMAL;

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
	}

	public int getXPos() {
		return xPos;
	}

	public void setXPos(int xPos) {
		if (xPos < 0)
			xPos = 0;
		this.xPos = xPos;
	}

	public int getYPos() {
		return yPos;
	}

	public void setYPos(int yPos) {
		if (yPos < 0)
			yPos = 0;
		this.yPos = yPos;
	}

	public int getMouseX() {
		return mouseX;
	}

	public void setMouseX(int mouseX) {
		this.mouseX = mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public void setMouseY(int mouseY) {
		this.mouseY = mouseY;
	}

	public boolean isXYTransform() {
		return xyTransform;
	}

	public void setXyTransform(boolean xyTransform) {
		this.xyTransform = xyTransform;
	}

	public boolean isExponents() {
		return exponents;
	}

	public void setExponents(boolean exponents) {
		this.exponents = exponents;
	}

	public boolean isDrawRect() {
		return drawRect;
	}

	public void setDrawRect(boolean drawRect) {
		this.drawRect = drawRect;
	}

	public boolean isHelper() {
		return helper;
	}

	public void setHelper(boolean helper) {
		this.helper = helper;
	}

	public boolean isRays() {
		return rays;
	}

	public void setRays(boolean rays) {
		this.rays = rays;
	}

	public boolean isRayBox() {
		return rayBox;
	}

	public void setRayBox(boolean rayBox) {
		this.rayBox = rayBox;
	}

	public boolean isChart() {
		return chart;
	}

	public void setChart(boolean chart) {
		this.chart = chart;
	}

	public boolean isChartProp() {
		return chartProp;
	}

	public void setChartProp(boolean chartProp) {
		this.chartProp = chartProp;
	}

	public boolean isChartExp() {
		return chartExp;
	}

	public void setChartExp(boolean chartExp) {
		this.chartExp = chartExp;
	}

	public boolean isChartPrimes() {
		return chartPrimes;
	}

	public void setChartPrimes(boolean chartPrimes) {
		this.chartPrimes = chartPrimes;
	}

	public boolean isChartMatch() {
		return chartMatch;
	}

	public void setChartMatch(boolean chartMatch) {
		this.chartMatch = chartMatch;
	}

	public boolean isChartExpSum() {
		return chartExpSum;
	}

	public void setChartExpSum(boolean chartExpSum) {
		this.chartExpSum = chartExpSum;
	}

	public int getVerticalStep() {
		return verticalStep;
	}

	public void setVerticalStep(int verticalStep) {
		if (verticalStep > 0) {
			this.verticalStep = verticalStep;
			this.setVerticalOffset(0);
		}
	}

	public int getHorizontalStep() {
		return horizontalStep;
	}

	public void setHorizontalStep(int horizontalStep) {
		if (horizontalStep > 0) {
			this.horizontalStep = horizontalStep;
			this.setHorizontalOffset(0);
		}
	}

	public PMMode getPmmode() {
		return pmmode;
	}

	public void setPmmode(PMMode pmmode) {
		this.pmmode = pmmode;
	}

	public int getVerticalOffset() {
		return verticalOffset;
	}

	public void setVerticalOffset(int verticalOffset) {
		if (verticalOffset >= 0) this.verticalOffset = verticalOffset%this.getVerticalStep();
	}

	public int getHorizontalOffset() {
		return horizontalOffset;
	}

	public void setHorizontalOffset(int horizontalOffset) {
		if (horizontalOffset >= 0) this.horizontalOffset = horizontalOffset%this.getHorizontalStep();
	}
	
	public boolean isStats() {
		return stats;
	}

	public void setStats(boolean stats) {
		this.stats = stats;
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
		}
		ret.put("_MODE",str);
		ret.put("_BLOCK_SIZE",""+this.getBlockSize());
		ret.put("_DELTA",""+this.getDelta());
		ret.put("_EXP_SUM",""+this.isChartExpSum());
		ret.put("_CHART_PROP",""+this.isChartProp());
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
