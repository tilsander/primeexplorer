package de.sander.til;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class PrimeExplorer {
	
	private PrimeModel model;
	private PrimeController controller;

	public static void main(String[] args) {
		PrimeExplorer pe = new PrimeExplorer();
		pe.init();
		pe.run();
		System.exit(0);
	}
	
	public void init() {
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
					this.model = new Gson().fromJson(json,PrimeModel.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		if (this.model == null) this.model = new PrimeModel();
		this.controller = new PrimeController(this.model,new PrimeView(model));
		Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
            	File saved_model = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(),"model.json");
            	if (!saved_model.exists()) {
            		try {
						saved_model.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                controller.stop();
            }
        });
	}
	
	public void run() {
		this.controller.start();
	}

}
