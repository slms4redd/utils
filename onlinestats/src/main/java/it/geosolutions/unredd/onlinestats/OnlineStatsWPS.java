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
package it.geosolutions.unredd.onlinestats;

import static it.geosolutions.unredd.onlinestats.OnlineStatsWPSUtils.addOutputFile;
import static it.geosolutions.unredd.onlinestats.OnlineStatsWPSUtils.checkReferencedResources;
import static it.geosolutions.unredd.onlinestats.OnlineStatsWPSUtils.createOutputMsg;
import static it.geosolutions.unredd.onlinestats.OnlineStatsWPSUtils.outputFileExist;
import static it.geosolutions.unredd.onlinestats.OnlineStatsWPSUtils.validateInput;
import it.geosolutions.unredd.onlinestats.OnlineStatsWPSUtils.OUTPUT_RESULT;
import it.geosolutions.unredd.stats.impl.StatsRunner;
import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;
import it.geosolutions.unredd.stats.model.config.util.ROIGeometryBuilder;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;
import org.jaitools.imageutils.ROIGeometry;

import com.vividsolutions.jts.geom.Geometry;
/**
 * This WPS Process wraps The UN-REDD stat engine in order to Implement the online statistic calculation. With this feature an user of the portal is
 * able to select a custom Area on the portal client and request the stats calculation. 
 * This class uses the static utility methods of OnlineStatsWPSUtils class.
 * 
 * @author DamianoG
 * 
 */
@DescribeProcess(title = "OnlineStatsWPS", description = "A services that expose the RasterClassificationStatistics")
public class OnlineStatsWPS implements GSProcess {

    private final static Logger LOGGER = Logger.getLogger(OnlineStatsWPSUtils.class);

    @DescribeResult(name = "result", description = "a String representing Statistics in csv format. If an error is occurred a string starting with 'internal_server_error' is returned")
    public String execute(
            @DescribeParameter(name = "geometry", description = "Geometry representing a Region Of Interest.") Geometry roi,
            @DescribeParameter(name = "statConf", description = "An object represent the Statistic definition.") StatisticConfiguration statConf) {

        String resultDescription = "";
        if(!validateInput(roi, statConf, resultDescription)){
            return createOutputMsg(OUTPUT_RESULT.internal_server_error, resultDescription);
        }
        if(!checkReferencedResources(statConf, resultDescription)){
            return createOutputMsg(OUTPUT_RESULT.resource_not_found, resultDescription);
        }

        String result = "";
        try {
            long startTime = 0;
            // Build the Region Of Interest in raster space as a JAITools ROIGeometry object from the provided WKT
            ROIGeometry roiGeom = ROIGeometryBuilder.build(statConf, roi);
            // Instantiate a Stats runner with ROI using the appropriate constructor
            StatsRunner runner = new StatsRunner(statConf, roiGeom);
            // If the output file path inside statsConf is null, add a temp file path... Remember: the stats engine store the output only on filesystem.
            if(!outputFileExist(statConf)){
                addOutputFile(statConf);
            }
            // Launch the stasts Calculation
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Starting Statistic calculation...");
                startTime = System.currentTimeMillis();
            }
            runner.run();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Statistic calculation end with no error in"
                        + (System.currentTimeMillis() - startTime) * 1000 + "seconds");
            }
            // sure that statConf.getOutput().getFile() doesn't return NULL due to previous checks
            String outputPath = statConf.getOutput().getFile();
            result = FileUtils.readFileToString(new File(outputPath));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result = createOutputMsg(OUTPUT_RESULT.internal_server_error, e.getMessage());
        }
        return result;

    }
}