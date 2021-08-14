package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertiesData;

public class ValueSelector implements PropertiesSelector {

	@Override
	public Double getField(PropertiesData data) {
		return data.getMarketValue();
	}
	
	

}
