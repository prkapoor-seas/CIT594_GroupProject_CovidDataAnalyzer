package edu.upenn.cit594.util;

public class PropertiesData {

	private final Integer zipcode;
	private final Double  marketvalue;
	private final Double totalLivableArea;
	
	public PropertiesData(Integer zipcode, Double marketvalue, Double totalLivableArea){
		this.zipcode = zipcode;
		this.marketvalue = marketvalue;
		this.totalLivableArea = totalLivableArea;
	}
	
	public Integer getZipcode() {
		return zipcode;
	}

	public Double getTotalLivableArea() {
		return totalLivableArea;
	}

	public Double getMarketValue() {
		return marketvalue;
	}
	
}

