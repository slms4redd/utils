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

import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.geoserver.wps.WPSTestSupport;
import org.geotools.TestData;
import org.junit.Ignore;
import org.junit.Test;

import com.mockrunner.mock.web.MockHttpServletResponse;

import static org.junit.Assert.*;

/**
 * @author DamianoG
 * 
 */
public class OnlineStatsWPSTest extends WPSTestSupport {

    private static final String RESULT = "0;7.745265757021875E10;366384.75;368400.21875;210967.0\n"
            + "1;3.739946002626875E11;366384.75;368400.21875;1016419.0\n";

    private static final String POLY_NOT_INTERSECT = "POLYGON ((30 10, 10 20, 20 40, 40 40, 30 10))";

    private static final String POLY_INTERSECT = "POLYGON ((21.3 0.7, 27 0.7, 26.9 -6, 21.7 -6, 21.3 0.7))";

    private static final String START_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">"
            + "<ows:Identifier>gs:OnlineStatsWPS</ows:Identifier>\""
            + "<wps:DataInputs>"
            + "<wps:Input>"
            + "<ows:Identifier>geometry</ows:Identifier>"
            + "<wps:Data>"
            + "<wps:ComplexData mimeType=\"text/xml; subtype=gml/3.1.1\"><![CDATA[POLYGON_ROI]]></wps:ComplexData>"
            + "</wps:Data>"
            + "</wps:Input>"
            + "<wps:Input>"
            + "<ows:Identifier>statConf</ows:Identifier>"
            + "<wps:Data>"
            + "<wps:ComplexData mimeType=\"text/xml\"><![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<statisticConfiguration>"
            + "<name>area_admin_small</name>"
            + "<title>Low resolution admin areas</title>"
            + "<description>Compute the area for the administrative areas. Low   resolutions raster.</description>"
            + "<topics>"
            + "<string>rdc</string>"
            + "<string>area</string>"
            + "<string>administartive</string>"
            + "</topics>"
            + "<stats>"
            + "<stat>SUM</stat>"
            + "<stat>MIN</stat>"
            + "<stat>MAX</stat>"
            + "<stat>COUNT</stat>"
            + "</stats>" + "<dataLayer>" + "<file>" + "AREA_PATH" + "</file>" + "</dataLayer>"
            + "<classifications>"
            + "<classificationLayer>" + "<file>"
            + "FOREST_MASK_PATH" + "</file>" + "<nodata>255.0</nodata>" + "<pivot>"
            + "<double>0.0</double>" + "<double>1.0</double>" + "</pivot>"
            + "</classificationLayer>"
            + "</classifications>";

    private static final String MIDDLE_REQUEST = "<output>" + "<format>CSV</format>"
            + "<separator>;</separator>" + "</output>";

    private static final String END_REQUEST = "<deferredMode>false</deferredMode>"
            + "</statisticConfiguration>]]></wps:ComplexData>"
            + "</wps:Data>" + "</wps:Input>" + "</wps:DataInputs>" + "<wps:ResponseForm>"
            + "<wps:RawDataOutput>" + "<ows:Identifier>result</ows:Identifier>"
            + "</wps:RawDataOutput>" + "</wps:ResponseForm>" + "</wps:Execute>";

    private static final String FULL_REQUEST = START_REQUEST + MIDDLE_REQUEST + END_REQUEST;

    /**
     * This test provide to the service a ROI that doesn't intersect with the area. so the result returned must be an internal_sever_error.
     * 
     * IN ORDER TO RUN THIS TEST REGISTER THE XSTREAM PPIO FOR StatisticsConfiguration class INTO applicationContext TODO fix
     * 
     * @throws Exception
     */
    @Test
    public void testROInotIntersect() throws Exception {

//        final String area_path = TestData.file(this, "area.tif").getAbsolutePath();
//        final String forestmask_path = TestData.file(this, "forest_mask.tif").getAbsolutePath();
//
//        String xml = FULL_REQUEST;
//
//        xml = xml.replace("POLYGON_ROI", POLY_NOT_INTERSECT);
//        xml = xml.replace("AREA_PATH", area_path);
//        xml = xml.replace("FOREST_MASK_PATH", forestmask_path);
//
//        MockHttpServletResponse response = postAsServletResponse("wps?", xml);
//        // The supplied ROI does not intersect the source image, so an internal server error is returned
//        assertTrue(response.getOutputStreamContent().startsWith("internal_server_error"));
    }

    /**
     * This test provide a valid request without outputfile specified. so the WPS implementation must create a temp file for the output and the result
     * must be ok
     * 
     * IN ORDER TO RUN THIS TEST REGISTER THE XSTREAM PPIO FOR StatisticsConfiguration class INTO applicationContext TODO fix
     * 
     * @throws Exception
     */
    @Ignore
    @Test
    public void testROIIntersect() throws Exception {

//        final String area_path = TestData.file(this, "area.tif").getAbsolutePath();
//        final String forestmask_path = TestData.file(this, "forest_mask.tif").getAbsolutePath();
//
//        String xml = FULL_REQUEST;
//
//        xml = xml.replace("POLYGON_ROI", POLY_INTERSECT);
//        xml = xml.replace("AREA_PATH", area_path);
//        xml = xml.replace("FOREST_MASK_PATH", forestmask_path);
//
//        MockHttpServletResponse response = postAsServletResponse("wps?", xml);
//        // The supplied ROI intersect the source image, so the stats are returned
//        assertTrue(!response.getOutputStreamContent().startsWith("internal_server_error"));
//        assertTrue(RESULT.equals(response.getOutputStreamContent()));
    }

    /**
     * This test provide a request without the whole Output object specified. so the WPS implementation must create a default output object and the
     * result must be ok
     * 
     * IN ORDER TO RUN THIS TEST REGISTER THE XSTREAM PPIO FOR StatisticsConfiguration class INTO applicationContext TODO fix
     * 
     * @throws Exception
     */
    @Ignore
    @Test
    public void testROIIntersectNoOutputProvided() throws Exception {

//        final String area_path = TestData.file(this, "area.tif").getAbsolutePath();
//        final String forestmask_path = TestData.file(this, "forest_mask.tif").getAbsolutePath();
//
//        String xml = START_REQUEST + END_REQUEST;
//
//        xml = xml.replace("POLYGON_ROI", POLY_INTERSECT);
//        xml = xml.replace("AREA_PATH", area_path);
//        xml = xml.replace("FOREST_MASK_PATH", forestmask_path);
//
//        MockHttpServletResponse response = postAsServletResponse("wps?", xml);
//        // The supplied ROI intersect the source image, so the stats are returned
//        assertTrue(!response.getOutputStreamContent().startsWith("internal_server_error"));
//        assertTrue(RESULT.equals(response.getOutputStreamContent()));
    }

    /**
     * This test tests the unmarshalling of the stats conf stored into geostore with JAXB and the marshalling with XStream
     * 
     * @throws Exception
     */
    @Test
    public void testJAXBUnmarshallingXStreamMarshalling() throws Exception {

        // Resolve token in stat configuration TODO use token resolver
        final String area_path = TestData.file(this, "area.tif").getAbsolutePath();
        final String forestmask_path = TestData.file(this, "forest_mask.tif").getAbsolutePath();

        // The Configuration as stored on geostore
        String statsStoredData = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<statisticConfiguration>" + "<name>forest_mask_stats</name>"
                + "<title>Forest change in regions</title>"
                + "<description>Compute the forest and not forest area for the"
                + "administrative areas</description>" + "<topic>DRC</topic>"
                + "<topic>area</topic>"
                + "<topic>regions</topic>"
                + "<topic>forest mask</topic>"
                + "<stats>"
                + "<stat>SUM</stat>"
                + "<stat>MIN</stat>"
                + "<stat>MAX</stat>"
                + "<stat>COUNT</stat>"
                + "</stats>"
                + "<deferredMode>true</deferredMode>"
                + "<dataLayer>"
                + "<file>"
                + area_path
                + "</file>"
                + "</dataLayer>"
                + "<classificationLayer>" + "<file>" + forestmask_path + "</file>" + "<pivot>"
                + "<value>0</value>" + "<value>1</value>" + "</pivot>" + "<nodata>255</nodata>"
                + "</classificationLayer>" + "<output>" + "<format>CSV</format>"
                + "<separator>;</separator>" + "<missingValue>0</missingValue>"
                + "<NaNValue>0</NaNValue>" + "</output>" + "</statisticConfiguration>"

        ;

        // Unmarshalling with JAXB (needed for the structure of the xml stored into geostore)
        JAXBContext context = JAXBContext.newInstance(StatisticConfiguration.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StatisticConfiguration cfg = (StatisticConfiguration) unmarshaller
                .unmarshal(new StringReader(statsStoredData));

//        XStream xstream = new XStream();
        // Unmarshalling with XStream
        // StatisticConfiguration sc2 = (StatisticConfiguration) xstream.fromXML(s);

        // Marshalling with XStream (needed because is those used server side and StatisticConfiguration is )
//        String sw = xstream.toXML(cfg);

        // Marshalling with JAXB
         Marshaller marshaller = context.createMarshaller();
         Writer sw = new StringWriter();
         marshaller.marshal(cfg, sw);
         sw.close();

        String xml =

        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wps:Execute version=\"1.0.0\" service=\"WPS\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.opengis.net/wps/1.0.0\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:wps=\"http://www.opengis.net/wps/1.0.0\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xsi:schemaLocation=\"http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd\">"
                + "<ows:Identifier>gs:OnlineStatsWPS</ows:Identifier>\""
                + "<wps:DataInputs>"
                + "<wps:Input>"
                + "<ows:Identifier>geometry</ows:Identifier>"
                + "<wps:Data>"
                + "<wps:ComplexData mimeType=\"text/xml; subtype=gml/3.1.1\"><![CDATA[POLYGON ((21.3 0.7, 27 0.7, 26.9 -6, 21.7 -6, 21.3 0.7))]]></wps:ComplexData>"
                + "</wps:Data>" + "</wps:Input>" + "<wps:Input>"
                + "<ows:Identifier>statConf</ows:Identifier>" + "<wps:Data>"
                + "<wps:ComplexData mimeType=\"text/xml\"><![CDATA[" + sw.toString()
                + "]]></wps:ComplexData>" + "</wps:Data>" + "</wps:Input>" + "</wps:DataInputs>"
                + "<wps:ResponseForm>" + "<wps:RawDataOutput>"
                + "<ows:Identifier>result</ows:Identifier>" + "</wps:RawDataOutput>"
                + "</wps:ResponseForm>" + "</wps:Execute>";
        MockHttpServletResponse response = postAsServletResponse("wps?", xml);
        assertTrue(!response.getOutputStreamContent().startsWith("internal_server_error"));
        assertTrue(RESULT.equals(response.getOutputStreamContent()));
    }

}
