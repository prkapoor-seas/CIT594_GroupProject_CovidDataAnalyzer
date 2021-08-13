package edu.upenn.cit594.util;

public class PopulationData {
	
	private final int zipcode;
	private final int population;
	
	public PopulationData(int zipcode, int population) {
		this.zipcode = zipcode;
		this.population = population;
	}

	public int getZipcode() {
		return zipcode;
	}

	public int getPopulation() {
		return population;
	}

}
