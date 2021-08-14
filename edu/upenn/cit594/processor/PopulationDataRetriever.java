package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertiesData;


/**
 * Interface to be implemented to specify which set of data should be retrieved from a PopulationData object
 */
public interface PopulationDataRetriever {
    public double returnData(PropertiesData data);
}
