/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  https://github.com/nfms4redd/nfms-geobatch
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
package it.geosolutions.unredd.onlinestats;

import it.geosolutions.unredd.stats.model.config.Output;
import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Utility class for OnlineStatsWPS Process
 * 
 * @author DamianoG
 * 
 */
public class OnlineStatsWPSUtils {

    private final static Logger LOGGER = Logger.getLogger(OnlineStatsWPSUtils.class);

    /**
     * 
     * @param roi
     * @param statConf
     * @param resultDescription
     * @return True if the input provided to the service are not null and not empty
     */
    public static boolean validateInput(Geometry roi, StatisticConfiguration statConf,
            String resultDescription) {
        // Validate input parameter
        if (roi == null || roi.isEmpty() || statConf == null) {
            resultDescription = "one or more input parameter are null or empty";
            LOGGER.error(resultDescription);
            return false;
        }
        return true;
    }

    /**
     * Check if the files referenced into a StatisticConfiguration object exists or not
     * 
     * @param statConf
     * @param resultDescription
     * @return true if all referenced resources exist, false otherwise
     */
    public static boolean checkReferencedResources(StatisticConfiguration statConf,
            String resultDescription) {
        if (!(statConf.getDataLayer() != null && statConf.getDataLayer().getFile() != null)) {
            resultDescription = "The DataLayer file is not specified in StatisticConfiguration";
            LOGGER.error(resultDescription);
            return false;
        } else if (!new File(statConf.getDataLayer().getFile()).exists()) {
            resultDescription = "The DataLayer file [" + statConf.getDataLayer().getFile()
                    + "] specified in StatisticConfiguration doesn't exist";
            LOGGER.error(resultDescription);
            return false;
        } else {
            if (statConf.getClassifications() == null || statConf.getClassifications().isEmpty()) {
                resultDescription = "You don't have provide any Classification Layer";
                LOGGER.error(resultDescription);
                return false;
            }
            for (int i = 0; i < statConf.getClassifications().size(); i++) {
                if (!new File(statConf.getClassifications().get(i).getFile()).exists()) {
                    resultDescription = "The ClassificationLayer file ["
                            + statConf.getClassifications().get(i).getFile()
                            + "] specified in StatisticConfiguration doesn't exist";
                    LOGGER.error(resultDescription);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Create the output message concatenating The output result code with a descriprion message
     * 
     * @param value
     * @param msg
     * @return the message
     */
    public static String createOutputMsg(OUTPUT_RESULT value, String msg) {
        StringBuilder builder = new StringBuilder();
        builder.append(value);
        builder.append(" - ");
        builder.append(msg);
        return builder.toString();
    }

    /**
     * Check if inside a not null StatisticConfiguration is specified the output file
     * 
     * @param statConf
     * @return true if is specified, false otherwise
     */
    public static boolean outputFileExist(StatisticConfiguration statConf) {
        if (statConf.getOutput() == null) {
            return false;
        } else if (statConf.getOutput().getFile() == null
                || statConf.getOutput().getFile().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Add the output file to a provided not null StatisticConfiguration instance
     * 
     * @param statConf
     * @throws IOException if the tmp file creation isn't done
     */
    public static void addOutputFile(StatisticConfiguration statConf) throws IOException {
        File tmp = File.createTempFile("nfmsWPS", ".stats");
        if (statConf.getOutput() == null) {
            Output out = new Output();
            statConf.setOutput(out);
            out.setFormat("CSV");
            out.setSeparator(";");
        }
        statConf.getOutput().setFile(tmp.getAbsolutePath());
    }

    /**
     * The results codes of the OnlineStatsWPS process
     * 
     * @author DamianoG
     * 
     */
    public enum OUTPUT_RESULT {
        internal_server_error, result_linked, resource_not_found
    }
}
