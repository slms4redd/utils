/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  https://github.com/nfms4redd/nfms-geobatch
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
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
package it.geosolutions.unredd.stats.model.config.util;

import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;

import java.io.File;

import org.apache.log4j.Logger;
import org.geotools.data.DataSourceException;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.JTS;
import org.jaitools.imageutils.ROIGeometry;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author DamianoG
 * 
 */
public class ROIGeometryBuilder {

    private final static Logger LOGGER = Logger.getLogger(ROIGeometryBuilder.class);

    /**
     * 
     * @param conf a Statistic Configuration object
     * @param worldGeom a Geometry that identifies a Region Of Interest
     * @return A Region Of Interest (as ROIGeometry object) reprojected into the raster space of the Layer specified into StatisticConfiguration
     * @throws Exception
     */
    public static ROIGeometry build(StatisticConfiguration conf, Geometry worldGeom) throws Exception {

        //TODO the CRS of worldGeom must be equals to the dataLayer specified into conf object. ADD A CONTROL. 
        
        // get the reference to the target geotiff then did the reprojection into raster space
        File f = new File(conf.getDataLayer().getFile());
        ROIGeometry roiGeom = null;
        try {
            roiGeom = build(f, worldGeom);
        } catch (MismatchedDimensionException e) {
            LOGGER.error(e.getMessage(), e);
            throw new Exception(e.getMessage(), e.getCause());
        } catch (DataSourceException e) {
            LOGGER.error(e.getMessage(), e);
            throw new Exception(e.getMessage(), e.getCause());
        } catch (TransformException e) {
            LOGGER.error(e.getMessage(), e);
            throw new Exception(e.getMessage(), e.getCause());
        }

        return roiGeom;
    }

    private static ROIGeometry build(File geoTiffFile, Geometry worldGeom) throws DataSourceException,
            MismatchedDimensionException, TransformException {

        // wrap tiff into geotiffReader
        GeoTiffReader gtr = new GeoTiffReader(geoTiffFile);

        // get the world to grid matrix
        MathTransform g2w = gtr.getOriginalGridToWorld(PixelInCell.CELL_CORNER); // TODO Corner or Center? Are you Sure?
        MathTransform w2g = g2w.inverse();

        // Translate the geometry from Model space to Raster space
        Geometry reprojectedGeom = JTS.transform(worldGeom, w2g);

        gtr.dispose();

        return new ROIGeometry(reprojectedGeom);
    }

}
