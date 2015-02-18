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
package it.geosolutions.unredd.geostore.utils;

import it.geosolutions.geostore.core.model.Attribute;
import it.geosolutions.geostore.core.model.Category;
import it.geosolutions.geostore.core.model.enums.DataType;
import it.geosolutions.unredd.services.data.AttributePOJO;
import it.geosolutions.unredd.services.data.CategoryPOJO;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author DamianoG
 *
 */
public class EnumTests extends Assert{

    @Test
    public void testDataTypeEnum(){
        
        Attribute attr = new Attribute();
        AttributePOJO.DataType result = null;
        
        attr.setType(DataType.DATE);
        result = AttributePOJO.DataType.valueOf(attr.getType().toString());
        assertTrue(result.equals(AttributePOJO.DataType.DATE));
        
        attr.setType(DataType.NUMBER);
        result = AttributePOJO.DataType.valueOf(attr.getType().toString());
        assertTrue(result.equals(AttributePOJO.DataType.NUMBER));
        
        attr.setType(DataType.STRING);
        result = AttributePOJO.DataType.valueOf(attr.getType().toString());
        assertTrue(result.equals(AttributePOJO.DataType.STRING));
    }
    
    @Test
    public void testCategoryEnum2(){
        
        Category cat = new Category();
        
        cat.setName(CategoryPOJO.STATSDEF.toString().toUpperCase());
        CategoryPOJO.valueOf(cat.getName().toString());
        
        boolean failed = false;
        try{
        cat.setName(CategoryPOJO.STATSDEF.toString().toLowerCase());
        CategoryPOJO.valueOf(cat.getName().toString());
        }
        catch(Exception e){
            failed = true;
        }
        assertTrue(failed);
        
        cat.setName(CategoryPOJO.STATSDEF.toString());
        CategoryPOJO.valueOf(cat.getName().toString());
    }
}
