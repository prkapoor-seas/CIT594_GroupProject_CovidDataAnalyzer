package edu.upenn.cit594.datamanagement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.PopulationData;

public class PopReader implements PopulationDataReader{
	
	protected String filename;

	protected Logger logger = Logger.getInstance();
	
	public PopReader(String filename) {
		this.filename = filename;
	}

	public List<PopulationData> getAllRows() throws Exception {
		
		List<PopulationData> list = new ArrayList<PopulationData>();
		
		Scanner in = new Scanner(new File(this.filename));

		logger.logString(filename);
		
		while(in.hasNextLine()) {
			String text = in.nextLine();
			String txt[]  = text.split(" ",-1);
			
			try {
				int zipcode = Integer.parseInt(txt[0]);
				int population = Integer.parseInt(txt[1]);
				PopulationData data = new PopulationData(zipcode, population);
				list.add(data);
			}
			catch(NumberFormatException e) {
				
			}
			
		}
		
		return list;
	}

}
