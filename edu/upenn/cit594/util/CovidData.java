package edu.upenn.cit594.util;

public class CovidData {
	
	private final Integer zipcode;
	private final Integer partiallyVaccinated;
	private final Integer fullyVaccinated;
	private final String timestamp;
	
	public CovidData(Integer zipcode, Integer partiallyVaccinated, Integer fullyVaccinated, String timestamp) {
		this.zipcode = zipcode;
		this.partiallyVaccinated = partiallyVaccinated;
		this.fullyVaccinated =  fullyVaccinated;
		this.timestamp = timestamp;
	}

	public Integer getZipcode() {
		return zipcode;
	}

	public Integer getPartiallyVaccinated() {
		return partiallyVaccinated;
	}

	public Integer getFullyVaccinated() {
		return fullyVaccinated;
	}

	public String getTimestamp() {
		return timestamp;
	}
	
}

