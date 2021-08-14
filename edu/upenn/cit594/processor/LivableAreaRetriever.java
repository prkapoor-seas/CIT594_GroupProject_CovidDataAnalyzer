package edu.upenn.cit594.processor;

import edu.upenn.cit594.util.PropertiesData;

/**
 * Class that implements PopulationDataRetriever that has a method to return the list of livable areas
 * for the PopulationData object
 */
public class LivableAreaRetriever implements PopulationDataRetriever {
    public double returnData(PropertiesData data) {
        return data.getTotalLivableArea();
    }
}
