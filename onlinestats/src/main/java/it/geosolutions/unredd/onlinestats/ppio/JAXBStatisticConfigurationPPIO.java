/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  https://github.com/slms4redd/nfms-geobatch
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
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
package it.geosolutions.unredd.onlinestats.ppio;

import it.geosolutions.unredd.stats.model.config.ClassificationLayer;
import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * The PPIO for The StatisticConfiguration object implemented using JAXB
 * 
 * @author DamianoG
 * 
 */
public class JAXBStatisticConfigurationPPIO extends JAXBPPIO {

    public JAXBStatisticConfigurationPPIO() {
        super(StatisticConfiguration.class);
    }

    public StatisticConfiguration decode(InputStream arg0) throws Exception {
        Unmarshaller unmarshaller = buildUnmarshaller();
        StatisticConfiguration cfg = (StatisticConfiguration) unmarshaller.unmarshal(arg0);
        return cfg;
    }
    
	public StatisticConfiguration decode(Object input) throws Exception {
    	if (input instanceof String) {
    		return decodeString((String)input);
    	} else if(input instanceof Map<?, ?>) {
    		return decodeMap((Map<?, ?>)input);
    	} else {
    		return (StatisticConfiguration)super.decode(input);
    	}
    }
    
    StatisticConfiguration decodeString(String input) throws Exception {
    	InputStream is = new ByteArrayInputStream(input.getBytes());
        return(decode(is));
    }
	
    
    StatisticConfiguration decodeMap(Map<?, ?> input) throws Exception {
    	StatisticConfiguration statConf = new StatisticConfiguration();
    	/*
    	 *  The "dump" will show the Map structure in console.
    	 *  The "populate" would try to build StatConf using introspection, but some bits are missing, so hot fully possible.
    	 *  Third option would be writing a code that traverses all the tree structure instantiating the needed fields. Which is painful and not very elegant.
    	 */
    	//dump(input);
    	//statConf = (StatisticConfiguration)populate(input, StatisticConfiguration.class);
        
    	throw new Exception("Decoding from pre-parsed Object is not implemented.");
        //return statConf;
    }
    
    private void dump(Map<?, ?> input) {
    	for(Entry<?, ?> e : input.entrySet()) {
    		Class<?> c = e.getValue().getClass();
    		if (Map.class.isAssignableFrom(c)) {
    			System.out.println(e.getKey() + ":");
    			dump((Map<?, ?>)e.getValue());
    		} else {
    			System.out.println(e.getKey() + ": (" + c.getSimpleName() + ") " + e.getValue());
    		}
    	}
    }
    
    private Object populate(Map m, Class c) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    	Object o = c.newInstance();
      	for(Object eo : m.entrySet()) {
    		Entry e = (Entry)eo;
    		Object v = e.getValue();
    		Class<?> ec = v.getClass();
    		if (Map.class.isAssignableFrom(ec)) {
    			PropertyUtilsBean pub = new PropertyUtilsBean();
    			String ek = (String)e.getKey();
    			PropertyDescriptor pd = pub.getPropertyDescriptor(o, ek);
    			Class pt = pd.getPropertyType();
    			if(List.class.isAssignableFrom(pt)) {
    				Class k = Object.class;
    				if (ek.equals("classifications")) {
    					k = ClassificationLayer.class;
    				//} else if (ek.equals("topics")) {
    				//	k = String.class;
    				//} else if (ek.equals("stats")) {
    				//	k = StatsType.class;
    				}
    				List l = new ArrayList();
    				// This is the point where I give up.
    				Object ojeto = populate((Map)e.getValue(), k);
    				l.add(ojeto);
    				m.put(ek, l);
    			} else {
    				Object p = populate((Map)e.getValue(), pt);
    				m.put(ek, p);
    			}
    		}
    	}
    	BeanUtils.populate(o, m);
    	return o;
    }

}
