/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  https://github.com/nfms4redd/nfms-geobatch
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

package it.geosolutions.unredd.stats.model.config.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXB;

/**
 *
 * @param <O> The class to be un/marshalled
 * @param <T> The token type
 * @author etj
 */
public class TokenResolver<O, T extends Enum<T>> {

    private O instance;
    private Class<O> type;

    private Map<T,String> tokens = new HashMap<T, String>();

    public TokenResolver(O instance, Class<O>type) {
        this.instance = instance;
        this.type = type;
    }

    public void put(T token, String value) {
        tokens.put(token, value);
    }
    
    public void putAll(Map<T, ? extends String> m) {
        tokens.putAll(m);
    }

    public void setTokens(Map<T, String> tokens) {
        this.tokens = tokens;
    }

    public O resolve() {
        StringWriter sw = new StringWriter();
        JAXB.marshal(instance, sw);
        String buff = sw.getBuffer().toString();
        for (Map.Entry<T, String> entry : tokens.entrySet()) {
            String val = entry.getValue() == null? "" : entry.getValue();
            buff = buff.replace('{'+entry.getKey().name()+'}', val);
        }
        StringReader sr = new StringReader(buff);
        return JAXB.unmarshal(sr, type);
    }
}
