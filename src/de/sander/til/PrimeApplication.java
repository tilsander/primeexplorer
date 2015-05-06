package de.sander.til;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class loads the settings and models and creates a view controller for every model.
 * The controllers are updated in a continuous update cycle. 
 * It listens for changes in the settings.
 */
public class PrimeApplication implements SettingsListener {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PrimeApplication.class.getSimpleName());
	
	private Settings settings;
	private Map<PrimeModel,PrimeController> controller;
	private boolean EXIT=false;
	private ColorController colorer;

	/**
	 * 
	 */
	public PrimeApplication() {
		StateLoader sl = new StateLoader();
		this.settings = sl.loadSettings();
		this.settings.setListener(this);
		this.controller = new HashMap<PrimeModel,PrimeController>();
		this.colorer = new ColorController(this.settings.getCurrentModel());
		Iterator<Entry<String, PrimeModel>> iter = this.settings.getOpenModels().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, PrimeModel> entry = (Map.Entry<String, PrimeModel>) iter.next();
			PrimeModel model = (PrimeModel) entry.getValue();
			this.controller.put(model,new PrimeController(model,this.settings));
		}
		Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
            	StateLoader sl = new StateLoader();
            	sl.saveModels(settings.getOpenModels());
            	sl.saveSettings(settings);
            	stopApp();
            }
        });
	}
	
	/**
	 * run the application
	 */
	public void run() {
		while (true) {
			if (EXIT) return;
			Iterator<Entry<PrimeModel, PrimeController>> iter = this.controller.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<PrimeModel,PrimeController> entry = (Map.Entry<PrimeModel,PrimeController>) iter.next();
				PrimeController pcon = (PrimeController) entry.getValue();
				if (pcon != null) pcon.updateView();
			}
			this.sleep(100);
		}
	}
	
	/**
	 * set the exit flag to stop the run cycle
	 */
	public void stopApp() {
		this.EXIT = true;
	}
	
	/**
	 * sleep for the specified milliseconds
	 * @param milis
	 */
	public void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch(Exception e) {}
	}

	@Override
	public void modelCreated(PrimeModel model) {

	}

	@Override
	public void modelOpened(PrimeModel model) {
		PrimeController pcon = this.controller.get(model);
		if (pcon == null) this.controller.put(model, new PrimeController(model,this.settings));
	}

	@Override
	public void modelClosed(PrimeModel model) {
		PrimeController pcon = this.controller.get(model);
		if (pcon != null) pcon.closeView();
	}

	@Override
	public void modelChanged(PrimeModel model) {
		PrimeController pcon = this.controller.get(model);
		if (pcon != null) {
			pcon.focusView();
			if (this.colorer == null) System.out.println("no colorer");
			this.colorer.setModel(model);
		}
	}

	@Override
	public void closeApp() {
		this.stopApp();
	}

	@Override
	public void recentlyOpenedChanged() {
		Iterator<Entry<PrimeModel, PrimeController>> iter = this.controller.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<PrimeModel,PrimeController> entry = (Map.Entry<PrimeModel,PrimeController>) iter.next();
			PrimeController pcon = (PrimeController) entry.getValue();
			if (pcon != null) pcon.getMenu().updateRecentlyOpened();
		}
	}
	
}
