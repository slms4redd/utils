package it.geosolutions.unredd.geostore;


import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.services.dto.ShortResource;
import it.geosolutions.geostore.services.dto.search.AndFilter;
import it.geosolutions.geostore.services.dto.search.AttributeFilter;
import it.geosolutions.geostore.services.dto.search.BaseField;
import it.geosolutions.geostore.services.dto.search.CategoryFilter;
import it.geosolutions.geostore.services.dto.search.FieldFilter;
import it.geosolutions.geostore.services.dto.search.SearchFilter;
import it.geosolutions.geostore.services.dto.search.SearchOperator;
import it.geosolutions.geostore.services.rest.GeoStoreClient;
import it.geosolutions.geostore.services.rest.model.ShortResourceList;

import it.geosolutions.unredd.geostore.model.UNREDDCategories;
import it.geosolutions.unredd.geostore.model.UNREDDChartData;
import it.geosolutions.unredd.geostore.model.UNREDDChartScript;
import it.geosolutions.unredd.geostore.model.UNREDDLayerUpdate;
import it.geosolutions.unredd.geostore.model.UNREDDStatsDef;
import it.geosolutions.unredd.geostore.model.UNREDDStatsData;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

/**
 * Invoke calls on GeoStore, bridging UNREDD model.
 */
public class UNREDDGeostoreManager_ {

    private final GeoStoreClient client;

    public UNREDDGeostoreManager_(String host, String user, String pwd) {
        
        client = new GeoStoreClient();

        client.setGeostoreRestUrl(host);
        client.setUsername(user);
        client.setPassword(pwd);
    }

    public UNREDDGeostoreManager_(GeoStoreClient client) {

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
                new CategoryFilter(category.getName(), SearchOperator.EQUAL_TO));
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
    public boolean existLayerUpdate(String baseLayerName, int year, int month) {
        String layerName = baseLayerName + "_" + year;
        if (month != 0) {
            layerName += "_" + month;
        }
        return existResourceInCategory(layerName, UNREDDCategories.LAYERUPDATE);
    }

    /************
     * returns the pairs statsdef name and the related data associated to layername
     * @param layername
     * @return
     * @throws UnsupportedEncodingException
     * @throws JAXBException
     */
    public Map<String, String> searchStatsDefByLayer(String layername) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                new CategoryFilter(UNREDDCategories.STATSDEF.getName(), SearchOperator.EQUAL_TO),
                new AttributeFilter(layername,
                        UNREDDStatsDef.ReverseAttributes.LAYER.getName(),
                        UNREDDStatsDef.ReverseAttributes.LAYER.getType(),
                        SearchOperator.EQUAL_TO));

        ShortResourceList list = client.searchResources(filter);

        Map<String, String> storedDataList = new TreeMap<String, String>();
        for (ShortResource shortResource : list.getList()) {
            Resource resource = client.getResource(shortResource.getId());
            if (resource.getCategory().getName().equals(UNREDDCategories.STATSDEF.getName())) {
                String responseData = client.getData(resource.getId());

                storedDataList.put(resource.getName(), responseData);
            }
        }
        return storedDataList;
    }

    public List<Resource> searchChartScriptByStatsDef(String statsdef) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                //  new FieldFilter(BaseField.NAME, "%", SearchOperator.LIKE) ,
                new CategoryFilter(UNREDDCategories.CHARTSCRIPT.getName(), SearchOperator.EQUAL_TO),
                new AttributeFilter(statsdef,
                                    UNREDDChartScript.ReverseAttributes.STATSDEF.getName(),
                                    UNREDDChartScript.ReverseAttributes.STATSDEF.getType(),
                                    SearchOperator.EQUAL_TO));

        ShortResourceList list = client.searchResources(filter);
        if (list == null) {
            return null;
        }
        List<Resource> scriptPathList = new ArrayList<Resource>();

        if (list.getList() != null) {
            for (ShortResource shortResource : list.getList()) {

                Resource resource = client.getResource(shortResource.getId());
                scriptPathList.add(resource);
            }
        }
        
        return scriptPathList;
    }

    /*********
     * Returns a list of statsDef objects having attribute StatsDef=statsDefName
     * @param statsDefName
     * @return
     */
    public Map<Resource, String> searchStatsDataByStatsDef(String statsDefName) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter;
        if (statsDefName == null) {
            filter = new CategoryFilter(UNREDDCategories.STATSDATA.getName(), SearchOperator.EQUAL_TO);
        } else {
            filter = new AndFilter(
                     new CategoryFilter(UNREDDCategories.STATSDATA.getName(), SearchOperator.EQUAL_TO),
                     new AttributeFilter(
                            UNREDDStatsData.Attributes.STATSDEF.getName(),
                            statsDefName,
                            UNREDDStatsData.Attributes.STATSDEF.getDataType(),
                            SearchOperator.EQUAL_TO));
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
    
    /*********
     * Returns a list of layerUpdate objects having attribute Layer=layerName
     * @param statsDefName
     * @return
     */
    public List<Resource> searchLayerUpdatesByLayerName(String layerName) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                new CategoryFilter(UNREDDCategories.LAYERUPDATE.getName(), SearchOperator.EQUAL_TO),
                new AttributeFilter(
                        UNREDDLayerUpdate.Attributes.LAYER.getName(),
                        layerName,
                        UNREDDLayerUpdate.Attributes.LAYER.getDataType(),
                        SearchOperator.EQUAL_TO));

        ShortResourceList list = client.searchResources(filter);

        return getResourceList(list);
    }
        
    /*********
     * Returns a list of layerUpdate objects having attribute Layer=layerName
     * @param statsDefName
     * @return
     */
    public List<Resource> searchChartDataByChartScript(String chartScriptName) throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new AndFilter(
                new CategoryFilter(UNREDDCategories.CHARTDATA.getName(), SearchOperator.EQUAL_TO),
                new AttributeFilter(
                        UNREDDChartData.Attributes.CHARTSCRIPT.getName(),
                        chartScriptName,
                        UNREDDLayerUpdate.Attributes.LAYER.getDataType(),
                        SearchOperator.EQUAL_TO));

        ShortResourceList list = client.searchResources(filter);

        return getResourceList(list);
    }
        
    /*********
     * Returns the list of all stat defs
     * @return
     */
    public List<Resource> getLayers() throws UnsupportedEncodingException, JAXBException {
        SearchFilter filter = new CategoryFilter(UNREDDCategories.LAYER.getName(), SearchOperator.EQUAL_TO);
        ShortResourceList list = client.searchResources(filter);
        
        return getResourceList(list);
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
        SearchFilter filter = new CategoryFilter(cat.getName(), SearchOperator.EQUAL_TO);
        ShortResourceList list = client.searchResources(filter);

        return getResourceList(list);
    }
    
    /*********
     * Converts a ShortResourceList to a list of Resource objects
     * @return
     */
    private List<Resource> getResourceList(ShortResourceList list) {
        ArrayList resources = new ArrayList();
        if (list.getList() != null && !list.getList().isEmpty()) {
            for (ShortResource shortResource : list.getList()) {
                Resource resource = client.getResource(shortResource.getId());
                resources.add(resource);
            }
        } else {
            System.out.println("No resource found");
        }
        
        return resources;
    }
    
    public void deleteResource(long id) {
        client.deleteResource(id);
    }
}
