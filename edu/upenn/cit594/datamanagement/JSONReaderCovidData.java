package edu.upenn.cit594.datamanagement;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import edu.upenn.cit594.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import edu.upenn.cit594.util.CovidData;


public class JSONReaderCovidData implements CovidDataReader{
	
String filename;

	protected Logger logger = Logger.getInstance();
	
	public JSONReaderCovidData(String filename){
		this.filename = filename;
	}
	
	public List<CovidData> getAllRows() throws Exception {
	
		long time = System.currentTimeMillis();
		String out = Long.toString(time);
		out += " " + filename;
		logger.logString(out); 
		
		Object obj = new JSONParser().parse(new FileReader(this.filename));
        JSONArray ja = (JSONArray) obj;
        
        List<CovidData> list = new ArrayList<CovidData>();
        
        for(int i = 0; i < ja.size(); i++) {
        	
        	JSONObject jo = (JSONObject) ja.get(i);
        	
        	Integer zip = null;
        	Integer partialVacc = null;
        	Integer fullyVacc = null;
        	String timeStamp = "";
        	
        	try {
        		zip = (int) (long) jo.get("zip_code");
        		partialVacc = (int) (long) jo.get("partially_vaccinated");
                fullyVacc = (int) (long) jo.get("fully_vaccinated");
            	timeStamp = (String) jo.get("etl_timestamp");    	
        	}
        	catch(Exception e) {
        	
        	}
        	
        	if(zip != null && !timeStamp.isBlank()) {
        		CovidData data = new CovidData(zip, partialVacc, fullyVacc, timeStamp);
            	list.add(data);
        	}
        }
        
		return list;
	}

}
