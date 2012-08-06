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
 * A Reverse attribute is a workaround used to store multivalue attributes in
 * a GeoStore Resource.
 * <P>
 * The attributes of GeoStore resources are persisted more or less in a key+value
 * map. It means that every key can exist at most once for a given resource.<BR>
 * This problem can be partially solved by inverting the key+value pair,
 * and saving the value as it was the key and vice versa.
 *
 * <P>
 * A shortcoming of this is that no element can be doubled, not we can have
 * a reverse attribute with the value equal to a key of a normal attribute.
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class ReverseAttributeDef {
    final private String name;

    protected ReverseAttributeDef(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return DataType.STRING;
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
        final ReverseAttributeDef other = (ReverseAttributeDef) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+name+"]";
    }

}
