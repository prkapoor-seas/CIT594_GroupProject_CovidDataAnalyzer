package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.CovidData;
	
public class CSVReaderCovidData implements CovidDataReader{
	
	protected String filename;
	protected Logger logger = Logger.getInstance();
	
	public CSVReaderCovidData(String filename) {
		this.filename = filename;
	}
	
	 public HashMap<Integer, List<CovidData>> getAllRows() throws Exception {
	      
	       File inputFile = new File(filename);
	        
	      long time = System.currentTimeMillis();
	      String out = Long.toString(time);
	      out += " " + filename;
	      logger.logString(out); 
	        
	        BufferedReader file = new BufferedReader(new FileReader(inputFile));
	       
	       HashMap<Integer, List<CovidData>> map = new HashMap<Integer, List<CovidData>>();
	       
	        String line = file.readLine();
	        String txt[]  = line.split(",",-1);
	      int timestampIndex = -1;
	      int zipIndex = -1;
	      int partialIndex = -1;
	      int fullIndex = -1;
	        for(int i = 0; i < txt.length; i++) {
	         String str = txt[i];
	      
	         while(str.contains("\"")) {
	            int index = str.indexOf("\"");
	            str = str.substring(0, index) + str.substring(index+1);
	         }

	         if(str.equals("etl_timestamp")) {
	            timestampIndex = i;
	         }
	         else if(str.equals("zip_code")) {
	            zipIndex = i;
	         }
	         else if(str.equals("partially_vaccinated")) {
	            partialIndex = i;
	         }
	         else if(str.equals("fully_vaccinated")) {
	            fullIndex = i;
	         }
	      }
	        
	        // move onto next line
	        line = file.readLine();

	      while(line!= null) {

	         String text[]  = line.split(",",-1);
	         String timeStamp = text[timestampIndex];
	         Integer zipcode = null;
	         Integer partiallyVaccinated = null;
	         Integer fullyVaccinated = null;
	         
	         try {
	            zipcode = Integer.parseInt(text[zipIndex].trim());
	         }
	         catch(Exception e) {
	            
	         }
	         try {
	            partiallyVaccinated = Integer.parseInt(text[partialIndex].trim());
	         }
	         catch(Exception e) {
	            
	         }
	         try {
	            fullyVaccinated = Integer.parseInt(text[fullIndex].trim());
	         }
	         catch(Exception e) {
	            
	         }
	         
	         if(zipcode != null && !timeStamp.isBlank()) {
	            CovidData data = new CovidData(zipcode, partiallyVaccinated, fullyVaccinated, timeStamp);
	            if (map.containsKey(zipcode)){
	               List<CovidData> list = map.get(zipcode);
	               list.add(data);
	               map.put(zipcode, list);
	            }
	            else{
	               List<CovidData> newList = new ArrayList<CovidData>();
	               newList.add(data);
	               map.put(zipcode, newList);
	            }
	         }
	        
	         line = file.readLine();
	      }

	      return map;

	   }
}