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
package it.geosolutions.unredd.stats.impl;

import it.geosolutions.unredd.stats.RangeParser;
import it.geosolutions.unredd.stats.model.config.Range;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
public class DataFile {

    protected File file;
    protected Double noValue;
    protected List<RangeParser> rangeParser;

    public DataFile() {
    }

    public DataFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Double getNoValue() {
        return noValue;
    }

    public void setNoValue(Double noValue) {
        this.noValue = noValue;
    }
    
    public void setRanges(List<Range> rangeList){
        if(rangeParser == null){
            rangeParser = new ArrayList<RangeParser>();
        }
        for(Range r : rangeList){
            rangeParser.add(new RangeParser(r.getRange(), r.getIsAnExcludeRange()));
        }
    }
    
    public List getRanges(){
        if(rangeParser == null){
            return null;
        }
        return ListUtils.unmodifiableList(rangeParser);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[file=" + file + ", noValue=" + noValue + ", rangeSize=" + rangeParser.size() + "]";
    }

}
