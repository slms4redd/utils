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
public enum ModelDomainNames {

    //Resource Attributes names
    ATTRIBUTES_LAYER("Layer"),
    ATTRIBUTES_ZONALLAYER("ZonalLayer"),
    ATTRIBUTES_STATSDEF("StatsDef"),
    ATTRIBUTES_SCRIPTPATH("ScriptPath"),
    
    //Layer Attributes names
    LAYER_LAYERTYPE("LayerType"),
    LAYER_LAYERSTYLE("LayerStyle"),
    LAYER_MOSAICPATH("MosaicPath"),
    LAYER_DISSMOSAICPATH("DissMosaicPath"),
    LAYER_ORIGDATADESTPATH("OrigDataDestPath"),
    LAYER_URL("URL"),
    LAYER_RASTERPIXELWIDTH("RasterPixelWidth"),
    LAYER_RASTERPIXELHEIGHT("RasterPixelHeight"),
    LAYER_RASTERX0("RasterX0"),
    LAYER_RASTERX1("RasterX1"),
    LAYER_RASTERY0("RasterY0"),
    LAYER_RASTERY1("RasterY1"),
    LAYER_RASTERATTRIBNAME("RasterAttribName"),
    LAYER_RASTERCQLFILTER("RasterCQLFilter"),
    LAYER_RASTERNODATA("RasterNoData"),
    LAYER_RASTERDATATYPE("RasterDataType"),
    LAYER_TABLENAME("TableName"),
    LAYER_YEARATTRIBNAME("YearAttribName"),
    LAYER_MONTHATTRIBNAME("MonthAttribName"),
    LAYER_DAYATTRIBNAME("DayAttribName"),

    //LayerUpdate Attributes names    
    LAYERUPDATE_LAYER("Layer"),
    LAYERUPDATE_YEAR("Year"),
    LAYERUPDATE_MONTH("Month"),
    LAYERUPDATE_DAY("Day"),
    LAYERUPDATE_PUBLISHED("Published"),
    
    //StatDef Attributes names
    STATS_DEF_LAYER("Layer"),
    STATS_DEF_ZONALLAYER("ZonalLayer"),
    STATS_DEF_MONTH("Month"),
    STATS_DEF_YEAR("Year"),
    
    //ChartScript Attributes names
    CHARTSCRIPT_STATDEF("StatsDef"),
    CHARTSCRIPT_SCRIPTPATH("ScriptPath");
    
    private String name;

    private ModelDomainNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
