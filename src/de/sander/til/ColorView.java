package de.sander.til;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColorView extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private ColorController controller;
	private JFrame viewFrame;
	private JComboBox<String> colors;
	private JColorChooser chooser;
	
	public ColorView(PrimeModel model) {
		this.viewFrame = new JFrame();
		this.viewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.viewFrame.getContentPane().add(this);
		this.viewFrame.setSize(700, 430);
		this.viewFrame.setResizable(false);
		this.colors = new JComboBox<String>(model.getColors());
		this.colors.setSelectedIndex(1);
		this.chooser = new JColorChooser(model.getColor((String)this.colors.getSelectedItem()));
		this.viewFrame.getContentPane().add(this.colors, BorderLayout.NORTH);
		this.viewFrame.getContentPane().add(this.chooser, BorderLayout.SOUTH);
		this.viewFrame.setLocation(1200, 0);
		this.viewFrame.setVisible(true);
	}
	
	/**
	 * sets the controller of this view and adds it as a listener
	 * @param controller a color view controller
	 */
	public void setController(ColorController controller) {
		this.controller = controller;
		this.colors.addActionListener(this.controller);
		this.chooser.getSelectionModel().addChangeListener(this.controller);
	}
	
	/**
	 * sets the color of the chooser
	 * @param col the color to display
	 */
	public void setColor(Color col) {
		this.chooser.setColor(col);
	}
	
	/**
	 * @return the current color
	 */
	public Color getColor() {
		return this.chooser.getColor();
	}
	
	/**
	 * 
	 * @return the selected color name
	 */
	public String getSelectedColorName() {
		return (String)this.colors.getSelectedItem();
	}

}
