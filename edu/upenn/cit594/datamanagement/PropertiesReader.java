package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PropertiesData;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesReader implements PropertiesDataReader {


    protected String filename;
    protected Logger logger = Logger.getInstance();

    public PropertiesReader(String name) {
        filename = name;
    }

    public List<PropertiesData> getAllRows() throws Exception {

        File inputFile = new File(filename);

		long time = System.currentTimeMillis();
		String out = Long.toString(time);
		out += " " + filename;
		logger.logString(out); 
        
	
        BufferedReader file = new BufferedReader(new FileReader(inputFile));
        
        List<PropertiesData> list = new LinkedList<PropertiesData>();

        String line = file.readLine();
        String[] firstLineAsArray = line.split(",");
        int zipIndex = -1;
        int livableAreaIndex = -1;
        int marketValueIndex = -1;
        for (int i = 0; i < firstLineAsArray.length; i++) {
            if (firstLineAsArray[i].equals("zip_code")) {
                zipIndex = i;
            }
            else if (firstLineAsArray[i].equals("total_livable_area")) {
                livableAreaIndex = i;
            }
            else if (firstLineAsArray[i].equals("market_value")) {
                marketValueIndex = i;
            }
        }
        line = file.readLine();
        if(line != null) {
        	line = line.trim().concat("\n");
        }
        
        Pattern pattern = Pattern.compile("([^,\"]*|\"[^\"]*\"|\"[^\"]*\"[^\"]*\"[^\"]*\"|\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"|\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"|\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\"[^\"]*\")[,\n]" );
        
        while(line != null) {
        	line = line.trim().concat("\n");
        	ArrayList<String> strList = new ArrayList<String>();
            Matcher matcher = pattern.matcher(line);
            while(matcher.find()) {
            	String match = matcher.group(1);
            	strList.add(match);
            }
            
            String zip =  strList.get(zipIndex).trim();
            
            if(zip.length() < 5) {
            	line = file.readLine();
            	continue;
            }
            
            zip = zip.substring(0,5);
            String livableArea = strList.get(livableAreaIndex).trim();
            String marketValue = strList.get(marketValueIndex).trim();
            
            Integer zipCode = null;
            Double livArea = null;
            Double markValue = null;
            
            try {
            	zipCode = Integer.parseInt(zip);
            }
            catch(Exception e) {
            	
            }
            try {
            	livArea = Double.parseDouble(livableArea);
            }
            catch(Exception e) {
            	
            }
            try {
            	markValue = Double.parseDouble(marketValue);
            }
            catch(Exception e) {
            	
            }
        	
            if(zipCode != null) {
            	PropertiesData data = new PropertiesData(zipCode, livArea, markValue);
            	list.add(data);
            }
            
            line = file.readLine();
            
        }
        	
        file.close();
        
        return list;
    }
}