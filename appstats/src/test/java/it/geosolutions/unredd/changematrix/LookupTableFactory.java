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

import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import it.geosolutions.jaiext.rlookup.RangeLookupTable;

/**
 * @author DamianoG
 *
 */
public class LookupTableFactory {
    
    /**
     * @param provinceId the pixel value to keep in the output raster
     * @return a {@link RangeLookupTable} representing the lookup to apply to the provices raster to obtain a raster with only one province represented
     */
    public static RangeLookupTable<Integer, Byte> getProvinceLookupTable(int provinceId) {
        Range r = RangeFactory.create(provinceId-1, false, provinceId+1, false);
        RangeLookupTable.Builder<Integer, Byte> builder = new RangeLookupTable.Builder<Integer, Byte>();
        builder.add(r, (byte)provinceId);
        RangeLookupTable<Integer, Byte> table = builder.build();
        return table;
    }
    
    /**
     * 
     * @return a {@link RangeLookupTable} representing the mapping of the NFIS classes over the NFI 
     */
    public static RangeLookupTable<Integer, Byte> getHarmonizationTable(){
        
        RangeLookupTable.Builder<Integer, Byte> builder = new RangeLookupTable.Builder<Integer, Byte>();
        
        
        byte nfisCodeToBurn = 1;
        short nfiCode = 1;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        nfiCode = 9;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        nfiCode = 14;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        nfiCode = 34;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        
        nfisCodeToBurn = 2;
        nfiCode = 2;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        nfiCode = 10;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        nfiCode = 15;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        nfiCode = 35;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        
        nfisCodeToBurn = 3;
        nfiCode = 16;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        nfiCode = 36;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        nfiCode = 17;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        nfiCode = 37;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        
        nfisCodeToBurn = 4;
        nfiCode = 18;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        nfiCode = 38;
        builder.add(RangeFactory.create(nfiCode-1, false, nfiCode+1, false), nfisCodeToBurn);
        
        nfisCodeToBurn = 5;
        short nfiCodeMIN = 3;
        short nfiCodeMAX = 4;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        nfiCodeMIN = 19;
        nfiCodeMAX = 23;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        
        nfisCodeToBurn = 6;
        nfiCodeMIN = 5;
        nfiCodeMAX = 6;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        nfiCodeMIN = 24;
        nfiCodeMAX = 28;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        
        nfisCodeToBurn = 9;
        nfiCodeMIN = 7;
        nfiCodeMAX = 8;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        nfiCodeMIN = 29;
        nfiCodeMAX = 33;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        
        nfisCodeToBurn = 10;
        nfiCodeMIN = 11;
        nfiCodeMAX = 13;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        nfiCodeMIN = 39;
        nfiCodeMAX = 47;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        
        nfisCodeToBurn = 6;
        nfiCodeMIN = 48;
        nfiCodeMAX = 53;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        
        nfisCodeToBurn = 7;
        nfiCodeMIN = 54;
        nfiCodeMAX = 59;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        
        nfisCodeToBurn = 12;
        nfiCodeMIN = 60;
        nfiCodeMAX = 71;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        
        nfisCodeToBurn = 14;
        nfiCodeMIN = 72;
        nfiCodeMAX = 91;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        
        nfisCodeToBurn = 17;
        nfiCodeMIN = 92;
        nfiCodeMAX = 93;
        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        
        // How to handle NODATA?
//        nfisCodeToBurn = x;
//        nfiCodeMIN = y;
//        nfiCodeMAX = z;
//        builder.add(RangeFactory.create(nfiCodeMIN, true, nfiCodeMAX, true), nfisCodeToBurn);
        
        RangeLookupTable<Integer, Byte> table = builder.build();
        return table;
    }
}
