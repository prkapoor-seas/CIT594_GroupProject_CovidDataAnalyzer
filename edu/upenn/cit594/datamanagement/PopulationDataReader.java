package edu.upenn.cit594.datamanagement;

import java.util.TreeMap;
import java.util.List;

import edu.upenn.cit594.util.PopulationData;

public interface PopulationDataReader {
	
	public List<PopulationData> getAllRows() throws Exception;
	
	public TreeMap<Integer, Integer> getMap() throws Exception;

}
