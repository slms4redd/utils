package it.geosolutions.unredd.geostore;

import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.core.model.enums.DataType;
import it.geosolutions.geostore.services.dto.search.AndFilter;
import it.geosolutions.geostore.services.dto.search.AttributeFilter;
import it.geosolutions.geostore.services.dto.search.BaseField;
import it.geosolutions.geostore.services.dto.search.CategoryFilter;
import it.geosolutions.geostore.services.dto.search.FieldFilter;
import it.geosolutions.geostore.services.dto.search.SearchFilter;
import it.geosolutions.geostore.services.dto.search.SearchOperator;
import it.geosolutions.geostore.services.rest.GeoStoreClient;
import it.geosolutions.geostore.services.rest.model.RESTResource;
import it.geosolutions.geostore.services.rest.model.ResourceList;
import it.geosolutions.unredd.geostore.adapter.UNREDDGeostoreManagerAdapter;
import it.geosolutions.unredd.geostore.model.AttributeDef;
import it.geosolutions.unredd.geostore.model.ReverseAttributeDef;
import it.geosolutions.unredd.geostore.model.UNREDDCategories;
import it.geosolutions.unredd.geostore.model.UNREDDChartData;
import it.geosolutions.unredd.geostore.model.UNREDDChartScript;
import it.geosolutions.unredd.geostore.model.UNREDDFeedback;
import it.geosolutions.unredd.geostore.model.UNREDDLayerUpdate;
import it.geosolutions.unredd.geostore.model.UNREDDReport;
import it.geosolutions.unredd.geostore.model.UNREDDStatsData;
import it.geosolutions.unredd.geostore.model.UNREDDStatsDef;
import it.geosolutions.unredd.geostore.utils.NameUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invoke calls on GeoStore, bridging UNREDD model.
 * This class is the services interface on Geostore. 
 * Since after some time we decide to abstract from the underlying persistence system 
 * this class is used in a specific Adapter to a more generic interface exposed to clients.
 * 
 * Although this class is not Deprecated its usage is highly discouraged, use {@link UNREDDGeostoreManagerAdapter} instead. 
 */
public class UNREDDGeostoreManager{

    private final static Logger LOGGER = LoggerFactory.getLogger(UNREDDGeostoreManager.class);

    private GeoStoreClient client;

    /**
     * @deprecated Instantiate the client in the client class and use {@link UNREDDGeostoreManager#UNREDDGeostoreManager(GeoStoreClient)} instead
     */
    @Deprecated
    public UNREDDGeostoreManager(String host, String user, String pwd) {
        
        client = new GeoStoreClient();

        client.setGeostoreRestUrl(host);
        client.setUsername(user);
        client.setPassword(pwd);
    }

    public UNREDDGeostoreManager(GeoStoreClient client) {
        this.client = client;
    }

    /**
     * @deprecated This method shouldn't be used at all, all operations of the GeoStore client should be bridged through an Higher level Interface that can be found in the <b>it.geosolutions.unredd.services</b> package 
     */
    @Deprecated
    public GeoStoreClient getClient() {
        return client;
    }

    public List<Resource> searchStatsDefByLayer(String layername) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                createCategoryFilter(UNREDDCategories.STATSDEF),
                createAttributeFilter(UNREDDStatsDef.ReverseAttributes.LAYER, layername));

        return search(filter);
    }
    
    public List<Resource> searchChartScriptByStatsDef(String statsdef) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                //  new FieldFilter(BaseField.NAME, "%", SearchOperator.LIKE) ,
                createCategoryFilter(UNREDDCategories.CHARTSCRIPT),
                createAttributeFilter(UNREDDChartScript.ReverseAttributes.STATSDEF, statsdef));

        return search(filter);
    }

    public List<Resource> searchStatsDataByStatsDef(String statsDefName) throws UnsupportedEncodingException, JAXBException {

        SearchFilter filter = createCategoryFilter(UNREDDCategories.STATSDATA);
        if (statsDefName != null) {
            filter = new AndFilter(filter, createAttributeFilter(UNREDDStatsData.Attributes.STATSDEF, statsDefName));
        }

        List<Resource> list = search(filter);
        if ( CollectionUtils.isEmpty(list)) {
        	LOGGER.info("No stats data found for stats def: " + statsDefName);
        }
        return list;
    }
    
    public Resource searchLayer(String layerName) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
            createCategoryFilter(UNREDDCategories.LAYER),
            new FieldFilter(BaseField.NAME, layerName, SearchOperator.EQUAL_TO)
        );

        List<Resource> resourceList = search(filter);
        if(CollectionUtils.isEmpty(resourceList)) {
            return null;
        }
    
        return resourceList.get(0);
    }
    
    public List<Resource> searchLayerUpdatesByLayerName(String layerName) {
        SearchFilter filter = new AndFilter(
                createCategoryFilter(UNREDDCategories.LAYERUPDATE),
                createAttributeFilter(UNREDDLayerUpdate.Attributes.LAYER, layerName)
                );

        return search(filter);
    }

    public List<Resource> searchChartDataByChartScript(String chartScriptName) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                createCategoryFilter(UNREDDCategories.CHARTDATA),
                createAttributeFilter(UNREDDChartData.Attributes.CHARTSCRIPT, chartScriptName) );

        return search(filter);
    }
    
    public Resource searchResourceByName(String resourceName, UNREDDCategories cat)
    {
        SearchFilter filter = new AndFilter(
            createCategoryFilter(cat),
            new FieldFilter(BaseField.NAME, resourceName, SearchOperator.EQUAL_TO)
        );
        
        List<Resource> list = search(filter);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        
        return null;
    }

    public List<Resource> getLayers() throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = createCategoryFilter(UNREDDCategories.LAYER);
        
        return search(filter);
    }
    
    public List<Resource> getUNREDDResources(UNREDDCategories cat) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = createCategoryFilter(cat);
        return search(filter);
    }
    
    public void deleteLayer(String layerName) {
        recurseDelete(layerName, UNREDDCategories.LAYER, UNREDDCategories.LAYERUPDATE, UNREDDLayerUpdate.Attributes.LAYER);
    }
    
    public void deleteStats(String statsDefName) {
        recurseDelete(statsDefName, UNREDDCategories.STATSDEF, UNREDDCategories.STATSDATA, UNREDDStatsData.Attributes.STATSDEF);
    }
    
    public Resource getResource(Long id, boolean full) {
        return client.getResource(id, full);
    }

    public void deleteResource(Long id) {
        client.deleteResource(id);
        
    }

    public void updateResource(Long id, RESTResource resource) {
        client.updateResource(id, resource);
        
    }

    public Long insert(RESTResource resource) {
        return client.insert(resource);
    }

    public void setData(Long id, String data) {
        client.setData(id, data);
        
    }

    public String getData(Long id, MediaType acceptMediaType) {
        if(acceptMediaType == null){
            acceptMediaType = MediaType.WILDCARD_TYPE;
        }
        return client.getData(id, acceptMediaType);
    }

    
    /* ****************************************************************************
     * *** Specific GeoStore METHODS that aren't overridden from any interfaces *** 
     * ****************************************************************************
     */
    
    /**
     * Inserts a feedback resource
     */
    public void insertFeedback(Map<String, String> attributes, String data) {
        UNREDDFeedback feedback = new UNREDDFeedback();
        feedback.setAttributes(attributes);

        RESTResource resource = feedback.createRESTResource();
        resource.setData(data);
        resource.setName(String.valueOf(resource.hashCode()));

        client.insert(resource);
    }

    /**
     * Inserts a custom statistics report
     */
    public Long insertReport(Map<String, String> attributes, String data) {
        UNREDDReport report = new UNREDDReport();
        report.setAttributes(attributes);

        RESTResource resource = report.createRESTResource();
        resource.setData(data);
        resource.setName(String.valueOf(resource.hashCode()));

        return client.insert(resource);
    }
    	
    public Resource searchResourceByName(String resourceName)
    {
        SearchFilter filter = new FieldFilter(BaseField.NAME, resourceName, SearchOperator.EQUAL_TO);
        List<Resource> list = search(filter);
        if (list!= null && !list.isEmpty()) {
            return list.get(0);
        }
        
        return null;
    }
        
    /*********
     * Returns the list of all layers
     * @return
     */
    public List<Resource> getStatsDefs() throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new CategoryFilter(UNREDDCategories.STATSDEF.getName(), SearchOperator.EQUAL_TO);

        return search(filter);
    }

    public void deleteChartScript(String chartScriptName) {
        recurseDelete(chartScriptName, UNREDDCategories.CHARTSCRIPT, UNREDDCategories.CHARTDATA, UNREDDChartData.Attributes.CHARTSCRIPT);
    }
    
    public void recurseDelete(String resourceName, UNREDDCategories parentCategory, UNREDDCategories childCategory, AttributeDef childAttribute) {
        LOGGER.warn("Recurse delete on " + parentCategory.getName() + " " + resourceName);

        SearchFilter filter = new AndFilter(
            createCategoryFilter(childCategory),
            createAttributeFilter(childAttribute, resourceName)
        );
        ResourceList statsDataList = client.searchResources(filter, null, null, false, false);
        if( CollectionUtils.isNotEmpty(statsDataList.getList())) {
            for (Resource scd : statsDataList.getList()) {
                LOGGER.warn("Removing " + childCategory.getName() + " " + scd.getName());
                client.deleteResource(scd.getId());
            }
        }
        
        filter = new AndFilter(
            createCategoryFilter(parentCategory),
            new FieldFilter(BaseField.NAME, resourceName, SearchOperator.EQUAL_TO)
        );
        ResourceList statsDefList = client.searchResources(filter, null, null, false, false);
        if( CollectionUtils.isNotEmpty(statsDefList.getList())) {
            Resource statsDef = statsDefList.getList().get(0);
            LOGGER.warn("Removing " + parentCategory.getName() + " " + statsDef);
            client.deleteResource(statsDef.getId());
        } else {
            LOGGER.warn(parentCategory.getName() + " not found : " + resourceName);
        }    
    }

    /**
     * Returns all feedback resources
     */
    public List<Resource> getFeedbacks() throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = createCategoryFilter(UNREDDCategories.FEEDBACK);
        return search(filter);
    }

    public List<Resource> searchLayersByStatsDef(UNREDDStatsDef statsDef) {
        List<Resource> layers = new ArrayList<Resource>();
        for(String layerName : statsDef.getLayerNames()) {
                layers.add(this.searchResourceByName(layerName, UNREDDCategories.LAYER));
        }
        return layers;
    }

    public List<Resource> searchStatsDefsByChartScript(UNREDDChartScript chartScript) {
    	List<Resource> statsDefs = new ArrayList<Resource>();
    	for(String statsDefName : chartScript.getStatsDefNames()) {
    		statsDefs.add(this.searchResourceByName(statsDefName, UNREDDCategories.STATSDEF));
    	}
    	return statsDefs;
    }

    /**
     * Kept for compatibility with some Groovy scripts out there.
     * 
     * @deprecated use the exact same method called {@link #searchStatsDataByStatsDef(String statsDefName)}
     */
    @Deprecated
    public List<Resource> searchStatsDataByStatsDef2(String statsDefName) throws UnsupportedEncodingException, JAXBException {
    	return searchStatsDataByStatsDef(statsDefName);
    }

    /***********
     * check if a generic resource exists in a given category
     * @param resourceName
     * @param category
     * @return
     */
    public boolean existResourceInCategory(String resourceName, UNREDDCategories category) {
        SearchFilter filter = new AndFilter(
                new FieldFilter(BaseField.NAME, resourceName, SearchOperator.LIKE),
                createCategoryFilter(category));
        ResourceList list = client.searchResources(filter, null, null, false, false);
    
        return list != null && list.getList() != null && !list.getList().isEmpty();
    }

    /*********
     * checks if layer exists in the category layer
     * @param layerName
     * @return
     */
    public boolean existLayer(String layerName) {
        return existResourceInCategory(layerName, UNREDDCategories.LAYER);
    }

    /************
     * check if layer exists in the category layerupdate
     * @param baseLayerName
     * @param year
     * @param month
     * @return
     */
    public boolean existLayerUpdate(String baseLayerName, String year, String month, String day) {
        String layerName = NameUtils.buildLayerUpdateName(baseLayerName, year, month, day);
        return existResourceInCategory(layerName, UNREDDCategories.LAYERUPDATE);
    }

    private static SearchFilter createCategoryFilter(UNREDDCategories category) {
        return new CategoryFilter(category.getName(), SearchOperator.EQUAL_TO);
    }

    private static SearchFilter createAttributeFilter(AttributeDef at, String value) {
        return new AttributeFilter(at.getName(), value, at.getDataType(),SearchOperator.EQUAL_TO);
    }

    private static SearchFilter createAttributeFilter(ReverseAttributeDef at, String value) {
        return new AttributeFilter(value, at.getName(), DataType.STRING, SearchOperator.EQUAL_TO);
    }

    private List<Resource> search(SearchFilter filter) {
    	return client.searchResources(filter, null, null, true, true).getList();
    }

    
}
