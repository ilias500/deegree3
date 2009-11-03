//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2009 by:
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
package org.deegree.record.persistence.dc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMNamespace;
import org.deegree.commons.xml.XMLAdapter;
import org.deegree.record.persistence.RecordStore;
import org.deegree.record.persistence.RecordStoreException;

/**
 * {@link RecordStore} implementation of Dublin Core.
 * 
 * @author <a href="mailto:thomas@lat-lon.de">Steffen Thomas</a>
 * @author last edited by: $Author: thomas $
 * 
 * @version $Revision: $, $Date: $
 */
public class DCRecordStore implements RecordStore {
    
    public String output;
    
    private QName typeNames;
    
    public DCRecordStore(QName typeNames){
        this.typeNames = typeNames;
        
    }
    
    
    /* (non-Javadoc)
     * @see org.deegree.record.persistence.RecordStore#describeRecord(javax.xml.stream.XMLStreamWriter)
     */
    @Override
    public void describeRecord() {
        
        
        File file = new File( "/home/thomas/workspace/d3_core/src/org/deegree/record/persistence/dc/dc.xsd" );
        
        if(typeNames.equals( new QName("","Record", "csw") )){
        XMLAdapter ada = new XMLAdapter(file);
        
         
        System.out.println(ada.toString());
        output = ada.toString();
        OMNamespace elem = ada.getRootElement().getDefaultNamespace();
        //ada.getNamespaceContext( ada.getRootElement() );
        
        this.typeNames = new QName(elem.getNamespaceURI());
        }
    }

    /* (non-Javadoc)
     * @see org.deegree.record.persistence.RecordStore#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.deegree.record.persistence.RecordStore#init()
     */
    @Override
    public void init()
                            throws RecordStoreException {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.deegree.record.persistence.RecordStore#getTypeNames()
     */
    @Override
    public QName getTypeNames() {
        
        return typeNames;
    }

    /**
     * @return the output
     */
    public String getOutput() {
        return output;
    }
    
    
    
   

}
