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
package it.geosolutions.unredd.services.data;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author DamianoG
 *
 */
public class ResourcePOJO {

    private Long id;

    private String name;

    private String description;

    private Date creation;

    private Date lastUpdate;

    private String metadata;

    private List<AttributePOJO> attribute;

    private StoredDataPOJO data;

    private CategoryPOJO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public List<AttributePOJO> getAttribute() {
        if(attribute == null){
            return null;
        }
        return Collections.unmodifiableList(attribute);
    }

    public void setAttribute(List<AttributePOJO> attribute) {
        this.attribute = attribute;
    }

    public StoredDataPOJO getData() {
        return data;
    }

    public void setData(StoredDataPOJO data) {
        this.data = data;
    }

    public CategoryPOJO getCategory() {
        return category;
    }

    public void setCategory(CategoryPOJO category) {
        this.category = category;
    }
}
