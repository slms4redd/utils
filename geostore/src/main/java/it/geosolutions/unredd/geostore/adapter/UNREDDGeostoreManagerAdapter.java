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
package it.geosolutions.unredd.geostore.adapter;

import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.services.rest.model.RESTResource;
import it.geosolutions.unredd.geostore.UNREDDGeostoreManager;
import it.geosolutions.unredd.geostore.model.UNREDDCategories;
import it.geosolutions.unredd.services.data.CategoryPOJO;
import it.geosolutions.unredd.services.data.ResourcePOJO;
import it.geosolutions.unredd.services.interfaces.UNREDDPersistenceFacade;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author DamianoG
 *
 */
public class UNREDDGeostoreManagerAdapter implements UNREDDPersistenceFacade{

    @Autowired
    private UNREDDDataConverter converter;

    private UNREDDGeostoreManager manager;
    
    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDLayerDAO#getUNREDDResources(it.geosolutions.unredd.services.data.CategoryPOJO)
     */
    @Override
    public List<ResourcePOJO> getUNREDDResources(CategoryPOJO cat)
            throws UnsupportedEncodingException, JAXBException {
        UNREDDCategories categ = converter.convertCategoryPOJO2UNREDDCategories(cat);
        List<Resource> res = manager.getUNREDDResources(categ);
        return converter.convertResource2ResourcePOJO(res);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDLayerDAO#deleteLayer(java.lang.String)
     */
    @Override
    public void deleteLayer(String layerName) {
        manager.deleteLayer(layerName);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDLayerDAO#deleteStats(java.lang.String)
     */
    @Override
    public void deleteStats(String statsDefName) {
        manager.deleteStats(statsDefName);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDLayerDAO#getLayers()
     */
    @Override
    public List<ResourcePOJO> getLayers() throws UnsupportedEncodingException, JAXBException {
        List<Resource> list = manager.getLayers();
        return converter.convertResource2ResourcePOJO(list);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDResourceDAO#getResource(java.lang.Long, boolean)
     */
    @Override
    public ResourcePOJO getResource(Long id, boolean full) {
        Resource res = manager.getResource(id, full);
        return converter.convertResource2ResourcePOJO(res);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDResourceDAO#deleteResource(java.lang.Long)
     */
    @Override
    public void deleteResource(Long id) {
        manager.deleteResource(id);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDResourceDAO#updateResource(java.lang.Long, it.geosolutions.unredd.services.data.ResourcePOJO)
     */
    @Override
    public void updateResource(Long id, ResourcePOJO resource) {
        RESTResource res = converter.convertResourcePOJO2RESTResource(resource);
        manager.updateResource(id, res);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDResourceDAO#insert(it.geosolutions.unredd.services.data.ResourcePOJO)
     */
    @Override
    public Long insert(ResourcePOJO resource) {
        RESTResource res = converter.convertResourcePOJO2RESTResource(resource);
        return manager.insert(res);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDResourceDAO#setData(java.lang.Long, java.lang.String)
     */
    @Override
    public void setData(Long id, String data) {
        manager.setData(id, data);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDResourceDAO#getData(java.lang.Long, java.awt.PageAttributes.MediaType)
     */
    @Override
    public String getData(Long id, String acceptMediaType) {
        MediaType type = converter.convertString2MediaType(acceptMediaType);
        return manager.getData(id, type);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDSearchServices#searchChartDataByChartScript(java.lang.String)
     */
    @Override
    public List<ResourcePOJO> searchChartDataByChartScript(String chartScriptName)
            throws UnsupportedEncodingException, JAXBException {
        List<Resource> list = manager.searchChartDataByChartScript(chartScriptName);
        return converter.convertResource2ResourcePOJO(list);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDSearchServices#searchStatsDefByLayer(java.lang.String)
     */
    @Override
    public List<ResourcePOJO> searchStatsDefByLayer(String layername)
            throws UnsupportedEncodingException, JAXBException {
        List<Resource> res = manager.searchStatsDefByLayer(layername);
        return converter.convertResource2ResourcePOJO(res);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDSearchServices#searchChartScriptByStatsDef(java.lang.String)
     */
    @Override
    public List<ResourcePOJO> searchChartScriptByStatsDef(String statsdef)
            throws UnsupportedEncodingException, JAXBException {
        List<Resource> res = manager.searchChartScriptByStatsDef(statsdef);
        return converter.convertResource2ResourcePOJO(res);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDSearchServices#searchLayerUpdatesByLayerName(java.lang.String)
     */
    @Override
    public List<ResourcePOJO> searchLayerUpdatesByLayerName(String layerName) {
        List<Resource> res = manager.searchLayerUpdatesByLayerName(layerName);
        return converter.convertResource2ResourcePOJO(res);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDSearchServices#searchLayer(java.lang.String)
     */
    @Override
    public ResourcePOJO searchLayer(String layerName) throws UnsupportedEncodingException,
            JAXBException {
        Resource res = manager.searchLayer(layerName);
        return converter.convertResource2ResourcePOJO(res);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDSearchServices#searchStatsDataByStatsDef(java.lang.String)
     */
    @Override
    public List<ResourcePOJO> searchStatsDataByStatsDef(String statsDefName)
            throws UnsupportedEncodingException, JAXBException {
        List<Resource> res = manager.searchStatsDataByStatsDef(statsDefName);
        return converter.convertResource2ResourcePOJO(res);
    }

    /**
     * @see it.geosolutions.unredd.services.interfaces.UNREDDSearchServices#searchResourceByName(java.lang.String, it.geosolutions.unredd.services.data.CategoryPOJO)
     */
    @Override
    public ResourcePOJO searchResourceByName(String resourceName, CategoryPOJO cat) {
        UNREDDCategories categ = converter.convertCategoryPOJO2UNREDDCategories(cat);
        Resource res = manager.searchResourceByName(resourceName, categ);
        return converter.convertResource2ResourcePOJO(res);
    }
}
