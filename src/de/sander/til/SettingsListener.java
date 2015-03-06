package de.sander.til;

public interface SettingsListener {
	
	/**
	 * a model was created
	 * @param model
	 */
	public void modelCreated(PrimeModel model);
	/**
	 * a model was opened
	 * @param model
	 */
	public void modelOpened(PrimeModel model);
	/**
	 * a model was closed
	 * @param model
	 */
	public void modelClosed(PrimeModel model);
	/**
	 * a model was set as the current/focused
	 * @param model
	 */
	public void modelChanged(PrimeModel model);
	/**
	 * there are no mode open, close the app
	 */
	public void closeApp();
	
}
