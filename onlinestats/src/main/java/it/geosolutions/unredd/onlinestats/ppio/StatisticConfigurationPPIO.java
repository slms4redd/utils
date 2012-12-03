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
package it.geosolutions.unredd.onlinestats.ppio;

import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;
import org.geoserver.wps.ppio.XStreamPPIO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * A PPIO used to generate good looking xml for the StatisticConfiguration used in unredd stats
 * 
 * @author DamianoG
 * 
 */
public class StatisticConfigurationPPIO extends XStreamPPIO {

    protected StatisticConfigurationPPIO() {
        super(StatisticConfiguration.class);
    }

    @Override
    protected XStream buildXStream() {
        XStream xstream = new XStream() {
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new UppercaseTagMapper(next);
            };
        };
        xstream.alias("StatisticConfiguration", StatisticConfiguration.class);
        return xstream;
    }

    @Override
    public Object decode( Object input ) throws Exception {
        // prepare xml encoding
        XStream xstream = buildXStream();
        return xstream.fromXML((String)input);
    }
    

}
