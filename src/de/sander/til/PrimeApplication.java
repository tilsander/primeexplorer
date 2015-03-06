package de.sander.til;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PrimeApplication {
	
	private Map<String,PrimeModel> open_models;
	private Settings settings;
	private List<PrimeController> controller;
	private boolean EXIT=false;

	public PrimeApplication() {
		StateLoader sl = new StateLoader(); 
		this.settings = sl.loadSettings();
		this.open_models = sl.loadModels(this.settings);
		if (this.open_models.isEmpty()) {
			String model_file = PrimeUtil.getNewFile(sl.getHomeDirectory(), "primes.json").getPath();
			this.open_models.put(model_file,new PrimeModel());
			this.settings.setCurrentModel(model_file);
		}
		this.controller = new ArrayList<PrimeController>();
		Iterator iter = open_models.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String file_name = (String) entry.getKey();
			PrimeModel model = (PrimeModel) entry.getValue();
			this.controller.add(new PrimeController(model,new PrimeView(model)));
		}
		Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
            	StateLoader sl = new StateLoader();
            	sl.saveModels(open_models);
            	sl.saveSettings(settings);
            	stopApp();
            }
        });
	}
	
	public void run() {
		while (true) {
			if (EXIT) return;
			for (PrimeController pcon : this.controller) pcon.updateView();
			this.sleep(60);
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
	
}
