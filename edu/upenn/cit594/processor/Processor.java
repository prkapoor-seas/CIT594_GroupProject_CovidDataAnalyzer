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
	
	
	public Processor(CovidDataReader covidReader, PopulationDataReader popReader) throws Exception{
		this.covidReader = covidReader;
		this.covidData = covidReader.getAllRows();
		this.popReader = popReader;
		this.popData = popReader.getAllRows();
	}
	
	public int getTotalPopulationAllZips() {
		
		int totalPop = 0;
		for(int i = 0; i < this.popData.size(); i++) {
			PopulationData data = this.popData.get(i);
			totalPop += data.getPopulation();
		}
		
		return totalPop;
	}
	
	public TreeMap<Integer, Double> getFullyVaccinatedPerCapita(){
		
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

		return map;
	}
	
	public TreeMap<Integer, Double> getPartiallyVaccinatedPerCapita(){
		
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
		return map;
		
	}
	
}
