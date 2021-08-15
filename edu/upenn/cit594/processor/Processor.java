package edu.upenn.cit594.processor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.upenn.cit594.datamanagement.CovidDataReader;
import edu.upenn.cit594.datamanagement.PopulationDataReader;
import edu.upenn.cit594.datamanagement.PropertiesDataReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;
import edu.upenn.cit594.util.PropertiesData;

public class Processor {

	protected CovidDataReader covidReader;
	protected HashMap<Integer, List<CovidData>> covidData;
	protected PopulationDataReader popReader;
	protected PropertiesDataReader propertiesDataReader;
	protected TreeMap<Integer, Integer> popMap;
	protected List<PropertiesData> propertiesData;

	protected Logger logger = Logger.getInstance(); // check on this
	
	// memoization
	private static int totalPopulation = 0;
	private static String partialPerCapVacc = "";
	private static String fullPerCapVacc = "";
	private static HashMap<Integer, Integer> avgMktValMap = new HashMap<Integer, Integer>();
	private static HashMap<Integer, Integer> avgLivMap = new HashMap<Integer, Integer>();
	private static HashMap<Integer, Integer> MktValPerCapMap= new HashMap<Integer, Integer>();
	private static Integer zipWithMaxPerCapVacc = null;
	private static String outputOfSix = "";


	public Processor(CovidDataReader covidReader, PopulationDataReader popReader, PropertiesDataReader propertiesDataReader) throws Exception{
		this.covidReader = covidReader;
		this.covidData = covidReader.getAllRows();
		this.popReader = popReader;
		this.popMap = popReader.getMap();
		this.propertiesDataReader = propertiesDataReader;
		this.propertiesData = propertiesDataReader.getAllRows();
	}
	
	// This method returns the answer to 1
	public int getTotalPopulationAllZips() {
		
		if(totalPopulation != 0) {
			return totalPopulation;
		}
		
		int totalPop = 0;
		
		for(int key: this.popMap.keySet()) {
			totalPop += this.popMap.get(key);
		}
		
		totalPopulation = totalPop;
		return totalPop;
	}
	
	
	// This outputs 2 depending on string being "full" or partial"
	public String getVaccinatedPerCapita(String string) {
		
		if(string.equals("partial")) {
			if(!partialPerCapVacc.isEmpty()) {
				return partialPerCapVacc;
			}
		}
		else if(string.equals("full")) {
			if(!fullPerCapVacc.isEmpty()) {
				return fullPerCapVacc;
			}
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		DateFormat dateFormatTwo  = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.ENGLISH);

		
		String out = "";
		Integer maxZip = null;
		Double maxVacc = null;
		for(int key: popMap.keySet()) {
		
			double population = popMap.get(key);
			int zip = key;

			if(population==0) {
				continue;
			}
		
			
			Date maxDate = null;
			Integer maxFullVacc = null;
			Integer maxPartialVacc = null;
			List<CovidData> list = covidData.get(zip);
			for(int i = 0; i < list.size(); i++) {
				CovidData data = list.get(i);
				if(data.getZipcode() == zip) {
					
					String str = data.getTimestamp();
					while(str.contains("\"")) {
						int index = str.indexOf("\"");
						str = str.substring(0, index) + str.substring(index+1);
					}
							
		
					
					Date date = null;
					try {
						date = dateFormat.parse(str);
					} catch (ParseException e) {
						
					}
					
					if(date == null) {
						try {
							date = dateFormatTwo.parse(str);
						} catch (ParseException e) {
							continue;
						}
					}
					
					if(maxDate == null) {
						maxDate = date;
					}
					else {
						if(date.after(maxDate)) {
							maxDate = date;
							maxFullVacc = data.getFullyVaccinated();
							maxPartialVacc = data.getPartiallyVaccinated();
						}
					}
				}
				
			}
			
			if(maxDate != null) {
				
				if(string.equals("partial")) {
					if(maxPartialVacc != null) {
						double maxPart = maxPartialVacc;
						double perCapPartialVacc = maxPart/population;
					
						out += String.valueOf(key) + " " +  String.format("%.4f", perCapPartialVacc) + "\n";
					}
				}
				else if(string.equals("full")) {
					if(maxFullVacc != null) {
						double maxFull = maxFullVacc;
						double perCapFullVacc = maxFull/population;
						if(maxZip == null) {
							maxZip = zip;
							maxVacc = perCapFullVacc;
						}
						else {
							if(perCapFullVacc > maxVacc) {
								maxZip = zip;
								maxVacc = perCapFullVacc;
							}
						}
		
						out += String.valueOf(key) + " " +  String.format("%.4f", perCapFullVacc) + "\n";
					}
				}
			}

		}
		
		if(string.equals("full")) {
			fullPerCapVacc = out;
			zipWithMaxPerCapVacc = maxZip;
		}
		else if(string.equals("partial")){
			partialPerCapVacc = out;
		}
		
		return out;
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
		
		for(int key: popMap.keySet()) {
			if(key == zip) {
				population = this.popMap.get(key);
				break;
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
	public String getMinMaxMktVal(){
	
		if(!outputOfSix.isEmpty()) {
			return outputOfSix;
		}
		
		this.getVaccinatedPerCapita("full");
		Integer zipCode = zipWithMaxPerCapVacc;
		
		if(zipCode == null) {
			String str = "Zip is invalid or null. No min or max market value \n";
			outputOfSix = str;
			return str;
		}
		
		int zip = zipCode;
		
		// get max market value for zipcode from propertiesData
		Double max = null;
		Double min = null;
		
		for (PropertiesData data : propertiesData) {
			if (data.getZipcode() == zip && data.getMarketValue() != null) {
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
		
		String str = "Zip with Highest Per Capita Full Vaccinations: " + String.valueOf(zip) + "\n";
		if(max == null) {
			str += "Max Market Value for Zip above : Invalid Zip\n";
			
		}
		else {
			str += "Max Market Value for : " + max + "\n";
		}
		
		if(min == null) {
			str += "Min Market Value for Zip above : Invalid Zip\n";
		}
		else {
			str += "Min Market Value for : " + min + "\n";
		}
		
		outputOfSix = str;
		
		return str;
		
	}
	
}
