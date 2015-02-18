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

import it.geosolutions.unredd.services.data.ResourcePOJO;

/**
 * This Interface exposes the CRUD operations over the underlying persistence system.
 * At a first glance some operations could be similar to the ones exposed by the {@link UNREDDLayerDAO} but please note that these are generic operations that deal with generic entities
 * the operations in {@link UNREDDResourceDAO} deal with the UNREDD Model.
 * 
 * @author DamianoG
 *
 */
public interface UNREDDResourceDAO {
    
    public ResourcePOJO getResource(Long id, boolean full);
    
    public void deleteResource(Long id);
    
    /**
     * 
     * @param id
     * @param resource  RESTResource
     */
    public void updateResource(Long id, ResourcePOJO resource);
    
    /**
     * @param resource RESTResource
     * @return
     */
    public Long insert(ResourcePOJO resource);
    
    public void setData(Long id, String data);
    
    /**
     * 
     * @param id
     * @param acceptMediaType a string belong to the domain of the javax.ws.rs.core.MediaType enumerator. See <a href="http://docs.oracle.com/javaee/6/api/javax/ws/rs/core/MediaType.html">the javadoc</a>.
     * @return
     */
    public String getData(Long id, String acceptMediaType);
}
