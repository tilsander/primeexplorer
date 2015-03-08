package de.sander.til;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class Settings {
	
	private State state;
	private Map<String,PrimeModel> open_models;
	private SettingsListener listener;
	
	/**
	 * This class captures the state of the settings.
	 * 
	 */
	static class State {
		
		private Set<String> open_models;
		private Map<Long,String> recently_opened;
		private String current_model;
		
		public State() {
			this.open_models = new HashSet<String>();
			this.recently_opened = new TreeMap<Long,String>();
		}
		
	}
	
	/**
	 * 
	 */
	public Settings() {
		this(new State());
	}
	
	/**
	 * 
	 * @param state a state object that should be used by this settings instance
	 */
	public Settings(State state) {
		this.state = state;
		StateLoader sl = new StateLoader();
		this.open_models = sl.loadModels(this);
		if (this.open_models.isEmpty()) {
			String model_file = PrimeUtil.getNewFile(PrimeUtil.getContentDir(), "primes.json").getPath();
			PrimeModel model = new PrimeModel();
			model.setTitle(new File(model_file).getName());
			this.open_models.put(model_file,model);
			this.state.current_model = model_file;
		}
		this.setCurrentModel(this.state.current_model);
	}
	
	/**
	 * set the listener for setting changes
	 * @param listener
	 */
	public void setListener(SettingsListener listener) {
		this.listener = listener;
	}
	
	/**
	 * @return the application setting state
	 */
	public State getState() {
		return this.state;
	}
	
	/**
	 * 
	 * @return all open models, stored by file name
	 */
	public Map<String,PrimeModel> getOpenModels() {
		return this.open_models;
	}
	
	/**
	 * 
	 * @return file names of all open models
	 */
	public Set<String> getOpenModelNames() {
		return this.state.open_models;
	}
	
	/**
	 * 
	 * @return recently opened files stored by closing time as unix timestamp
	 */
	public Map<Long,String> getRecentlyOpened() {
		return this.state.recently_opened;
	}
	
	/**
	 * 
	 * @return the current model
	 */
	public PrimeModel getCurrentModel() {
		return this.open_models.get(this.state.current_model);
	}
	
	/**
	 * 
	 * @return the file name of the current model
	 */
	public String getCurrentModelName() {
		return this.state.current_model;
	}
	
	/**
	 * sets the currently focused model
	 * @param cur_mod
	 */
	public void setCurrentModel(String cur_mod) {
		if (cur_mod == null) return;
		this.state.current_model = cur_mod;
		this.openModel(cur_mod);
		if (this.listener != null) this.listener.modelChanged(this.open_models.get(cur_mod));
	}
	
	/**
	 * sets the currently focused model
	 * @param cur_mod
	 */
	public void setCurrentModel(PrimeModel cur_mod) {
		this.setCurrentModel(this.getFileName(cur_mod));
	}
	
	/**
	 * Create a prime model and open it. The model is set as the current model.
	 * @param fileName a file name
	 */
	public void createModel(String fileName) {
		PrimeModel model = new PrimeModel();
		model.setTitle(new File(fileName).getName());
		this.open_models.put(fileName,model);
		if (this.listener != null) this.listener.modelCreated(model);
		this.openModel(fileName);
		this.setCurrentModel(model);
	}
	
	/**
	 * Open the prime model with the given file name
	 * @param fileName a file name
	 */
	public void openModel(String fileName) {
		this.state.open_models.add(fileName);
		this.state.recently_opened.values().remove(fileName);
		if (this.open_models.get(fileName) == null) this.open_models.put(fileName, new StateLoader().loadModel(fileName));
		if (this.listener != null) this.listener.modelOpened(this.open_models.get(fileName));
		if (this.listener != null) this.listener.recentlyOpenedChanged();
	}
	
	/**
	 * Close the model with the given file name
	 * @param model a prime model
	 */
	public void closeModel(String model) {
		new StateLoader().saveModel(this.open_models.get(model), model);
		if (this.listener != null) this.listener.modelClosed(this.open_models.get(model));
		this.state.open_models.remove(model);
		this.state.recently_opened.put(System.currentTimeMillis(),model);
		this.open_models.remove(model);
		if (this.open_models.size() == 0) {
			if (this.listener != null) this.listener.closeApp();
		} else {
			Iterator<Entry<String, PrimeModel>> iter = this.open_models.entrySet().iterator();
			Map.Entry<String, PrimeModel> entry = iter.next();
			if (entry != null) this.setCurrentModel(entry.getKey());
		}
		if (this.listener != null) this.listener.recentlyOpenedChanged();
	}
	
	/**
	 * Close the model
	 * @param model a prime model
	 */
	public void closeModel(PrimeModel model) {
		this.closeModel(this.getFileName(model));
	}
	
	/**
	 * 
	 * @param model a prime model
	 * @return the file name of the model
	 */
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
