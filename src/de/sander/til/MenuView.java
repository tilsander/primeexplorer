package de.sander.til;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import de.sander.til.MenuListener.MenuAction;;

public class MenuView {
	
	private PrimeModel model;
	private JMenuBar menuBar;
	@SuppressWarnings("unused")
	private JMenu fileMenu, viewMenu, chartMenu, helpMenu,
			cModeSub, vModeSub, recently;
	@SuppressWarnings("unused")
	private JMenuItem newFile, openFile, closeFile,
			zoomIn, zoomOut, origin,
			documentation, online;
	@SuppressWarnings("unused")
	private JCheckBoxMenuItem chartPrimes, chartExponent, chartMatchCount, chartFirstMatch, chartVoidCount, chartFirstVoid, chartPrimesCalc, chartMatchCountCalc, chartDivisorSum, chartEulerTotient,
			chartProp, expSum, showCharts,
			primes, factors, rectangles, helper, rays, polys, mirror, yTransform, polarFactors, showStats, polarBalance, factorsOnlyOuter, factorsOnlyNeeded, rotateView;
	private JRadioButtonMenuItem zoomMode, verticalStep, horizontalStep, verticalOffset, horizontalOffset, polySize, polyFactor, polyDelta, divisorExp,
			goldbachMode, factorMode;
	private MenuListener listener;

	public MenuView(PrimeModel model) {
		this.model = model;
		this.buildMenu();
	}
	
	public void setListener(MenuListener ml) {
		this.listener = ml;
	}
	
	public JMenuBar getMenuBar() {
		return this.menuBar;
	}
	
	private void buildMenu() {
		int CTRL = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
		int CTRL_SHIFT = CTRL + InputEvent.SHIFT_DOWN_MASK;
		int CTRL_ALT = CTRL + InputEvent.ALT_DOWN_MASK;
		
		menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		viewMenu = new JMenu("View");
		chartMenu = new JMenu("Chart");
		helpMenu = new JMenu("Help");

		// file menu
		fileMenu.add(this.connect(newFile = new JMenuItem("New"),		MenuAction.NEW_FILE, KeyEvent.VK_N,CTRL));
		fileMenu.add(this.connect(openFile = new JMenuItem("Open"),		MenuAction.OPEN_FILE, KeyEvent.VK_O,CTRL));
		fileMenu.add(this.connect(closeFile = new JMenuItem("Close"),	MenuAction.CLOSE_FILE, KeyEvent.VK_W, CTRL));
		fileMenu.add(recently = new JMenu("Recently Opened"));
		
		// view menu		
		cModeSub = new JMenu("Control Mode");
		ButtonGroup cgroup = new ButtonGroup();
		cModeSub.add(this.connect(zoomMode = new JRadioButtonMenuItem("Zoom",this.model.getPmmode()==PrimeModel.PMMode.ZOOM),									MenuAction.ZOOM,KeyEvent.VK_Z,CTRL_ALT));
		cModeSub.add(this.connect(verticalStep = new JRadioButtonMenuItem("Vertical Step",this.model.getPmmode()==PrimeModel.PMMode.VERTICAL_STEP),				MenuAction.VERTICAL_STEP,KeyEvent.VK_V,CTRL_ALT));
		cModeSub.add(this.connect(horizontalStep = new JRadioButtonMenuItem("Horizontal Step",this.model.getPmmode()==PrimeModel.PMMode.HORIZONTAL_STEP),		MenuAction.HORIZONTAL_STEP,KeyEvent.VK_H,CTRL_ALT));
		cModeSub.add(this.connect(verticalOffset = new JRadioButtonMenuItem("Vertical Offset",this.model.getPmmode()==PrimeModel.PMMode.VERTICAL_OFFSET),		MenuAction.VERTICAL_OFFSET,KeyEvent.VK_A,CTRL_ALT));
		cModeSub.add(this.connect(horizontalOffset = new JRadioButtonMenuItem("Horizontal Offset",this.model.getPmmode()==PrimeModel.PMMode.HORIZONTAL_OFFSET),	MenuAction.HORIZONTAL_OFFSET,KeyEvent.VK_W,CTRL_ALT));
		cModeSub.add(this.connect(polySize = new JRadioButtonMenuItem("Poly Size",this.model.getPmmode()==PrimeModel.PMMode.POLY_SIZE),							MenuAction.POLY_SIZE,KeyEvent.VK_S,CTRL_ALT));
		cModeSub.add(this.connect(polyFactor = new JRadioButtonMenuItem("Poly Factor",this.model.getPmmode()==PrimeModel.PMMode.POLY_FACTOR),					MenuAction.POLY_FACTOR,KeyEvent.VK_F,CTRL_ALT));
		cModeSub.add(this.connect(polyDelta = new JRadioButtonMenuItem("Poly Delta",this.model.getPmmode()==PrimeModel.PMMode.POLY_DELTA),						MenuAction.POLY_DELTA,KeyEvent.VK_D,CTRL_ALT));
		cModeSub.add(this.connect(divisorExp = new JRadioButtonMenuItem("Divisor Exponent",this.model.getPmmode()==PrimeModel.PMMode.DIVISOR_EXP),				MenuAction.DIVISOR_EXP,KeyEvent.VK_E,CTRL_ALT));
		cgroup.add(zoomMode);
		cgroup.add(verticalStep);
		cgroup.add(horizontalStep);
		cgroup.add(verticalOffset);
		cgroup.add(horizontalOffset);
		cgroup.add(polySize);
		cgroup.add(polyFactor);
		cgroup.add(polyDelta);
		cgroup.add(divisorExp);
		viewMenu.add(cModeSub);
		
		vModeSub = new JMenu("View Mode");
		ButtonGroup vgroup = new ButtonGroup();
		vModeSub.add(this.connect(goldbachMode = new JRadioButtonMenuItem("Goldbach",this.model.getPmview()==PrimeModel.PMView.GOLDBACH),	MenuAction.GOLDBACH_VIEW,KeyEvent.VK_G,CTRL_SHIFT));
		vModeSub.add(this.connect(factorMode = new JRadioButtonMenuItem("Factors",this.model.getPmview()==PrimeModel.PMView.FACTOR),	MenuAction.FACTOR_VIEW,KeyEvent.VK_F,CTRL_SHIFT));
		vgroup.add(goldbachMode);
		vgroup.add(factorMode);
		viewMenu.add(vModeSub);
		
		viewMenu.add(this.connect(zoomIn = new JMenuItem("Zoom In"),	MenuAction.ZOOM_IN,KeyEvent.VK_PLUS,CTRL));
		viewMenu.add(this.connect(zoomOut = new JMenuItem("Zoom Out"),	MenuAction.ZOOM_OUT,KeyEvent.VK_MINUS,CTRL));
		viewMenu.add(this.connect(origin = new JMenuItem("Origin"),		MenuAction.ORIGIN,KeyEvent.VK_X,CTRL_SHIFT));
		viewMenu.addSeparator();
		viewMenu.add(this.connect(primes = new JCheckBoxMenuItem("Primes",this.model.isPrimes()),					MenuAction.PRIMES,KeyEvent.VK_P,CTRL));
		viewMenu.add(this.connect(factors = new JCheckBoxMenuItem("Factors",this.model.isFactors()),				MenuAction.FACTORS,KeyEvent.VK_F,CTRL));
		viewMenu.add(this.connect(rectangles = new JCheckBoxMenuItem("Rectangles",this.model.isDrawRect()),			MenuAction.RECTANGLES,KeyEvent.VK_R,CTRL));
		viewMenu.add(this.connect(helper = new JCheckBoxMenuItem("Helper",this.model.isHelper()),					MenuAction.HELPER,KeyEvent.VK_I,CTRL));
		viewMenu.add(this.connect(rays = new JCheckBoxMenuItem("Rays",this.model.isRays()),							MenuAction.RAYS,KeyEvent.VK_L,CTRL));
		viewMenu.add(this.connect(polys = new JCheckBoxMenuItem("Polys",this.model.isPolynomials()),				MenuAction.POLYS,KeyEvent.VK_U,CTRL));
		viewMenu.add(this.connect(mirror = new JCheckBoxMenuItem("Mirror",this.model.isPrimeMirror()),				MenuAction.MIRROR,KeyEvent.VK_M,CTRL));
		viewMenu.add(this.connect(yTransform = new JCheckBoxMenuItem("Y-Transorm",this.model.isCheckedPattern()),					MenuAction.Y_TRANSFORM,KeyEvent.VK_Y,CTRL));
		viewMenu.add(this.connect(polarFactors = new JCheckBoxMenuItem("Polar Factors",this.model.isPolarFactors()),				MenuAction.POLAR_FACTORS,KeyEvent.VK_P,CTRL_SHIFT));
		viewMenu.add(this.connect(polarBalance = new JCheckBoxMenuItem("Polar Balance",this.model.isPolarBalance()),				MenuAction.POLAR_BALANCE,KeyEvent.VK_B,CTRL_SHIFT));
		viewMenu.add(this.connect(factorsOnlyOuter = new JCheckBoxMenuItem("Factors Only Outer",this.model.isFactorOnlyOuter()),	MenuAction.FACTORS_ONLY_OUTER,KeyEvent.VK_O,CTRL_SHIFT));
		viewMenu.add(this.connect(factorsOnlyNeeded = new JCheckBoxMenuItem("Factors Only Needed",this.model.isFactorOnlyNeeded()),	MenuAction.FACTORS_ONLY_NEEDED,KeyEvent.VK_N,CTRL_SHIFT));
		viewMenu.add(this.connect(rotateView = new JCheckBoxMenuItem("Rotate View",this.model.isRotateView()),						MenuAction.ROTATE_VIEW,KeyEvent.VK_R,CTRL_SHIFT));
		viewMenu.add(this.connect(showStats = new JCheckBoxMenuItem("Show Stats",this.model.isStats()),								MenuAction.SHOW_STATS,KeyEvent.VK_S,CTRL_SHIFT));
		
		// chart menu
		chartMenu.add(this.connect(chartPrimes = new JCheckBoxMenuItem("Primes (Algo)",this.model.isChartPrimes()),						MenuAction.CHART_PRIMES,KeyEvent.VK_1,CTRL));
		chartMenu.add(this.connect(chartExponent = new JCheckBoxMenuItem("Exponent Count",this.model.isChartExp()),						MenuAction.CHART_EXPONENTS,KeyEvent.VK_2,CTRL));
		chartMenu.add(this.connect(chartMatchCount = new JCheckBoxMenuItem("Match Count (Algo)",this.model.isChartMatchCount()),		MenuAction.CHART_MATCH_COUNT,KeyEvent.VK_3,CTRL));
		chartMenu.add(this.connect(chartFirstMatch = new JCheckBoxMenuItem("First Match",this.model.isChartFirstMatch()),				MenuAction.CHART_FIRST_MATCH,KeyEvent.VK_4,CTRL));
		chartMenu.add(this.connect(chartVoidCount = new JCheckBoxMenuItem("Void Count",this.model.isChartVoidCount()),					MenuAction.CHART_VOID_COUNT,KeyEvent.VK_5,CTRL));
		chartMenu.add(this.connect(chartFirstVoid = new JCheckBoxMenuItem("First Void",this.model.isChartFirstVoid()),					MenuAction.CHART_FIRST_VOID,KeyEvent.VK_6,CTRL));
		chartMenu.add(this.connect(chartPrimesCalc = new JCheckBoxMenuItem("Primes (Calc)",this.model.isChartPrimeCountCalc()),			MenuAction.CHART_PRIMES_CALC,KeyEvent.VK_7,CTRL));
		chartMenu.add(this.connect(chartMatchCountCalc = new JCheckBoxMenuItem("Match Count (Calc)",this.model.isChartMatchCountCalc()),MenuAction.CHART_MATCH_COUNT_CALC,KeyEvent.VK_8,CTRL));
		chartMenu.add(this.connect(chartDivisorSum = new JCheckBoxMenuItem("Divisor Function",this.model.isChartDivisorSum()),			MenuAction.CHART_DIVISOR_SUM,KeyEvent.VK_9,CTRL));
		chartMenu.add(this.connect(chartEulerTotient = new JCheckBoxMenuItem("Euler's Totient",this.model.isChartEulerTotient()),		MenuAction.CHART_EULER_TOTIENT,KeyEvent.VK_0,CTRL));
		chartMenu.addSeparator();
		chartMenu.add(this.connect(chartProp = new JCheckBoxMenuItem("Proportional",this.model.isChartProp()),	MenuAction.CHART_PROP,KeyEvent.VK_T,CTRL));
		chartMenu.add(this.connect(expSum = new JCheckBoxMenuItem("Exponent Sum",this.model.isChartExpSum()),	MenuAction.EXPONENT_SUM,KeyEvent.VK_E,CTRL));
		chartMenu.add(this.connect(showCharts = new JCheckBoxMenuItem("Show Charts",this.model.isChart()),		MenuAction.SHOW_CHARTS,KeyEvent.VK_C,CTRL_SHIFT));
		
		// help menu
		helpMenu.add(this.connect(documentation = new JMenuItem("Documentation"), MenuAction.DOCUMENTATION));
		helpMenu.add(this.connect(online = new JMenuItem("About PrimeExplorer"), MenuAction.ABOUT));
		
		menuBar.add(fileMenu);
		menuBar.add(viewMenu);
		menuBar.add(chartMenu);
		menuBar.add(helpMenu);
	}
	
	private ActionListener createActionListener(final MenuAction act) {
		return new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				listener.handle(act);
			}
			
		};
	}
	
	private JMenuItem connect(JMenuItem item, MenuAction act) {
		item.addActionListener(this.createActionListener(act));
		return item;
	}
	
	private JMenuItem connect(JMenuItem item, MenuAction act, int key, int modifier) {
		item.setAccelerator(KeyStroke.getKeyStroke(key,modifier));
		item.addActionListener(this.createActionListener(act));
		return item;
	}
	
}
