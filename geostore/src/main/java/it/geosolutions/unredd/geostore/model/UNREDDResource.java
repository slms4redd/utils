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

import it.geosolutions.geostore.core.model.Attribute;
import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.core.model.enums.DataType;
import it.geosolutions.geostore.services.dto.ShortAttribute;
import it.geosolutions.geostore.services.dto.search.AttributeFilter;
import it.geosolutions.geostore.services.dto.search.SearchOperator;
import it.geosolutions.geostore.services.rest.model.RESTCategory;
import it.geosolutions.geostore.services.rest.model.RESTResource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * This is a generic GeoStore Resource, used in UNREDD project.
 * <BR>
 * Its subclasses are mainly used for encoding and decoding RESTResource, and
 * related attributes.<BR>
 * Subclasses should define a <B>set of attributes</B> and
 * <B>a set of <I>reverse</I> attributes</B>
 * (see {@link ReverseAttributeDef} for an explation about reverse attrs).
 *
 * The two pratical uses involve: <UL>
 * <LI>RESTResource creation<BR/>
 * es: <BLOCKQUOTE><PRE>
 * {@code 
 *      UNREDDLayerUpdate layerUpdate = new UNREDDLayerUpdate();
 * layerUpdate.setAttribute(UNREDDLayerUpdate.Attributes.LAYER, name);
 * layerUpdate.setAttribute(UNREDDLayerUpdate.Attributes.YEAR, Integer.toString(year));
 * RESTResource res = layerUpdate.createRESTResource();
 * res.setName(resName);
 * } </PRE></BLOCKQUOTE></LI>
 *
 * <LI>Resource parsing<BR/>
 * es: <BLOCKQUOTE><PRE>
 * {@code
 *      Resource resource = ... (load resource from GeoStore)...
 * UNREDDChartScript chartScript = new UNREDDChartScript(resource);
 * String scriptPath = chartScript.getAttribute(UNREDDChartScript.Attributes.SCRIPTPATH);
 * } </PRE></BLOCKQUOTE></LI>
 * </UL>
 * @author ETj (etj at geo-solutions.it)
 */
public abstract class UNREDDResource<A extends AttributeDef, R extends ReverseAttributeDef> {
    private final static Logger LOGGER = LoggerFactory.getLogger(UNREDDResource.class);

    private Map<String, String> attributes = new HashMap<String, String>();
    private Map<String, Attribute> originalAttributes = new HashMap<String, Attribute>();

    public abstract String getCategoryName();
    protected abstract Map<String, DataType> getAttributeMap();
    protected abstract List<String> getReverseAttributes();

    public UNREDDResource() {
    }

    public UNREDDResource(Resource resource) {

        // category sanity check
        if(! getCategoryName().equals(resource.getCategory().getName())) {
            throw new IllegalArgumentException("Bad resource category " + resource.getCategory().getName() + ": only handling " + getCategoryName());
        }

        // copy attribs
        if (resource.getAttribute() != null) { // SG - when there are no attributes, resource.getAttribute() returns null - can't modify resource.getAttribute()
            for (Attribute a : resource.getAttribute()) {
                originalAttributes.put(a.getName(), a);

                DataType type = getAttributeMap().get(a.getName());
                if(type == null) {
                    if( ! getReverseAttributes().contains(a.getTextValue())) {
                        LOGGER.error("Bad attribute name " + a + " for class " + this.getClass().getSimpleName());
                        continue;
                    }
                } else if( type != a.getType()) {
                    LOGGER.error("Bad attribute type " + a + " for class " + this.getClass().getSimpleName());
                    continue;
                }

                attributes.put(a.getName(), a.getValue());
            }
        }

        // TODO: copy other resource info 
    }

    private boolean checkAttribute(String name, String value) {
        boolean direct = getAttributeMap().containsKey(name);
        boolean reverse = getReverseAttributes().contains(value);

        if( direct ^ reverse ) {
            LOGGER.warn("Attribute clash name:"+name +" value:"+value);
            return false;
        }

        return direct || reverse;
    }

    public void setAttribute(A att, String value) {
        setAttribute(att.getName(), value);
    }

    protected void setAttribute(String name, String value) {

        DataType type = getAttributeMap().get(name);
        if( type == null) {
            if(getReverseAttributes().contains(value)) {
                if(LOGGER.isDebugEnabled())
                    LOGGER.debug("Setting inverse attribute "+name+":"+value);
                attributes.put(name, value);
            } else {
                throw new IllegalArgumentException("Attribute " + name + " not handled");
            }
        } else {
            // TODO: check that value is assignable to attDef.getDataType() type

            attributes.put(name, value);
        }
    }

    public String getAttribute(A att) {
        return attributes.get(att.getName());
    }

    public void addReverseAttribute(R att, String... values) {
        for (String value : values) {
            setAttribute(value, att.getName()); // yes, name and value are swapped: it's a *reverse* attribute :)
        }
    }

    public boolean removeReverseAttribute(R att, String value) {
        String revAttName = attributes.get(value); // yes, name and value are swapped: it's a *reverse* attribute :)
        if(getAttributeMap().containsKey(value)) {
            LOGGER.warn("Can't remove reverse attribute '"+att.getName()+"': '"+value+"' is a forward key");
            return false;
        }

        if(revAttName != null) { // ok: it exists
            if( ! att.getName().equals(revAttName)) {
                LOGGER.warn("Tried to remove reverse attribute '" + att.getName() + "' with value '" +value  +"', but found '"+ revAttName+"'  as reverse key");
                return false;
            } else {
                attributes.remove(value);
                return true;
            }
        } else {
            return false;
        }
    }

    public List<String> getReverseAttributesInternal(R att) {
        List<String> ret = new ArrayList<String>();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            if(entry.getValue().equals(att.getName()))
                ret.add(entry.getKey());
        }

        return ret;
    }

    public List<String> getReverseAttributes(String attName) {
        if( ! getReverseAttributes().contains(attName) )
            throw new IllegalArgumentException("Attribute " + attName + " not handled");

        List<String> ret = new ArrayList<String>();
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            if(entry.getValue().equals(attName))
                ret.add(entry.getKey());
        }

        return ret;
    }

    public ShortAttribute createShortAttribute(String name) {

        if( ! attributes.containsKey(name))
            throw new IllegalArgumentException("Attribute " + name + " not handled");
        
        DataType type = getAttributeMap().get(name);
        if ( type == null )
            type = DataType.STRING; // it's a reverse attribute

        return new ShortAttribute(name, attributes.get(name), type);
    }

    public List<ShortAttribute> createShortAttributeList() {
        List ret = new ArrayList();
        for (String name : attributes.keySet()) {
            ret.add(createShortAttribute(name));
        }
        return ret;
    }

    public RESTResource createRESTResource() {

        RESTCategory cat = new RESTCategory();
        cat.setName(getCategoryName());

        RESTResource resource = new RESTResource();
        resource.setCategory(cat);
        resource.setAttribute(createShortAttributeList());
        return resource;
    }

    public Attribute getOriginalAttribute(A att) {
        return originalAttributes.get(att.getName());
    }
}
