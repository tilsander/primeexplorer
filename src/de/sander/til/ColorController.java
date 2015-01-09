package de.sander.til;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorController implements ActionListener, ChangeListener {
	
	private PrimeModel model;
	private ColorView view;
	
	public ColorController(PrimeModel pmod, ColorView cview) {
		this.model = pmod;
		this.view = cview;
		this.view.setController(this);
	}

	public void actionPerformed(ActionEvent e) {
		this.view.setColor(this.model.getColor(this.view.getSelectedColorName()));
	}

	public void stateChanged(ChangeEvent e) {
		this.model.setColor(this.view.getSelectedColorName(), this.view.getColor());
	}

}
