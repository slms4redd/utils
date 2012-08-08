package it.geosolutions.unredd.geostore;


import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.core.model.StoredData;
import it.geosolutions.geostore.core.model.enums.DataType;
import it.geosolutions.geostore.services.dto.ShortResource;
import it.geosolutions.geostore.services.dto.search.*;
import it.geosolutions.geostore.services.exception.BadRequestServiceEx;
import it.geosolutions.geostore.services.rest.GeoStoreClient;
import it.geosolutions.geostore.services.rest.model.RESTCategory;
import it.geosolutions.geostore.services.rest.model.RESTResource;
import it.geosolutions.geostore.services.rest.model.ShortResourceList;
import it.geosolutions.unredd.geostore.model.*;
import it.geosolutions.unredd.geostore.utils.NameUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invoke calls on GeoStore, bridging UNREDD model.
 */
public class UNREDDGeostoreManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(UNREDDGeostoreManager.class);

    private final GeoStoreClient client;

    public UNREDDGeostoreManager(String host, String user, String pwd) {
        
        client = new GeoStoreClient();

        client.setGeostoreRestUrl(host);
        client.setUsername(user);
        client.setPassword(pwd);
    }

    public UNREDDGeostoreManager(GeoStoreClient client) {

        this.client = client;
    }

    public GeoStoreClient getClient() {
        return client;
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
        ShortResourceList list = client.searchResources(filter);

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
    public boolean existLayerUpdate(String baseLayerName, String year, String month) {
        String layerName = NameUtils.buildLayerUpdateName(baseLayerName, year, month);
        return existResourceInCategory(layerName, UNREDDCategories.LAYERUPDATE);
    }

    public List<Resource> searchStatsDefByLayer(String layername) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                createCategoryFilter(UNREDDCategories.STATSDEF),
                createAttributeFilter(UNREDDStatsDef.ReverseAttributes.LAYER, layername));

        ShortResourceList list = client.searchResources(filter);

        return getResourceList(list);
    }

    public List<Resource> searchChartScriptByStatsDef(String statsdef) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                //  new FieldFilter(BaseField.NAME, "%", SearchOperator.LIKE) ,
                createCategoryFilter(UNREDDCategories.CHARTSCRIPT),
                createAttributeFilter(UNREDDChartScript.ReverseAttributes.STATSDEF, statsdef));

        ShortResourceList list = client.searchResources(filter);
        //if (list == null) {
        //    return null;
        //}
        List<Resource> resources = new ArrayList<Resource>();

        if (list.getList() != null) {
            for (ShortResource shortResource : list.getList()) {
                
                Resource resource = client.getResource(shortResource.getId());
                resources.add(resource);
            }
        }
        
        return resources;
    }

    /*********
     * Returns a list of statsDef objects having attribute StatsDef=statsDefName
     * @param statsDefName
     * @return
     *
     * @deprecated 
     */
    public Map<Resource, String> searchStatsDataByStatsDef(String statsDefName) throws UnsupportedEncodingException, JAXBException {

        SearchFilter filter = new CategoryFilter(UNREDDCategories.STATSDATA.getName(), SearchOperator.EQUAL_TO);
        if (statsDefName != null) {
            filter = new AndFilter(filter, createAttributeFilter(UNREDDStatsData.Attributes.STATSDEF, statsDefName));
        }
        
        ShortResourceList list = client.searchResources(filter);

        Map resources = new HashMap();
        if (list.getList() != null && !list.getList().isEmpty()) {
            for (ShortResource shortResource : list.getList()) {
                Resource resource = client.getResource(shortResource.getId());
                String data = client.getData(shortResource.getId(), MediaType.WILDCARD_TYPE);

                resources.put(resource, data);
            }
        } else {
            System.out.println("No stats data found with name " + statsDefName);
        }
        
        return resources;
    }

    // TODO: change name in searchStatsDataByStatsDef
    public List<Resource> searchStatsDataByStatsDef2(String statsDefName) throws UnsupportedEncodingException, JAXBException {

        SearchFilter filter = createCategoryFilter(UNREDDCategories.STATSDATA);
        if (statsDefName != null) {
            filter = new AndFilter(filter, createAttributeFilter(UNREDDStatsData.Attributes.STATSDEF, statsDefName));
        }

        List<Resource> ret = new ArrayList<Resource>();

        ShortResourceList list = client.searchResources(filter);
        if ( CollectionUtils.isNotEmpty(list.getList())) {
            for (ShortResource shortResource : list.getList()) {
                Resource resource = client.getResource(shortResource.getId()); // TODO: use new metod getFull()
                String data = client.getData(shortResource.getId(), MediaType.WILDCARD_TYPE);
                StoredData sdata = new StoredData();
                sdata.setData(data);
                resource.setData(sdata);
                ret.add(resource);
            }
        } else {
            LOGGER.info("No stats data found for stats def: " + statsDefName);
        }

        return ret;
    }

    
    /*********
     * Returns a layer with the given name
     * @param layerName
     * @return
     */
    public Resource searchLayer(String layerName) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                createCategoryFilter(UNREDDCategories.LAYER),
                new FieldFilter(BaseField.NAME, layerName, SearchOperator.EQUAL_TO)
                );

        ShortResourceList shortResourceList = client.searchResources(filter);

        List<Resource> resourceList = getResourceList(shortResourceList);
        if ( CollectionUtils.isEmpty(resourceList) )
            return null;
    
        return resourceList.get(0);
    }
    
    /*********
     * Returns a list of layerUpdate objects having attribute Layer=layerName
     * @param statsDefName
     * @return
     */
    public List<Resource> searchLayerUpdatesByLayerName(String layerName) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                createCategoryFilter(UNREDDCategories.LAYERUPDATE),
                createAttributeFilter(UNREDDLayerUpdate.Attributes.LAYER, layerName)
                );

        ShortResourceList list = client.searchResources(filter);

        return getResourceList(list);
    }
        
    /*********
     * Returns a list of ChartData objects having attribute CHARTSCRIPT=chartScriptName
     * @param statsDefName
     * @return
     */
    public List<Resource> searchChartDataByChartScript(String chartScriptName) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                createCategoryFilter(UNREDDCategories.CHARTDATA),
                createAttributeFilter(UNREDDChartData.Attributes.CHARTSCRIPT, chartScriptName) );

        ShortResourceList list = client.searchResources(filter);

        return getResourceList(list);
    }
        
    /*********
     * Returns the list of all stat defs
     * @return
     */
    public List<Resource> getLayers() throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = createCategoryFilter(UNREDDCategories.LAYER);
        ShortResourceList list = client.searchResources(filter);
        
        return getResourceList(list);
    }

    /**
     * Returns all feedback resources
     */
    public List<Resource> getFeedbacks() throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = createCategoryFilter(UNREDDCategories.FEEDBACK);
        ShortResourceList list = client.searchResources(filter);
        
        return getResourceList(list);
    }
    
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
    
    public Resource searchResourceByName(String resourceName)
    {
        SearchFilter searchFilter = new FieldFilter(BaseField.NAME, resourceName, SearchOperator.EQUAL_TO);
        ShortResourceList rlist = client.searchResources(searchFilter);
        if (!rlist.isEmpty()) {
            ShortResource shortRes = rlist.getList().get(0);
            return client.getResource(shortRes.getId());
        }
        
        return null;
    }
    
    public Resource searchResourceByName(String resourceName, UNREDDCategories cat)
    {
        //SearchFilter searchFilter = new FieldFilter(BaseField.NAME, resourceName, SearchOperator.EQUAL_TO);
        SearchFilter searchFilter = new AndFilter(
                createCategoryFilter(cat),
                new FieldFilter(BaseField.NAME, resourceName, SearchOperator.EQUAL_TO));
        
        ShortResourceList rlist = client.searchResources(searchFilter);
        if (!rlist.isEmpty()) {
            ShortResource shortRes = rlist.getList().get(0);
            return client.getResource(shortRes.getId());
        }
        
        return null;
    }
    
    /*********
     * Returns the list of all layers
     * @return
     */
    public List<Resource> getStatsDefs() throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new CategoryFilter(UNREDDCategories.STATSDEF.getName(), SearchOperator.EQUAL_TO);
        ShortResourceList list = client.searchResources(filter);

        return getResourceList(list);
    }
    
    /*********
     * Returns the list of all resources with a given UNREDDResources value
     * @return
     */
    public List<Resource> getUNREDDResources(UNREDDCategories cat) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = createCategoryFilter(cat);
        ShortResourceList list = client.searchResources(filter);

        return getResourceList(list);
    }
    
    /*********
     * Converts a ShortResourceList to a list of Resource objects
     * @return
     */
    private List<Resource> getResourceList(ShortResourceList list) {
        ArrayList resources = new ArrayList();
        if ( CollectionUtils.isNotEmpty(list.getList()) ) {
            for (ShortResource shortResource : list.getList()) {
                Resource resource = client.getResource(shortResource.getId());
                resources.add(resource);
            }
        } else {
            LOGGER.info("No resource found");
        }
        
        return resources;
    }

    public void deleteLayer(String layerName) {
        recurseDelete(layerName, UNREDDCategories.LAYER, UNREDDCategories.LAYERUPDATE, UNREDDLayerUpdate.Attributes.LAYER);
    }
    
    public void deleteStats(String statsDefName) {
        recurseDelete(statsDefName, UNREDDCategories.STATSDEF, UNREDDCategories.STATSDATA, UNREDDStatsData.Attributes.STATSDEF);
        /*
        LOGGER.warn("Recurse delete on StatsDef " + statsDefName);

        SearchFilter filter = new AndFilter(
                createCategoryFilter(UNREDDCategories.STATSDATA),
                createAttributeFilter(UNREDDStatsData.Attributes.STATSDEF, statsDefName));
        ShortResourceList statsDataList = client.searchResources(filter);
        if( CollectionUtils.isNotEmpty(statsDataList.getList())) {
            for (ShortResource scd : statsDataList.getList()) {
                LOGGER.warn("Removing statsData " + scd.getName());
                client.deleteResource(scd.getId());
            }
        }
        filter = new AndFilter(
                    createCategoryFilter(UNREDDCategories.STATSDEF),
                    new FieldFilter(BaseField.NAME, statsDefName, SearchOperator.EQUAL_TO)
                );
        ShortResourceList statsDefList = client.searchResources(filter);
        if( CollectionUtils.isNotEmpty(statsDefList.getList())) {
            ShortResource statsDef = statsDefList.getList().get(0);
            LOGGER.warn("Removing statsDef " + statsDef);
            client.deleteResource(statsDef.getId());
        } else {
            LOGGER.warn("StatsDef not found : " + statsDefName);
        }
        */
    }

    public void deleteChartScript(String chartScriptName) {
        recurseDelete(chartScriptName, UNREDDCategories.CHARTSCRIPT, UNREDDCategories.CHARTDATA, UNREDDChartData.Attributes.CHARTSCRIPT);
    }
    
    public void recurseDelete(String resourceName, UNREDDCategories parentCategory, UNREDDCategories childCategory, AttributeDef childAttribute) {
        LOGGER.warn("Recurse delete on " + parentCategory.getName() + " " + resourceName);

        SearchFilter filter = new AndFilter(
                createCategoryFilter(childCategory),
                createAttributeFilter(childAttribute, resourceName));
        ShortResourceList statsDataList = client.searchResources(filter);
        if( CollectionUtils.isNotEmpty(statsDataList.getList())) {
            for (ShortResource scd : statsDataList.getList()) {
                LOGGER.warn("Removing " + childCategory.getName() + " " + scd.getName());
                client.deleteResource(scd.getId());
            }
        }
        filter = new AndFilter(
                    createCategoryFilter(parentCategory),
                    new FieldFilter(BaseField.NAME, resourceName, SearchOperator.EQUAL_TO)
                );
        ShortResourceList statsDefList = client.searchResources(filter);
        if( CollectionUtils.isNotEmpty(statsDefList.getList())) {
            ShortResource statsDef = statsDefList.getList().get(0);
            LOGGER.warn("Removing " + parentCategory.getName() + " " + statsDef);
            client.deleteResource(statsDef.getId());
        } else {
            LOGGER.warn(parentCategory.getName() + " not found : " + resourceName);
        }    
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
}
