package it.geosolutions.unredd.onlinestats;

import it.geosolutions.unredd.stats.impl.StatsRunner;
import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;
import it.geosolutions.unredd.stats.model.config.util.ROIGeometryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
 * 
 * @author DamianoG
 * 
 */
@DescribeProcess(title = "OnlineStatsWPS", description = "A services that expose the RasterClassificationStatistics")
public class OnlineStatsWPS implements GSProcess {

    private final static Logger LOGGER = Logger.getLogger(StatsRunner.class);

    @DescribeResult(name = "result", description = "a String representing Statistics in a csv format")
    public String execute(
            @DescribeParameter(name = "geometry", description = "Geometry representing a Region Of Interest.") Geometry roi,
            @DescribeParameter(name = "statConf", description = "An object represent the Statistic definition.") StatisticConfiguration statConf) {

        String result = "";
        try {
            long startTime = 0;
            // Build the Region Of Interest in raster space as a JAITools ROIGeometry object from the provided WKT
            ROIGeometry roiGeom = ROIGeometryBuilder.build(statConf, roi);
            // Instantiate a Stats runner with ROI using the appropriate constructor
            StatsRunner runner = new StatsRunner(statConf, roiGeom);
            // Launch the stasts Calculation
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Starting Statistic calculation...");
                startTime = System.currentTimeMillis();
            }
            runner.run();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Statistic calculation end with no error in"
                        + (System.currentTimeMillis() - startTime)*1000 + "seconds");
            }

        } catch (Exception e) {
            result = OUTPUT_RESULT.internal_server_error.toString();
        }

        String outputPath = statConf.getOutput().getFile();
        try {
            result = FileUtils.readFileToString(new File(outputPath));
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return result;

    }

    private enum OUTPUT_RESULT {
        internal_server_error, result_linked
    }
}