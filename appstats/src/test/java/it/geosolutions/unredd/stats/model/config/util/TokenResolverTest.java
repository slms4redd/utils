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

import it.geosolutions.unredd.stats.model.config.Layer;
import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;
import junit.framework.TestCase;

/**
 *
 * @author etj
 */
public class TokenResolverTest extends TestCase {

    public static enum TestToken {
        TOKEN1,
        TOK
    }
    
    public TokenResolverTest(String testName) {
        super(testName);
    }

    public void testSomeMethod() {
        StatisticConfiguration cfg = new StatisticConfiguration();
        cfg.setName("cfg_{TOKEN1}");
        cfg.setTitle("title_{TOK}");
        cfg.setDataLayer(new Layer());
        cfg.getDataLayer().setName("layernameWithoutTokens");
        TokenResolver<StatisticConfiguration, TestToken> resolver = new TokenResolver(cfg, StatisticConfiguration.class);
        resolver.put(TestToken.TOKEN1, "value1");
        resolver.put(TestToken.TOK, "value2");
        cfg = resolver.resolve();

        assertEquals("cfg_value1", cfg.getName());
        assertEquals("title_value2", cfg.getTitle());
        assertEquals("layernameWithoutTokens", cfg.getDataLayer().getName());
    }

}
