package edu.upenn.cit594.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;

public class CommandUserInterface {
	
	protected Processor processor;
	protected Scanner in;
	protected Logger logger = Logger.getInstance();
	
	public CommandUserInterface(Processor processor){
		this.processor = processor;
		this.in = new Scanner(System.in);
	}

	public void start() {
		
		while(true) {
			System.out.println("Enter 0 to exit \nEnter 1 to get total population across all zips \nEnter 2 to get total (partial/full) vaccinations per capita by zip \nEnter 3 to get average market value by zip \nEnter 4 to get average livable area by zip \nEnter 5 to get total residential market value per capita \nEnter 6 to get maximum and minimum market value for zip with the highest fully vaccinated per capita");
			System.out.println("> ");
			System.out.flush();
			
			String choice = in.nextLine();
			
			long time = System.currentTimeMillis();
			String out = Long.toString(time);
			out+= " " + String.valueOf(choice);
			logger.logString(out);
			
			int ch;
			try {
				ch = Integer.parseInt(choice);
			}
			catch(NumberFormatException e) {
				System.out.println("Error: Invalid input");
				break;
			}
			
			if(ch == 0) {
				break;
			}
			else if(ch == 1) {
				getTotalPopulation();
			}
			else if(ch == 2) {
				getVaccinationsPerCapita();
			}
			else if(ch == 3) {
				getAvgMktValue();
			}
			else if(ch == 4) {
				getAvgLivArea();
			}
			else if(ch == 5) {
				getResMktValPerCapita();
			}
			else if(ch == 6) {
				getMaxVaccinePerCapita();
			}
			else {
				System.out.println("Error: Wrong choice");
				break;
			}
		}
	}
	
	
	protected void getTotalPopulation() {
		System.out.println("BEGIN OUTPUT");
		System.out.println(this.processor.getTotalPopulationAllZips());
		System.out.println("END OUTPUT");
	}
	
	protected void getVaccinationsPerCapita() {

		String vaccine = "";
		while(!vaccine.equals("partial") | !vaccine.equals("full")) {
			System.out.println("Enter \"partial\" for partial vaccinations or \"full\" for full vaccinations");
			System.out.println("> ");
			System.out.flush();
			vaccine = in.nextLine();
			if(vaccine.equals("partial")) {
				System.out.println("BEGIN OUTPUT");
				TreeMap<Integer, Double> map = this.processor.getPartiallyVaccinatedPerCapita();
				for (Map.Entry<Integer, Double> entry : map.entrySet()) {
					System.out.println(entry.getKey() +
							" " + String.format("%.4f", entry.getValue()));

				}
				System.out.println("END OUTPUT");
				break;
			}
			else if(vaccine.equals("full")) {
				System.out.println("BEGIN OUTPUT");
				TreeMap<Integer, Double> map = this.processor.getFullyVaccinatedPerCapita();
				for (Map.Entry<Integer, Double> entry : map.entrySet()) {
					System.out.println(entry.getKey() +
							" " + String.format("%.4f", entry.getValue()));
				}
				System.out.println("END OUTPUT");
				break;
			}
		}
		
	}
	
	protected void getAvgMktValue() {
		System.out.println("Please enter a zip code below");
		System.out.println("> ");
		System.out.flush();
		
		String val = in.nextLine();

		long time = System.currentTimeMillis();
		String out = Long.toString(time);
		out+= " " + val;
		logger.logString(out);
		
		int zip ;
		try {
			zip = Integer.parseInt(val);
			System.out.println("BEGIN OUTPUT");
			System.out.println(this.processor.getAverageMktValue(zip));
			System.out.println("END OUTPUT");
		}
		catch(NumberFormatException e) {
			System.out.println("Error: Invalid input");
		}
		
	}
	
	protected void getAvgLivArea() {
		System.out.println("Please enter a zip code below");
		System.out.println("> ");
		System.out.flush();
		String val = in.nextLine();

		long time = System.currentTimeMillis();
		String out = Long.toString(time);
		out+= " " + val;
		logger.logString(out);
		
		int zip ;
		try {
			zip = Integer.parseInt(val);
			System.out.println("BEGIN OUTPUT");
			System.out.println(this.processor.getAverageLivArea(zip));
			System.out.println("END OUTPUT");
		}
		catch(NumberFormatException e) {
			System.out.println("Error: Invalid input");
		}
		
		System.out.println("BEGIN OUTPUT");
		
		System.out.println("END OUTPUT");
	}
	
	protected void getResMktValPerCapita() {
		System.out.println("Please enter a zip code below");
		System.out.println("> ");
		System.out.flush();
		String val = in.nextLine();
		
		long time = System.currentTimeMillis();
		String out = Long.toString(time);
		out+= " " + val;
		logger.logString(out);
		
		int zip ;
		try {
			zip = Integer.parseInt(val);
			System.out.println("BEGIN OUTPUT");
			System.out.println(this.processor.getResMktValPerCapita(zip));
			System.out.println("END OUTPUT");
		}
		catch(NumberFormatException e) {
			System.out.println("Error: Invalid input");
		}
		
	}

	protected void getMaxVaccinePerCapita() {
		System.out.println("BEGIN OUTPUT");
		HashMap<Integer, List<Double>> map = this.processor.getZipWithHighestFullVaccinationPerCapita();
		for (Entry<Integer, List<Double>> entry : map.entrySet()) {
			System.out.println("Zip with Highest Per Capita Full Vaccinations: " + entry.getKey());
			List<Double> list = map.get(entry.getKey());
			Double max = list.get(0);
			Double min = list.get(1);
			if(max == null) {
				System.out.println("Max Market Value for Zip above : Invalid Zip");
			}
			else {
				System.out.println("Max Market Value for : " + max);
			}
			
			if(min == null) {
				System.out.println("Max Market Value for Zip above : Invalid Zip");
			}
			else {
				System.out.println("Max Market Value for : " + min);
			}
			
		}
		
		System.out.println("END OUTPUT");
	}
	

}
