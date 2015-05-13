package de.sander.til;

public interface MenuListener {

	/**
	 * this enum defines all actions of a menu
	 */
	public enum MenuAction {
		NEW_FILE,
		OPEN_FILE,
		CLOSE_FILE,
		ZOOM,
		VERTICAL_STEP,
		VERTICAL_OFFSET,
		HORIZONTAL_STEP,
		HORIZONTAL_OFFSET,
		POLY_SIZE,
		POLY_FACTOR,
		POLY_DELTA,
		DIVISOR_EXP,
		ZOOM_IN,
		ZOOM_OUT,
		PRIMES,
		FACTORS,
		RECTANGLES,
		HELPER,
		RAYS,
		POLYS,
		MIRROR,
		Y_TRANSFORM,
		POLAR_FACTORS,
		CHART_PRIMES,
		CHART_EXPONENTS,
		CHART_MATCH_COUNT,
		CHART_FIRST_MATCH,
		CHART_VOID_COUNT,
		CHART_FIRST_VOID,
		CHART_PRIMES_CALC,
		CHART_MATCH_COUNT_CALC,
		CHART_DIVISOR_SUM,
		CHART_EULER_TOTIENT,
		CHART_PROP,
		EXPONENT_SUM,
		ROTATE_VIEW,
		HIGHLY_COMPOSITE_NUMBERS,
		SHOW_CHARTS,
		SHOW_STATS,
		POLAR_BALANCE,
		POLAR_OVERLAP,
		FACTORS_ONLY_OUTER,
		FACTORS_ONLY_NEEDED,
		GOLDBACH_VIEW,
		FACTOR_VIEW,
		ORIGIN,
		DOCUMENTATION,
		ABOUT,
		UPDATE
	}
	
	/**
	 * handle the given menu action
	 * @param act
	 */
	public void handle(MenuListener.MenuAction act);
	
	/**
	 * open the file
	 * @param file a file name
	 */
	public void openRecently(String file);

}
