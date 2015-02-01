package de.sander.til;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class StateLoader {

	public PrimeModel loadModel() {
		File saved_model = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(),"model.json");
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
	
	public void saveModel(PrimeModel model) {
		File saved_model = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(),"model.json");
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
	
}
