/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  https://github.com/nfms4redd/nfms-geobatch
 *  Copyright (C) 2007-2008-2009 GeoSolutions S.A.S.
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.log4j.Logger;
import org.jaitools.media.jai.classifiedstats.Result;
import org.jaitools.numeric.Statistic;

/**
 * Unit test for simple App.
 */
public class AppTestNo
    extends TestCase
{
    private final static Logger LOGGER = Logger.getLogger(AppTestNo.class);
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTestNo( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTestNo.class );
    }

    /**
     * used to prevent junit complain about no tests
     */
    public void testDummy() {
        LOGGER.info("dummy test");
    }

    /**
     * Disabled b/c files path are hardcoded.
     */
    public void __testApp() throws IOException
    {
        final String baseDir = "/home/geosol/data/unredd";

        RasterClassifiedStatistics rcs = new RasterClassifiedStatistics();

        LOGGER.info("Setting classification files");
        List<DataFile> classifications = new ArrayList<DataFile>();

        {
            File classificationFile = new File(baseDir, "rdc_limite_administrative_fixed_4326.tif");
            DataFile classificationDataFile = new DataFile(classificationFile);
            classificationDataFile.setNoValue(65535d);
            classifications.add(classificationDataFile);
        }

        LOGGER.info("Setting data files");
        
        File dataFile = new File(baseDir, "rdc_admin_area.tif");
        DataFile datadf = new DataFile(dataFile);


        List<Statistic> reqStats = Arrays.asList(Statistic.SUM, Statistic.MIN, Statistic.MAX, Statistic.MEAN);

        LOGGER.info("Executing stats");
        Map<MultiKey,List<Result>> results = rcs.execute(true, datadf, classifications, reqStats);

        LOGGER.info("Outputting stats results");
        for (MultiKey classes : results.keySet()) {
            List<Result> result = results.get(classes);
            long cnt = result.get(0).getNumAccepted();
            double sum = result.get(0).getValue();
            double min = result.get(1).getValue();
            double max = result.get(2).getValue();
            double avg = result.get(3).getValue();
//            double sdv = result.get(4).getValue();

            System.out.println("["+classes+"] cnt:"+cnt+ " sum:"+sum+" min:"+min+" max:"+max+" avg:"+avg);//+" sdev:"+sdv);
        }

    }
//    public void testApp() throws IOException
//    {
//        RasterClassifiedStatistics rcs = new RasterClassifiedStatistics();
//
//        LOGGER.info("Setting classification files");
//        List<DataFile> classifications = new ArrayList<DataFile>();
//
//        {
//            File classificationFile = new File("/home/geosol/data/unredd", "rdc_limite_administrative_small.tif");
//            DataFile classificationDataFile = new DataFile(classificationFile);
//            classificationDataFile.setNoValue(65535d);
//            classifications.add(classificationDataFile);
//        }
//
//        LOGGER.info("Setting data files");
//        File dataFile = new File("/home/geosol/data/unredd", "rdc_admin_area_small.tif");
//        DataFile datadf = new DataFile(dataFile);
//
//
//        List<Statistic> reqStats = Arrays.asList(Statistic.SUM, Statistic.MIN, Statistic.MAX, Statistic.MEAN);
//
//        LOGGER.info("Executing stats");
//        Map<MultiKey,List<Result>> results = rcs.execute(false, datadf, classifications, reqStats);
//
//        LOGGER.info("Outputting stats results");
//        for (MultiKey classes : results.keySet()) {
//            List<Result> result = results.get(classes);
//            long cnt = result.get(0).getNumAccepted();
//            double sum = result.get(0).getValue();
//            double min = result.get(1).getValue();
//            double max = result.get(2).getValue();
//            double avg = result.get(3).getValue();
////            double sdv = result.get(4).getValue();
//
//            System.out.println("["+classes+"] cnt:"+cnt+ " sum:"+sum+" min:"+min+" max:"+max+" avg:"+avg);//+" sdev:"+sdv);
//        }
//
//    }
}
