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
package it.geosolutions.unredd.services;

import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.unredd.geostore.model.UNREDDCategories;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.xml.bind.JAXBException;

/**
 * This Interface provides a set of operations to look for resources belonging to the UNREDD Model in the underlying persistence system.
 * All the NFMS components that has to search persisted resources should do it through this Interface.
 * 
 * @author DamianoG
 *
 */
public interface UNREDDSearchServices {

    /**
     * Returns a list of ChartData objects having attribute CHARTSCRIPT=chartScriptName
     * @param statsDefName
     * @return
     */
    public List<Resource> searchChartDataByChartScript(String chartScriptName) throws UnsupportedEncodingException, JAXBException;
    
    public List<Resource> searchStatsDefByLayer(String layername) throws UnsupportedEncodingException, JAXBException;
    
    public List<Resource> searchChartScriptByStatsDef(String statsdef) throws UnsupportedEncodingException, JAXBException;
    
    /**
     * Returns a list of layerUpdate objects having attribute Layer=layerName
     * @param statsDefName
     * @return
     */
    public List<Resource> searchLayerUpdatesByLayerName(String layerName);
    
    /**
     * Returns a layer with the given name
     * @param layerName
     * @return first found layer with this name, or null if none found
     */
    public Resource searchLayer(String layerName) throws UnsupportedEncodingException, JAXBException;
    
    public List<Resource> searchStatsDataByStatsDef(String statsDefName) throws UnsupportedEncodingException, JAXBException;
    
    public Resource searchResourceByName(String resourceName, UNREDDCategories cat);
}
