/*
 *  Copyright (C) 2007 - 2012 GeoSolutions S.A.S.
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

import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.core.model.enums.DataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User feedback comments, coming from portal interface.
 * 
 * @author Oscar Fonts
 */
public class UNREDDFeedback extends UNREDDResource<UNREDDFeedback.Attributes, UNREDDFeedback.ReverseAttributes> {

    public static final String CATEGORY_NAME = UNREDDCategories.FEEDBACK.getName();
	
    public static class ReverseAttributes extends ReverseAttributeDef {
        private final static List<String> list = new ArrayList<String>();

        private ReverseAttributes(String name) {
            super(name);
            list.add(name);
        }
    }

    /**
     * Attributes for UNREDDFeedback.
     * 
     * Values are:
     * <ul>
     *   <li>{@link #LAYERNAME}
     *   <li>{@link #LAYERDATE}
     *   <li>{@link #USERNAME}
     *   <li>{@link #USERMAIL}
     * </ul>
     */
    public static class Attributes extends AttributeDef {
        private final static Map<String, DataType> map = new HashMap<String, DataType>();

        /**
         * The layer feedback is referring to, as published in GeoServer. E.g. "unredd:forest_mask_mosaic"
         */
        public final static Attributes LAYERNAME = new Attributes("LayerName", DataType.STRING);
        /**
         * Optional, for time dependent layers. UNIX timestamp (UTC).
         */
        public final static Attributes LAYERDATE = new Attributes("LayerDate", DataType.NUMBER);
        /**
         * The user contact name, as filled in the feedback form.
         */
        public final static Attributes USERNAME  = new Attributes("UserName", DataType.STRING);
        /**
         * The user contant mail, as filled in the feedback form.
         */
        public final static Attributes USERMAIL  = new Attributes("UserMail", DataType.STRING);

        private Attributes(String name, DataType dataType) {
            super(name, dataType);
            map.put(name, dataType);
        }
    }
	
	public UNREDDFeedback() {
	}

	public UNREDDFeedback(Resource resource) {
		super(resource);
	}

	@Override
	public String getCategoryName() {
		return CATEGORY_NAME;
	}

	@Override
	protected Map<String, DataType> getAttributeMap() {
        return Attributes.map;
	}

	@Override
	protected List<String> getReverseAttributes() {
        return Collections.unmodifiableList(ReverseAttributes.list);
	}
}
