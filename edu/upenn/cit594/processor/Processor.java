package edu.upenn.cit594.processor;

import java.util.*;

import edu.upenn.cit594.datamanagement.CovidDataReader;
import edu.upenn.cit594.datamanagement.PopulationDataReader;
import edu.upenn.cit594.datamanagement.PropertiesDataReader;
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
	private static HashMap<Integer, List<Double>> minMaxMap = new HashMap<Integer, List<Double>>();


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
			
			if(population == 0) {
				continue;
			}
		
			Integer fullyVaccinated = 0;
			for(int j = this.covidData.size() -1; j >=0 ; j--) {
				if(this.covidData.get(j).getZipcode() == zip) {
					fullyVaccinated = this.covidData.get(j).getFullyVaccinated();
					break;
				}
			}
			
			if(fullyVaccinated != null && fullyVaccinated != 0) {
				double fullyVacc = fullyVaccinated;
				double perCapitaVaccinations = fullyVacc/population;
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
			
			if(population == 0) {
				continue;
			}
			
			Integer partiallyVaccinated = 0;
			for(int j = this.covidData.size()-1; j >= 0; j--) {
				if(this.covidData.get(j).getZipcode() == zip ) {
					partiallyVaccinated = this.covidData.get(j).getPartiallyVaccinated();
					break;
				}
			}

			if(partiallyVaccinated != null && partiallyVaccinated != 0) {
				double partiallyVacc = partiallyVaccinated;
				double perCapitaVaccinations = partiallyVacc/population;
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
		
		if(count == 0) {
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
		
		if(!avgMktValMap.isEmpty()) {
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
		
		if(!avgLivMap.isEmpty()) {
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
		
		if(!MktValPerCapMap.isEmpty()) {
			for(int key: MktValPerCapMap.keySet()) {
				if(key == zip) {
					return MktValPerCapMap.get(key);
				}
			}
		}
		
		Integer population = null;
		for(int i = 0; i < this.popData.size(); i++) {
			if(this.popData.get(i).getZipcode() == zip) {
				population = this.popData.get(i).getPopulation();
			}
		}
		
		// If population is 0 or null then return 0;
		if(population == 0 || population == null) {
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
	//This method returns the answer to 6 - get Highest Mkt Value and Lowest Mkt Value for zip with highest full vaccination per capita
	public HashMap<Integer, List<Double>> getMinMaxMktVal(){
		
		if(!minMaxMap.isEmpty()) {
			return minMaxMap;
		}

		TreeMap<Integer, Double> map;
		if (!fullMap.isEmpty()){
			map = fullMap;
		}
		else{
			map = getFullyVaccinatedPerCapita();
		}

		// get zip with the max fullyvaccinated from map
		Double m = null;
		Integer zip = null;
		for(int key: map.keySet()) {
			// initialize m if not initialized by the first entry
			if(m == null) {
				m = map.get(key);
				zip = key;
			}
			else if(m < map.get(key)){
				m = map.get(key);
				zip = key;
			}
		}
		
		int zipCode = zip;
		
		// get max market value for zipcode from propertiesData
		Double max = null;
		Double min = null;
		
		for (PropertiesData data : propertiesData) {
			if (data.getZipcode() == zipCode && data.getMarketValue() != null) {
				if(max == null || min == null) {
					max = data.getMarketValue();
					min = data.getMarketValue();
				}
				else {
					if(data.getMarketValue() > max) {
						max = data.getMarketValue();
					}
					if(data.getMarketValue() < min) {
						min = data.getMarketValue();
					}
				}
			}
		}
		
		ArrayList<Double> list = new ArrayList<Double>();
		list.add(max);
		list.add(min);
		minMaxMap.put(zip, list);
		
		return minMaxMap;
	}
}
