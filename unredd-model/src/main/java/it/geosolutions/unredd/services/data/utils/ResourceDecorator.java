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
package it.geosolutions.unredd.services.data.utils;

import it.geosolutions.unredd.services.data.AttributePOJO;
import it.geosolutions.unredd.services.data.AttributePOJO.DataType;
import it.geosolutions.unredd.services.data.CategoryPOJO;
import it.geosolutions.unredd.services.data.ModelDomainNames;
import it.geosolutions.unredd.services.data.ResourcePOJO;
import it.geosolutions.unredd.services.data.StoredDataPOJO;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This class Decorate {@link ResourcePOJO} adding utility methods to manage the attributes List 
 * 
 * @author DamianoG
 *
 */
public class ResourceDecorator {
    
    private ResourcePOJO resource;
    
    public ResourceDecorator(ResourcePOJO res){
        this.resource = res;
    }
    
    private List<AttributePOJO> getClonedAttributeslist(){
        List<AttributePOJO> list = resource.getAttribute();
        if(list == null){
            return null;
        }
        List<AttributePOJO> clonedList = new ArrayList<AttributePOJO>();
        for(AttributePOJO el : list){
            clonedList.add((AttributePOJO)el.clone());
        }
        return clonedList;
    }
    
    public void addTextAttribute(ModelDomainNames name, String value) {
        addAttribute(name, value, AttributePOJO.DataType.STRING);
    }
    
    public boolean updateTextAttribute(ModelDomainNames name, String value) {
        return updateAttribute(name, value, AttributePOJO.DataType.STRING);
    }
    
    public void addNumericAttribute(ModelDomainNames name, String value) {
        try{
            Double.parseDouble(value);
        }
        catch(Exception e){
            throw new IllegalArgumentException("the passed value: '" + value + "' is not a number...");
        }
        addAttribute(name, value, AttributePOJO.DataType.NUMBER);
    }
    
    public boolean updateNumericAttribute(ModelDomainNames name, String value) {
        try{
            Double.parseDouble(value);
        }
        catch(Exception e){
            throw new IllegalArgumentException("the passed value: '" + value + "' is not a number...");
        }
        return updateAttribute(name, value, AttributePOJO.DataType.NUMBER);
    }
    
    public void addTextAttributes(ModelDomainNames name, String... value){
        for(String el : value){
            addTextAttribute(name, el);
        }
    }
    
    public String getFirstAttributeValue(ModelDomainNames name){
        if(name == null){
            return null;
        }
        AttributePOJO found = null;
        List<AttributePOJO> attribute = resource.getAttribute();
        for(AttributePOJO attr : attribute){
            if(name.getName().equals(attr.getName())){
                found = attr;
                break;
            }
        }
        if(found == null){
            return null;
        }
        return found.getValue();
    }
    
    public List<String> getAttributeValues(ModelDomainNames name){
        if(name == null){
            return null;
        }
        List<String> output = new ArrayList<String>();
        List<AttributePOJO> attribute = resource.getAttribute();
        for(AttributePOJO attr : attribute){
            if(name.getName().equals(attr.getName())){
                output.add(attr.getValue());
            }
        }
        return output;
    }
    
    public boolean deleteAttributes(ModelDomainNames name){
        List<AttributePOJO> attribute = getClonedAttributeslist();
        boolean flag = false;
        if(name == null){
            return false;
        }
        if(attribute == null){
            attribute = new ArrayList<AttributePOJO>();
        }
        Iterator<AttributePOJO> iter = attribute.iterator();
        while(iter.hasNext()){
            AttributePOJO attr = iter.next(); 
            if(name.getName().equals(attr.getName())){
                iter.remove();
                flag = true;
            }
        }
        resource.setAttribute(attribute);
        return flag;
    }
    
    private void addAttribute(ModelDomainNames name, String value, DataType dtype) {
        List<AttributePOJO> attribute = getClonedAttributeslist();
        if(attribute == null){
            attribute = new ArrayList<AttributePOJO>();
        }
        attribute.add(new AttributePOJO(name.getName(), value, dtype));
        resource.setAttribute(attribute);
    }
    
    private boolean updateAttribute(ModelDomainNames name, String value, DataType dtype) {
        List<AttributePOJO> attribute = getClonedAttributeslist();
        if(attribute == null){
            attribute = new ArrayList<AttributePOJO>();
            return false;
        }
        AttributePOJO found = removeAttribute(name);
        if(found == null){
            return false;
        }
        attribute.remove(found);
        found.setValue(value);
        attribute.add(found);
        resource.setAttribute(attribute);
        return true;
    }
    
    private AttributePOJO removeAttribute(ModelDomainNames name){
        List<AttributePOJO> attribute = getClonedAttributeslist();
        if(name == null){
            return null;
        }
        AttributePOJO found = null;
        for(AttributePOJO attr : attribute){
            if(name.getName().equals(attr.getName())){
                found = attr;
                break;
            }
        }
        if(found == null){
            return null;
        }
        attribute.remove(found);
        return found;
    }
    
    public Long getId() {
        return resource.getId();
    }

    public void setId(Long id) {
        resource.setId(id);
    }

    public String getName() {
        return resource.getName();
    }

    public void setName(String name) {
        resource.setName(name);
    }

    public String getDescription() {
        return resource.getDescription();
    }

    public void setDescription(String description) {
        resource.setDescription(description);
    }

    public Date getCreation() {
        return resource.getCreation();
    }

    public void setCreation(Date creation) {
        resource.setCreation(creation);
    }

    public Date getLastUpdate() {
        return resource.getLastUpdate();
    }

    public void setLastUpdate(Date lastUpdate) {
        resource.setLastUpdate(lastUpdate);
    }

    public String getMetadata() {
        return resource.getMetadata();
    }

    public void setMetadata(String metadata) {
        resource.setMetadata(metadata);
    }

    public List<AttributePOJO> getAttribute() {
        return resource.getAttribute();
    }

    public void setAttribute(List<AttributePOJO> attribute) {
        resource.setAttribute(attribute);
    }

    public StoredDataPOJO getData() {
        return resource.getData();
    }

    public void setData(StoredDataPOJO data) {
        resource.setData(data);
    }

    public CategoryPOJO getCategory() {
        return resource.getCategory();
    }

    public void setCategory(CategoryPOJO category) {
        resource.setCategory(category);
    }
}
