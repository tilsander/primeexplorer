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

import com.google.gson.Gson;

public class StateLoader {
	
	public Map<String,PrimeModel> loadModels(Settings settings) {
		HashMap<String,PrimeModel> models = new HashMap<String,PrimeModel>(); 
		for (String model : settings.getOpenModels()) {
			models.put(model,this.loadModel(model));
		}
		return models;
	}
	
	public void saveModels(Map<String,PrimeModel> models) {
		Iterator iter = models.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String file_name = (String) entry.getKey();
			PrimeModel model = (PrimeModel) entry.getValue();
			this.saveModel(model, file_name);
		}
	}

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
	
	public Settings loadSettings() {
		File setting_file = new File(this.getHomeDirectory(),"settings.json");
		if (setting_file.exists()) {
			BufferedReader br=null;
			try {
				br = new BufferedReader(new FileReader(setting_file));
				String sCurrentLine, json="";
				try {
					while ((sCurrentLine = br.readLine()) != null) {
						json += sCurrentLine;
					}
					return (new Gson().fromJson(json,Settings.class));
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
		return new Settings();
	}
	
	public void saveSettings(Settings settings) {
		File setting_file = new File(this.getHomeDirectory(),"settings.json");
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
			bw.write(new Gson().toJson(settings));
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getHomeDirectory() {
		return this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	}
	
}
