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
package it.geosolutions.unredd.geostore.model;

import it.geosolutions.geostore.core.model.Attribute;
import it.geosolutions.geostore.core.model.Category;
import it.geosolutions.geostore.core.model.Resource;
import it.geosolutions.geostore.core.model.enums.DataType;
import it.geosolutions.geostore.services.rest.model.RESTResource;
import java.util.ArrayList;
import java.util.Date;
import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class UNREDDChartScriptTest extends TestCase {
    private final static Logger LOGGER = LoggerFactory.getLogger(UNREDDChartScriptTest.class);

    public UNREDDChartScriptTest(String testName) {
        super(testName);
    }

    public void testCreate() {
        Resource res = createResource();

        LOGGER.info("Resource --> " + res);

        UNREDDChartScript unreddRes = new UNREDDChartScript(res);
        LOGGER.info("ChartScript --> " + unreddRes);
        RESTResource outRes = unreddRes.createRESTResource();
        assertEquals(4, outRes.getAttribute().size());
    }

    /**
     * Force the category for UNREDD* resources
     */
    public void testBadCategory() {
        Resource res = createResource();
        res.getCategory().setName(UNREDDCategories.LAYERUPDATE.getName());

        LOGGER.info("Resource --> " + res);

        try {
            UNREDDChartScript cs = new UNREDDChartScript(res);
            fail("Untrapped exception");
        } catch(IllegalArgumentException e) {
            LOGGER.info("Successfully trapped exception: " + e.getMessage());
        }
    }

    /**
     * Bad attributes in Resources should not be added in UNREDD* resources
     */
    public void testBadAttribute() {
        Resource res = createResource();
        res.getAttribute().add(buildAttr(UNREDDLayerUpdate.Attributes.YEAR.getName(), "2001"));

        LOGGER.info("Resource --> " + res);

        UNREDDChartScript unreddRes = new UNREDDChartScript(res);
        LOGGER.info("ChartScript --> " + unreddRes);
        RESTResource outRes = unreddRes.createRESTResource();
        assertEquals(4, outRes.getAttribute().size());
    }

    public void testAddReverseAttribute() {
        Resource res = createResource();
        assertEquals(4, res.getAttribute().size());

        res.getAttribute().add(buildAttr("x4", UNREDDChartScript.ReverseAttributes.STATSDEF.getName()));
        assertEquals(5, res.getAttribute().size());

        LOGGER.info("Resource --> " + res);

        UNREDDChartScript unreddRes = new UNREDDChartScript(res);
        LOGGER.info("ChartScript --> " + unreddRes);
        RESTResource outRes = unreddRes.createRESTResource();
        assertEquals(5, outRes.getAttribute().size());
    }

    public void testRemoveReverseAttribute() {
        Resource res = createResource();
        assertEquals(4, res.getAttribute().size());

        LOGGER.info("Resource --> " + res);

        UNREDDChartScript unreddRes = new UNREDDChartScript(res);
        LOGGER.info("ChartScript --> " + unreddRes);
        boolean ret = unreddRes.removeReverseAttribute(UNREDDChartScript.ReverseAttributes.STATSDEF, "x3");
        assertTrue(ret);
        RESTResource outRes = unreddRes.createRESTResource();
        assertEquals(3, outRes.getAttribute().size());
    }

    public void testBadRemoveReverseAttribute() {
        Resource res = createResource();
        assertEquals(4, res.getAttribute().size());

        LOGGER.info("Resource --> " + res);

        UNREDDChartScript unreddRes = new UNREDDChartScript(res);
        LOGGER.info("ChartScript --> " + unreddRes);
        boolean ret = unreddRes.removeReverseAttribute(UNREDDChartScript.ReverseAttributes.STATSDEF, "DOES_NOT_EXIST");
        assertFalse(ret);
        RESTResource outRes = unreddRes.createRESTResource();
        assertEquals(4, outRes.getAttribute().size());
    }

    public void testConflictRemoveReverseAttribute() {
        Resource res = createResource();
        assertEquals(4, res.getAttribute().size());

        LOGGER.info("Resource --> " + res);

        UNREDDChartScript unreddRes = new UNREDDChartScript(res);
        LOGGER.info("ChartScript --> " + unreddRes);
        boolean ret = unreddRes.removeReverseAttribute(UNREDDChartScript.ReverseAttributes.STATSDEF, UNREDDChartScript.Attributes.SCRIPTPATH.getName());
        assertFalse(ret);
        RESTResource outRes = unreddRes.createRESTResource();
        assertEquals(4, outRes.getAttribute().size());
    }

    protected Resource createResource() {
        Resource res = new Resource();

        Category cat = new Category();
        cat.setName(UNREDDChartScript.CATEGORY_NAME);
        res.setCategory(cat);

        res.setAttribute(new ArrayList<Attribute>());
        res.getAttribute().add(buildAttr(UNREDDChartScript.Attributes.SCRIPTPATH.getName(), "XXPATH"));

//        res.getAttribute().add(buildAttr(UNREDDLayerUpdate.Attributes.LAYER.getName(), "XXlayer"));
        res.getAttribute().add(buildAttr("x1", UNREDDChartScript.ReverseAttributes.STATSDEF.getName()));
        res.getAttribute().add(buildAttr("x2", UNREDDChartScript.ReverseAttributes.STATSDEF.getName()));
        res.getAttribute().add(buildAttr("x3", UNREDDChartScript.ReverseAttributes.STATSDEF.getName()));

        return res;
    }

    protected Attribute buildAttr(String name, String text) {
        Attribute att = new Attribute();
        att.setName(name);
        att.setTextValue(text);
        att.setType(DataType.STRING);
        return att;
    }
    protected Attribute buildAttr(String name, Double d) {
        Attribute att = new Attribute();
        att.setName(name);
        att.setNumberValue(d);
        att.setType(DataType.NUMBER);
        return att;
    }
    protected Attribute buildAttr(String name, Date date) {
        Attribute att = new Attribute();
        att.setName(name);
        att.setDateValue(date);
        att.setType(DataType.DATE);
        return att;
    }
}
