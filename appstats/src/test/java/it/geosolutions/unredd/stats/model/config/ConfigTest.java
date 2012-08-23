/*
 *  Copyright (C) 2007 - 2011 GeoSolutions S.A.S.
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
package it.geosolutions.unredd.stats.model.config;

import it.geosolutions.unredd.stats.impl.StatsRunner;
import it.geosolutions.unredd.stats.model.config.util.StatisticChecker;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXB;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class ConfigTest extends TestCase {
    private final static Logger LOGGER = Logger.getLogger(ConfigTest.class);

    protected static ClassPathXmlApplicationContext ctx = null;


    public ConfigTest(String testName) {
        super(testName);
        ctx = new ClassPathXmlApplicationContext();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.out.println("================= " + getName());
    }

    public void testClassificationLayer() throws IOException {
        ClassificationLayer cl = new ClassificationLayer();
        cl.setFile("filename");
//        cl.setLayerName("layername");
        cl.setPivot(Arrays.asList(1.0, 2.0, 3.0));
        cl.setZonal(false);
        cl.setNodata(65535d);

//        File outFile = File.createTempFile("classif", ".xml");
//        System.out.println("Creating file " + outFile);
        JAXB.marshal(cl, System.out);
    }

    public void testStatistic() throws IOException {
        StatisticConfiguration s = new StatisticConfiguration();
        s.setName("test_stat");
        s.setTitle("A sample statistic");
        s.setDescription("A sample statistic definition for testing purposes");

        s.setTopics(Arrays.asList("test", "stat", "example"));

        s.setStats(Arrays.asList(StatsType.SUM, StatsType.COUNT));


        ClassificationLayer cl1 = new ClassificationLayer();
        cl1.setFile("filename");
        List<Double> pivot = new ArrayList<Double>();
        pivot.add(1.0);
        pivot.add(20.0);
        pivot.add(300.0);
//        cl1.setPivot(Arrays.asList(1.0, 20.0, 300.0));
        cl1.setPivot(pivot);
        cl1.setZonal(false);

        ClassificationLayer cl2 = new ClassificationLayer();
        cl2.setFile("filename2");

        s.setClassifications(Arrays.asList(cl1, cl2));

        assertNotNull(cl1.getPivot());
        assertTrue(cl1.isPivotDefined());
//        File outFile = File.createTempFile("classif", ".xml");
//        System.out.println("Creating file " + outFile);
        JAXB.marshal(s, System.out);
    }

    public void testLoad00() throws IOException {

        File file = ctx.getResource("classpath:testStat00.xml").getFile();
        assertNotNull("test file not found", file);

        StatisticConfiguration cfg = JAXB.unmarshal(file, StatisticConfiguration.class);
        assertNotNull("Error unmarshalling file", cfg);

        boolean check = StatisticChecker.check(cfg);
        System.out.println("Check is " + check);

    }

    public void testLoad01() throws IOException {

        File file = ctx.getResource("classpath:classStatSmall.xml").getFile();
        assertNotNull("test file not found", file);

        StatisticConfiguration cfg = JAXB.unmarshal(file, StatisticConfiguration.class);
        assertNotNull("Error unmarshalling file", cfg);

        boolean check = StatisticChecker.check(cfg);
        assertTrue("Configuration is not valid", check);

        assertNotNull(cfg.getOutput().getFile());

        for (ClassificationLayer xls : cfg.getClassifications()) {
            LOGGER.info("Classification: " + xls);
        }
    }

    public void _testRunAreaSmall() throws IOException {

        File file = ctx.getResource("classpath:smallStat.xml").getFile();
        assertNotNull("test file not found", file);

        StatisticConfiguration cfg = JAXB.unmarshal(file, StatisticConfiguration.class);
        assertNotNull("Error unmarshalling file", cfg);

        boolean check = StatisticChecker.check(cfg);
        assertTrue("Configuration is not valid", check);

        StatsRunner sr = new StatsRunner(cfg);
        sr.run();
    }

    public void _testRunClassSmall() throws IOException {

        File file = ctx.getResource("classpath:classStatSmall.xml").getFile();
        assertNotNull("test file not found", file);

        StatisticConfiguration cfg = JAXB.unmarshal(file, StatisticConfiguration.class);
        assertNotNull("Error unmarshalling file", cfg);

        boolean check = StatisticChecker.check(cfg);
        assertTrue("Configuration is not valid", check);

        StatsRunner sr = new StatsRunner(cfg);
        sr.run();
    }

    public void _testRunClassNormal() throws IOException {

        File file = ctx.getResource("classpath:classStat.xml").getFile();
        assertNotNull("test file not found", file);

        StatisticConfiguration cfg = JAXB.unmarshal(file, StatisticConfiguration.class);
        assertNotNull("Error unmarshalling file", cfg);

        boolean check = StatisticChecker.check(cfg);
        assertTrue("Configuration is not valid", check);

        StatsRunner sr = new StatsRunner(cfg);
        sr.run();
    }
}
