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

	public void start() throws Exception {
		
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
	
	protected void getVaccinationsPerCapita() throws Exception {

		System.out.println("Enter \"partial\" for partial vaccinations or \"full\" for full vaccinations");
		System.out.println("> ");
		System.out.flush();
		String vaccine = in.nextLine();
		
		long time = System.currentTimeMillis();
		String out = Long.toString(time);
		out+= " " + vaccine;
		logger.logString(out);
		
		vaccine = vaccine.toLowerCase();
	
		if(vaccine.equals("partial")) {
			System.out.println("BEGIN OUTPUT");
			System.out.print(this.processor.getVaccinatedPerCapita(vaccine));
			System.out.println("END OUTPUT");
		}
		else if(vaccine.equals("full")) {
			System.out.println("BEGIN OUTPUT");
			System.out.print(this.processor.getVaccinatedPerCapita(vaccine));
			System.out.println("END OUTPUT");
		}
		else {
			System.out.println("Error: Invalid choice");
			throw new Exception();
		}
		
	}
	
	protected void getAvgMktValue() throws Exception {
		System.out.print("Please enter a zip code below");
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
			throw new Exception();
		}
		
	}
	
	protected void getAvgLivArea() throws Exception {
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
			throw new Exception();
		}
		
	}
	
	protected void getResMktValPerCapita() throws Exception {
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
			throw new Exception();
		}
		
	}

	protected void getMaxVaccinePerCapita() {
		System.out.println("BEGIN OUTPUT");
		System.out.print(this.processor.getMinMaxMktVal());
		System.out.println("END OUTPUT");
	}
	

}
