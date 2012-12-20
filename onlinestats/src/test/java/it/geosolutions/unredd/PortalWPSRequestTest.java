package it.geosolutions.unredd;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.geoserver.wps.WPSTestSupport;
import org.geotools.TestData;
import org.junit.Test;
import com.mockrunner.mock.web.MockHttpServletResponse;

public class PortalWPSRequestTest extends WPSTestSupport {
	
	@Test
	public void test() throws Exception {
	      InputStream is = this.getClass().getResourceAsStream("portal_execute.xml");
	      String xml = IOUtils.toString(is, "UTF-8");
	      xml = xml.replace("{AREA}", TestData.file(this, "area.tif").getAbsolutePath());
	      xml = xml.replace("{PROVINCES}", TestData.file(this, "provinces.tif").getAbsolutePath());
	      xml = xml.replace("{FOREST_MASK}", TestData.file(this, "forest_mask.tif").getAbsolutePath());
	      System.out.println(xml);
	      MockHttpServletResponse response = postAsServletResponse("wps?", xml);
	      System.out.println(response.getOutputStreamContent());
	}
	
}
