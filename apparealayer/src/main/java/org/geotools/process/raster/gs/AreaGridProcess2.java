/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.process.raster.gs;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BandedSampleModel;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.media.jai.JAI;
import javax.media.jai.NullOpImage;
import javax.media.jai.TileCache;
import javax.media.jai.TileScheduler;
import javax.media.jai.TiledImage;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;
import org.geotools.referencing.CRS;
import org.jaitools.imageutils.ImageLayout2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A process that build a regular cell grid where each pixel represents its effective area in the
 * envelope in square meters.
 * <p>
 * Internally the process uses a reprojection to EckertIV to ensure proper area computation. Current
 * limitations:
 * <ul>
 * <li>won't work for very large rasters since it allocates the entire grid in memory</li>
 * <li>area accuracy increases as the cell size shrinks, avoid having cells that occupy sizeable
 * chunks of the world</li>
 * </ul>
 * 
 * @author Luca Paolino - GeoSolutions
 */
@DescribeProcess(title = "areaGrid", description = "Builds a regular cell grid where each pixel represents its effective area in the envelope using the EckertIV projection")
public class AreaGridProcess2 implements GSProcess {
    private static final String targetCRSWKT = "PROJCS[\"World_Eckert_IV\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Eckert_IV\"],PARAMETER[\"Central_Meridian\",0.0],UNIT[\"Meter\",1.0]]";

    final static ColorModel CM = new ComponentColorModel(
            ColorSpace.getInstance(ColorSpace.CS_GRAY), false, false, Transparency.OPAQUE,
            DataBuffer.TYPE_FLOAT);
    
    final static int DEFAULT_TILE_SIZE = 512;
    
    final static int PREFETCH_LIMIT = 1024 * 1024 * 200;

    private class AreaGridOpImage extends NullOpImage {

        // PlanarImage ti = null;
        final SampleModel sm;

        double startX;

        double startY;

        double stepX;

        double stepY;

        int tileWidth = 512;

        int tileHeight = 512;

        private GeometryFactory geomFactory;
        
        boolean usePrefetch = false;
        
        private MathTransform transform;

        public AreaGridOpImage(RenderedImage source, ImageLayout2 layout,
                ReferencedEnvelope bounds, MathTransform transform, GeometryFactory geomFactory,
                Map configuration) {
            super(source, layout, configuration, OP_COMPUTE_BOUND);
            tileWidth = layout.getTileWidth(null);
            tileHeight = layout.getTileHeight(null);
            sm = new BandedSampleModel(DataBuffer.TYPE_FLOAT, tileWidth, tileHeight, 1);
            startX = bounds.getMinX();
            startY = bounds.getMaxY();
            stepX = (bounds.getMaxX() - bounds.getMinX()) / width;
            stepY = (bounds.getMaxY() - bounds.getMinY()) / height;
            this.geomFactory = geomFactory;
            this.transform = transform;
            usePrefetch = source.getWidth() * source.getHeight() * 4 > PREFETCH_LIMIT;

        }

        @Override
        public Raster computeTile(int tileX, int tileY) {
            //Refining sizes
            System.out.println("tile: " + tileX + ", " + tileY);
            if (usePrefetch && /*(tileY + nVTiles < nY && (tileX % prefetchParallelism == 0))*/
                    tileX == 0 && tileY > 1 && (tileY % nVTiles == 0)){
                TileScheduler ts = JAI.getDefaultInstance().getTileScheduler();
                List<Point> tiles = new ArrayList<Point>();
                for (int j = tileY; j < nVTiles + tileY; j++) {
                    for (int i = 0; i < nX; i++) {
                        tiles.add(new Point( minTx + i, minTy + j));
                    }
                }
                Point[] tilesArray = (Point[]) tiles.toArray(new Point[tiles.size()]);
                ts.prefetchTiles(this, tilesArray);
            }
            int maxX = tileX * tileWidth + tileWidth;
            int maxY = tileY * tileHeight + tileHeight;
            int localW = tileWidth;
            int localH = tileHeight;
            if (maxX > width) {
                localW = tileWidth - (maxX - width);
            }
            if (maxY > height) {
                localH = tileHeight - (maxY - height);
            }

            TiledImage ti = new TiledImage(tileX * tileWidth, tileY * tileHeight, localW, localH, 0, 0, sm, CM);
            WritableRaster raster = ti.getWritableTile(tileX, tileY);
            Coordinate[] tempCoordinates = new Coordinate[5];
            Polygon polygon = null;
            double pX = startX + (stepX * tileX * tileWidth);
            try {
                // scroll thrhough every cell
                double pY = startY - (stepY * tileY * tileHeight);
                for (int trow = 0; trow < localH; trow++) {
                    pX = startX + (stepX * tileX * tileWidth);
                    int row = (tileY * tileHeight) + trow;
                    if ((row >= 0) && (row <= height - 1)) {
                        for (int tcol = 0; tcol < localW; tcol++) {
                            int col = (tileX * tileWidth) + tcol;
                            if ((col >= 0) && (col <= width - 1)) {
                                double nX = pX + stepX;
                                double nY = pY - stepY;

                                if (polygon == null) {
                                    tempCoordinates[0] = new Coordinate(pX, pY);
                                    tempCoordinates[1] = new Coordinate(nX, pY);
                                    tempCoordinates[2] = new Coordinate(nX, nY);
                                    tempCoordinates[3] = new Coordinate(pX, nY);
                                    tempCoordinates[4] = tempCoordinates[0];
                                    LinearRing linearRing = geomFactory.createLinearRing(tempCoordinates);
                                    polygon = geomFactory.createPolygon(linearRing, null);
                                } else {
                                    tempCoordinates[0].x = pX;
                                    tempCoordinates[0].y = pY;
                                    tempCoordinates[1].x = nX;
                                    tempCoordinates[1].y = pY;
                                    tempCoordinates[2].x = nX;
                                    tempCoordinates[2].y = nY;
                                    tempCoordinates[3].x = pX;
                                    tempCoordinates[3].y = nY;
                                    polygon.geometryChanged();
                                }

                                // transform to EckertIV and compute area
                                Geometry targetGeometry = JTS.transform(polygon, transform);
                                raster.setSample(col, row, 0, (float) targetGeometry.getArea());

                                // move on
                                pX = pX + stepX;
                            }
                        }
                    }
                    pY = pY - stepY;
                }
                return raster;
            } catch (Exception e) {
                return null;
            }
        }
    }

    private int nVTiles;

    private int nY;

    private int nX;

    private int minTy;

    private int minTx;

    @DescribeResult(name = "result", description = "The grid")
    public GridCoverage2D execute(
            @DescribeParameter(name = "envelope", description = "The envelope. The envelope must be in WGS84") ReferencedEnvelope bounds,
            @DescribeParameter(name = "width", description = "image width ") int width,
            @DescribeParameter(name = "height", description = "image height ") int height)
            throws ProcessException {
        // basic checks
        if (height <= 0 || width <= 0) {
            throw new ProcessException("height and width parameters must be greater than 0");
        }
        if (bounds.getCoordinateReferenceSystem() == null) {
            throw new ProcessException("Envelope CRS must not be null");
        }

        // build the grid
        GeometryFactory geomFactory = new GeometryFactory();
        try {
            CoordinateReferenceSystem sourceCRS = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
            CoordinateReferenceSystem targetCRS = CRS.parseWKT(targetCRSWKT);
            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);

            TiledImage ti = new TiledImage(0, 0, width, height, 0, 0, new BandedSampleModel(
                    DataBuffer.TYPE_FLOAT, DEFAULT_TILE_SIZE, DEFAULT_TILE_SIZE, 1), CM);
            TileCache tc = JAI.getDefaultInstance().getTileCache();
            RenderingHints hints = new RenderingHints(JAI.KEY_TILE_CACHE, tc);
            TileScheduler ts = JAI.getDefaultInstance().getTileScheduler();
            hints.add(new RenderingHints(JAI.KEY_TILE_SCHEDULER, ts));
            AreaGridOpImage image = new AreaGridOpImage(ti, new ImageLayout2(ti), bounds,
                    transform, geomFactory, hints);

            if (image.usePrefetch){
                // Prefetching tiles
                List<Point> tiles = new ArrayList<Point>();
                nX = image.getNumXTiles();
                nY = image.getNumYTiles();
                minTx = image.getMinTileX();
                minTy = image.getMinTileY();
                final long memory = tc.getMemoryCapacity();
                final int tileMemory = DEFAULT_TILE_SIZE * DEFAULT_TILE_SIZE * 4;
                final int numTiles = (int)(memory / tileMemory);
                nVTiles = Math.min((numTiles / nX) - 2, nY); 
                System.out.println("Prefetching " + nVTiles + " stripes at once");
                for (int j = 0; j < nVTiles; j++) {
                    for (int i = 0; i < nX; i++) {
                        tiles.add(new Point( minTx + i, minTy + j));
                    }
                }
                Point[] tilesArray = (Point[]) tiles.toArray(new Point[tiles.size()]);
                ts.prefetchTiles(image, tilesArray);
            }

            // build the grid coverage
            GridCoverageFactory coverageFactory = new GridCoverageFactory();
            GridSampleDimension[] bands = new GridSampleDimension[] { new GridSampleDimension(
                    "area").geophysics(true) };

            GridCoverage2D grid = coverageFactory.create("AreaGridCoverage", image, bounds, bands,
                    null, null);

            return grid;

        } catch (org.opengis.referencing.FactoryException ef) {
            throw new ProcessException("Unable to create the target CRS", ef);
        }
    }
}
