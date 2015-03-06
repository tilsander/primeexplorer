package de.sander.til;

import java.io.File;
import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class PrimeUtil {
	
	public static File getNewFile(String path, String file_name) {
		File file = new File(path,file_name);
		int suffix = 2, last_index;
		String new_file_name=null;
		while (file.exists()) {
			last_index = file_name.lastIndexOf(".");
			if (last_index < 0) new_file_name = file_name + suffix;
			else new_file_name = file_name.substring(0, last_index) + suffix + file_name.substring(last_index, file_name.length());
			file = new File(path,new_file_name);
		}
		return file;
	}
	
	public static void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	public static void openWebpage(URL url) {
	    try {
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	}
	
	public static void openWebpage(String site) {
		try {
			PrimeUtil.openWebpage(new URL(site));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}