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
package it.geosolutions.unredd.services.interfaces;

import it.geosolutions.unredd.services.data.CategoryPOJO;
import it.geosolutions.unredd.services.data.ResourcePOJO;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.bind.JAXBException;

/**
 * This Interface provides a set of basic operations for Get and Delete belonging to the UNREDD Model in the underlying persistence system.
 * Some operations could be similar at a first glance to the ones exposed by the {@link UNREDDResourceDAO} but please note that these operations deal with
 * the UNREDD Model, the operations in {@link UNREDDResourceDAO} are generic operations that deal with generic entities.
 * 
 * @author DamianoG
 *
 */
public interface UNREDDLayerDAO {

    /**
     * Returns the list of all resources with a given UNREDDResources value
     * @return
     */
    public List<ResourcePOJO> getUNREDDResources(CategoryPOJO cat) throws UnsupportedEncodingException, JAXBException;
    
    public void deleteLayer(String layerName);
    
    public void deleteStats(String statsDefName);
    
    /**
     * Returns the list of all stat defs
     * @return
     */
    public List<ResourcePOJO> getLayers() throws UnsupportedEncodingException, JAXBException;
}
