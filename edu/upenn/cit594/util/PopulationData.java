package edu.upenn.cit594.util;

public class PopulationData {
	
	private final Integer zipcode;
	private final Integer population;
	
	public PopulationData(Integer zipcode, Integer population) {
		this.zipcode = zipcode;
		this.population = population;
	}

	public Integer getZipcode() {
		return zipcode;
	}

	public Integer getPopulation() {
		return population;
	}

}
