package edu.upenn.cit594.util;

public class PropertiesData {

	private final int zipcode;
	private final double  marketvalue;
	private final double totalLivableArea;
	
	public PropertiesData(int zipcode, double marketvalue, double totalLivableArea){
		this.zipcode = zipcode;
		this.marketvalue = marketvalue;
		this.totalLivableArea = totalLivableArea;
	}
	
	public int getZipcode() {
		return zipcode;
	}

	public double getMarketvalue() {
		return marketvalue;
	}

	public double getTotalLivableArea() {
		return totalLivableArea;
	}

	public double getMarketValue() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

