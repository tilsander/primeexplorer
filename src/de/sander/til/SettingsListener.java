package de.sander.til;

public interface SettingsListener {
	
	public void modelCreated(PrimeModel model);
	public void modelOpened(PrimeModel model);
	public void modelClosed(PrimeModel model);
	public void modelChanged(PrimeModel model);
	public void closeApp();
	
}
