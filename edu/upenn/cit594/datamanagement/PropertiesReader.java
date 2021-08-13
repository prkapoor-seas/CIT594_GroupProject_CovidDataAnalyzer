package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.PopulationData;
import edu.upenn.cit594.util.PropertiesData;

import java.io.*;
import java.util.*;

public class PropertiesReader implements PropertiesDataReader {


    protected String filename;

    public static final int START = 0;
    private static final int QUOTEOPENED = 1;
    private static final int NORMAL = 2;
    private static final int QUOTECLOSED = 3;

    public PropertiesReader(String name) {
        filename = name;
    }

    public List<PropertiesData> getAllRows() {
        List<PropertiesData> listOfData = new LinkedList<PropertiesData>();
        Integer zipIndex = null;
        Integer livableAreaIndex = null;
        Integer marketValueIndex = null;
        String zipCodeAsString;
        Integer zipCode;
        Double livableArea;
        Double marketValue;
        BufferedReader file = null;

        try {
            File inputFile = new File(filename);
            file = new BufferedReader(new FileReader(inputFile));

            String line = file.readLine();
            String[] firstLineAsArray = line.split(",");
            for (int i = 0; i < firstLineAsArray.length; i++) {
                if (firstLineAsArray[i] == "zip_code") {
                    zipIndex = i;
                }
                if (firstLineAsArray[i] == "total_livable_area") {
                    livableAreaIndex = i;
                }
                if (firstLineAsArray[i] == "market_value") {
                    marketValueIndex = i;
                }
            }
            line = file.readLine();
            int state = START;
            StringBuilder accum = null;
            ArrayList<String> record;
            while (line != null) {
                state = START;
                record = new ArrayList<String>();
                for (byte b : line.getBytes()) {
                    char c = (char) b;
                    switch (state) {
                        case START:
                            switch (c) {
                                case ',':
                                    record.add("");
                                    continue;
                                case '\"':
                                    accum = new StringBuilder();
                                    state = QUOTEOPENED;
                                    continue;
                                default:
                                    accum = new StringBuilder();
                                    state = NORMAL;
                                    continue;
                            }
                        case NORMAL:
                            switch (c) {
                                case ',':
                                    record.add(accum.toString());
                                    continue;
                                default:
                                    accum.append(c);
                                    continue;
                            }
                        case QUOTEOPENED:
                            switch (c) {
                                case '\"':
                                    state = QUOTECLOSED;
                                    continue;
                                default:
                                    accum.append(c);
                            }
                        case QUOTECLOSED:
                            switch (c) {
                                case ',':
                                    record.add(accum.toString());
                                    state = START;
                                    continue;

                                case '\"':
                                    state = QUOTEOPENED;
                                    accum.append('\"');
                                    continue;
                            }
                    }
                }
                try {
                    zipCodeAsString = record.get(zipIndex).substring(0, 4);
                    zipCode = Integer.parseInt(zipCodeAsString);
                    marketValue = Double.parseDouble(record.get(marketValueIndex));
                    livableArea = Double.parseDouble(record.get(livableAreaIndex));
                    listOfData.add(new PropertiesData(zipCode, marketValue, livableArea));
                } catch (NumberFormatException e) {
                    continue;
                }


                line = file.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return listOfData;
    }
}
