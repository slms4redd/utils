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
package it.geosolutions.unredd.stats.model.config;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author DamianoG
 * 
 * DTO for Range. see @{it.geosolutions.unredd.stats.RangeParser} for more details
 *
 */
@XmlRootElement()
public class Range {

    /**
     * A Range of Integer in ISO_31-11 format
     */
    private String range;
    
    /**
     * Indicate if this range indicate the inclusion of an exclusion from a set of Numbers
     * null value should be interpreted as FALSE 
     */
    private Boolean isAnExcludeRange;

    /**
     * @return the range
     */
    public String getRange() {
        return range;
    }

    /**
     * @param range the range to set
     */
    public void setRange(String range) {
        this.range = range;
    }

    /**
     * @return the isAnExcludeRange
     */
    public Boolean getIsAnExcludeRange() {
        return isAnExcludeRange;
    }

    /**
     * @param isAnExcludeRange the isAnExcludeRange to set
     */
    public void setIsAnExcludeRange(Boolean isAnExcludeRange) {
        this.isAnExcludeRange = isAnExcludeRange;
    }
    
    
}
