package edu.upenn.cit594.datamanagement;

import java.util.HashMap;
import java.util.List;

import edu.upenn.cit594.util.CovidData;

public interface CovidDataReader {

	public HashMap<Integer, List<CovidData>> getAllRows() throws Exception;
}
