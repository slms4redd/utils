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

import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
@XmlRootElement()
public class ClassificationLayer extends Layer {


    private Boolean zonal;
    private List<Double> pivot;

    @XmlElementWrapper(name="pivot")
    @XmlElement(name="value")
    public List<Double> getPivot() {
        return pivot;
    }

    public void setPivot(List<Double> pivot) {
        this.pivot = pivot;
    }

    @XmlTransient
    public boolean isPivotDefined() {
        return pivot != null && ! pivot.isEmpty();
    }

    @XmlAttribute
    public Boolean getZonal() {
        return zonal != null ? zonal : false;
    }

    public void setZonal(Boolean zonal) {
        this.zonal = zonal;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "["
                + "zonal:" + getZonal()
                + " pivot:" + isPivotDefined()
                + (isPivotDefined()? pivot : "")
                + "]";
    }

}
