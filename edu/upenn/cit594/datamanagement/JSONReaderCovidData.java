package edu.upenn.cit594.datamanagement;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import edu.upenn.cit594.util.CovidData;


public class JSONReaderCovidData implements CovidDataReader{
	
String filename;
	
	public JSONReaderCovidData(String filename){
		this.filename = filename;
	}
	
	public List<CovidData> getAllRows() throws Exception {
		
		List<CovidData> list = new ArrayList<CovidData>();
		
		Object obj = new JSONParser().parse(new FileReader(this.filename));
		
        JSONArray ja = (JSONArray) obj;
        
        for(int i = 0; i < ja.size(); i++) {
        	
        	JSONObject jo = (JSONObject) ja.get(i);
        	
        	long zipcode = 0;
        	long partiallyVaccinated = 0;
        	long fullyVaccinated = 0;
        	String timeStamp = "";
        	
        	try {
        		zipcode = (long) jo.get("zip_code");
        		partiallyVaccinated = (long) jo.get("partially_vaccinated");
            	fullyVaccinated = (long) jo.get("fully_vaccinated");
            	timeStamp = (String) jo.get("etl_timestamp");
        	}
        	catch(NullPointerException e) {
        		
        	}
        	
        	if(zipcode != 0 & !timeStamp.isBlank()) {
        		CovidData data = new CovidData(zipcode, partiallyVaccinated, fullyVaccinated, timeStamp);
            	list.add(data);
        	}
        }
        
		return list;
	}

}
