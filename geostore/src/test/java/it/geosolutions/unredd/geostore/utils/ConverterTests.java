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
import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.core.model.enums.DataType;
import it.geosolutions.unredd.geostore.adapter.UNREDDDataConverter;
import it.geosolutions.unredd.geostore.model.UNREDDChartData;
import it.geosolutions.unredd.geostore.model.UNREDDChartScript;
import it.geosolutions.unredd.geostore.model.UNREDDFeedback;
import it.geosolutions.unredd.geostore.model.UNREDDLayer;
import it.geosolutions.unredd.geostore.model.UNREDDLayerUpdate;
import it.geosolutions.unredd.geostore.model.UNREDDReport;
import it.geosolutions.unredd.geostore.model.UNREDDStatsData;
import it.geosolutions.unredd.geostore.model.UNREDDStatsDef;
import it.geosolutions.unredd.services.data.CategoryPOJO;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author DamianoG
 *
 */
public class ConverterTests extends Assert{
    
    @Test
    public void generateFullAttributesList(){
        
        Attribute a1 = new Attribute();
        a1.setId(1);
        a1.setDateValue(new GregorianCalendar().getTime());
        a1.setName("reverseValue1");
        a1.setNumberValue(null);
        a1.setResource(null);
        a1.setTextValue("Layer"); 
        a1.setType(DataType.STRING);
        
        Attribute a2 = new Attribute();
        a2.setId(2);
        a2.setDateValue(new GregorianCalendar().getTime());
        a2.setName("reverseValue2");
        a2.setNumberValue(null);
        a2.setResource(null);
        a2.setTextValue("Layer"); 
        a2.setType(DataType.STRING);
        
        Attribute a3 = new Attribute();
        a3.setId(3);
        a3.setDateValue(new GregorianCalendar().getTime());
        a3.setName("reverseValue3");
        a3.setNumberValue(null);
        a3.setResource(null);
        a3.setTextValue("Layer"); 
        a3.setType(DataType.STRING);
        
        Attribute a4 = new Attribute();
        a4.setId(4);
        a4.setDateValue(new GregorianCalendar().getTime());
        a4.setName("StatDef");
        a4.setNumberValue(null);
        a4.setResource(null);
        a4.setTextValue("normalValue"); 
        a4.setType(DataType.STRING);
        
        List<Attribute> list = new ArrayList<Attribute>();
        list.add(a1);
        list.add(a2);
        list.add(a3);
        list.add(a4);
        
        Category cat = new Category();
        cat.setId(2l);
        cat.setName(CategoryPOJO.STATSDATA.getName());
        
        Resource res = new Resource();
        res.setAttribute(list);
        res.setCategory(cat);
        
        UNREDDDataConverter converter = new UNREDDDataConverter();
        List<Attribute> listAttribute = converter.generateFullAttributesList(res);
        
        Assert.assertEquals(4, listAttribute.size());
    }
    
    @Test
    public void testCategoryMap(){
        Class clazz = null; 
        clazz = UNREDDDataConverter.categoryMap.get("Layer");
        assertEquals(clazz, UNREDDLayer.class);
        clazz = UNREDDDataConverter.categoryMap.get("LayerUpdate");
        assertEquals(clazz, UNREDDLayerUpdate.class);
        clazz = UNREDDDataConverter.categoryMap.get("StatsDef");
        assertEquals(clazz, UNREDDStatsDef.class);
        clazz = UNREDDDataConverter.categoryMap.get("StatsData");
        assertEquals(clazz, UNREDDStatsData.class);
        clazz = UNREDDDataConverter.categoryMap.get("ChartScript");
        assertEquals(clazz, UNREDDChartScript.class);
        clazz = UNREDDDataConverter.categoryMap.get("ChartData");
        assertEquals(clazz, UNREDDChartData.class);
        clazz = UNREDDDataConverter.categoryMap.get("Feedback");
        assertEquals(clazz, UNREDDFeedback.class);
        clazz = UNREDDDataConverter.categoryMap.get("Report");
        assertEquals(clazz, UNREDDReport.class);
    }
    
}
