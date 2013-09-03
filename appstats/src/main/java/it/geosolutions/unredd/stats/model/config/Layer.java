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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class models a layer used for any kind of stats computation.
 * It can be used to model data layers or classification layer as well
 *
 * @author ETj (etj at geo-solutions.it)
 * @author DamianoG (damiano.giampaoli at geo-solutions.it)
 */
@XmlRootElement
@XmlSeeAlso({FileLayer.class,GeoServerLayer.class,ClassificationLayer.class})
public class Layer {
    /**
     * The path on filesystem of the raster file representing this layer
     */
    private String file;
    
    /**
     * The name 
     */
    private String name;
    
    /**
     * Value to use as noData
     */
    private Double nodata;
    
    /**
     * A list of range to use as classes when computing stats on the layer
     */
    private List<Range> ranges = new ArrayList<Range>();
    
    /**
     * Use the ranges list as a single non-continuos class or each range as a different class 
     */
    private Boolean computeRangesAsDifferentClasses;
    
    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getNodata() {
        return nodata;
    }

    public void setNodata(Double nodata) {
        this.nodata = nodata;
    }

    @XmlElement(name="range")
    public List<Range> getRanges() {
        return ranges;
    }

    public void setRanges(List<Range> ranges) {
        this.ranges = ranges;
    }

    public Boolean getComputeRangesAsDifferentClasses() {
        return computeRangesAsDifferentClasses;
    }

    public void setComputeRangesAsDifferentClasses(Boolean computeRangesAsDifferentClasses) {
        this.computeRangesAsDifferentClasses = computeRangesAsDifferentClasses;
    }
    
    
}
