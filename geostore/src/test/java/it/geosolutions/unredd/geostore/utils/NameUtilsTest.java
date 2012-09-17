/*
 *  Copyright (C) 2007 - 2012 GeoSolutions S.A.S.
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
package it.geosolutions.unredd.geostore.utils;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class NameUtilsTest {

    @Test
    public void buildLayerUpdateNameTest() {
        assertEquals("test_2010", NameUtils.buildLayerUpdateName("test", "2010", null, null));
        assertEquals("test_2011-12", NameUtils.buildLayerUpdateName("test", "2011", "12",null));
        assertEquals("test_2011-01", NameUtils.buildLayerUpdateName("test", "2011", "1",null));
        assertEquals("test_2011-12-04", NameUtils.buildLayerUpdateName("test", "2011", "12","4"));
        assertEquals("test_2011-01-30", NameUtils.buildLayerUpdateName("test", "2011", "1","30"));
        assertEquals("any1_any2-any3", NameUtils.buildLayerUpdateName("any1", "any2", "any3",null)); // mh, do we really want this behaviour?
    }

    @Test
    public void buildStatsDataNameTest() {
        assertEquals("test_2010", NameUtils.buildStatsDataName("test", "2010", null,null));
        assertEquals("test_2011-12", NameUtils.buildStatsDataName("test", "2011", "12",null));
        assertEquals("any1_any2-any3", NameUtils.buildStatsDataName("any1", "any2", "any3",null));
        assertEquals("test_2011-12-04", NameUtils.buildLayerUpdateName("test", "2011", "12","4"));
        assertEquals("test_2011-01-30", NameUtils.buildLayerUpdateName("test", "2011", "1","30"));
    }
    
    @Test
    public void buildTifFileName() {
        assertEquals("test_2010.tif", NameUtils.buildTifFileName("test", "2010", null,null));
        assertEquals("test_2011-12.tif", NameUtils.buildTifFileName("test", "2011", "12",null));
        assertEquals("any1_any2-any3.tif", NameUtils.buildTifFileName("any1", "any2", "any3",null));
        assertEquals("test_2011-12-04.tif", NameUtils.buildTifFileName("test", "2011", "12","4"));
        assertEquals("test_2011-01-30.tif", NameUtils.buildTifFileName("test", "2011", "1","30"));
    }

}
