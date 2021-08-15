package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PopulationData;

public class PopReader implements PopulationDataReader{
	
	protected String filename;

	protected Logger logger = Logger.getInstance();
	
	public PopReader(String filename) {
		this.filename = filename;
	}
	
	public List<PopulationData> getAllRows() throws Exception {

		 File inputFile = new File(filename);
		 
		long time = System.currentTimeMillis();
		String out = Long.toString(time);
		out += " " + filename;
		logger.logString(out); 
	     
	     BufferedReader file = new BufferedReader(new FileReader(inputFile));
	     
	     List<PopulationData> list = new ArrayList<PopulationData>();
	        
		String line = file.readLine();
		
		 while(line != null) {
			 String txt[]  = line.split(" ",-1);
			 
			 try {
				Integer zipcode = Integer.parseInt(txt[0]);
				Integer population = Integer.parseInt(txt[1]);
				PopulationData data = new PopulationData(zipcode, population);
				list.add(data);
			}
			catch(NumberFormatException e) {
				
			}
			
			line = file.readLine();
		 }
		
		
		return list;
	}
	
	public TreeMap<Integer, Integer> getMap() throws Exception{
		
		 File inputFile = new File(filename);
		 
		long time = System.currentTimeMillis();
		String out = Long.toString(time);
		out += " " + filename;
		logger.logString(out); 
	     
	    BufferedReader file = new BufferedReader(new FileReader(inputFile));
		
		TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
		
		String line = file.readLine();
		
		 while(line != null) {
			 String txt[]  = line.split(" ",-1);
			 
			 try {
				Integer zipcode = Integer.parseInt(txt[0]);
				Integer population = Integer.parseInt(txt[1]);
				if(map.containsKey(zipcode)) {
					throw new Exception();
				}
				else {
					map.put(zipcode, population);
				}
				
			}
			catch(NumberFormatException e) {
				
			}
			
			line = file.readLine();
		 }
		
		
		return map;
		
	}
	
	

}
