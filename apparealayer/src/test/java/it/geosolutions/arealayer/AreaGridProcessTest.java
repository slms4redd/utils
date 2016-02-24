/*
 *  Copyright (C) 2007-2008-2009 GeoSolutions S.A.S.
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
package it.geosolutions.arealayer;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.geotools.coverage.grid.GridCoverage2D;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import it.geosolutions.unredd.apputil.AreaBuilder;

/**
 * Basic test to check if the process terminates without exceptions
 */
// Problems with JAI and maven... let's ignore the test
@Ignore
public class AreaGridProcessTest extends Assert{

    @Test
    public void testArea() throws Exception{
        
        //compute the area in km2
        File outFileKM2=File.createTempFile("testImageKM2", ".tif");
        GridCoverage2D gridKM2 = AreaBuilder.createAreaGrid(2, 2, 109f, 8f, 108f, 10f);
        AreaBuilder.saveAreaGrid(gridKM2, outFileKM2);
        
        BufferedImage imgKM2 = ImageIO.read(outFileKM2);
    }
}
