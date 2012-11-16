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
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class UNREDDLayer 
    extends UNREDDResource<UNREDDLayer.Attributes,
                           UNREDDLayer.ReverseAttributes> {
    private final static Logger LOGGER = LoggerFactory.getLogger(UNREDDLayer.class);

    public static final String CATEGORY_NAME = UNREDDCategories.LAYER.getName();

    public static class ReverseAttributes extends ReverseAttributeDef {
        private final static List<String> list = new ArrayList<String>();

        // NONE

        private ReverseAttributes(String name) {
            super(name);
            list.add(name);
        }
    }

    public static class Attributes extends AttributeDef {
        private final static Map<String, DataType> map = new HashMap<String, DataType>();

        /**
         * Should be "vector" or "raster".
         * @see UNREDDFormat
         */
        public final static Attributes LAYERTYPE            = new Attributes("LayerType", DataType.STRING);
        /**
         * Should be an existing style on target geoserver. It could be blank or nullable. In that case 
         * will be selected a default geoserver style
         */
        public final static Attributes LAYERSTYLE            = new Attributes("LayerStyle", DataType.STRING);
        /**
         * Full path where the layer tiff should be placed into.
         */
        public final static Attributes MOSAICPATH           = new Attributes("MosaicPath", DataType.STRING);
        /**
         * Full path where the layer tiff should be placed into in the dissemination system when published.
         */
        public final static Attributes DISSMOSAICPATH           = new Attributes("DissMosaicPath", DataType.STRING);
        /**
         * Relative path where the original data should be stored.
         * Base path is given in the configuration.
         */
        //public final static Attributes DESTORIGRELATIVEPATH = new Attributes("DestOrigDataRelativePath", DataType.STRING);
        /**
         * Full path where the orig/ data has to be moved in
         */
        public final static Attributes ORIGDATADESTPATH = new Attributes("OrigDataDestPath", DataType.STRING);
        /**
         * Full path where the orig/ data has to be moved in
         */
        public final static Attributes URL = new Attributes("URL", DataType.STRING);
        
        /** Used in vector rasterization. */
        public final static Attributes RASTERPIXELWIDTH     = new Attributes("RasterPixelWidth", DataType.NUMBER);
        /** Used in vector rasterization. */
        public final static Attributes RASTERPIXELHEIGHT    = new Attributes("RasterPixelHeight", DataType.NUMBER);
        /** Used in vector rasterization. */
        public final static Attributes RASTERX0             = new Attributes("RasterX0", DataType.NUMBER);
        /** Used in vector rasterization. */
        public final static Attributes RASTERX1             = new Attributes("RasterX1", DataType.NUMBER);
        /** Used in vector rasterization. */
        public final static Attributes RASTERY0             = new Attributes("RasterY0", DataType.NUMBER);
        /** Used in vector rasterization. */
        public final static Attributes RASTERY1             = new Attributes("RasterY1", DataType.NUMBER);
        /** Used in vector rasterization. The attrib containing the value to burn */
        public final static Attributes RASTERATTRIBNAME     = new Attributes("RasterAttribName", DataType.STRING);
        /** Used in vector rasterization. */
        public final static Attributes RASTERCQLFILTER      = new Attributes("RasterCQLFilter", DataType.STRING);
        /** Used in vector rasterization. */
        public final static Attributes RASTERNODATA         = new Attributes("RasterNoData", DataType.NUMBER);
        /** Used in vector rasterization.  data type for output raster.
         * It’s one of the GDAL-recognized datatypes:
         *    Byte / Int16 / UInt16 / UInt32 / Int32 / Float32 / Float64 / CInt16 / CInt32 / CFloat32 / CFloat64
         */
        public final static Attributes RASTERDATATYPE       = new Attributes("RasterDataType", DataType.STRING);

        /** DB Storage for vector data. Table where the features will be copied into. */
        public final static Attributes TABLENAME            = new Attributes("TableName", DataType.STRING);
        /** DB Storage for vector data.
         * Table attrib containing the year info as a 4-digits string.
         * This attrib is not in the original shapefile: it's added to every feature by the ingestion flow*/
        public final static Attributes YEARATTRIBNAME       = new Attributes("YearAttribName", DataType.STRING);
        /** DB Storage for vector data.
         * Table attrib containing the month info as a 2-digits string.
         * This attrib is not in the original shapefile: it's added to every feature by the ingestion flow*/
        public final static Attributes MONTHATTRIBNAME      = new Attributes("MonthAttribName", DataType.STRING);
        /** DB Storage for vector data.
         * Table attrib containing the day info as a 2-digits string.
         * This attrib is not in the original shapefile: it's added to every feature by the ingestion flow*/
        public final static Attributes DAYATTRIBNAME      = new Attributes("DayAttribName", DataType.STRING);
        
        private Attributes(String name, DataType dataType) {
            super(name, dataType);
            map.put(name, dataType);
        }
    }

    public UNREDDLayer() {
    }

    public UNREDDLayer(Resource resource) {
        super(resource);
    }

    @Override
    protected Map<String, DataType> getAttributeMap() {
        return Attributes.map;
    }

    @Override
    public List<String> getReverseAttributes() {
        return Collections.unmodifiableList(ReverseAttributes.list);
    }
    
    @Override
    public String getCategoryName() {
        return CATEGORY_NAME;
    }
}
