package it.geosolutions.unredd;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.geoserver.wps.WPSTestSupport;
import org.junit.Test;
import com.mockrunner.mock.web.MockHttpServletResponse;

public class PortalWPSRequestTest extends WPSTestSupport {
	
	@Test
	public void test() throws Exception {
	      InputStream is = this.getClass().getResourceAsStream("portal_execute.xml");
	      String xml = IOUtils.toString(is, "UTF-8");
	      MockHttpServletResponse response = postAsServletResponse("wps?", xml);
	      System.out.println(response.getOutputStreamContent());
	}
	
}
