package de.sander.til;

public class PrimeExplorer {
	
	private PrimeModel model;
	private PrimeController controller;

	public static void main(String[] args) {
		PrimeExplorer pe = new PrimeExplorer();
		pe.run();
		System.exit(0);
	}
	
	public PrimeExplorer() {
		
		//PrimeTest.test();
		
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
