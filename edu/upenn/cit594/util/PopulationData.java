package edu.upenn.cit594.util;

import java.util.LinkedList;
import java.util.List;

public class PopulationData {
	
	private final int zipcode;
	private final int population;
	private LinkedList<Double> marketValues = new LinkedList<Double>();
	private LinkedList<Double> livableAreas = new LinkedList<Double>();
	
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

	public List<Double> getMarketValues() {return marketValues;}

	public List<Double> getLivableAreas() {return livableAreas;}

	public void appendMarketValue(Double marketValue) {
		marketValues.add(marketValue);
	}

	public void appendLivableArea(Double liveableArea) {
		livableAreas.add(liveableArea);
	}

}
