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
package it.geosolutions.unredd.geostore.model;

import it.geosolutions.geostore.core.model.enums.DataType;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class AttributeDef {
    private final String name;
    private final DataType dataType;

    protected AttributeDef(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public DataType getDataType() {
        return dataType;
    }

//    public void setDataType(DataType dataType) {
//        this.dataType = dataType;
//    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AttributeDef other = (AttributeDef) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.dataType != other.dataType) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 73 * hash + (this.dataType != null ? this.dataType.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+name+":"+dataType+"]";
    }

}
