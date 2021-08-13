package edu.upenn.cit594.datamanagement;

import java.util.List;

import edu.upenn.cit594.util.PropertiesData;

public interface PropertiesDataReader {

	public List<PropertiesData> getAllRows() throws Exception;
	
}
