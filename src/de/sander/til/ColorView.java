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
		this.viewFrame.setSize(450, 430);
		this.viewFrame.setResizable(false);
		this.colors = new JComboBox<String>(model.getColors());
		this.colors.setSelectedIndex(1);
		this.chooser = new JColorChooser(model.getColor((String)this.colors.getSelectedItem()));
		this.viewFrame.getContentPane().add(this.colors, BorderLayout.NORTH);
		this.viewFrame.getContentPane().add(this.chooser, BorderLayout.SOUTH);
		this.viewFrame.setLocation(1200, 0);
		this.viewFrame.setVisible(true);
	}
	
	public void setController(ColorController controller) {
		this.controller = controller;
		this.colors.addActionListener(this.controller);
		this.chooser.getSelectionModel().addChangeListener(this.controller);
	}
	
	public void setColor(Color col) {
		this.chooser.setColor(col);
	}
	
	public Color getColor() {
		return this.chooser.getColor();
	}
	
	public String getSelectedColorName() {
		return (String)this.colors.getSelectedItem();
	}

}
