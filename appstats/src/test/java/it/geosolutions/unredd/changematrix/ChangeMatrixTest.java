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
package it.geosolutions.unredd.changematrix;

import it.geosolutions.jaiext.changematrix.ChangeMatrixDescriptor.ChangeMatrix;
import it.geosolutions.jaiext.rlookup.RangeLookupDescriptor;
import it.geosolutions.jaiext.rlookup.RangeLookupRIF;
import it.geosolutions.jaiext.rlookup.RangeLookupTable;

import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;

import org.apache.log4j.Logger;
import org.geotools.TestData;
import org.geotools.image.jai.Registry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This test is used to draft the computation of the Land use Change Matrix using the JAI-ext operator jt-changematrix
 * 
 * @author DamianoG
 *
 */
public class ChangeMatrixTest extends Assert{
    
    private final static Logger LOGGER = Logger.getLogger(ChangeMatrixTest.class);

    /**
     * Params used for this test: the 4 raster needed, the province code
     */
    private String RASTER_REFERENCE_PATH;
    private String RASTER_ACTUAL_PATH;
    private String RASTER_PROVINCES_PATH;
    private String AREA_LAYER;
    private Integer PROVINCE_CODE;
    private Integer TILE_WIDTH;
    private Integer TILE_HEIGTH;
    
    @Before
    public void loadTestParams() throws FileNotFoundException, IOException{
        ChangeMatrixTest cmt = new ChangeMatrixTest();
        Map<String,Object> map = cmt.loadDataConfigurations();
        RASTER_REFERENCE_PATH = (String)map.get("RASTER_REFERENCE_PATH"); 
        RASTER_ACTUAL_PATH = (String)map.get("RASTER_ACTUAL_PATH");
        RASTER_PROVINCES_PATH = (String)map.get("RASTER_PROVINCES_PATH");
        AREA_LAYER = (String)map.get("AREA_LAYER");
        PROVINCE_CODE = Integer.parseInt((String) map.get("PROVINCE_CODE"));
        TILE_WIDTH = Integer.parseInt((String) map.get("TILE_WIDTH"));
        TILE_HEIGTH = Integer.parseInt((String) map.get("TILE_HEIGTH"));
    }
    
    @Test
    //@Ignore("This test is used as facility to run the process with production data stored locally...")
    public void changeMatrixTest() throws IOException{
        
        if (!(new File(RASTER_REFERENCE_PATH).exists()) || !(new File(RASTER_ACTUAL_PATH).exists())
                || !(new File(RASTER_PROVINCES_PATH).exists()) || !(new File(AREA_LAYER).exists())) {
            fail();
        }
        
        long startTime = System.currentTimeMillis();
        
        forceLookupOperatorRegistration();
        
        RenderingHints hints = getTileLayout(TILE_WIDTH, TILE_HEIGTH);

        LOGGER.info("1/5 Loading and processing Data Layer...");
        // load, force data format to double and set the tiling to the Area Layer (aka Data Layer in the SLMS context)
        final RenderedOp area = JAI.create("ImageRead", AREA_LAYER);
        RenderedOp areaLayer = doFormat(area, hints);
        
        LOGGER.info("2/5 Loading and processing Region Of Interest...");
        // load the provice layer and perform a lookup in order to keep only the province we want to compute
        final RenderedOp provinces = JAI.create("ImageRead", RASTER_PROVINCES_PATH);
        RenderedOp roiImg = doLookup(LookupTableFactory.getProvinceLookupTable(PROVINCE_CODE), provinces, Short.MIN_VALUE);
        // create a Region Of Interest instance using the previously created raster
        ROI roi = new ROI(roiImg);
        roi.setThreshold(0);
        
        LOGGER.info("3/5 Loading reference image (NFI)...");
        // load the reference image (NFI), no further computation on it, we expect it ready to be used in the CM computation 
        final RenderedOp reference = JAI.create("ImageRead", RASTER_REFERENCE_PATH);
        
        LOGGER.info("4/5 Loading and processing actual image (NFIS)...");
        // load the actual image (NFIS) and harmonize it with the NFI classification 
        final RenderedOp actual = JAI.create("ImageRead", RASTER_ACTUAL_PATH);
        RenderedOp actualHarmonized = doLookup(LookupTableFactory.getHarmonizationTable(), actual,99);
        
        LOGGER.info("5/5 Running Change Matrix!...");
        // Create the Change Matrix object who is responsible to store the results, configure there the classes we want to observe
        final Set<Integer> classes = new HashSet<Integer>();
        classes.add(1);
        classes.add(2);
        classes.add(3);
        classes.add(4);
        classes.add(5);
        classes.add(6);
        classes.add(7);
        classes.add(8);
        classes.add(9);
        classes.add(10);
        classes.add(11);
        classes.add(12);
        classes.add(13);
        classes.add(14);
        classes.add(15);
        classes.add(16);
        classes.add(17);
        classes.add(99);
        final ChangeMatrix cm = new ChangeMatrix(classes);

        
        // Finally we are ready to set and run the Change Matrix operator                
        final ParameterBlockJAI pbj = new ParameterBlockJAI("ChangeMatrix");
        pbj.addSource(reference);
        pbj.addSource(actualHarmonized);
        pbj.setParameter("result", cm);
        pbj.setParameter("roi", roi);
        pbj.setParameter("pixelMultiplier", 200);
        pbj.setParameter("area", areaLayer);
        final RenderedOp result = JAI.create("ChangeMatrix", pbj, hints);
        
        // force computation
        // ------------------------------------------------------------------------------------------------------------------------------------
        final Queue<Point> tiles = new ArrayBlockingQueue<Point>(result.getNumXTiles()
                * result.getNumYTiles());
        for (int i = 0; i < result.getNumXTiles(); i++) {
            for (int j = 0; j < result.getNumYTiles(); j++) {
                tiles.add(new Point(i, j));
            }
        }
        final CountDownLatch sem = new CountDownLatch(result.getNumXTiles() * result.getNumYTiles());
        ExecutorService ex = Executors.newFixedThreadPool(20);
        for (final Point tile : tiles) {
            ex.execute(new Runnable() {

                public void run() {
                    result.getTile(tile.x, tile.y);
                    sem.countDown();
                }
            });
        }
        try {
            sem.await();
        } catch (InterruptedException e) {
            fail();
        }
        cm.freeze();
        // ------------------------------------------------------------------------------------------------------------------------------------        

        LOGGER.info("...Change Matrix results:\n");
        // Visualization of the results
        // TODO Add some assert
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(10);
        df.setMaximumIntegerDigits(10);
        int x=0;
        int y=0;
        for(int i=1; i<=19; i++){
            for(int j=1; j<=19; j++){
                x = (i==18)?99:i;
                y = (j==18)?99:j;
                x = (i==19)?Integer.MIN_VALUE:i;
                y = (j==19)?Integer.MIN_VALUE:j;
                LOGGER.info("(" + x + ", " + y + ") -num pixel--> '" + cm.retrievePairOccurrences(x, y) + "'");
                LOGGER.info("(" + x + ", " + y + ") -     area--> '" + df.format(cm.retrieveTotalArea(x, y)) + "'");
            }
        }
        
        long delta = (System.currentTimeMillis() - startTime) / 1000;
        LOGGER.info("--process lasted: --> '" + delta + "' seconds...");
    }
    
    private static RenderedOp doLookup(RangeLookupTable table, RenderedOp img, int defaultValue){
        ParameterBlockJAI pb = new ParameterBlockJAI("RLookup");
        pb.setSource("source0", img);
        pb.setParameter("table", table);
        pb.setParameter("default", defaultValue);
        return JAI.create("RLookup", pb);
    }
    
    private static RenderedOp doFormat(RenderedOp area, RenderingHints hints){
      //Convert the area image format to double
        ParameterBlockJAI pb = new ParameterBlockJAI("Format");
        pb.addSource(area);
        pb.setParameter("dataType", DataBuffer.TYPE_DOUBLE);
        final RenderedOp areaLayer = JAI.create("Format", pb, hints);
        return areaLayer;
    }
    
    private static RenderingHints getTileLayout(int tileWidth, int tileHeight){
        final ImageLayout layout = new ImageLayout();
        layout.setTileWidth(tileWidth).setTileHeight(tileHeight);
        return new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
    }
    
    private static void forceLookupOperatorRegistration(){
        try {
            Registry.registerRIF(JAI.getDefaultInstance(), new RangeLookupDescriptor(),
                    new RangeLookupRIF(), Registry.JAI_TOOLS_PRODUCT);
    
        } catch (Throwable e) {
            // swallow exception in case the op has already been registered.
        } finally{
            ImageIO.scanForPlugins();
        }
    }
    
    private Map loadDataConfigurations() throws FileNotFoundException, IOException{
        Properties prop = new Properties();
        File propFile = TestData.file(this, "changematrix-test.properties");
        InputStream is = new FileInputStream(propFile);
        if (is != null) {
            prop.load(is);
        } else {
                throw new FileNotFoundException("property file '" + propFile + "' not found in the classpath");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator iter = prop.keySet().iterator();
        while(iter.hasNext()){
            String key = (String)iter.next();
            map.put(key, prop.getProperty(key));
        }
        return map;
    }
    
    
    @Test
    public void testFormats(){
        
        //double myvalue = 0.00000021d;
        double myvalue = 6556565656.0345635621d;

        //Option 1 Print bare double.
        System.out.println("1) --> " + myvalue);

        //Option2, use decimalFormat.
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(10);
        df.setMaximumIntegerDigits(10);
        System.out.println("2) --> " + df.format(myvalue));

        //Option 3, use printf.
        System.out.printf("3) --> %.9f", myvalue);
        System.out.println();

        //Option 4, convert toBigDecimal and ask for toPlainString().
        System.out.println("4) --> " + new BigDecimal(myvalue).toPlainString());

        //Option 5, String.format 
        System.out.println("5) --> " + String.format("%.12f", myvalue));
    }
}