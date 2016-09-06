/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  https://github.com/slms4redd/nfms-geobatch
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
package it.geosolutions.unredd.apputil;

import it.geosolutions.imageio.plugins.tiff.TIFFImageWriteParam;
import java.io.File;
import java.io.IOException;
import javax.media.jai.JAI;
import javax.media.jai.TileScheduler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.raster.gs.AreaGridProcess2;
import org.geotools.referencing.CRS;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;



/**
 * Hello world!
 *
 */
public class AreaBuilder {
    private final static Logger LOGGER = Logger.getLogger(AreaBuilder.class.toString());

    private static final char OPT_EXTENTS = 'x';
    private static final char OPT_SIZE =    's';
    private static final char OPT_OUTFILE = 'o';
    private static final char OPT_THREADS = 't';
    private static final char OPT_MEM     = 'm';

    private static final long KILO = 1024;
    private static final long MEGA = 1024 * KILO;
    private static final long GIGA = 1024 * MEGA;

    public static void main(String[] args) {
        Option help = OptionBuilder
                    .withLongOpt( "help" )
                    .withDescription(  "print help" )
                    .create( '?' );

        Options helpOptions = new Options()
                .addOption(help);

        Options options = new Options()
                .addOption(help)
                .addOption(OptionBuilder
                    .withLongOpt( "extents" )
                    .withArgName( "n/e/s/w" )
                    .withDescription(  "extents in the format n/e/s/w" )
                    .hasArgs(4)
                    .withValueSeparator('/')
                    .isRequired()
                    .withType(Double.class)
                    .create( OPT_EXTENTS ))
                .addOption(OptionBuilder
                    .withLongOpt( "size" )
                    .withArgName( "width,height" )
                    .withDescription(  "size of output image in pixel in the format width,height" )
                    .hasArgs(2)
                    .withValueSeparator(',')
                    .isRequired()
                    .withType(Integer.class)
                    .create( OPT_SIZE ))
                .addOption(OptionBuilder
                    .withLongOpt( "outfile" )
                    .withArgName( "file" )
                    .withDescription(  "the output tiff file" )
                    .hasArg()
                    .isRequired()
                    .withType(String.class)
                    .create( OPT_OUTFILE ))
                .addOption(OptionBuilder
                    .withLongOpt( "mem" )
                    .withArgName( "megabytes" )
                    .withDescription(  "the max memory available for the operation" )
                    .hasArg()
                    .create( OPT_MEM ))
                .addOption(OptionBuilder
                    .withLongOpt( "threads" )
                    .withArgName( "numThreads" )
                    .withDescription(  "number of threads JAI will use" )
                    .hasArg()
                    .create( OPT_THREADS ));


        try {

            //=== Create parser
            CommandLineParser parser = new PosixParser();
            CommandLine cmd = parser.parse( options, args);

            //=== Parse input params
            String sFile = cmd.getOptionValue(OPT_OUTFILE);

            String[] sSizeArr = cmd.getOptionValues(OPT_SIZE);
            if(sSizeArr.length != 2) {
                LOGGER.error("size requires 2 args");
                return;
            }
            String[] sExtArr = cmd.getOptionValues(OPT_EXTENTS);
            if(sExtArr.length != 4) {
                LOGGER.error("extents require 4 args");
                return;
            }

            File file = new File(sFile);
            LOGGER.info("Output file " + file);

            int w = Integer.parseInt(sSizeArr[0]);
            int h = Integer.parseInt(sSizeArr[1]);
            LOGGER.info("Image size " + w + " x " + h);

            double boxn = Double.parseDouble(sExtArr[0]);
            double boxe = Double.parseDouble(sExtArr[1]);
            double boxs = Double.parseDouble(sExtArr[2]);
            double boxw = Double.parseDouble(sExtArr[3]);
            LOGGER.info("Image bbox is n:"+boxn+ "e:"+boxe+" s:"+boxs+" w:"+boxw);

            //=== Parse and set tilecache memory
            Long mega = 512l;
            if(cmd.hasOption(OPT_MEM)) {
                mega = Long.parseLong(cmd.getOptionValue(OPT_MEM));
                LOGGER.info("JAI tilecache memory set to " + mega + "MB");
            } else {
                LOGGER.info("JAI tilecache memory defaulting to " + mega + "MB");
            }

            JAI.getDefaultInstance().getTileCache().setMemoryCapacity(mega * MEGA);

            //=== Parse and set JAI parallelism level
            int threads = 4;
            if(cmd.hasOption(OPT_THREADS)) {
                threads = Integer.parseInt(cmd.getOptionValue(OPT_THREADS));
                LOGGER.info("JAI tile scheduler parallelism set to " + threads);
            } else {
                LOGGER.info("JAI tile scheduler parallelism defaulting to " + threads);
            }

            TileScheduler ts  = JAI.getDefaultInstance().getTileScheduler();
            ts.setParallelism(threads);
            ts.setPrefetchParallelism(threads);

            //=== Create grid and save

            System.setProperty("org.geotools.referencing.forceXY", "true");
            GridCoverage2D grid = createAreaGrid(w, h, boxw, boxe, boxs, boxn);
            saveAreaGrid(grid, file);

        } catch (ParseException ex) {

            CommandLine cmd0 = null;
            try { // find out if an help was requested (it's missing mandatory params)
                CommandLineParser helpParser = new PosixParser();
                cmd0 = helpParser.parse( helpOptions, args);
            } catch (ParseException ex1) {
                LOGGER.error("Unexpected error: " + ex1);
            }

            if(cmd0 == null || ! cmd0.hasOption("help")) {
                LOGGER.error("Parse error: " + ex);
            }

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "createAreaLayer", options );
        } catch(Exception e) {
            LOGGER.error("Unexpected exception", e);
        }
    }


    public static GridCoverage2D createAreaGrid(int width, int height, double w, double e, double s, double n ) throws Exception {
        LOGGER.info("AREAGRIDPROCESS: creating area grid");
        AreaGridProcess2 process = new AreaGridProcess2();
            CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
//                CoordinateReferenceSystem
                // org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
        ReferencedEnvelope envelope = new ReferencedEnvelope(w, e, s, n, crs);
        GridCoverage2D grid = process.execute(envelope, width, height);
        return grid;
    }

    public static void saveAreaGrid(GridCoverage2D grid, File output) throws IOException{
        LOGGER.info("AREAGRIDPROCESS: saving area grid");

        GeoTiffWriter writer = new GeoTiffWriter(output);
        GeoTiffWriteParams params = new GeoTiffWriteParams();
        params.setTilingMode(TIFFImageWriteParam.MODE_EXPLICIT);
        params.setTiling(512, 512);
        ParameterValue<GeoToolsWriteParams> value = GeoTiffFormat.GEOTOOLS_WRITE_PARAMS.createValue();
        value.setValue(params);

        writer.write(grid, new GeneralParameterValue[]{value});
        writer.dispose();
    }

//    public void testAreaGridParameters() throws Exception {
//        LOGGER.info("AREAGRIDPROCESS: Performing parameter test");
//        AreaGridProcess process = new AreaGridProcess();
//        CoordinateReferenceSystem crs = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
//        ReferencedEnvelope envelope = new ReferencedEnvelope(-180, 180, -90, 90, crs);
//        GridCoverage2D grid = process.execute(envelope, -2, 10);
//
//        geotiffwriter
//    }
//
//    public void testAreaGrid() throws Exception {
//        LOGGER.info("AREAGRIDPROCESS: Performing process execute test");
//        int cx = 100;
//        int cy = 150;
//        AreaGridProcess process = new AreaGridProcess();
//        CoordinateReferenceSystem crs = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
//        ReferencedEnvelope envelope = new ReferencedEnvelope(-180, 180, -90, 90, crs);
//        GridCoverage2D grid = process.execute(envelope, cx, cy);
//        double sum = 0.0;
//        for (int i = 0; i < cy; i++) {
//            for (int j = 0; j < cx; j++) {
//                GridCoordinates2D gridCoordinate = new GridCoordinates2D(i, j);
//                float[] band = new float[1];
//                float[] result = grid.evaluate(gridCoordinate, band);
//                sum = sum + result[0];
//            }
//        }
////        assertTrue( sum > 5.10E14 && sum < 5.13E14 );
//    }

}
