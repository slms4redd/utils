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

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.bind.Unmarshaller;

/**
 * The PPIO for The StatisticConfiguration object implemented using JAXB
 * 
 * @author DamianoG
 * 
 */
public class JAXBStatisticConfigurationPPIO extends JAXBPPIO {

    public JAXBStatisticConfigurationPPIO() {
        super(StatisticConfiguration.class);
    }

    @Override
    public Object decode(Object input) throws Exception {
        StringReader sr = null;
        try {
            Unmarshaller unmarshaller = buildUnmarshaller();
            sr = new StringReader((String) input);
            StatisticConfiguration cfg = (StatisticConfiguration) unmarshaller.unmarshal(sr);
            return cfg;
        } finally {
            if (sr != null) {
                sr.close();
            }
        }
    }

    @Override
    public Object decode(InputStream arg0) throws Exception {
        Unmarshaller unmarshaller = buildUnmarshaller();
        StatisticConfiguration cfg = (StatisticConfiguration) unmarshaller.unmarshal(arg0);
        return cfg;
    }
}
