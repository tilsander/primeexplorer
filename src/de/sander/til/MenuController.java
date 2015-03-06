package de.sander.til;

import javax.swing.JMenuBar;


public class MenuController implements MenuListener {
	
	private PrimeModel model;
	private MenuView view;
	
	public MenuController(PrimeModel model, MenuView view) {
		this.model = model;
		this.view = view;
		this.view.setListener(this);
	}
	
	public JMenuBar getMenuBar() {
		return this.view.getMenuBar();
	}

	@Override
	public void handle(MenuListener.MenuAction act) {
		switch (act) {
		case ABOUT:
			this.openAbout();
			break;
		case CHART_DIVISOR_SUM:
			this.model.setChartDivisorSum(!this.model.isChartDivisorSum());
			break;
		case CHART_EULER_TOTIENT:
			this.model.setChartEulerTotient(!this.model.isChartEulerTotient());
			break;
		case CHART_EXPONENTS:
			this.model.setChartExp(!this.model.isChartExp());
			break;
		case CHART_FIRST_MATCH:
			this.model.setChartFirstMatch(!this.model.isChartFirstMatch());
			break;
		case CHART_FIRST_VOID:
			this.model.setChartFirstVoid(!this.model.isChartFirstVoid());
			break;
		case CHART_MATCH_COUNT:
			this.model.setChartMatchCount(!this.model.isChartMatchCount());
			break;
		case CHART_MATCH_COUNT_CALC:
			this.model.setChartMatchCountCalc(!this.model.isChartMatchCountCalc());
			break;
		case CHART_PRIMES:
			this.model.setChartPrimes(!this.model.isChartPrimes());
			break;
		case CHART_PRIMES_CALC:
			this.model.setChartPrimeCountCalc(!this.model.isChartPrimeCountCalc());
			break;
		case CHART_PROP:
			this.model.setChartProp(!this.model.isChartProp());
			break;
		case CHART_VOID_COUNT:
			this.model.setChartVoidCount(!this.model.isChartVoidCount());
			break;
		case CLOSE_FILE:
			break;
		case DIVISOR_EXP:
			this.model.setPmmode(PrimeModel.PMMode.DIVISOR_EXP);
			break;
		case DOCUMENTATION:
			this.openDocumentation();
			break;
		case EXPONENT_SUM:
			this.model.setChartExpSum(!this.model.isChartExpSum());
			break;
		case FACTORS:
			this.model.setFactors(!this.model.isFactors());
			break;
		case FACTORS_ONLY_NEEDED:
			this.model.setFactorOnlyNeeded(!this.model.isFactorOnlyNeeded());
			break;
		case FACTORS_ONLY_OUTER:
			this.model.setFactorOnlyOuter(!this.model.isFactorOnlyOuter());
			break;
		case FACTOR_VIEW:
			this.model.setPmview(PrimeModel.PMView.FACTOR);
			break;
		case GOLDBACH_VIEW:
			this.model.setPmview(PrimeModel.PMView.GOLDBACH);
			break;
		case HELPER:
			this.model.setHelper(!this.model.isHelper());
			break;
		case HORIZONTAL_OFFSET:
			this.model.setPmmode(PrimeModel.PMMode.HORIZONTAL_OFFSET);
			break;
		case HORIZONTAL_STEP:
			this.model.setPmmode(PrimeModel.PMMode.HORIZONTAL_STEP);
			break;
		case MIRROR:
			this.model.setPrimeMirror(!this.model.isPrimeMirror());
			break;
		case NEW_FILE:
			break;
		case OPEN_FILE:
			break;
		case ORIGIN:
			this.model.setXPos(0);
			this.model.setYPos(0);
			break;
		case POLAR_BALANCE:
			this.model.setPolarBalance(!this.model.isPolarBalance());
			break;
		case POLAR_FACTORS:
			this.model.setPolarFactors(!this.model.isPolarFactors());
			break;
		case POLYS:
			this.model.setPolynomials(!this.model.isPolynomials());
			break;
		case POLY_DELTA:
			this.model.setPmmode(PrimeModel.PMMode.POLY_DELTA);
			break;
		case POLY_FACTOR:
			this.model.setPmmode(PrimeModel.PMMode.POLY_FACTOR);
			break;
		case POLY_SIZE:
			this.model.setPmmode(PrimeModel.PMMode.POLY_SIZE);
			break;
		case PRIMES:
			this.model.setPrimes(!this.model.isPrimes());
			break;
		case RAYS:
			this.model.setRays(!this.model.isRays());
			break;
		case RECTANGLES:
			this.model.setDrawRect(!this.model.isDrawRect());
			break;
		case ROTATE_VIEW:
			this.model.setRotateView(!this.model.isRotateView());
			break;
		case SHOW_CHARTS:
			this.model.setChart(!this.model.isChart());
			break;
		case SHOW_STATS:
			this.model.setStats(!this.model.isStats());
			break;
		case VERTICAL_OFFSET:
			this.model.setPmmode(PrimeModel.PMMode.VERTICAL_OFFSET);
			break;
		case VERTICAL_STEP:
			this.model.setPmmode(PrimeModel.PMMode.VERTICAL_STEP);
			break;
		case Y_TRANSFORM:
			this.model.setCheckedPattern(!this.model.isCheckedPattern());
			break;
		case ZOOM:
			this.model.setPmmode(PrimeModel.PMMode.ZOOM);
			break;
		case ZOOM_IN:
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
			case ZOOM:
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
			case DIVISOR_EXP:
				this.model.setDivisorSumExponent(this.model.getDivisorSumExponent()+1);
				Primes._().resetDivisorSum();
				break;
			}
			break;
		case ZOOM_OUT:
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
			case ZOOM:
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
			case DIVISOR_EXP:
				this.model.setDivisorSumExponent(this.model.getDivisorSumExponent()-1);
				Primes._().resetDivisorSum();
				break;
			}
			break;
		default:
			break;
		
		}
	}
	
	public void openDocumentation() {
		
	}
	
	public void openAbout() {
		PrimeUtil.openWebpage("http://www.til-sander.de/");
	}


}
