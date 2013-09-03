/*
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.unredd;

import it.geosolutions.unredd.stats.impl.DataFile;
import it.geosolutions.unredd.stats.impl.RasterClassifiedStatistics;
import it.geosolutions.unredd.stats.impl.StatsRunner;
import it.geosolutions.unredd.stats.model.config.Output;
import it.geosolutions.unredd.stats.model.config.Range;
import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;
import it.geosolutions.unredd.stats.model.config.StatsType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.log4j.Logger;
import org.geotools.test.TestData;
import org.jaitools.media.jai.classifiedstats.Result;
import org.jaitools.numeric.Statistic;

/**
 * @author DamianoG
 * 
 */
public class StatsTests extends TestCase {

    private final static Logger LOGGER = Logger.getLogger(StatsTests.class);

    private static final String DATA = "data";
    private static final String CLASSIFICATOR_PREFIX = "classificator";
    private static final String PROP_FILE = "testSimpleStats.properties";
    private static final String DEFAULT_DATA = "test-data/area.tif"; //TODO fix this, maybe don't work on linux
    private static final String DEFAULT_CLASSIFICATOR = "test-data/forest_mask.tif";//TODO fix this, maybe don't work on linux
    private static final String OUTPUT_FILE = "outStats.csv";
    
    /**
     * If the dataset used to compute the stats is the default one perform some asserts, otherwise skip asserts
     */
    private static boolean isDefault = true;
    
    private static final Statistic [] STATS = new Statistic[] { Statistic.SUM};
    
    /**
     * A simple test that runs the stats throught the RasterClassifiedStatistics, useful for test different data when problems occurs
     * You can configure the data used in /src/test/resources/testSimpleStats.properties to test your custom data and check if the stats are generated...
     * 
     */
    public void testSimpleStats() {

        RasterClassifiedStatistics rcs = new RasterClassifiedStatistics();
        
        //load the test params
        Map<String, File> prop = loadTestParams();
        //create the classification layers array
        List<DataFile> classificatorsList = new ArrayList<DataFile>();
        for(int i=0; i<prop.size()-1; i++){
            classificatorsList.add(new DataFile(prop.get(CLASSIFICATOR_PREFIX + (i+1))));
        }
        OutputStatsTest ost = null;
        try {
            // run the stats
            Map<MultiKey, List<Result>> results = rcs.execute(true, new DataFile(prop.get(DATA)), classificatorsList, Arrays
                    .asList(STATS));
            LOGGER.info(results.toString());
            ost = OutputStatsTest.buildOutputStatsTest();
            ost.outputStats(results);
            
            if(isDefault){
                // do the checks only if the default data and clasificators layers are loaded
                assertEquals(results.toString(),"{MultiKey[0]=[band 0 sum: 3449837399963,9688 Naccepted=9454812 (offered:9454812 - NoData:0 - NaN:0)  Classifier:MultiKey[0]], MultiKey[1]=[band 0 sum: 1599142551508,9062 Naccepted=4355376 (offered:4355376 - NoData:0 - NaN:0)  Classifier:MultiKey[1]]}");
            }
            
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally{
            new File(ost.getOutputFile()).deleteOnExit();
        }
    }
    
    public void testStatsWithRanges() {

        RasterClassifiedStatistics rcs = new RasterClassifiedStatistics();
        
        //load the test params
        Map<String, File> prop = loadTestParams();
        //create the classification layers array
        List<DataFile> classificatorsList = new ArrayList<DataFile>();
        for(int i=0; i<prop.size()-1; i++){
            classificatorsList.add(new DataFile(prop.get(CLASSIFICATOR_PREFIX + (i+1))));
        }
        OutputStatsTest ost = null;
        try {
            // run the stats
            DataFile df = new DataFile(prop.get(DATA));
            Range r = new Range();
            r.setRange("[1;1]");
            r.setIsAnExcludeRange(true);
            List<Range> arrList = new ArrayList<Range>();
            arrList.add(r);
            df.setRanges(arrList);
            Map<MultiKey, List<Result>> results = rcs.execute(true, df, classificatorsList, Arrays
                    .asList(STATS));
            LOGGER.info(results.toString());
            ost = OutputStatsTest.buildOutputStatsTest();
            ost.outputStats(results);
            
            if(isDefault){
                // do the checks only if the default data and clasificators layers are loaded
                assertEquals(results.toString(),"{MultiKey[0]=[band 0 sum: 3449837399963,9688 Naccepted=9454812 (offered:9454812 - NoData:0 - NaN:0)  Classifier:MultiKey[0]], MultiKey[1]=[band 0 sum: 1599142551508,9062 Naccepted=4355376 (offered:4355376 - NoData:0 - NaN:0)  Classifier:MultiKey[1]]}");
            }
            
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } /*finally{
            File of = new File(ost.getOutputFile());
            of.delete();
            if(of.exists()){
                fail("Failed when try to remove tmp files...");
            }
        }*/
    }
    
    private static Map<String, File> loadTestParams(){
        
        Properties defaultProperties = new Properties();
        defaultProperties.put(DATA, DEFAULT_DATA);
        defaultProperties.put(CLASSIFICATOR_PREFIX + "1", DEFAULT_CLASSIFICATOR);
        File propFile = loadFileFromResources(PROP_FILE);
        InputStream inStream = null;
        Properties prop = null;
        try {
            inStream = new FileInputStream(propFile);
            prop = new Properties(defaultProperties);
            prop.load(inStream);
        } catch (Exception e) {
            fail("Fail when loading layer path file...");
        }
        // Transform the file names into File objects
        Map<String, File> map = new HashMap<String, File>();
        Enumeration kEnumeration = prop.propertyNames();
        while(kEnumeration.hasMoreElements()){
            String key = (String) kEnumeration.nextElement();
            if(prop.getProperty(key).startsWith("test-data")){
                map.put(key, loadFileFromResources(prop.getProperty(key)));
            }
            else{
                map.put(key, new File(prop.getProperty(key)));
                isDefault = false;
            }
        }
        return map;
    }
    
    private static File loadFileFromResources(String resourceName){
        
        URL propUrl = null;
        try {
            propUrl = StatsTests.class.getResource(resourceName);
            return new File(propUrl.toURI());
        } catch (URISyntaxException e) {
            fail("One resource layer fails the loading... check the file specified in testSimpleStats.properties files before start to debugging...");
        }
        return null;
    }
    
    public static class OutputStatsTest extends StatsRunner{

        private StatisticConfiguration cfg;
        /**
         * @param cfg
         */
        public OutputStatsTest(StatisticConfiguration cfg) {
            super(cfg);
            this.cfg = cfg;
        }
        
        public static OutputStatsTest buildOutputStatsTest() {
            StatisticConfiguration cfg = new StatisticConfiguration();
            StatsType [] sType = {StatsType.COUNT};
            cfg.setStats(Arrays.asList(sType));
            Output output = new Output();
            output.setSeparator(";");
            output.setNanValue("-256");
            String fileName = null;
            try {
                fileName = TestData.file(StatsTests.class, null).getAbsolutePath() + File.separator + OUTPUT_FILE;
            } catch (Exception e) {
                fail("fail during output file name creation....");
                LOGGER.error(e.getMessage(), e);
            }
            output.setFile(fileName);
            cfg.setOutput(output);
            return new OutputStatsTest(cfg);
        } 
        
        public void outputStats(Map<MultiKey, List<Result>> results){
            super.outputStats(results);
        }
        
        public String getOutputFile(){
            if(cfg != null && cfg.getOutput() != null){
               return cfg.getOutput().getFile(); 
            }
            return null;
        }
    }

}
