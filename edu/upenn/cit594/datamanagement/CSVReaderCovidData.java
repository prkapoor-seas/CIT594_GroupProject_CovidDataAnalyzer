package edu.upenn.cit594.datamanagement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.upenn.cit594.util.CovidData;
	
public class CSVReaderCovidData implements CovidDataReader{
	
	protected String filename;
	
	public CSVReaderCovidData(String filename) {
		this.filename = filename;
	}

	
	public List<CovidData> getAllRows() throws Exception {
		
		List<CovidData> list = new ArrayList<CovidData>();
		
		Scanner in = new Scanner(new File(this.filename));
		int line = 0;
		int timestampIndex = -1;
		int zipIndex = -1;
		int partialIndex = -1;
		int fullIndex = -1;
		while(in.hasNextLine()) {
			String text = in.nextLine();
			String txt[]  = text.split(",",-1);
			
			if(line == 0) {				
				for(int i = 0; i < txt.length; i++) {
					String str = txt[i];
					str = str.substring(1, str.length()-1);
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
			}
			else {
				//System.out.println(txt.length);
				String timeStamp = txt[timestampIndex];
				int zipcode = 0;
				int partiallyVaccinated = 0;
				int fullyVaccinated = 0;
				
				try {
					zipcode = Integer.parseInt(txt[zipIndex]);
					partiallyVaccinated = Integer.parseInt(txt[partialIndex]);
				    fullyVaccinated = Integer.parseInt(txt[fullIndex]);
				}
				catch(NumberFormatException e) {
					
				}
				
				if(zipcode != 0 & timeStamp.isBlank()) {
					CovidData data = new CovidData(zipcode, partiallyVaccinated, fullyVaccinated, timeStamp);
					list.add(data);
				}
				 
			}
			line++;
		}

		return list;

	}

}

