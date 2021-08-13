package edu.upenn.cit594.processor;

import java.util.List;
import java.util.TreeMap;

import edu.upenn.cit594.datamanagement.CovidDataReader;
import edu.upenn.cit594.datamanagement.PopulationDataReader;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PopulationData;

public class Processor {

	protected CovidDataReader covidReader;
	protected List<CovidData> covidData;
	protected PopulationDataReader popReader;
	protected List<PopulationData> popData;
	
	// memoization
	private static int totalPopulation = 0;
	private static TreeMap<Integer, Double> fullMap = new TreeMap<Integer, Double>();
	private static TreeMap<Integer, Double> partialMap = new TreeMap<Integer, Double>();
	
	
	public Processor(CovidDataReader covidReader, PopulationDataReader popReader) throws Exception{
		this.covidReader = covidReader;
		this.covidData = covidReader.getAllRows();
		this.popReader = popReader;
		this.popData = popReader.getAllRows();
	}
	
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

	/**
	 * From a list of PopulationData objects, calculates the average of the list of doubles specified by the dataRetriever parameter
	 * Implements the Strategy Pattern
	 * @param listOfData list of PopulationData objects
	 * @param zipCode for which the average value is calculated
	 * @param dataRetriever specifies what to find the average of (market value or living area)
	 * @return average of the specified list of data, truncated as an Integer
	 */
	public static Integer getAverageValue(List<PopulationData> listOfData, int zipCode, PopulationDataRetriever dataRetriever) {
		// initialize variables
		double totalOfValues = 0.0;
		int count = 0;
		PopulationData data = null;
		List<Double> valuesToAverage;
		// iterate over list of PopulationData objects to find data corresponding to zip code
		for (PopulationData entry : listOfData) {
			if (entry.getZipcode() == zipCode) {
				data = entry;
			}
		}
		// uses PopulationDataRetriever interface to retrieve the correct list for the given zip code
		valuesToAverage = dataRetriever.returnData(data);
		// calculates the average of the list of values
		for (Double value : valuesToAverage) {
			totalOfValues += value;
			count += 1;
		}
		double average = totalOfValues / count;
		// returns the value cast as an integer and truncated
		return (int) average;
	}
	
}
