package edu.upenn.cit594.ui;

import java.util.Scanner;
import java.util.TreeMap;

import edu.upenn.cit594.processor.Processor;

public class CommandUserInterface {
	
	protected Processor processor;
	protected Scanner in;
	
	public CommandUserInterface(Processor processor){
		this.processor = processor;
		this.in = new Scanner(System.in);
	}
	
	public void start() {
		
		while(true) {
			System.out.println("Enter 0 to exit, 1 to get total population across all zips, 2 to get total (partial/full) vaccinations per capita by zip");
			System.out.print("> ");
			String choice = in.nextLine();
			
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
			System.out.println("Enter partial for partial vaccinations or full for full vaccinations");
			System.out.print("> ");
			vaccine = in.nextLine().trim();
			if(vaccine.equals("partial")) {
				System.out.println("BEGIN OUTPUT");
				TreeMap<Integer, Double> map = this.processor.getPartiallyVaccinatedPerCapita();
				for(int key: map.keySet()) {
					System.out.print(key + " " + String.format("%.4f", map.get(key)));
					
				}
				System.out.println("END OUTPUT");
			}
			else if(vaccine.equals("full")) {
				System.out.println("BEGIN OUTPUT");
				TreeMap<Integer, Double> map = this.processor.getFullyVaccinatedPerCapita();
				for(int key: map.keySet()) {
					System.out.print(key + " " + String.format("%.4f", map.get(key)));
					
				}
				System.out.println("END OUTPUT");
			}
		}
	}
	
	

}