/*
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
package it.geosolutions.unredd;

import it.geosolutions.unredd.stats.impl.StatsRunner;
import it.geosolutions.unredd.stats.model.config.Output;
import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;

public class OutputStatsTest extends StatsRunner{

    /**
     * @param cfg
     */
    public OutputStatsTest(StatisticConfiguration cfg) {
        super(cfg);
    }
    
    public static OutputStatsTest buildOutputStatsTest() {
        StatisticConfiguration cfg = new StatisticConfiguration();
        Output output = new Output();
        output.setSeparator(";");
        output.setNanValue("-256");
        cfg.setOutput(output);
        return new OutputStatsTest(cfg);
    } 
    
}
