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
package it.geosolutions.unredd.changematrix;

import it.geosolutions.jaiext.changematrix.ChangeMatrixDescriptor.ChangeMatrix;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author DamianoG
 *
 */
public class ChangeMatrixResultExporterTest extends Assert{

    private ChangeMatrixResultExporter cmre = null;
    private ChangeMatrix cm = null;
    
    @Before
    public void initChangeMatrix(){
        Set<Integer> classes = new LinkedHashSet<Integer>();
        classes.add(1);classes.add(2);classes.add(3);
        cm = new ChangeMatrix(classes);
        cm.registerPair(1, 2, 12.12d);
        cm.registerPair(40, 2, 402.402d);
        cm.registerPair(3, 2, 0);
        cm.registerPair(3, 2, 0);
        cm.registerPair(3, 2, 0);
        cm.registerPair(3, 3, 0);
        cm.registerPair(2, 1, 21);
        cm.registerPair(2, 2, 22);
        cm.registerPair(2, 2, 22);
        cm.registerPair(2, 3, 23.23d);
        cmre = new ChangeMatrixResultExporter(cm);
    }
    
    @Test
    public void changeMatrixDataExtractionTest(){
        
        assertNotNull(cmre.areaMatrix);
        assertEquals(4, cmre.areaMatrix.size());
        assertNotNull(cmre.areaMatrix.get(Integer.MIN_VALUE));
        assertEquals(4, cmre.areaMatrix.get(Integer.MIN_VALUE).size());
        assertNotNull(cmre.areaMatrix.get(1));
        assertEquals(4, cmre.areaMatrix.get(1).size());
        assertNotNull(cmre.areaMatrix.get(2));
        assertEquals(4, cmre.areaMatrix.get(2).size());
        assertNotNull(cmre.areaMatrix.get(3));
        assertEquals(4, cmre.areaMatrix.get(3).size());
        
        assertNotNull(cmre.pixelMatrix);
        assertEquals(4, cmre.pixelMatrix.size());
        assertNotNull(cmre.areaMatrix.get(Integer.MIN_VALUE));
        assertEquals(4, cmre.areaMatrix.get(Integer.MIN_VALUE).size());
        assertNotNull(cmre.pixelMatrix.get(1));
        assertEquals(4, cmre.pixelMatrix.get(1).size());
        assertNotNull(cmre.pixelMatrix.get(1));
        assertEquals(4, cmre.pixelMatrix.get(2).size());
        assertNotNull(cmre.pixelMatrix.get(1));
        assertEquals(4, cmre.pixelMatrix.get(3).size());
        
        assertEquals(2d, cmre.pixelMatrix.get(2).get(2));
        assertEquals(1d, cmre.pixelMatrix.get(2).get(3));
        assertEquals(1d, cmre.pixelMatrix.get(1).get(2));
        assertNull(cmre.pixelMatrix.get(40));
        assertEquals(3d, cmre.pixelMatrix.get(3).get(2));
        
        assertEquals(44d, cmre.areaMatrix.get(2).get(2));
    }
    
    @Test
    public void changeMatrixJSONExporter(){
        
        assertEquals("[[0,0,0,0],[0,0,12.12,0],[0,21,44,23.23],[0,0,0,0]]", cmre.exportJSONAreaMatrix());
        assertEquals("[[0,0,0,0],[0,0,1,0],[0,1,2,1],[0,0,3,1]]", cmre.exportJSONPixelMatrix());
        cm.registerPair(2, 3, 5345345345.34534535);
        cmre = new ChangeMatrixResultExporter(cm);
        assertEquals("[[0,0,0,0],[0,0,12.12,0],[0,21,44,5345345368.575345],[0,0,0,0]]", cmre.exportJSONAreaMatrix());
        
    }
}
