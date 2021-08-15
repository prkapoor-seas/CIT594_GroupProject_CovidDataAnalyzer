package edu.upenn.cit594.datamanagement;

import java.util.HashMap;
import java.util.List;

import edu.upenn.cit594.util.PopulationData;

public interface PopulationDataReader {
	
	public List<PopulationData> getAllRows() throws Exception;
	
	public HashMap<Integer, Integer> getMap() throws Exception;

}
