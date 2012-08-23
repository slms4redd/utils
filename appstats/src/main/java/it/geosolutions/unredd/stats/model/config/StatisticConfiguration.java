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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
@XmlRootElement
@XmlType(propOrder={"name","title","description","topics","stats","deferredMode","dataLayer","classifications","output"})
public class StatisticConfiguration {

    private String name;
    private String title;
    private String description;
    private List<String> topics;

    private List<StatsType> stats;

    private Layer dataLayer;
    private List<ClassificationLayer> classifications;

    private Output output;

    private boolean deferredMode;

    @XmlElement(name="classificationLayer")
    public List<ClassificationLayer> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<ClassificationLayer> classifications) {
        this.classifications = classifications;
    }

    public Layer getDataLayer() {
        return dataLayer;
    }

    public void setDataLayer(Layer dataLayer) {
        this.dataLayer = dataLayer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeferredMode() {
        return deferredMode;
    }

    public void setDeferredMode(boolean deferredMode) {
        this.deferredMode = deferredMode;
    }

    @XmlElementWrapper(name="stats")
    @XmlElement(name="stat")
    public List<StatsType> getStats() {
        return stats;
    }

    public void setStats(List<StatsType> stats) {
        this.stats = stats;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    @XmlElement(name="topic")
    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public Output getOutput() {
        return output;
    }

    public void setOutput(Output output) {
        this.output = output;
    }
}
