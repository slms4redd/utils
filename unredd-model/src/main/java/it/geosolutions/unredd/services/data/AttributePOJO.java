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

/**
 * @author DamianoG
 *
 */
public class AttributePOJO implements Cloneable{

    public enum DataType {STRING, NUMBER, DATE}
    
    private Long id;
    
    private String name;
    
    private String value;
    
    private final DataType type;
    
    public AttributePOJO(){
        this.type = null;
    }
    
    public AttributePOJO(String name, String value, DataType type){
        this.name = name;
        this.value = value;
        this.type = type;
    }
    
    public AttributePOJO(Long id, String name, String value, DataType type){
        this.id = id;
        this.name = name;
        this.value = value;
        this.type = type;
    }
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public DataType getType() {
        return type;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public Object clone(){
        return new AttributePOJO(this.id, this.name, this.value, this.type);
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        AttributePOJO castedObj = null;
        if(obj instanceof AttributePOJO){
            castedObj = (AttributePOJO)obj;
        }
        else{
            return false;
        }
        if((this.id == null && castedObj.getId() != null) || (this.id != null && !this.id.equals(castedObj.getId()))){
            return false;
        }
        if((this.name == null && castedObj.getName() != null) || (this.name != null && !this.name.equals(castedObj.getName()))){
            return false;
        } 
        if((this.value == null && castedObj.getValue() != null) || (this.value != null && !this.value.equals(castedObj.getValue()))){
            return false;
        }
        if((this.type == null && castedObj.getType() != null) || (this.type != null && !this.type.equals(castedObj.getType()))){
            return false;
        }
        return true;
    }
}
