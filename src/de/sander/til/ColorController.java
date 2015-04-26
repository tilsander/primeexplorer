package de.sander.til;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class controls the color chooser view.
 */
public class ColorController implements ActionListener, ChangeListener {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(ColorController.class.getSimpleName());
	
	private PrimeModel model;
	private ColorView view;
	
	public ColorController(PrimeModel pmod) {
		this.model = pmod;
		this.view = new ColorView(this.model);
		this.view.setController(this);
	}

	/**
	 * ComboBox value has changed
	 */
	public void actionPerformed(ActionEvent e) {
		this.updateView();
	}

	/**
	 * Color changed
	 */
	public void stateChanged(ChangeEvent e) {
		this.model.setColor(this.view.getSelectedColorName(), this.view.getColor());
	}
	
	/**
	 * 
	 * @param model a PrimeModel that should be displayed by the chooser
	 */
	public void setModel(PrimeModel model) {
		this.model = model;
		this.updateView();
	}
	
	/**
	 * update the chooser color
	 */
	private void updateView() {
		this.view.setColor(this.model.getColor(this.view.getSelectedColorName()));
	}

}
