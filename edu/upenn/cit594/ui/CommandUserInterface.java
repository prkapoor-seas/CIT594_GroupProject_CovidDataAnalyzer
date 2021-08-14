package edu.upenn.cit594.ui;

import java.util.Map;
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
			System.out.println("Enter 0 to exit \nEnter 1 to get total population across all zips \nEnter 2 to get total (partial/full) vaccinations per capita by zip \nEnter 3 to get average market value by zip \nEnter 4 to get average livable area by zip \nEnter 5 to get total residential market value per capita \nEnter 6 to get ###to fill in later");
			System.out.println("> ");
			String choice = in.next();
			logger.logString(String.valueOf(choice));
			int ch;
			try {
				ch = Integer.parseInt(choice);
			}
			catch(NumberFormatException e) {
				System.out.println("Error: Wrong choice");
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
			System.out.print("> ");
			vaccine = in.next().trim();
			//logger.logString(vaccine);
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
			System.out.println("END OUTPUT");
		}
		
	}
	
	protected void getAvgMktValue() {
		System.out.println("Please enter a zip code below");
		System.out.print("> ");
		int zip = in.nextInt();
		logger.logString(String.valueOf(zip));
		System.out.println("BEGIN OUTPUT");
		System.out.println(this.processor.getAverageMktValue(zip));
		System.out.println("END OUTPUT");
	}
	
	protected void getAvgLivArea() {
		System.out.println("Please enter a zip code below");
		System.out.print("> ");
		int zip = in.nextInt();
		logger.logString(String.valueOf(zip));
		System.out.println("BEGIN OUTPUT");
		System.out.println(this.processor.getAverageLivArea(zip));
		System.out.println("END OUTPUT");
	}
	
	protected void getResMktValPerCapita() {
		System.out.println("Please enter a zip code below");
		System.out.print("> ");
		int zip = in.nextInt();
		logger.logString(String.valueOf(zip));
		System.out.println("BEGIN OUTPUT");
		System.out.println(this.processor.getAverageLivArea(zip));
		System.out.println("END OUTPUT");
	}

	protected void getMaxVaccinePerCapita() {
		System.out.println("BEGIN OUTPUT");
		System.out.println(this.processor.getZipWithHighestFullVaccinationPerCapita());
		System.out.println("END OUTPUT");
	}
	

}
