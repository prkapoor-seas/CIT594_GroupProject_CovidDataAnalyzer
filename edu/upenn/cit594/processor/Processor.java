package edu.upenn.cit594.processor;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import edu.upenn.cit594.datamanagement.CovidDataReader;
import edu.upenn.cit594.datamanagement.PopulationDataReader;
import edu.upenn.cit594.datamanagement.PropertiesReader;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertiesData;

public class Processor {

	protected CovidDataReader covidReader;
	protected List<CovidData> covidData;
	protected PopulationDataReader popReader;
	protected PropertiesReader propertiesReader;
	protected List<PopulationData> popData;
	protected List<PropertiesData> propertiesData;
	
	// memoization
	private static int totalPopulation = 0;
	private static TreeMap<Integer, Double> fullMap = new TreeMap<Integer, Double>();
	private static TreeMap<Integer, Double> partialMap = new TreeMap<Integer, Double>();
	private static HashMap<Integer, Integer> avgMktValMap = new HashMap<Integer, Integer>();
	private static HashMap<Integer, Integer> avgLivMap = new HashMap<Integer, Integer>();

	
	
	public Processor(CovidDataReader covidReader, PopulationDataReader popReader) throws Exception{
		this.covidReader = covidReader;
		this.covidData = covidReader.getAllRows();
		this.popReader = popReader;
		this.popData = popReader.getAllRows();
		this.propertiesData = propertiesReader.getAllRows();
	}
	
	// This method returns the answer to 1
	public int getTotalPopulationAllZips() {
		
		if(totalPopulation != 0) {
			return totalPopulation;
		}
		
		int totalPop = 0;
		for(int i = 0; i < this.popData.size(); i++) {
			PopulationData data = this.popData.get(i);
			totalPop += data.getPopulation();
		}
		
		totalPopulation = totalPop;
		return totalPop;
	}
	
	
	// This method returns the answer to 2a
	public TreeMap<Integer, Double> getFullyVaccinatedPerCapita(){
		
		if(fullMap != null) {
			return fullMap;
		}
		
		TreeMap<Integer, Double> map = new TreeMap<Integer, Double>();
		
		for(int i = 0; i < this.popData.size(); i++) {
			PopulationData data = this.popData.get(i);
			int zip = data.getZipcode();
			double population = (double) data.getPopulation();
			double fullyVaccinated = 0;
			for(int j =0; j < this.covidData.size(); j++) {
				if(this.covidData.get(j).getZipcode() == zip ) {
					fullyVaccinated += this.covidData.get(j).getFullyVaccinated();
				}
			}
			
			if(fullyVaccinated != 0 & population != 0) {
				double perCapitaVaccinations = fullyVaccinated/population;
				String value = (String) String.format("%.4f", perCapitaVaccinations);
				double ret = Double.parseDouble(value);
				map.put(zip, ret);
			}
		}

		fullMap = map;
		
		return map;
	}
	
	// This method returns the answer to 2a
	public TreeMap<Integer, Double> getPartiallyVaccinatedPerCapita(){
		
		if(partialMap != null) {
			return partialMap;
		}
		
		TreeMap<Integer, Double> map = new TreeMap<Integer, Double>();
		
		for(int i = 0; i < this.popData.size(); i++) {
			PopulationData data = this.popData.get(i);
			int zip = data.getZipcode();
			double population = (double) data.getPopulation();
			double partiallyVaccinated = 0;
			for(int j =0; j < this.covidData.size(); j++) {
				if(this.covidData.get(j).getZipcode() == zip ) {
					partiallyVaccinated += this.covidData.get(j).getPartiallyVaccinated();
				}
			}

			if(partiallyVaccinated != 0 & population != 0) {
				double perCapitaVaccinations = partiallyVaccinated/population;
				String value = (String) String.format("%.4f", perCapitaVaccinations);
				double ret = Double.parseDouble(value);
				map.put(zip, ret);
			}
			
		}
		
		partialMap = map;
		
		return map;
	}

	// Common code using selector (strategy method) for 3 and 4
	public int getAverage(int zip, PropertiesSelector selector) {
		
		double total = 0.0;
		double count = 0;
		
		
		for (PropertiesData data : propertiesData) {
			if (data.getZipcode() == zip) {
				if(selector.getField(data) != null) {
					total += selector.getField(data);
					count++;
				}
			}
		}
		
		double average = total / count;
		int ret = (int) Math.floor(average);
		
		return ret;
		
	}
	
	// This method returns the answer to 3
	public int getAverageMktValue(int zip) {
		
		if(avgMktValMap != null) {
			for(int key: avgMktValMap.keySet()) {
				if(key == zip) {
					return avgMktValMap.get(key);
				}
			}
		}
		
		int ret = getAverage(zip, new ValueSelector());
		avgMktValMap.put(zip, ret);
		
		return ret;
		
	}
	
	// This method returns the answer to 4
	public int getAverageLivArea(int zip) {
		
		if(avgLivMap != null) {
			for(int key: avgLivMap.keySet()) {
				if(key == zip) {
					return avgLivMap.get(key);
				}
			}
		}
		
		int ret = getAverage(zip, new AreaSelector());
		avgLivMap.put(zip, ret);
		
		return ret;
		
	}
	

}
