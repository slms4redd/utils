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
package it.geosolutions.unredd.stats.model.config;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ETj (etj at geo-solutions.it)
 */
@XmlRootElement
public class Output {
    public static final String EMPTY = "_EMPTY_";

    public static final String FORMAT_CSV = "CSV";
    public static final String FORMAT_JSON_ARRAY = "JSON_ARRAY";
    public static final String SEPARATOR_COMMA = ",";

    private String format;
    private String separator;

    private String missingValue;
    private String nanValue;

    private String file;

    public String getFormat() {
        return format != null ? format : FORMAT_CSV;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    /** the string to use in output when a pivot entry is missing */
    public String getMissingValue() {
        return missingValue == null? "" :
                missingValue.equals(EMPTY) ? "" :
                    missingValue;
    }

    public void setMissingValue(String missingValue) {
        this.missingValue = missingValue;
    }

    public String getNanValue() {
        return nanValue == null? "NaN" :
                nanValue.equals(EMPTY) ? "" :
                    nanValue;
    }

    public void setNanValue(String nanValue) {
        this.nanValue = nanValue;
    }

    public String getSeparator() {
        separator = (separator!=null) ? separator : SEPARATOR_COMMA;
        separator = (format==FORMAT_JSON_ARRAY) ? SEPARATOR_COMMA : separator;
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
