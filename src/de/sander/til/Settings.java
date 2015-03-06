package de.sander.til;

import java.util.HashSet;
import java.util.Set;

public class Settings {
	
	private Set<String> open_models, recently_opened;
	private String current_model;
	
	public Settings() {
		this.open_models = new HashSet<String>();
		this.recently_opened = new HashSet<String>();
	}
	
	public Set<String> getOpenModels() {
		return this.open_models;
	}
	
	public Set<String> getRecentlyOpened() {
		return this.recently_opened;
	}
	
	public String getCurrentModel() {
		return this.current_model;
	}
	
	public void setCurrentModel(String cur_mod) {
		this.current_model = cur_mod;
		this.openModel(cur_mod);
	}
	
	public void openModel(String model) {
		this.open_models.add(model);
		this.recently_opened.remove(model);
	}
	
	public void closeModel(String model) {
		this.open_models.remove(model);
		this.recently_opened.add(model);
	}

}
