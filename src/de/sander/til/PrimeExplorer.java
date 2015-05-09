package de.sander.til;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * This class is used to create and run the application
 * @author Til Sander
 */
public class PrimeExplorer {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(PrimeExplorer.class.getSimpleName());

	public static void main(String[] args) {
		
		PrimeApplication app = new PrimeApplication();
		app.run();
		
		System.exit(0);
	}

}
