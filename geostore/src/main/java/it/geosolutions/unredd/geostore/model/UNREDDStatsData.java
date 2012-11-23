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

import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.core.model.enums.DataType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class UNREDDStatsData
    extends UNREDDResource<UNREDDStatsData.Attributes,
                           UNREDDStatsData.ReverseAttributes> {

    private final static Logger LOGGER = LoggerFactory.getLogger(UNREDDStatsDef.class);

    public static final String CATEGORY_NAME = UNREDDCategories.STATSDATA.getName();

    public static class ReverseAttributes extends ReverseAttributeDef {
        private final static List<String> list = new ArrayList<String>();

        public final static ReverseAttributes LAYER = new ReverseAttributes("Layer");

        private ReverseAttributes(String name) {
            super(name);
            list.add(name);
        }
    }

    public static class Attributes extends AttributeDef {
        private final static Map<String, DataType> map = new HashMap<String, DataType>();

        public final static Attributes STATSDEF = new Attributes("StatsDef", DataType.STRING);
        public final static Attributes YEAR     = new Attributes("Year", DataType.STRING); // used to be a Number, but it returned as a float ("2010.0")
        public final static Attributes MONTH    = new Attributes("Month", DataType.STRING);
        public final static Attributes DAY      = new Attributes("Day", DataType.STRING);

        private Attributes(String name, DataType dataType) {
            super(name, dataType);
            map.put(name, dataType);
        }
    }

    public UNREDDStatsData() {
    }

    public UNREDDStatsData(Resource resource) {
        super(resource);
    }

    @Override
    protected Map<String, DataType> getAttributeMap() {
        return Attributes.map;
    }

    @Override
    protected List<String> getReverseAttributes() {
        return ReverseAttributes.list;
    }

    @Override
    public String getCategoryName() {
        return CATEGORY_NAME;
    }
}
