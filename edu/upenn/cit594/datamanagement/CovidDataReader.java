package edu.upenn.cit594.datamanagement;

import java.util.List;

import edu.upenn.cit594.util.CovidData;

public interface CovidDataReader {

	public List<CovidData> getAllRows() throws Exception;
}
