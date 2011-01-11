//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package org.deegree.services.wps;

import static org.deegree.protocol.wps.WPSConstants.VERSION_100;
import static org.deegree.protocol.wps.WPSConstants.WPS_100_NS;

import java.net.URL;

import org.deegree.commons.config.ResourceManager;
import org.deegree.commons.tom.ows.Version;
import org.deegree.protocol.wps.WPSConstants.WPSRequestType;
import org.deegree.services.OWS;
import org.deegree.services.OWSProvider;
import org.deegree.services.controller.ImplementationMetadata;

/**
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public class WPSProvider implements OWSProvider<WPSRequestType> {

    protected static final ImplementationMetadata<WPSRequestType> IMPLEMENTATION_METADATA = new ImplementationMetadata<WPSRequestType>() {
        {
            supportedVersions = new Version[] { VERSION_100 };
            handledNamespaces = new String[] { WPS_100_NS };
            handledRequests = WPSRequestType.class;
            supportedConfigVersions = new Version[] { Version.parseVersion( "3.0.0" ) };
            serviceName = "WPS";
        }
    };

    public String getConfigNamespace() {
        return "http://www.deegree.org/services/wps";
    }

    public URL getConfigSchema() {
        return WPSProvider.class.getResource( "/META-INF/schemas/wps/3.0.0/wps_configuration.xsd" );
    }

    public URL getConfigTemplate() {
        return WPSProvider.class.getResource( "/META-INF/schemas/wps/3.0.0/example.xml" );
    }

    public ImplementationMetadata<WPSRequestType> getImplementationMetadata() {
        return IMPLEMENTATION_METADATA;
    }

    public OWS<WPSRequestType> getService() {
        return new WPService();
    }

    @SuppressWarnings("unchecked")
    public Class<? extends ResourceManager>[] getDependencies() {
        return new Class[] {};
    }

}
