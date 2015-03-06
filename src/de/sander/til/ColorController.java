package de.sander.til;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorController implements ActionListener, ChangeListener {
	
	private PrimeModel model;
	private ColorView view;
	
	public ColorController(PrimeModel pmod) {
		this.model = pmod;
		this.view = new ColorView(this.model);
		this.view.setController(this);
	}

	public void actionPerformed(ActionEvent e) {
		this.updateView();
	}

	public void stateChanged(ChangeEvent e) {
		this.model.setColor(this.view.getSelectedColorName(), this.view.getColor());
	}
	
	public void setModel(PrimeModel model) {
		this.model = model;
		this.updateView();
	}
	
	private void updateView() {
		this.view.setColor(this.model.getColor(this.view.getSelectedColorName()));
	}

}
