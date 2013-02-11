/*
 *  Copyright (C) 2007 - 2012 GeoSolutions S.A.S.
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
package it.geosolutions.unredd.geostore.utils;

/**
 * Some utils to create names in UNREDD beans.
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class NameUtils {
    
    /**
     * This regex is that will be passed to Geoserver and with wich geotools ImageMosaic code will parse the granule name.
     * Is hardcoded here because of it is strict related to the granulename format that is specified in these methods, so it
     * is useless externalize just this regex outside of this class.
     * The regex must match dates in format <b>yyyy-mm-dd</b> or <b>yyyy-mm</b> or <b>yyyy</b>  
     */
    public static final String TIME_REGEX = "(?<=_)(\\d{4})";//(-[0-9]{2}(-[0-9]{2})?)?)";
    
    public static String buildLayerUpdateName(String layerName, String year, String month, String day) {
        return buildNameYearMonth(layerName, year, month, day);
    }

    public static String buildStatsDataName(String statsDefName, String year, String month, String day) {
        return buildNameYearMonth(statsDefName, year, month, day);
    }

    /**
     * Build the filename for the tif file.
     */
    public static String buildTifFileName(String layerName, String year, String month, String day) {
        return buildNameYearMonth(layerName, year, month, day) + ".tif";
    }

    protected static String buildNameYearMonth(String name, String year, String month, String day) {
        StringBuilder sb = new StringBuilder(name);
        sb.append('_').append(year);
        if (month != null) {
            sb.append('-');
            if(month.length() == 1)
                sb.append('0');
            sb.append(month);
        }
        if (day != null) {
            sb.append('-');
            if(day.length() == 1)
                sb.append('0');
            sb.append(day);
        }
        return sb.toString();
    }
}
