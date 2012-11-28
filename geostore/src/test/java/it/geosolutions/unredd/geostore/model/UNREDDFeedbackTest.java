package it.geosolutions.unredd.geostore.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.services.rest.GeoStoreClient;
import it.geosolutions.geostore.services.rest.model.RESTCategory;
import it.geosolutions.unredd.geostore.UNREDDGeostoreManager;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UNREDDFeedbackTest {
	
    private final static Logger LOGGER = LoggerFactory.getLogger(UNREDDFeedbackTest.class);
    private UNREDDGeostoreManager manager;
    private GeoStoreClient client;
    
	@Before
	public void setUp() throws Exception {
		client = new GeoStoreClient();
		//client.setGeostoreRestUrl("http://demo1.geo-solutions.it/stg_geostore/rest");
		client.setGeostoreRestUrl("http://localhost:9191/geostore/rest");
		client.setUsername("admin");
		client.setPassword("admin");
		manager = new UNREDDGeostoreManager(client);
	}

	@Test
	public void createResourceTest() throws Exception {
		//createCategory();
		//listCategories();
		
		//insertFeedback();
		//listFeedbacks();
	}

	private void createCategory() {
		RESTCategory category = new RESTCategory();
		category.setName(UNREDDFeedback.CATEGORY_NAME);
		client.insert(category);
	}
	
	private void listCategories() {
		List<it.geosolutions.geostore.core.model.Category> cl = client.getCategories().getList();
		
		for (it.geosolutions.geostore.core.model.Category c : cl) {
			LOGGER.info(c.toString());
		}		
	}

	private void insertFeedback() throws Exception {

		// Some test data
		String layer = "unredd:forest_mask_mosaic";
		long now = System.currentTimeMillis() / 1000L;
		String name = "Rodion Raskolnikov";
		String mail = "example@example.com";
		String data = "{\"text\":\"Something happened to Aliona Ivanovna\", \"geo\":{\"type\": \"GeometryCollection\",\"geometries\": [{\"type\": \"Feature\",\"properties\": {\"name\": \"Sankt-Peterburg\"},\"geometry\": {\"type\": \"Point\",\"coordinates\": [30.333333,59.933333]}}]}}";
		
		// Create attribute map
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(UNREDDFeedback.Attributes.LAYERNAME.getName(), layer);
		attributes.put(UNREDDFeedback.Attributes.LAYERDATE.getName(), String.valueOf(now));
		attributes.put(UNREDDFeedback.Attributes.USERNAME.getName(), name);
		attributes.put(UNREDDFeedback.Attributes.USERMAIL.getName(), mail);
		//attributes.put(UNREDDFeedback.Attributes.STATUS.getName(), "submitted");
        
		// Send the data
		manager.insertFeedback(attributes, data);
	}

	private void listFeedbacks() throws Exception {
		List<Resource> feedbacks = manager.getFeedbacks();
		for (Resource resource : feedbacks) {
			LOGGER.info(resource.toString());
		}
	}

}
