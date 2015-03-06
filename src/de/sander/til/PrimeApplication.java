package de.sander.til;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PrimeApplication implements SettingsListener {
	
	private Settings settings;
	private Map<PrimeModel,PrimeController> controller;
	private boolean EXIT=false;
	private ColorController colorer;

	public PrimeApplication() {
		this.settings = new StateLoader().loadSettings();
		this.settings.setListener(this);
		this.controller = new HashMap<PrimeModel,PrimeController>();
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
		this.colorer = new ColorController(this.settings.getCurrentModel());
	}
	
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
	
	public void stopApp() {
		this.EXIT = true;
	}
	
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
			this.colorer.setModel(model);
		}
	}

	@Override
	public void closeApp() {
		this.stopApp();
	}
	
}
