package de.sander.til;

public interface SettingsListener {
	
	public void modelOpened(PrimeModel model);
	public void modelClosed(PrimeModel model);
	public void modelChanged(PrimeModel model);
	
}
