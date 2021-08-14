package edu.upenn.cit594.processor;

import java.util.*;

import edu.upenn.cit594.datamanagement.CovidDataReader;
import edu.upenn.cit594.datamanagement.PopulationDataReader;
import edu.upenn.cit594.datamanagement.PropertiesDataReader;
import edu.upenn.cit594.datamanagement.PropertiesReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertiesData;

public class Processor {

	protected CovidDataReader covidReader;
	protected List<CovidData> covidData;
	protected PopulationDataReader popReader;
	protected PropertiesDataReader propertiesDataReader;
	protected List<PopulationData> popData;
	protected List<PropertiesData> propertiesData;

	protected Logger logger = Logger.getInstance(); // check on this
	
	// memoization
	private static int totalPopulation = 0;
	private static TreeMap<Integer, Double> fullMap = new TreeMap<Integer, Double>();
	private static TreeMap<Integer, Double> partialMap = new TreeMap<Integer, Double>();
	private static HashMap<Integer, Integer> avgMktValMap = new HashMap<Integer, Integer>();
	private static HashMap<Integer, Integer> avgLivMap = new HashMap<Integer, Integer>();
	private static HashMap<Integer, Integer> MktValPerCapMap= new HashMap<Integer, Integer>();
	private static int maxZip = 0;
	

	public Processor(CovidDataReader covidReader, PopulationDataReader popReader, PropertiesDataReader propertiesDataReader) throws Exception{
		this.covidReader = covidReader;
		this.covidData = covidReader.getAllRows();
		this.popReader = popReader;
		this.popData = popReader.getAllRows();
		this.propertiesDataReader = propertiesDataReader;
		this.propertiesData = propertiesDataReader.getAllRows();

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
		
		if(!fullMap.isEmpty()) {
			return fullMap;
		}
		
		TreeMap<Integer, Double> map = new TreeMap<Integer, Double>();
		
		for(int i = 0; i < this.popData.size(); i++) {
			PopulationData data = this.popData.get(i);
			int zip = data.getZipcode();
			double population = data.getPopulation();
			double fullyVaccinated = 0;
			
			for(int j =0; j < this.covidData.size(); j++) {
				if(this.covidData.get(j).getZipcode() == zip ) {
					if(this.covidData.get(j).getFullyVaccinated() != 0) {
						fullyVaccinated = this.covidData.get(j).getFullyVaccinated();
					}
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
	
	// This method returns the answer to 2b
	public TreeMap<Integer, Double> getPartiallyVaccinatedPerCapita(){
		

		if(!partialMap.isEmpty()) {
			return partialMap;
		}
		
		TreeMap<Integer, Double> map = new TreeMap<Integer, Double>();
		
		for(int i = 0; i < this.popData.size(); i++) {
			PopulationData data = this.popData.get(i);
			int zip = data.getZipcode();
			double population = data.getPopulation();
			double partiallyVaccinated = 0;
			for(int j =0; j < this.covidData.size(); j++) {
				if(this.covidData.get(j).getZipcode() == zip ) {
					if(this.covidData.get(j).getPartiallyVaccinated() != 0) {
						partiallyVaccinated = this.covidData.get(j).getPartiallyVaccinated();
					}
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
		
		if(Integer.toString(zip).length() != 5) {
			return 0;
		}
		
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
		
		if(total == 0 | count == 0) {
			return 0;
		}
		
		double average = total / count;
		
		int ret = 0;
		if(average > 0) {
			ret = (int) Math.floor(average);
		}
		else {
			ret = (int) Math.ceil(average);
		}
		
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

	// This method returns the answer to 5
	public int getResMktValPerCapita(int zip) {
		
		if(Integer.toString(zip).length() != 5) {
			return 0;
		}
		
		if(MktValPerCapMap != null) {
			for(int key: MktValPerCapMap.keySet()) {
				if(key == zip) {
					return MktValPerCapMap.get(key);
				}
			}
		}
		
		double population = 0;
		for(int i = 0; i < this.popData.size(); i++) {
			if(this.popData.get(i).getZipcode() == zip) {
				population = this.popData.get(i).getPopulation();
			}
		}
		
		if(population == 0) {
			MktValPerCapMap.put(zip, 0);
			return 0;
		}
		
		double total = 0;
		for (PropertiesData data : propertiesData) {
			if (data.getZipcode() == zip) {
				if(data.getMarketValue() != null) {
					total += data.getMarketValue();
				}
			}
		}
		
		if(total == 0) {
			MktValPerCapMap.put(zip, 0);
			return 0;
		}
		
		double avg = total/population;
		
		int ret = 0;
		if(avg > 0) {
			ret = (int) Math.floor(avg);
		}
		else {
			ret = (int) Math.ceil(avg);
		}
		
		MktValPerCapMap.put(zip, ret);
		
		return ret;
		
	}
	//This method returns the answer to 6
	public int getZipWithHighestFullVaccinationPerCapita(){
		
		if(maxZip != 0) {
			return maxZip;
		}

		TreeMap<Integer, Double> map;
		if (fullMap != null){
			map = fullMap;
		}
		else{
			map = getFullyVaccinatedPerCapita();
		}

		//double maxVaccinePerCapita = (Collections.max(map.values()));
		int zipWithMaxFullVacc = (Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getKey());
		maxZip = zipWithMaxFullVacc;
		return zipWithMaxFullVacc;
	}
}
