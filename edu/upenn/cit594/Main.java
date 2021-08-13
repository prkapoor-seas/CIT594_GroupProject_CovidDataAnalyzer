package edu.upenn.cit594;

import java.io.IOException;

import edu.upenn.cit594.datamanagement.CSVReaderCovidData;
import edu.upenn.cit594.datamanagement.CovidDataReader;
import edu.upenn.cit594.datamanagement.JSONReaderCovidData;
import edu.upenn.cit594.datamanagement.PopReader;
import edu.upenn.cit594.datamanagement.PopulationDataReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;
import edu.upenn.cit594.ui.CommandUserInterface;

public class Main {

	public static void main(String[] args) {
		
		if(args.length != 4) {
			System.out.println("Error in Syntax: covidDataFilename propertiesDataFilename popDataFilename logFilename");
			return;
		}
		
		String covidDataFilename = args[0];
		String propertiesDataFilename = args[1];
		String popDataFilename = args[2];
		String logFilename = args[3];
		
		Logger logger = Logger.getInstance();
		try {
			logger.init(logFilename);
		} catch (IOException e) {
			System.out.println("Error: Error opening logfile for writing");
			return;
		}
		
		CovidDataReader covidReader = null;
		
		if(covidDataFilename.substring(covidDataFilename.length()-4).toLowerCase().equals(".txt")) {
			covidReader = new CSVReaderCovidData(covidDataFilename);
		}
		else if(covidDataFilename.substring(covidDataFilename.length()-5).toLowerCase().equals(".json")) {
			covidReader = new JSONReaderCovidData(covidDataFilename);
		}
		else {
			System.out.println("Error: Tweets file should have .txt or .json extension");
			return;
		}
		
		PopulationDataReader popReader = new PopReader(popDataFilename);
		
		try {
			Processor processor = new Processor(covidReader, popReader);
			CommandUserInterface ui = new CommandUserInterface(processor);
			ui.start();
		} catch (Exception e) {
			System.out.println("One of the data files cannot be opened");
			return;
		}
		
	}

}