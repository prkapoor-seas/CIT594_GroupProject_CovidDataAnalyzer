package edu.upenn.cit594.datamanagement;

import java.util.TreeMap;
import java.util.List;

public interface PopulationDataReader {
	
	public TreeMap<Integer, Integer> getMap() throws Exception;

}
