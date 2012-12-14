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
package it.geosolutions.unredd.onlinestats.ppio;

import it.geosolutions.unredd.stats.model.config.StatisticConfiguration;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.geoserver.wps.ppio.XMLPPIO;
import org.xml.sax.ContentHandler;

/**
 * Marshalling and Unmarshalling beans using JAXB . This class is responsible only for Context, Marhaller and Unmarshaller creation.
 * A type specific subClass must implements overriding the decode and encode methods.
 * 
 * @author DamianoG - GeoSolutions
 */
public class JAXBPPIO extends XMLPPIO {

    private static JAXBContext contextJAXB;
    
    /**
     * Default constructor, Invoke superclass constructor and create JAXB context.
     */
    protected JAXBPPIO(Class type) {
        super(type, type, null);
        try {
            contextJAXB = JAXBContext.newInstance(type);
        } catch (JAXBException e) {
            // Swallow exception, check the creation in buildMarshaller / buildUnmarshaller methods
        }
    }

    /**
     * Provide Unmarshaller to a subclass 
     * 
     * @throws JAXBException if the JAXB context isn't inizialized.
     */
    protected Unmarshaller buildUnmarshaller() throws JAXBException {
        if(contextJAXB == null){
            throw new JAXBException("An error has occurred while JAXB context creation for the provided class");
        }
        Unmarshaller unmarshaller = contextJAXB.createUnmarshaller();
        return unmarshaller;
    }

    /**
     * Provide Marshaller to a subclass 
     * 
     * @throws JAXBException if the JAXB context isn't inizialized.
     */
    protected Marshaller buildMarshaller() throws JAXBException {
        throw new UnsupportedOperationException("buildMarshaller operation isn't implemented yet...");
    }

    @Override
    public void encode(Object arg0, ContentHandler arg1) throws Exception {

    }

    @Override
    public Object decode(Object input) throws Exception {
        return input;
    }

    @Override
    public Object decode(InputStream arg0) throws Exception {
        return arg0;
    }
}
