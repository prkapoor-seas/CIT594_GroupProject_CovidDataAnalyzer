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

public class PopReader implements PopulationDataReader{
	
	protected String filename;

	protected Logger logger = Logger.getInstance();
	
	public PopReader(String filename) {
		this.filename = filename;
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
				Integer zipcode = Integer.parseInt(txt[0].trim());
				Integer population = Integer.parseInt(txt[1].trim());
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
