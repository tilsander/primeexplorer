package de.sander.til;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

public class PrimeExplorer {
	
	private PrimeModel model;
	private PrimeView view;
	private PrimeController controller;
	private JTextField typingArea;

	public static void main(String[] args) {
		PrimeExplorer pe = new PrimeExplorer();
		pe.init();
		pe.run();
	}
	
	public void init() {
		JFrame frame = new JFrame();
		this.model = new PrimeModel();
		this.view = new PrimeView(model);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(view);
		typingArea = new JTextField(1);
		typingArea.setPreferredSize(new Dimension(3, 2));
		view.add(typingArea, BorderLayout.PAGE_END);
		typingArea.setOpaque(true);
		frame.getContentPane().add(view);
		frame.setSize(1200, 800);
		frame.setVisible(true);
		this.controller = new PrimeController(this.model,this.view);
		typingArea.addKeyListener(this.controller);
	}
	
	public void run() {
		this.controller.start();
	}

}
