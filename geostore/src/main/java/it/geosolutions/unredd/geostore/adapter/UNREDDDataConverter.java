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
package it.geosolutions.unredd.geostore.adapter;

import it.geosolutions.geostore.core.model.Attribute;
import it.geosolutions.geostore.core.model.Category;
import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.core.model.StoredData;
import it.geosolutions.geostore.services.dto.ShortAttribute;
import it.geosolutions.geostore.services.rest.model.RESTCategory;
import it.geosolutions.geostore.services.rest.model.RESTResource;
import it.geosolutions.geostore.services.rest.model.RESTStoredData;
import it.geosolutions.unredd.geostore.model.UNREDDCategories;
import it.geosolutions.unredd.services.data.AttributePOJO;
import it.geosolutions.unredd.services.data.CategoryPOJO;
import it.geosolutions.unredd.services.data.ResourcePOJO;
import it.geosolutions.unredd.services.data.StoredDataPOJO;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains All the methods strictly needed to convert resources from the GeoStore model to the Generic one and viceversa.
 * 
 * @author DamianoG
 *
 */
public class UNREDDDataConverter {

    private final static Logger LOGGER = LoggerFactory.getLogger(UNREDDDataConverter.class);
    
    /*
     * Resource to ResourcePOJO bindings
     */
    
    public ResourcePOJO convertResource2ResourcePOJO(Resource res){
        if(res == null){
            LOGGER.error("Input Resource instance is null...");
            return null;
        }
        ResourcePOJO outRes = new ResourcePOJO();
        outRes.setAttribute(convertAttribute2AttributePOJO(res.getAttribute()));
        outRes.setCategory(convertCategory2CategoryPOJO(res.getCategory()));
        outRes.setCreation(res.getCreation());
        outRes.setData(convertStoredData2StoredDataPOJO(res.getData()));
        outRes.setDescription(res.getDescription());
        outRes.setId(res.getId());
        outRes.setLastUpdate(res.getLastUpdate());
        outRes.setMetadata(res.getMetadata());
        outRes.setName(res.getName());
        return outRes;
    }
    
    public AttributePOJO convertAttribute2AttributePOJO(Attribute attr){
        if(attr == null){
            LOGGER.error("Input Attribute instance is null...");
            return null;
        }
        AttributePOJO newAttr = new AttributePOJO();
        //TODO
        throw new NotImplementedException("AttributePOJO convertAttribute2AttributePOJO(Attribute attr)");
    }
    
    public CategoryPOJO convertCategory2CategoryPOJO(Category cat){
        if(cat == null){
            LOGGER.error("Input Category instance is null...");
            return null;
        }
        CategoryPOJO newCat = new CategoryPOJO();
        //TODO
        throw new NotImplementedException("public CategoryPOJO convertCategory2CategoryPOJO(Category cat)");
    }
    
    public StoredDataPOJO convertStoredData2StoredDataPOJO(StoredData data){
        if(data == null){
            LOGGER.error("Input StoredData instance is null...");
            return null;
        }
        StoredDataPOJO newData = new StoredDataPOJO();
        //TODO
        throw new NotImplementedException("public StoredDataPOJO convertStoredData2StoredDataPOJO(StoredData data)");
    }
    
    /*
     * RESTResource to ResourcePOJO bindings
     */
    
    public RESTResource convertResourcePOJO2RESTResource(ResourcePOJO res){
        if(res == null){
            LOGGER.error("Input ResourcePOJO instance is null...");
            return null;
        }
        RESTResource newRes = new RESTResource();
        newRes.setAttribute(convertAttributePOJO2ShortAttribute(res.getAttribute()));
        newRes.setCategory(convertCategoryPOJO2RESTCategory(res.getCategory()));
        newRes.setCreation(res.getCreation());
        //newRes.setData(); ??? WTF is that?
        newRes.setDescription(res.getDescription());
        newRes.setId(res.getId());
        newRes.setLastUpdate(res.getLastUpdate());
        newRes.setMetadata(res.getMetadata());
        newRes.setName(res.getName());
        newRes.setStore(convertStoredData2StoredDataPOJO(res.getData()));
        return newRes;
    }
    
    public ShortAttribute convertAttributePOJO2ShortAttribute(AttributePOJO attr){
        if(attr == null){
            LOGGER.error("Input AttributePOJO instance is null...");
            return null;
        }
        ShortAttribute newAttr = new ShortAttribute();
        //TODO
        throw new NotImplementedException("public ShortAttribute convertAttribute2AttributePOJO(AttributePOJO attr)");
    }
    
    public RESTCategory convertCategoryPOJO2RESTCategory(CategoryPOJO cat){
        if(cat == null){
            LOGGER.error("Input CategoryPOJO instance is null...");
            return null;
        }
        RESTCategory newCat = new RESTCategory();
        //TODO
        throw new NotImplementedException("public CategoryPOJO convertCategory2CategoryPOJO(Category cat)");
    }
    
    public RESTStoredData convertStoredData2StoredDataPOJO(StoredDataPOJO data){
        if(data == null){
            LOGGER.error("Input StoredDataPOJO instance is null...");
            return null;
        }
        RESTStoredData newData = new RESTStoredData();
        //TODO
        throw new NotImplementedException("public RESTStoredData convertStoredData2StoredDataPOJO(StoredData data)");
    }
    
    
    /*
     * Other bindings
     */
    
    public UNREDDCategories convertCategoryPOJO2UNREDDCategories(CategoryPOJO cat){
        if(cat == null){
            LOGGER.error("Input CategoryPOJO instance is null...");
            return null;
        }
        throw new NotImplementedException("public UNREDDCategories convertCategoryPOJO2UNREDDCategories(CategoryPOJO cat)");
    }
    
    public MediaType convertString2MediaType(String acceptMediaType){
        try{
            return MediaType.valueOf(acceptMediaType);
        }
        catch(Exception e){
            LOGGER.error("No conversion found for the MediaType string: '" + acceptMediaType + "' return WILDCARD_TYPE");
            return MediaType.WILDCARD_TYPE;
        }
    }
    
    /*
     * LIST Methods
     */
    
    public List<ResourcePOJO> convertResource2ResourcePOJO(List<Resource> res){
        if(res == null){
            LOGGER.error("Input List<Resource> instance is null...");
            return new ArrayList<ResourcePOJO>();
        }
        List<ResourcePOJO> list = new ArrayList<ResourcePOJO>();
        for(Resource el : res){
            list.add(convertResource2ResourcePOJO(el));
        }
        return list;
    }
    
    public List<AttributePOJO> convertAttribute2AttributePOJO(List<Attribute> attr){
        if(attr == null){
            LOGGER.error("Input List<Attribute> instance is null...");
            return null;
        }
        List<AttributePOJO> list = new ArrayList<AttributePOJO>();
        for(Attribute el : attr){
            list.add(convertAttribute2AttributePOJO(el));
        }
        return list;
    }
    
    public List<ShortAttribute> convertAttributePOJO2ShortAttribute(List<AttributePOJO> attr){
        if(attr == null){
            LOGGER.error("Input List<ShortAttribute> instance is null...");
            return null;
        }
        List<ShortAttribute> list = new ArrayList<ShortAttribute>();
        for(AttributePOJO el : attr){
            list.add(convertAttributePOJO2ShortAttribute(el));
        }
        return list;
    }
}
