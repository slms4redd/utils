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

package it.geosolutions.unredd;

import org.geoserver.wps.WPSTestSupport;
import org.geotools.TestData;
import org.junit.Test;

import com.mockrunner.mock.web.MockHttpServletResponse;

/**
 * @author DamianoG
 * 
 */
public class OnlineStatsWPSTest extends WPSTestSupport {

    private static final String RESULT = "0;7.745265757021875E10;366384.75;368400.21875;210967.0\n"
            + "1;3.739946002626875E11;366384.75;368400.21875;1016419.0\n";

    /**
     * This test provide to the service a ROI that doesn't intersect with the area. so the result returned must be an internal_sever_error.
     * @throws Exception
     */
    @Test
    public void testROInotIntersect() throws Exception {
        
        final String area_path = TestData.file(this, "area.tif").getAbsolutePath();
        final String forestmask_path = TestData.file(this, "forest_mask.tif").getAbsolutePath();
        
        String xml =

        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">"
                + "<ows:Identifier>gs:OnlineStatsWPS</ows:Identifier>\""
                + "<wps:DataInputs>"
                + "<wps:Input>"
                + "<ows:Identifier>geometry</ows:Identifier>"
                + "<wps:Data>"
                + "<wps:ComplexData mimeType=\"text/xml; subtype=gml/3.1.1\"><![CDATA[POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))]]></wps:ComplexData>"
                + "</wps:Data>"
                + "</wps:Input>"
                + "<wps:Input>"
                + "<ows:Identifier>statConf</ows:Identifier>"
                + "<wps:Data>"
                + "<wps:ComplexData mimeType=\"text/xml\"><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<StatisticConfiguration>"
                + "<Name>area_admin_small</Name>"
                + "<Title>Low resolution admin areas</Title>"
                + "<Description>Compute the area for the administrative areas. Low   resolutions raster.</Description>"
                + "<Topics>"
                + "<string>rdc</string>"
                + "<string>area</string>"
                + "<string>administartive</string>"
                + "</Topics>"
                + "<Stats>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>SUM</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>MIN</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>MAX</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>COUNT</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "</Stats>"
                + "<DataLayer>"
                + "<File>" + area_path + "</File>"
                + "</DataLayer>"
                + "<Classifications>"
                + "<it.geosolutions.unredd.stats.model.config.ClassificationLayer>"
                + "<File>" + forestmask_path + "</File>"
                + "<Nodata>255.0</Nodata>" + "<Pivot>" + "<double>0.0</double>"
                + "<double>1.0</double>" + "</Pivot>"
                + "</it.geosolutions.unredd.stats.model.config.ClassificationLayer>"
                + "</Classifications>" + "<Output>" + "<Format>CSV</Format>"
                + "<Separator>;</Separator>" + "</Output>" + "<DeferredMode>false</DeferredMode>"
                + "</StatisticConfiguration>]]></wps:ComplexData>" + "</wps:Data>" + "</wps:Input>"
                + "</wps:DataInputs>" + "<wps:ResponseForm>" + "<wps:RawDataOutput>"
                + "<ows:Identifier>result</ows:Identifier>" + "</wps:RawDataOutput>"
                + "</wps:ResponseForm>" + "</wps:Execute>";

        MockHttpServletResponse response = postAsServletResponse("wps?", xml);
        // The supplied ROI does not intersect the source image, so an internal server error is returned
        assertTrue(response.getOutputStreamContent().startsWith("internal_server_error"));
    }

    /**
     * This test provide a valid request without outputfile specified. so the WPS implementation must create a temp file for the output and the result must be ok
     * @throws Exception
     */
    @Test
    public void testROIIntersect() throws Exception {
        
        final String area_path = TestData.file(this, "area.tif").getAbsolutePath();
        final String forestmask_path = TestData.file(this, "forest_mask.tif").getAbsolutePath();
        
        String xml =

        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">"
                + "<ows:Identifier>gs:OnlineStatsWPS</ows:Identifier>\""
                + "<wps:DataInputs>"
                + "<wps:Input>"
                + "<ows:Identifier>geometry</ows:Identifier>"
                + "<wps:Data>"
                + "<wps:ComplexData mimeType=\"text/xml; subtype=gml/3.1.1\"><![CDATA[POLYGON ((21.3 0.7, 27 0.7, 26.9 -6, 21.7 -6, 21.3 0.7))]]></wps:ComplexData>"
                + "</wps:Data>"
                + "</wps:Input>"
                + "<wps:Input>"
                + "<ows:Identifier>statConf</ows:Identifier>"
                + "<wps:Data>"
                + "<wps:ComplexData mimeType=\"text/xml\"><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<StatisticConfiguration>"
                + "<Name>area_admin_small</Name>"
                + "<Title>Low resolution admin areas</Title>"
                + "<Description>Compute the area for the administrative areas. Low   resolutions raster.</Description>"
                + "<Topics>"
                + "<string>rdc</string>"
                + "<string>area</string>"
                + "<string>administartive</string>"
                + "</Topics>"
                + "<Stats>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>SUM</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>MIN</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>MAX</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>COUNT</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "</Stats>"
                + "<DataLayer>"
                + "<File>" + area_path + "</File>"
                + "</DataLayer>"
                + "<Classifications>"
                + "<it.geosolutions.unredd.stats.model.config.ClassificationLayer>"
                + "<File>" + forestmask_path + "</File>"
                + "<Nodata>255.0</Nodata>" + "<Pivot>" + "<double>0.0</double>"
                + "<double>1.0</double>" + "</Pivot>"
                + "</it.geosolutions.unredd.stats.model.config.ClassificationLayer>"
                + "</Classifications>" + "<Output>" + "<Format>CSV</Format>"
                + "<Separator>;</Separator>" + "</Output>" + "<DeferredMode>false</DeferredMode>"
                + "</StatisticConfiguration>]]></wps:ComplexData>" + "</wps:Data>" + "</wps:Input>"
                + "</wps:DataInputs>" + "<wps:ResponseForm>" + "<wps:RawDataOutput>"
                + "<ows:Identifier>result</ows:Identifier>" + "</wps:RawDataOutput>"
                + "</wps:ResponseForm>" + "</wps:Execute>";

        MockHttpServletResponse response = postAsServletResponse("wps?", xml);
        // The supplied ROI intersect the source image, so the stats are returned
        assertTrue(!response.getOutputStreamContent().startsWith("internal_server_error"));
        assertTrue(RESULT.equals(response.getOutputStreamContent()));
    }
    
    /**
     * This test provide a request without the whole Output object specified. so the WPS implementation must create a default output object and the result must be ok
     * @throws Exception
     */
    @Test
    public void testROIIntersectNoOutputProvided() throws Exception {
        
        final String area_path = TestData.file(this, "area.tif").getAbsolutePath();
        final String forestmask_path = TestData.file(this, "forest_mask.tif").getAbsolutePath();
        
        String xml =

        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">"
                + "<ows:Identifier>gs:OnlineStatsWPS</ows:Identifier>\""
                + "<wps:DataInputs>"
                + "<wps:Input>"
                + "<ows:Identifier>geometry</ows:Identifier>"
                + "<wps:Data>"
                + "<wps:ComplexData mimeType=\"text/xml; subtype=gml/3.1.1\"><![CDATA[POLYGON ((21.3 0.7, 27 0.7, 26.9 -6, 21.7 -6, 21.3 0.7))]]></wps:ComplexData>"
                + "</wps:Data>"
                + "</wps:Input>"
                + "<wps:Input>"
                + "<ows:Identifier>statConf</ows:Identifier>"
                + "<wps:Data>"
                + "<wps:ComplexData mimeType=\"text/xml\"><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<StatisticConfiguration>"
                + "<Name>area_admin_small</Name>"
                + "<Title>Low resolution admin areas</Title>"
                + "<Description>Compute the area for the administrative areas. Low   resolutions raster.</Description>"
                + "<Topics>"
                + "<string>rdc</string>"
                + "<string>area</string>"
                + "<string>administartive</string>"
                + "</Topics>"
                + "<Stats>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>SUM</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>MIN</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>MAX</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "<it.geosolutions.unredd.stats.model.config.StatsType>COUNT</it.geosolutions.unredd.stats.model.config.StatsType>"
                + "</Stats>"
                + "<DataLayer>"
                + "<File>" + area_path + "</File>"
                + "</DataLayer>"
                + "<Classifications>"
                + "<it.geosolutions.unredd.stats.model.config.ClassificationLayer>"
                + "<File>" + forestmask_path + "</File>"
                + "<Nodata>255.0</Nodata>" + "<Pivot>" + "<double>0.0</double>"
                + "<double>1.0</double>" + "</Pivot>"
                + "</it.geosolutions.unredd.stats.model.config.ClassificationLayer>"
                + "</Classifications>" + "<DeferredMode>false</DeferredMode>"
                + "</StatisticConfiguration>]]></wps:ComplexData>" + "</wps:Data>" + "</wps:Input>"
                + "</wps:DataInputs>" + "<wps:ResponseForm>" + "<wps:RawDataOutput>"
                + "<ows:Identifier>result</ows:Identifier>" + "</wps:RawDataOutput>"
                + "</wps:ResponseForm>" + "</wps:Execute>";

        MockHttpServletResponse response = postAsServletResponse("wps?", xml);
        // The supplied ROI intersect the source image, so the stats are returned
        assertTrue(!response.getOutputStreamContent().startsWith("internal_server_error"));
        assertTrue(RESULT.equals(response.getOutputStreamContent()));
    }

}
