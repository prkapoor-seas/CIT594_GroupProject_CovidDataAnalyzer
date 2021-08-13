package edu.upenn.cit594.util;

public class CovidData {
	
	private final int zipcode;
	private final int partiallyVaccinated;
	private final int fullyVaccinated;
	private final String timestamp;
	
	public CovidData(long zipcode, long partiallyVaccinated, long fullyVaccinated, String timestamp) {
		this.zipcode = (int) zipcode;
		this.partiallyVaccinated = (int) partiallyVaccinated;
		this.fullyVaccinated = (int) fullyVaccinated;
		this.timestamp = timestamp;
	}

	public int getZipcode() {
		return zipcode;
	}

	public int getPartiallyVaccinated() {
		return partiallyVaccinated;
	}

	public int getFullyVaccinated() {
		return fullyVaccinated;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
}

