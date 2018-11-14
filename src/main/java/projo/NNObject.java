package projo;

import java.util.List;

public class NNObject {
	
	private List<Double> features;
	private String name;

	public NNObject(List<Double> features, String name) {
		this.features=features;
		this.name=name;
	}
	
	
	public List<Double> getFeatures(){
		return this.features;
	}
	
	public String getName() {
		return this.name;
	}
}
