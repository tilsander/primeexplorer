package de.sander.til;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Settings {
	
	private State state;
	private Map<String,PrimeModel> open_models;
	private SettingsListener listener;
	
	static class State {
		
		private Set<String> open_models, recently_opened;
		private String current_model;
		
		public State() {
			this.open_models = new HashSet<String>();
			this.recently_opened = new HashSet<String>();
		}
		
	}
	
	public Settings() {
		this(new State());
	}
	
	public Settings(State state) {
		this.state = state;
		StateLoader sl = new StateLoader();
		this.open_models = sl.loadModels(this);
		if (this.open_models.isEmpty()) {
			String model_file = PrimeUtil.getNewFile(sl.getHomeDirectory(), "primes.json").getPath();
			this.open_models.put(model_file,new PrimeModel());
		}
		this.setCurrentModel(this.state.current_model);
	}
	
	public void setListener(SettingsListener listener) {
		this.listener = listener;
	}
	
	public State getState() {
		return this.state;
	}
	
	public Map<String,PrimeModel> getOpenModels() {
		return this.open_models;
	}
	
	public Set<String> getOpenModelNames() {
		return this.state.open_models;
	}
	
	public Set<String> getRecentlyOpened() {
		return this.state.recently_opened;
	}
	
	public PrimeModel getCurrentModel() {
		return this.open_models.get(this.state.current_model);
	}
	
	public String getCurrentModelName() {
		return this.state.current_model;
	}
	
	public void setCurrentModel(String cur_mod) {
		if (cur_mod == null) return;
		this.state.current_model = cur_mod;
		this.openModel(cur_mod);
		if (this.listener != null) this.listener.modelChanged(this.open_models.get(cur_mod));
	}
	
	public void setCurrentModel(PrimeModel cur_mod) {
		this.setCurrentModel(this.getFileName(cur_mod));
	}
	
	public void openModel(String model) {
		this.state.open_models.add(model);
		this.state.recently_opened.remove(model);
		if (this.listener != null) this.listener.modelOpened(this.open_models.get(model));
	}
	
	public void closeModel(String model) {
		if (this.listener != null) this.listener.modelClosed(this.open_models.get(model));
		this.state.open_models.remove(model);
		this.state.recently_opened.add(model);
	}
	
	private String getFileName(PrimeModel model) {
		Iterator<Entry<String, PrimeModel>> iter = open_models.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String,PrimeModel> entry = (Map.Entry<String,PrimeModel>) iter.next();
			String file_name = (String) entry.getKey();
			PrimeModel mod = (PrimeModel) entry.getValue();
			if (mod == model) return file_name;
		}
		return null;
	}

}
