package de.sander.til;

import java.io.File;
import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This class provides some static auxiliary functions
 */
public class PrimeUtil {
	
	/**
	 * Create a unused filename. The path and file_name are used as a base.
	 * @param path a file path
	 * @param file_name a file name
	 * @return a file which does not exist in the file system.
	 */
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
	
	/**
	 * open a webpage with the given uri
	 * @param uri
	 */
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
	
	/**
	 * open a webpage with the given url
	 * @param uri
	 */
	public static void openWebpage(URL url) {
	    try {
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	}
	
	/**
	 * open a webpage with the given url
	 * @param site
	 */
	public static void openWebpage(String site) {
		try {
			PrimeUtil.openWebpage(new URL(site));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the home directory name of the PrimeExplorer
	 */
	public static String getRootDir() {
		return new File(new PrimeUtil().getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent();
	}
	
	/**
	 * 
	 * @return the content directory
	 */
	public static String getContentDir() {
		return new File(PrimeUtil.getRootDir(),"content").getPath();
	}
	
	/**
	 * 
	 * @return the documentation directory
	 */
	public static String getDocDir() {
		File doc = new File(PrimeUtil.getRootDir(),"doc");
		if (doc.exists()) return doc.getPath();
		return null; 
	}
	
	/**
	 * 
	 * @return the path to the documentation index.html file
	 */
	public static String getDocIndex() {
		String doc = PrimeUtil.getDocDir();
		if (doc == null) return null;
		File index = new File(doc,"index.html");
		if (index.exists()) return index.getPath();
		return null;
	}

}