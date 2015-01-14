package de.sander.til;

public class PrimeExplorer {
	
	private PrimeModel model;
	private PrimeController controller;

	public static void main(String[] args) {
		System.out.println("5.5%4: "+5.5%4);
		PrimeExplorer pe = new PrimeExplorer();
		pe.run();
		System.exit(0);
	}
	
	public PrimeExplorer() {
		this.model = new StateLoader().loadModel();
		this.controller = new PrimeController(this.model,new PrimeView(model));
		Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
            	new StateLoader().saveModel(model);
                controller.stop();
            }
        });
	}
	
	public void run() {
		this.controller.start();
	}

}
