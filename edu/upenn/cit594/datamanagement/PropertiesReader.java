package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.PropertiesData;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertiesReader implements PropertiesDataReader {


    protected String filename;

    public PropertiesReader(String name) {
        filename = name;
    }

    public List<PropertiesData> getAllRows() throws Exception {
        List<PropertiesData> list = new LinkedList<PropertiesData>();

        File inputFile = new File(filename);
        BufferedReader file = new BufferedReader(new FileReader(inputFile));

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
        
        Pattern pattern = Pattern.compile("(?:,|\\n|^)(\"(?:(?:\"\")*[^\"]*)*\"|[^\",\\n]*|(?:\\n|$))");
  
        while(line != null) {
        	ArrayList<String> strList = new ArrayList<String>();
            Matcher matcher = pattern.matcher(line);
            while(matcher.find()) {
            	String match = matcher.group(1);
            	strList.add(match);
            }
            
            String zip =  strList.get(zipIndex);
            
            if(zip.length() < 5) {
            	line = file.readLine();
            	continue;
            }
            
            zip = zip.substring(0,5);
            String livableArea = strList.get(livableAreaIndex);
            String marketValue = strList.get(marketValueIndex);
            
            int zipCode = 0;
            double livArea = 0.0;
            double markValue = 0.0;
            
            try {
            	zipCode = Integer.parseInt(zip);
            	livArea = Double.parseDouble(livableArea);
            	markValue = Double.parseDouble(marketValue);
            }
            catch(NumberFormatException e) {
            	
            }
            
            if(zipCode != 0 ) {
            	PropertiesData data = new PropertiesData(zipCode, livArea, markValue);
            	list.add(data);
            }
            
            line = file.readLine();
            
        }
        	
        file.close();
        
        return list;
    }
}