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
package it.geosolutions.unredd.stats.model.config.util;

import it.geosolutions.unredd.stats.model.config.ClassificationLayer;
import it.geosolutions.unredd.stats.model.config.Layer;
import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;
import org.apache.log4j.Logger;

/**
 * Check that the statistic configuration is well formed.
 * 
 * <P>
 * TODO: check that COUNT stats is backed by a real statistic when pivotting is requested.
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class StatisticChecker {
    private final static Logger LOGGER = Logger.getLogger(StatisticChecker.class);

    public static boolean check(StatisticConfiguration s) {

        if(s.getDataLayer() == null) {
            LOGGER.warn("Missing data layer");
            return false;
        }

        if(!check(s.getDataLayer()))
            return false;

        if(s.getStats() == null || s.getStats().isEmpty()) {
            LOGGER.warn("No stats requested");
            return false;
        }

        int cntZonal = 0;
        int cntPivot = 0;

        if(s.getClassifications() != null) {
            for (ClassificationLayer cl : s.getClassifications()) {
                if(!check(cl))
                    return false;

                if(cl.isPivotDefined()) {
                    cntPivot++;
                    if(cl.getPivot().isEmpty()) { // never happens: isPivotDefined() would return false
                        LOGGER.warn("Empty pivot classification");
                        return false;
                    } else if(cl.getPivot().size() == 1) {
                        LOGGER.warn("Pivot classification with a single element"); // is it really an error?
                        return false;
                    }
                }
                if(cl.getZonal())
                    cntZonal++;
            }
        }

        if(cntZonal > 1) {
            LOGGER.warn("Too many zonal classifiers ("+cntZonal+")");
            return false;
        }

        if(s.getStats().size() > 1 && cntPivot!=0) {
            LOGGER.warn("Cannot pivot with more than one stat value");
            return false;
        }

        if(cntPivot > 1) {
            LOGGER.warn("Too many pivot classifiers ("+cntPivot+")");
            return false;
        }

        if(s.getOutput() == null) {
            LOGGER.warn("Missing output info");
            return false;
        }

        if(! "CSV".equals(s.getOutput().getFormat() )) {
            LOGGER.warn("Only CSV format supported at the moment");
            return false;
        }

        return true;
    }

    public static boolean check(Layer l) {
        boolean xname = l.getName() != null;
        boolean xfile = l.getFile() != null;
        // this constraint may be relaxed if we intend use the same definition
        // for GeoBatch and WPS processed
        if(xname && xfile) {
            LOGGER.warn("Name and File cannot be both defined in Layer ["+l.getName()+","+l.getFile()+"]");
            return false;
        }

        if(! xname && ! xfile) {
            LOGGER.warn("At least one between Name and File must be defined in Layer");
            return false;
        }

        return true;
    }

}
