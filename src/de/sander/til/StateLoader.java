package de.sander.til;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A StateLoader is used to load settings and models from json files
 */
public class StateLoader {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(StateLoader.class.getSimpleName());
	
	/**
	 * load all models that are open in the setting
	 * @param settings
	 * @return all open models stored by file name
	 */
	public Map<String,PrimeModel> loadModels(Settings settings) {
		HashMap<String,PrimeModel> models = new HashMap<String,PrimeModel>(); 
		for (String model : settings.getOpenModelNames()) {
			models.put(model,this.loadModel(model));
		}
		return models;
	}
	
	/**
	 * save all prime models
	 * @param models the prime models stored by file name
	 */
	public void saveModels(Map<String,PrimeModel> models) {
		Iterator<Entry<String, PrimeModel>> iter = models.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, PrimeModel> entry = (Map.Entry<String, PrimeModel>) iter.next();
			String file_name = (String) entry.getKey();
			PrimeModel model = (PrimeModel) entry.getValue();
			this.saveModel(model, file_name);
		}
	}

	/**
	 * load a prime model from file
	 * @param file_name the file name of the model
	 * @return the prime model
	 */
	public PrimeModel loadModel(String file_name) {
		File saved_model = new File(file_name);
		if (saved_model.exists()) {
			BufferedReader br=null;
			try {
				br = new BufferedReader(new FileReader(saved_model));
				String sCurrentLine, json="";
				try {
					while ((sCurrentLine = br.readLine()) != null) {
						json += sCurrentLine;
					}
					return (new Gson().fromJson(json,PrimeModel.class));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return new PrimeModel();
	}
	
	/**
	 * save the model to file
	 * @param model a prime model
	 * @param file_name a file name
	 */
	public void saveModel(PrimeModel model, String file_name) {
		File saved_model = new File(file_name);
    	if (!saved_model.exists()) {
    		try {
				saved_model.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			saved_model.delete();
		}
		FileWriter fw;
		try {
			fw = new FileWriter(saved_model.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(new Gson().toJson(model));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * load the local settings
	 * @return a settings instance
	 */
	public Settings loadSettings() {
		File setting_file = new File(PrimeUtil.getConfigurationDir(),"settings.json");
		if (setting_file.exists()) {
			BufferedReader br=null;
			try {
				br = new BufferedReader(new FileReader(setting_file));
				String sCurrentLine, json="";
				try {
					while ((sCurrentLine = br.readLine()) != null) {
						json += sCurrentLine;
					}
					return new Settings(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create().fromJson(json,Settings.State.class));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Settings settings = new Settings();
		return settings;
	}
	
	/**
	 * save the settings to the local settings.json file
	 * @param settings
	 */
	public void saveSettings(Settings settings) {
		File setting_file = new File(PrimeUtil.getConfigurationDir(),"settings.json");
    	if (!setting_file.exists()) {
    		try {
    			setting_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			setting_file.delete();
		}
		FileWriter fw;
		try {
			fw = new FileWriter(setting_file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create().toJson(settings.getState()));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
