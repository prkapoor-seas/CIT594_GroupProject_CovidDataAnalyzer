package edu.upenn.cit594.datamanagement;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
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

	public HashMap<Integer, List<CovidData>> getAllRows() throws Exception {
		   
	      long time = System.currentTimeMillis();
	      String out = Long.toString(time);
	      out += " " + filename;
	      logger.logString(out); 
	      
	      Object obj = new JSONParser().parse(new FileReader(this.filename));
	        JSONArray ja = (JSONArray) obj;
	        
	        HashMap<Integer, List<CovidData>> map = new HashMap<Integer, List<CovidData>>();
	        
	        for(int i = 0; i < ja.size(); i++) {
	           
	           JSONObject jo = (JSONObject) ja.get(i);
	           
	           Integer zip = null;
	           Integer partialVacc = null;
	           Integer fullyVacc = null;
	           String timeStamp = (String) jo.get("etl_timestamp");       
	           
	           try {
	              zip = (int) (long) jo.get("zip_code");
	              String zipCode = String.valueOf(zip);
	              if(zipCode.trim().substring(0, 5).length() < 5) {
	            	  continue;
	              }
	           }
	           catch(Exception e) {
	              
	           }          
	           try {
	              partialVacc = (int) (long) jo.get("partially_vaccinated");
	           }
	           catch(Exception e) {
	              
	           }
	           try {
	              fullyVacc = (int) (long) jo.get("fully_vaccinated");
	           }
	           catch(Exception e) {
	              
	           }
	           
	           if(zip != null && !timeStamp.isBlank()) {
	  
        		   CovidData data = new CovidData(zip, partialVacc, fullyVacc, timeStamp);
    			   if (map.containsKey(zip)){
		               List<CovidData> list = map.get(zip);
		               list.add(data);
		               map.put(zip, list);
		            }
		            else{
		               List<CovidData> newList = new ArrayList<CovidData>();
		               newList.add(data);
		               map.put(zip, newList);
		            }
	           }
	           
	           
	        }
	        
	      return map;
	   }
}
