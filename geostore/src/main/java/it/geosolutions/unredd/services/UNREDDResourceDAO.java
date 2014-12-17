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
import it.geosolutions.geostore.services.rest.model.RESTResource;
import javax.ws.rs.core.MediaType;

/**
 * This Interface exposes the CRUD operations over the underlying persistence system.
 * At a first glance some operations could be similar to the ones exposed by the {@link UNREDDLayerDAO} but please note that these are generic operations that deal with generic entities
 * the operations in {@link UNREDDResourceDAO} deal with the UNREDD Model.
 * 
 * @author DamianoG
 *
 */
public interface UNREDDResourceDAO {
    
    public Resource getResource(Long id, boolean full);
    
    public void deleteResource(Long id);
    
    public void updateResource(Long id, RESTResource resource);
    
    public Long insert(RESTResource resource);
    
    public void setData(Long id, String data);
    
    public String getData(Long id, MediaType acceptMediaType);
}
