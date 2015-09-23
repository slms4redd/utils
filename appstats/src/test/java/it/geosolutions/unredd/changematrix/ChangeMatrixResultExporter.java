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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class is responsible to export the results produced by the JAI-ext ChangeMatrix operator.
 * The constructor takes as input a {@link ChangeMatrix} instance with the results stored inside and output them as JSON or CSV  
 * 
 * @author DamianoG
 *
 */
public class ChangeMatrixResultExporter {

    /**
     * Hold as a {@link Map} the total changed area calculated for all the transitions 
     * among the specified classes in a ChangeMatrix
     */
    Map<Integer, Map<Integer, Number>> areaMatrix;
    
    /**
     * Hold as a {@link Map} the number of pixel changed for all the transitions
     * among the specified classes in a ChangeMatrix 
     */
    Map<Integer, Map<Integer, Number>> pixelMatrix;
    
    public ChangeMatrixResultExporter(ChangeMatrix cm){
        
        areaMatrix = new HashMap<Integer, Map<Integer, Number>>();
        pixelMatrix = new HashMap<Integer, Map<Integer, Number>>();
        
        List<Integer> classes = cm.getRegisteredClasses();
        Collections.sort(classes);
        Integer [] classesArr = new Integer[1];
        classesArr = classes.toArray(classesArr);
        for(Integer xClass : classesArr){
            for(Integer yClass : classesArr){
                handleMatrix(cm, areaMatrix, xClass, yClass, true);
                handleMatrix(cm, pixelMatrix, xClass, yClass, false);
            }
        }
    }
    
    private void handleMatrix(ChangeMatrix cm, Map<Integer, Map<Integer, Number>> matrix, Integer xClazz, Integer yClazz, boolean isArea){
        
        if(matrix.get(xClazz) == null){
            matrix.put(xClazz, new HashMap<Integer, Number>()); 
        }
        Long pixelValue = cm.retrievePairOccurrences(xClazz, yClazz);
        if(pixelValue == null || pixelValue.longValue()<0){
            return;
        }
        Number value = (isArea)?cm.retrieveTotalArea(xClazz, yClazz):pixelValue;
        matrix.get(xClazz).put(yClazz, value);
    }
    
    /**
     * Export as a JSON bidimensional array the change matrix (pixel count)
     * @return
     */
    public String exportJSONPixelMatrix(){
        return exportJSON(pixelMatrix);
    }
    
    /**
     * Export as a JSON bidimensional array the change matrix (area count)
     * @return
     */
    public String exportJSONAreaMatrix(){
        return exportJSON(areaMatrix);
    }
    
    private String exportJSON(Map<Integer, Map<Integer, Number>> matrix){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Set<Integer> keys = matrix.keySet();
        SortedSet<Integer> sortedKeys = new TreeSet<Integer>(keys);
        for(Integer x : sortedKeys){
            sb.append("[");
            Set<Integer> keys2 = matrix.get(x).keySet();
            SortedSet<Integer> sortedKeys2 = new TreeSet<Integer>(keys2);
            for(Integer y : sortedKeys2){
                Number num = matrix.get(x).get(y);
                DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                dfs.setDecimalSeparator('.');
                DecimalFormat df = new DecimalFormat("#.#");
                df.setDecimalFormatSymbols(dfs);
                df.setMaximumFractionDigits(10);
                df.setMaximumIntegerDigits(10);
                sb.append(df.format(num)).append(",");
            }
            sb.setLength(sb.length() - 1);
            sb.append("],");
        }
        sb.setLength(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}